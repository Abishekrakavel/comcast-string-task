package com.comcast.stringinator.model;

public class StringinatorResult {
    private final String input;
    private final Integer length;
    private final Character mostFrequentChar;
    private final Integer frequency;

    public StringinatorResult(String input, Integer length,Character mostFrequentChar, Integer frequency) {
        this.input = input;
        this.length = length;
        this.mostFrequentChar=mostFrequentChar;
        this.frequency=frequency;
    }

    public Integer getLength() {
        return length;
    }

    public String getInput() {
        return this.input;
    }

    public Character getMostFrequentChar() {
        return mostFrequentChar;
    }

    public Integer getFrequency() {
        return frequency;
    }
}
