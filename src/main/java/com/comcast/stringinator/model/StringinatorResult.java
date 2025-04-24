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

    // Just trying not really used
    public static class Builder {
        private String input;
        private Integer length;
        private Character mostFrequentChar;
        private Integer frequency;

        public Builder setInput(String input){
            this.input = input;
            return this;
        }
        public Builder setLength(Integer length){
            this.length = length;
            return this;
        }
        public Builder setMostFrequentChar(Character mostFrequentChar){
            this.mostFrequentChar = mostFrequentChar;
            return this;
        }
        public Builder setFrequency(Integer frequency){
            this.frequency = frequency;
            return this;
        }
        public StringinatorResult build(){
            return new StringinatorResult(input,length,mostFrequentChar,frequency);
        }
    }
}
