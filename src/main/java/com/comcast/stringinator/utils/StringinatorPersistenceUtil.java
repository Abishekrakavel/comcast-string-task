package com.comcast.stringinator.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class StringinatorPersistenceUtil {

    private static final Logger log = LoggerFactory.getLogger(StringinatorPersistenceUtil.class);
    private static final String FILE_PATH = "seenStrings.json";
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void persistStateToFile(Map<String, AtomicInteger> seenStrings,
                                          String mostPopularString,
                                          int mostPopularStringCount,
                                          String longestInput) throws IOException {
        log.info("Persisting state to file: {}", FILE_PATH);
        StateData stateData = new StateData(seenStrings, mostPopularString, mostPopularStringCount, longestInput);
        try {
            mapper.writeValue(new File(FILE_PATH), stateData);
            log.info("Successfully wrote state to file.");
        } catch (IOException e) {
            log.error("Failed to write state to file: {}", FILE_PATH);
            throw e;
        }
    }

    public static StateData loadStateFromFile() throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            log.warn("State file not found: {}. Returning new empty state.", FILE_PATH);
            return new StateData();
        }

        try {
            StateData stateData = mapper.readValue(file, StateData.class);
            log.info("Successfully loaded state from file: {}", FILE_PATH);
            return stateData;
        } catch (IOException e) {
            log.error("Failed to load state from file: {}", FILE_PATH);
            throw e;
        }
    }

    public static class StateData {
        private Map<String, AtomicInteger> seenStrings = new ConcurrentHashMap<>();
        private String mostPopularString;
        private int mostPopularStringCount;
        private String longestInput;

        public StateData() {}

        public StateData(Map<String, AtomicInteger> seenStrings, String mostPopularString,
                         int mostPopularStringCount, String longestInput) {
            this.seenStrings = seenStrings;
            this.mostPopularString = mostPopularString;
            this.mostPopularStringCount = mostPopularStringCount;
            this.longestInput = longestInput;
        }

        public Map<String, AtomicInteger> getSeenStrings() {
            return seenStrings;
        }

        public String getMostPopularString() {
            return mostPopularString;
        }

        public int getMostPopularStringCount() {
            return mostPopularStringCount;
        }

        public String getLongestInput() {
            return longestInput;
        }

        public void setSeenStrings(Map<String, AtomicInteger> seenStrings) {
            this.seenStrings = seenStrings;
        }

        public void setMostPopularString(String mostPopularString) {
            this.mostPopularString = mostPopularString;
        }

        public void setMostPopularStringCount(int mostPopularStringCount) {
            this.mostPopularStringCount = mostPopularStringCount;
        }

        public void setLongestInput(String longestInput) {
            this.longestInput = longestInput;
        }
    }
}
