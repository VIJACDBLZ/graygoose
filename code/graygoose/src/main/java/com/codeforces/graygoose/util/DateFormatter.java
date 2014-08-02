package com.codeforces.graygoose.util;

import org.apache.commons.lang.StringUtils;

import java.util.concurrent.TimeUnit;

public class DateFormatter {
    public Long stringHHMMToLong(String value) {
        if (StringUtils.isBlank(value)) {
            throw new RuntimeException("String cannot be empty.");
        }

        long hours = 0;
        long minutes = 0;

        boolean wasColon = false;
        for (char c : value.toCharArray()) {
            if (c == ':')
                wasColon = true;
            if (Character.isDigit(c)) {
                if (!wasColon)
                    hours = hours * 10 + c - '0';
                else
                    minutes = minutes * 10 + c - '0';
            }
        }
        return hours * 60 + minutes;
    }

    public String longToStringHHMM(Long value) {
        if (value < 0 || value > TimeUnit.DAYS.toMinutes(1) - 1) {
            throw new RuntimeException("Time value is out of the bounds");
        }
        return ((value / 60) / 10) + "" + (((value / 60) % 10)) + ":" +
                ((value % 60) / 10) + "" + ((value % 60) % 10);
    }
}
