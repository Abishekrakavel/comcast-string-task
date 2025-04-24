package com.comcast.stringinator.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class StringinatorPersistenceUtil {

    private static final String FILE_PATH = "seenStrings.json";
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void persistStateToFile(Map<String, AtomicInteger> seenStrings,
                                          String mostPopularString,
                                          int mostPopularStringCount,
                                          String longestInput) throws IOException {
        StateData stateData = new StateData(seenStrings, mostPopularString, mostPopularStringCount, longestInput);
        mapper.writeValue(new File(FILE_PATH), stateData);
    }

    public static StateData loadStateFromFile() throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) return new StateData(); // Return empty if file doesn't exist
        return mapper.readValue(file, StateData.class);
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
