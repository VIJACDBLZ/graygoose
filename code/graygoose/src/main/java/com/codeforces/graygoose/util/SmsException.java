package com.codeforces.graygoose.util;

@SuppressWarnings({"DeserializableClassInSecureContext"})
public class SmsException extends RuntimeException {
    public SmsException(String message) {
        super(message);
    }

    public SmsException(String message, Throwable cause) {
        super(message, cause);
    }
}
