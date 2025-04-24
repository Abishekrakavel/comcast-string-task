package com.comcast.stringinator.service;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.comcast.stringinator.utils.StringinatorPersistenceUtil;
import org.apache.commons.lang3.tuple.Pair;

import com.comcast.stringinator.model.StatsResult;
import com.comcast.stringinator.model.StringinatorInput;
import com.comcast.stringinator.model.StringinatorResult;

import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class StringinatorServiceImpl implements StringinatorService {

    private static final Logger log = LoggerFactory.getLogger(StringinatorServiceImpl.class);

    private final Map<String, AtomicInteger> seenStrings = new ConcurrentHashMap<>();
    private volatile String mostPopularString = null;
    private volatile int mostPopularStringCount = 0;
    private volatile String longestInput = null;

    @PostConstruct
    public void loadState() throws IOException{
        try {
            log.info("Loading state from persistent storage...");
            StringinatorPersistenceUtil.StateData stateData = StringinatorPersistenceUtil.loadStateFromFile();
            seenStrings.putAll(stateData.getSeenStrings());
            mostPopularString = stateData.getMostPopularString();
            mostPopularStringCount = stateData.getMostPopularStringCount();
            longestInput = stateData.getLongestInput();

            log.info("State loaded successfully from file. Most popular: {}, Longest: {}",
                    mostPopularString, longestInput);
        } catch (IOException e) {
            log.error("Could not load state: {}", e.getMessage());
        }
    }

    @PreDestroy
    public void persistState() throws IOException{
        try {
            log.info("Persisting state to file...");
            StringinatorPersistenceUtil.persistStateToFile(seenStrings, mostPopularString, mostPopularStringCount, longestInput);
            log.info("State persisted successfully to file.");
        } catch (IOException e) {
            log.error("Could not persist state: {}", e.getMessage());
        }
    }

    @Override
    public StringinatorResult stringinate(StringinatorInput input) {
        String inputStr = input.getInput();
        log.info("Processing input string: {}", inputStr);

        int updatedCount = updateSeenStrings(inputStr);
        log.debug("Updated seenStrings count for '{}': {}", inputStr, updatedCount);

        // logic for most frequent character
        Pair<Character, Integer> charFrequencyAndCount = getMostFrequentCharacter(inputStr);
        Character mostFrequentChar = null;
        Integer frequency = 0;
        if (charFrequencyAndCount != null) {
            mostFrequentChar = charFrequencyAndCount.getLeft();
            frequency = charFrequencyAndCount.getRight();
            log.debug("Most frequent char in '{}' is '{}' with frequency {}", inputStr, mostFrequentChar, frequency);
        } else{
            log.debug("No frequent character found for '{}'", inputStr);
        }
        // logic for most popular string and longest string
        updateMostPopularString(inputStr, updatedCount);
        updateLongestInputString(inputStr);

        return new StringinatorResult(inputStr, inputStr.length(), mostFrequentChar, frequency);
    }

    @Override
    public StatsResult stats() {
        log.info("Fetching current stats...");
        StatsResult result = new StatsResult(Collections.unmodifiableMap(seenStrings.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().get()
                ))), mostPopularString, longestInput);

        log.debug("Stats generated. SeenStrings size: {}, Most popular: '{}', Longest: '{}'",
                seenStrings.size(), mostPopularString, longestInput);

        return result;
    }

    private int updateSeenStrings(String input) {
        AtomicInteger count = seenStrings.computeIfAbsent(input, k -> new AtomicInteger(0));
        return count.incrementAndGet();
    }

    private void updateMostPopularString(String input, int count) {
        synchronized (this) {
            if (count > mostPopularStringCount) {
                log.debug("Updating most popular string to '{}' with new count: {}", input, count);
                mostPopularStringCount = count;
                mostPopularString = input;
            }
        }
    }

    private void updateLongestInputString(String input) {
        synchronized (this) {
            if (longestInput == null || input.length() > longestInput.length()) {
                log.debug("Updating longest input string to '{}'", input);
                longestInput = input;
            }
        }
    }

    private Pair<Character,Integer> getMostFrequentCharacter(String input) {
        Map<Character, Integer> frequencyMap = new ConcurrentHashMap<>();
        for (char c : input.toLowerCase().toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                frequencyMap.merge(c, 1, Integer::sum);
            }
        }

        Character mostFreqChar = null;
        int maxFreq = 0;

        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            if (entry.getValue() > maxFreq) {
                mostFreqChar = entry.getKey();
                maxFreq = entry.getValue();
            }
        }

        return mostFreqChar==null ? null : Pair.of(mostFreqChar,maxFreq);
    }

}
