package io.github.httpsdpschu2.classtime.managers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.util.Log;
import android.widget.TextView;

import io.github.httpsdpschu2.classtime.Activities.TextViews;
import io.github.httpsdpschu2.classtime.Alarm.AlarmMute;
import io.github.httpsdpschu2.classtime.Alarm.AlarmReceiver;
import io.github.httpsdpschu2.classtime.R;
import io.github.httpsdpschu2.classtime.models.Reminder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by Dean on 2/18/17.
 */

public final class ReminderManager {

    private static Realm mRealm;
    private static ReminderManager mInstance;
    private static Context mContext;
    private int setBack;
    private static int mId = 0;
    private static HashMap<String, ArrayList<Integer>> mReminders;
    AlarmManager alarmManager, muteManager;
    PendingIntent broadcast, mutePending;
    SimpleDateFormat mInputFormatter = new SimpleDateFormat("H:mm");
    SimpleDateFormat mFormatter = new SimpleDateFormat("h:mm a",Locale.getDefault());
    private ReminderManager() {}
    public ReminderManager getmInstance(){
        return mInstance;
    }
    public static ReminderManager getInstance() {
        if(mInstance == null) {
            mInstance = new ReminderManager();
            mReminders = new HashMap<>();
        }
        return mInstance;
    }

    public void setContext(Context context) {
        mContext = context;
        mRealm = RealmManager.getInstance().getRealm(context);
    }

    public Realm getRealm() {
        return mRealm;
    }

    public Reminder find(String key) {
        Reminder reminder = mRealm.where(Reminder.class).equalTo("description", key).findFirst();
        return reminder;
    }

    public void remove(String key){
    }
    public boolean exists(String key) {
        return find(key) != null;
    }

    public List<Reminder> getReminders() {
        return mRealm.where(Reminder.class).findAll();
    }

    public void create(String desc, int hour, int min, String days, int setBack, long mil, String duration, boolean repeating, boolean reminder) {
        mRealm.beginTransaction();

        Reminder r = mRealm.createObject(Reminder.class);
        r.setDescription(desc);
        r.setHour(hour);
        r.setMin(min);
        r.setCountGoal(mil);
        r.setDays(days);
        this.setBack = setBack;
        r.setTimeGoal(setBack);
        r.setReminder(reminder);
        r.setActive(true);
        r.setRepeating(repeating);
        r.setProgress(duration);

        mRealm.commitTransaction();

        setActive(true, desc);

    }

    public void update(String key, String desc, int hour, int min, String days, int setBack, long mil, String duration, boolean repeating, boolean reminder) {
        Reminder r = find(key);

        cancelAlarms(key);

        mRealm.beginTransaction();
        r.setDescription(desc);
        r.setHour(hour);
        r.setMin(min);
        r.setRepeating(repeating);
        r.setReminder(reminder);
        r.setProgress(duration);
        r.setCountGoal(mil);
        r.setDays(days);
        r.setActive(true);
        r.setTimeGoal(setBack);
        this.setBack = r.getTimeGoal();
        mRealm.commitTransaction();

        setActive(true, desc);
    }


    public void setActive(boolean active, String key) {
        Reminder r = find(key);

        mRealm.beginTransaction();

        r.setActive(active);

        mRealm.commitTransaction();

        getNextAlarm();

        cancelAlarms(key);

        if(active){
            char[] dayArray = r.getDays().toCharArray();
            for(int ind=0; ind<dayArray.length; ind++) {
                if(Integer.parseInt(Character.toString(dayArray[ind])) == 1) {
                    Log.i("Reminder", "Scheduled "+(ind+1)+" at "+r.getHour()+":"+r.getMin());
                    scheduleAlarm(r.getDescription(), ind+1, r.getHour(), r.getMin(), r.getProgress(), r.isRepeating(), r.getTimeGoal(), r.isReminder());
                }
            }
        }
    }
    public void delete(String key){
        Reminder result = mRealm.where(Reminder.class).equalTo("description",key).findFirst();
        mRealm.beginTransaction();
        result.removeFromRealm();
        mRealm.commitTransaction();
    }

