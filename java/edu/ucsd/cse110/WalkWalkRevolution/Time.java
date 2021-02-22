package edu.ucsd.cse110.WalkWalkRevolution;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * create the element in the Time object..
 */
public class Time {
    private long hour;
    private long minute;
    private long second;
    private long ms;
    private long startTime;
    private long endTime;
    private long timeInMillis;
    private MockManager manager;
    private boolean mocking;

    /**
     * default the time data and constructor an initialize.
     */
    public Time(){
        hour = 0;
        minute = 0;
        second = 0;
        ms = 0;
        startTime = 0;
        endTime = 0;
        timeInMillis = 0;
    }

    /**
     *
     * @param hour is the hour information
     * @param minute is the minutes information
     * @param second is the seconds information
     * @param ms is the milliseconds information
     */
    public Time(int hour, int minute, int second, int ms){
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.ms = ms;
    }

    /**
     * record the start Time  with Millis second when we tap the start the run/walk
     */
    public void startRecord(boolean mocking){
        if(mocking) {
            manager = MockManager.getInstance();
            manager.setTimeInSeconds(0);
            startTime = manager.getTime();
        } else {
            startTime = System.currentTimeMillis();
        }
        this.mocking = mocking;
    }

    /**
     * convert the milliseconds time into the hours, minute, second and milliseconds,
     * and update once the time is change
     */
    public void getUpdate(){
        if(mocking) {
            endTime = manager.getTime();
        } else {
            endTime = System.currentTimeMillis();
        }
        timeInMillis = endTime - startTime;
        minute = TimeUnit.MILLISECONDS.toMinutes(timeInMillis)%60;
        second = TimeUnit.MILLISECONDS.toSeconds(timeInMillis)%60;
        hour = TimeUnit.MILLISECONDS.toHours(timeInMillis);
        ms = timeInMillis%1000;
    }

    /**
     *
     * @return to the hour.
     */
    public long getHour(){
        return hour;
    }

    /**
     *
     * @return to the minutes
     */
    public long getMinute(){
        return minute;
    }

    /**
     *
     * @return to the seconds.
     */
    public long getSecond(){
        return second;
    }

    /**
     *
     * @return to the milliseconds.
     */
    public long getMs(){
        return ms;
    }
}
