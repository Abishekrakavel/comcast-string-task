package com.comcast.stringinator.service;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.Pair;

import com.comcast.stringinator.model.StatsResult;
import com.comcast.stringinator.model.StringinatorInput;
import com.comcast.stringinator.model.StringinatorResult;

import org.springframework.stereotype.Service;

@Service
public class StringinatorServiceImpl implements StringinatorService {

    private final Map<String, AtomicInteger> seenStrings = new ConcurrentHashMap<>();
    private volatile String mostPopularString = null;
    private volatile int mostPopularStringCount = 0;
    private volatile String longestInput = null;

    @Override
    public StringinatorResult stringinate(StringinatorInput input) {
        String inputStr = input.getInput();
        int updatedCount = updateSeenStrings(inputStr);

        // logic for most frequent character
        Pair<Character, Integer> charFrequencyAndCount = getMostFrequentCharacter(inputStr);
        Character mostFrequentChar = null;
        Integer frequency = 0;
        if (charFrequencyAndCount != null) {
            mostFrequentChar = charFrequencyAndCount.getLeft();
            frequency = charFrequencyAndCount.getRight();
        }

        // logic for most popular string and longest string
        updateMostPopularString(inputStr, updatedCount);
        updateLongestInputString(inputStr);

        return new StringinatorResult(inputStr, inputStr.length(), mostFrequentChar, frequency);
    }

    @Override
    public StatsResult stats() {
        return new StatsResult(Collections.unmodifiableMap(seenStrings.entrySet()
                .stream()
                .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                e -> e.getValue().get()
                        )
                )), mostPopularString, longestInput);
    }

    private int updateSeenStrings(String input) {
        AtomicInteger count = seenStrings.computeIfAbsent(input, k -> new AtomicInteger(0));
        return count.incrementAndGet();
    }

    private void updateMostPopularString(String input, int count) {
        synchronized (this) {
            if (count > mostPopularStringCount) {
                mostPopularStringCount = count;
                mostPopularString = input;
            }
        }
    }

    private void updateLongestInputString(String input) {
        synchronized (this) {
            if (longestInput == null || input.length() > longestInput.length()) {
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
