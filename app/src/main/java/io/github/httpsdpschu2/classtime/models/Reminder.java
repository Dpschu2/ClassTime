package io.github.httpsdpschu2.classtime.models;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.Required;

/**
 * Created by Dean on 2/18/17.
 */
public class Reminder extends RealmObject {


    @Required
    private String description;
    private String days;
    private String progress;
    private int timeGoal;
    private long countGoal;
    private int hour;
    private int min;
    private boolean active;

    public boolean isReminder() {
        return reminder;
    }

    public void setReminder(boolean reminder) {
        this.reminder = reminder;
    }

    private boolean repeating;
    private boolean reminder;

    public void setProgress(String progress) {
        this.progress = progress;
    }
    public String getProgress(){
        return progress;
    }

    public boolean isRepeating() {
        return repeating;
    }

    public void setRepeating(boolean repeating) {
        this.repeating = repeating;
    }

    public int getTimeGoal() {
        return timeGoal;
    }

    public void setTimeGoal(int timeGoal) {
        this.timeGoal = timeGoal;
    }

    public void setCountGoal(long countGoal) {
        this.countGoal = countGoal;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

}
