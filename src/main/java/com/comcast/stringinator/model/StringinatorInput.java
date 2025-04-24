package com.comcast.stringinator.model;

import javax.validation.constraints.NotBlank;

public class StringinatorInput {

    @NotBlank(message = "Input string must not be blank")
    private String input;

    public StringinatorInput() {
    }

    public StringinatorInput(String input) {
        this.input = input;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }
}
