package com.comcast.stringinator.exceptionHandling;

public class EmptyInputCustomException extends Exception{

    public EmptyInputCustomException() {
        super("Input cannot be empty."); // Default message
    }

    public EmptyInputCustomException(String message) {
        super(message);
    }
}
