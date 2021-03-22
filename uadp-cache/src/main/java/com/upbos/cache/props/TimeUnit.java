package com.upbos.cache.props;


import lombok.Data;

/**
 * @author wangjz
 */
@Data
public class TimeUnit {

    private long value;

    private java.util.concurrent.TimeUnit unit;

    public static TimeUnit parse(String expireTimeStr) {
        TimeUnit expireTime = new TimeUnit();
        String value = expireTimeStr.substring(0, expireTimeStr.length() - 1);
        expireTime.setValue(Long.parseLong(value));
        String unitStr = expireTimeStr.substring(expireTimeStr.length() - 1);
        java.util.concurrent.TimeUnit timeUnit;
        switch (unitStr) {
            case TIME_UNIT_MINUTE:
                timeUnit = java.util.concurrent.TimeUnit.MINUTES;
                break;
            case TIME_UNIT_HOUR:
                timeUnit = java.util.concurrent.TimeUnit.HOURS;
                break;
            case TIME_UNIT_DAY:
                timeUnit = java.util.concurrent.TimeUnit.DAYS;
                break;
            default:
                timeUnit = java.util.concurrent.TimeUnit.SECONDS;
        }
        expireTime.setUnit(timeUnit);
        return expireTime;
    }

    public static final String TIME_UNIT_SECOND = "s";
    public static final String TIME_UNIT_MINUTE = "m";
    public static final String TIME_UNIT_HOUR = "h";
    public static final String TIME_UNIT_DAY = "d";

}