    public void cancelAlarms(String description){
        ArrayList<Integer> values = mReminders.get(description);
        if(values != null) {
            for(int id : values) {
                Log.i("Reminder", "Cancelling "+id);
                Intent intent = new Intent(mContext, AlarmReceiver.class);
                intent.addCategory("android.intent.category.DEFAULT");
                Intent intentm = new Intent(mContext, AlarmMute.class);
                intentm.addCategory("android.intent.category.DEFAULT");
                broadcast = PendingIntent.getBroadcast(mContext, id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                mutePending = PendingIntent.getBroadcast(mContext, id, intentm, PendingIntent.FLAG_CANCEL_CURRENT);
                alarmManager.cancel(broadcast);
                muteManager.cancel(mutePending);
            }
            mReminders.remove(description);
        }
    }
    public void getNextAlarm(){
        List<Reminder> items = getReminders();
        List<Integer> daySorted = new ArrayList<>();

        if (getReminders().isEmpty()){
            TextViews.getInstance().setClassNameMain("No classes");
            TextViews.getInstance().setClassTimeMain("");
            TextViews.getInstance().setClassMain();
        }
        else {
    //gets current time
            Calendar c = Calendar.getInstance();
            String min;
            if(c.get(Calendar.MINUTE) < 10){
                min = "0" + String.valueOf(c.get(Calendar.MINUTE));
            }
            else {
                min = String.valueOf(c.get(Calendar.MINUTE));
            }
            String stringTime = String.valueOf(c.get(Calendar.HOUR_OF_DAY)) + min;
            int time = Integer.parseInt(stringTime);
    //sort by days
            String t = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
            List<Reminder> sortedArray = new ArrayList<>();
            int dayOfWeek = Integer.valueOf(t);
            int count = 0;
            boolean found = false;
            int sCount = 0;
            int mCount = 0;
            while(count == 0){
                String day = "";
                for(Reminder rem : items) {
                    day = "";
                    day = day + rem.getDays().charAt(dayOfWeek - 1);
                    int remTime = Integer.parseInt(getFormattedTime(rem));
                    if(day.equals("1") && remTime > time && sCount == 0) {
                        int vTime = Integer.parseInt(getFormattedTime(rem));
                        daySorted.add(vTime);
                        sortedArray.add(rem);
                        sCount++;
                        found = true;
                    }
                    else if(day.equals("1") && sCount != 0) {
                        int vTime = Integer.parseInt(getFormattedTime(rem));
                        daySorted.add(vTime);
                        sortedArray.add(rem);
                        sCount++;
                        found = true;
                    }
                }
                if(found)
                    break;
                if(sCount == 7)
                    count = 1;
                mCount++;
                sCount++;
                dayOfWeek++;
                if(dayOfWeek == 8)
                    dayOfWeek = 1;
            }
            Collections.sort(daySorted);
    //finds next class in sorted array
            List<Reminder> lessItems = new ArrayList<>();
                for (Reminder r : sortedArray) {
                    int vTime = Integer.parseInt(getFormattedTime(r));
                    for(int h = 0; h < sortedArray.size(); h++) {
                        if (daySorted.get(h) == vTime) {
                            lessItems.add(r);
                        }
                    }
                }
            Reminder less = lessItems.get(0);
            String setLess = getFormattedTime(less);
            int timeLess = Integer.parseInt(setLess);
            for (int j = 0; j < daySorted.size(); j++){
                if (mCount != 0 && daySorted.get(j) < timeLess) {
                    for (Reminder rem : items) {
                        int timeS = Integer.parseInt(getFormattedTime(rem));
                        if(daySorted.get(j) == timeS){
                            less = rem;
                            break;
                        }
                    }
                }
                else if (mCount == 0 && daySorted.get(j) > time && daySorted.get(j) < timeLess) {
                    for (Reminder rem : items) {
                        int timeS = Integer.parseInt(getFormattedTime(rem));
                        if(daySorted.get(j) == timeS){
                            less = rem;
                            break;
                        }
                    }
                }
            }
    //display next class
            String minutes;
            if(less.getMin() < 10){
                minutes = "0" + String.valueOf(less.getMin());
            }
            else {
                minutes = String.valueOf(less.getMin());
            }
            String formattedTime = String.valueOf(less.getHour()) + ":" + minutes;
            try {
                Date timeAsDate = mInputFormatter.parse(formattedTime);
                formattedTime = mFormatter.format(timeAsDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //Displays next class
            TextViews.getInstance().setClassTimeMain(formattedTime);
            TextViews.getInstance().setClassNameMain(less.getDescription());
            TextViews.getInstance().setClassMain();
        }
    }
    private String getFormattedTime(Reminder vRem){
        String vMinutes;
        if(vRem.getMin() < 10){
            vMinutes = "0" + String.valueOf(vRem.getMin());
        }
        else {
            vMinutes = String.valueOf(vRem.getMin());
        }
        return String.valueOf(vRem.getHour()) + vMinutes;
    }

    private void scheduleAlarm(String description, int day, int hour, int minute, String progress, boolean repeating, int timeGoal, boolean reminder) {
        String[] splitString = progress.split(":");
        int hourMute = Integer.parseInt(splitString[0]);
        int minuteMute = Integer.parseInt(splitString[1]);
        Reminder r = find(description);
        if(mContext == null) { return; }
        alarmManager = (AlarmManager) mContext.getSystemService(ALARM_SERVICE);
        muteManager = (AlarmManager) mContext.getSystemService(ALARM_SERVICE);

        Intent intentM = new Intent(mContext, AlarmMute.class);
        intentM.addCategory("android.intent.category.DEFAULT");
        intentM.putExtra("id", mId);
        intentM.putExtra("hour", hourMute);
        intentM.putExtra("minute", minuteMute);
        intentM.putExtra("hourStart", hour);
        intentM.putExtra("day", day);
        intentM.putExtra("repeating", repeating);
        intentM.putExtra("reminder", reminder);
        intentM.putExtra("minuteStart", minute);
        intentM.putExtra("string_name", description);
        Intent intent = new Intent(mContext, AlarmReceiver.class);
        intent.addCategory("android.intent.category.DEFAULT");
        intent.putExtra("id", mId);
        intent.putExtra("string_name", description);
        intent.putExtra("repeating", repeating);
        intent.putExtra("reminder", reminder);
        intent.putExtra("setBack", timeGoal);

//Sending time to alarm.xml
        String timer = String.valueOf(hour)+":"+String.valueOf(minute);
        try {
            Date timeAsDate = mInputFormatter.parse(timer);
            timer = mFormatter.format(timeAsDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        intent.putExtra("string_time", timer);
//setting reminder alarm
        int setHour = hour;
        int setMinute = minute;
        if(setBack > minute){
            for(int i = 0; i < setBack; i++){
                if(setMinute == 0){
                    setMinute = 60;
                    if(setHour == 1){
                        setHour = 13;
                    }
                    setHour--;
                }
                setMinute--;
            }
        }
        else{
            setMinute -= setBack;
        }

        broadcast = PendingIntent.getBroadcast(mContext, mId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        mutePending = PendingIntent.getBroadcast(mContext, mId, intentM, PendingIntent.FLAG_CANCEL_CURRENT);
        // save reference to scheduled notifications and their broadcast ids
        ArrayList<Integer> values = mReminders.get(description);
        if(values==null) {
            values = new ArrayList<>();
        }
        values.add(mId);
        mReminders.put(description, values);
        Log.i("Reminders", "Reminder ids for "+mId+":"+description+": "+values.size());
        mId++;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, day);
        calendar.set(Calendar.HOUR_OF_DAY, setHour);
        calendar.set(Calendar.MINUTE, setMinute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Calendar calendarM = Calendar.getInstance();
        calendarM.set(Calendar.DAY_OF_WEEK, day);
        calendarM.set(Calendar.HOUR_OF_DAY, hour);
        calendarM.set(Calendar.MINUTE, minute);
        calendarM.set(Calendar.SECOND, 0);
        calendarM.set(Calendar.MILLISECOND, 0);

        long time = calendar.getTimeInMillis();
        if(time<System.currentTimeMillis()){
            time=time+(7*AlarmManager.INTERVAL_DAY);
        }
        long times = calendarM.getTimeInMillis();
        if(times<System.currentTimeMillis()){
            times=times+(7*AlarmManager.INTERVAL_DAY);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(repeating) {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, 7*AlarmManager.INTERVAL_DAY, broadcast);
                muteManager.setRepeating(AlarmManager.RTC_WAKEUP, times, 7*AlarmManager.INTERVAL_DAY, mutePending);
            }
            else{
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, broadcast);
                muteManager.setExact(AlarmManager.RTC_WAKEUP, times, mutePending);
            }
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if(repeating) {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, AlarmManager.INTERVAL_DAY * 7, broadcast);
                muteManager.setRepeating(AlarmManager.RTC_WAKEUP, times, AlarmManager.INTERVAL_DAY * 7, mutePending);
            }
            else{
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, broadcast);
                muteManager.setExact(AlarmManager.RTC_WAKEUP, times, mutePending);
            }
        }
        else {
            if(repeating) {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, AlarmManager.INTERVAL_DAY * 7, broadcast);
                muteManager.setRepeating(AlarmManager.RTC_WAKEUP, times, AlarmManager.INTERVAL_DAY * 7, mutePending);
            }
            else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, time, broadcast);
                muteManager.set(AlarmManager.RTC_WAKEUP, times, mutePending);
            }
        }

    }

}