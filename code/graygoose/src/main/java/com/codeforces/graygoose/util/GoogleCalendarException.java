package com.codeforces.graygoose.util;

@SuppressWarnings({"DeserializableClassInSecureContext"})
public class GoogleCalendarException extends Exception {
    public GoogleCalendarException(String message, Throwable cause) {
        super(message, cause);
    }
}
