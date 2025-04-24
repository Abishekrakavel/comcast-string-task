package com.comcast.stringinator.model;

import java.util.Map;

public class StatsResult {
    private final Map<String, Integer> inputs;
    private final String mostPopularString;
    private final String longestInput;

    public StatsResult(Map<String, Integer> inputs, String mostPopularString, String longestInput) {
        this.inputs = inputs;
        this.mostPopularString = mostPopularString;
        this.longestInput = longestInput;
    }

    public Map<String, Integer> getInputs() {
        return inputs;
    }

    public String getMostPopularString() {
        return mostPopularString;
    }

    public String getLongestInput() {
        return longestInput;
    }
}
