package com.codeforces.graygoose.misc;

import org.nocturne.main.ApplicationContext;

import java.util.Arrays;
import java.util.Comparator;

public enum TimeInterval {
    TEN_MINUTES("10 minutes", 10L * TimeConstants.MILLIS_PER_MINUTE),
    HALF_HOUR("30 minutes", 30L * TimeConstants.MILLIS_PER_MINUTE),
    HOUR("1 hour", TimeConstants.MILLIS_PER_HOUR),
    TWO_HOURS("2 hour", 2L * TimeConstants.MILLIS_PER_HOUR),
    SIX_HOURS("6 hours", 6L * TimeConstants.MILLIS_PER_HOUR),
    TWELVE_HOURS("12 hours", 12L * TimeConstants.MILLIS_PER_HOUR),
    TWENTY_FOUR_HOURS("24 hours", 24L * TimeConstants.MILLIS_PER_HOUR),
    TWO_DAYS("2 days", 2L * TimeConstants.MILLIS_PER_DAY),
    FOUR_DAYS("4 days", 4L * TimeConstants.MILLIS_PER_DAY),
    SEVEN_DAYS("7 days", 7L * TimeConstants.MILLIS_PER_DAY),
    FOURTEEN_DAYS("14 days", 14L * TimeConstants.MILLIS_PER_DAY),
    THIRTY_DAYS("30 days", 30L * TimeConstants.MILLIS_PER_DAY);

    private final String synonym;
    private final long valueMillis;

    TimeInterval(String synonym, Long valueMillis) {
        this.synonym = synonym;
        this.valueMillis = valueMillis;
    }

    public String getSynonym() {
        return ApplicationContext.getInstance().$(synonym);
    }

    public long getValueMillis() {
        return valueMillis;
    }

    @SuppressWarnings({"SameReturnValue"})
    public static TimeInterval getDefaultValue() {
        return TWENTY_FOUR_HOURS;
    }

    public static TimeInterval[] getTimeIntervalsOrderedByValueDesc() {
        TimeInterval[] timeIntervals = TimeInterval.values();

        Arrays.sort(timeIntervals, new Comparator<TimeInterval>() {
            @Override
            public int compare(TimeInterval o1, TimeInterval o2) {
                if (o1.getValueMillis() < o2.getValueMillis()) {
                    return 1;
                }

                if (o1.getValueMillis() > o2.getValueMillis()) {
                    return -1;
                }

                return 0;
            }
        });

        return timeIntervals;
    }
}
