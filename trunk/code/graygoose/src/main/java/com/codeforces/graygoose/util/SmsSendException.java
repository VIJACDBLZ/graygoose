package com.codeforces.graygoose.util;

@SuppressWarnings({"DeserializableClassInSecureContext"})
public class SmsSendException extends Exception {
    public SmsSendException(String message, Throwable cause) {
        super(message, cause);
    }
}
