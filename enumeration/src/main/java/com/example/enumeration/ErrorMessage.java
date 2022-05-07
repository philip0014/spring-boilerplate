package com.example.enumeration;

public enum ErrorMessage {
    DEFAULT("Oops there is something wrong, please try again later"),
    AUTHENTICATION_FAILED("Username or password do not match"),
    INVALID_TOKEN("Token provided is invalid"),

    ID_NOT_FOUND("Id %s cannot be found");

    private String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String writeMessage(Object... args) {
        return String.format(message, args);
    }
}
