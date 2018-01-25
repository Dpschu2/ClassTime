package io.github.httpsdpschu2.classtime.Alarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Calendar;

import io.github.httpsdpschu2.classtime.Activities.PreferenceVariables;
import io.github.httpsdpschu2.classtime.Activities.PreferenceVars;
import io.github.httpsdpschu2.classtime.Fragments.PreferenceManaging;
import io.github.httpsdpschu2.classtime.managers.RealmManager;
import io.github.httpsdpschu2.classtime.managers.ReminderManager;
import io.github.httpsdpschu2.classtime.models.Reminder;
import io.realm.Realm;

import static android.content.ContentValues.TAG;
import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by Dean on 2/24/2017.
 */

public class AlarmMute extends BroadcastReceiver{

    AlarmManager muteManagers;
    PendingIntent mutePendings;
    Context mContext;
//    private void setMobileDataEnabled(Context context, boolean enabled) {
//        final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        final Class conmanClass = Class.forName(conman.getClass().getName());
//        final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
//        iConnectivityManagerField.setAccessible(true);
//        final Object iConnectivityManager = iConnectivityManagerField.get(conman);
//        final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
//        final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
//        setMobileDataEnabledMethod.setAccessible(true);
//        setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
//    }
int bv = Build.VERSION.SDK_INT;

    boolean turnOnDataConnection(Context context, boolean ON)
    {

        try{
            if(bv == Build.VERSION_CODES.FROYO)

            {
                Method dataConnSwitchmethod;
                Class<?> telephonyManagerClass;
                Object ITelephonyStub;
                Class<?> ITelephonyClass;

                TelephonyManager telephonyManager = (TelephonyManager) context
                        .getSystemService(Context.TELEPHONY_SERVICE);

                telephonyManagerClass = Class.forName(telephonyManager.getClass().getName());
                Method getITelephonyMethod = telephonyManagerClass.getDeclaredMethod("getITelephony");
                getITelephonyMethod.setAccessible(true);
                ITelephonyStub = getITelephonyMethod.invoke(telephonyManager);
                ITelephonyClass = Class.forName(ITelephonyStub.getClass().getName());

                if (ON) {
                    dataConnSwitchmethod = ITelephonyClass
                            .getDeclaredMethod("enableDataConnectivity");
                } else {
                    dataConnSwitchmethod = ITelephonyClass
                            .getDeclaredMethod("disableDataConnectivity");
                }
                dataConnSwitchmethod.setAccessible(true);
                dataConnSwitchmethod.invoke(ITelephonyStub);

            }
            else
            {
                //log.i("App running on Ginger bread+");
                final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
                final Class<?> conmanClass = Class.forName(conman.getClass().getName());
                final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
                iConnectivityManagerField.setAccessible(true);
                final Object iConnectivityManager = iConnectivityManagerField.get(conman);
                final Class<?> iConnectivityManagerClass =  Class.forName(iConnectivityManager.getClass().getName());
                final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
                setMobileDataEnabledMethod.setAccessible(true);
                setMobileDataEnabledMethod.invoke(iConnectivityManager, ON);
            }

            return true;
        }catch(Exception e){
            Log.e(TAG,"error turning on/off data");

            return false;
        }

    }

    public Boolean isMobileDataEnabled(){
        Object connectivityService = mContext.getSystemService(CONNECTIVITY_SERVICE);
        ConnectivityManager cm = (ConnectivityManager) connectivityService;

        try {
            Class<?> c = Class.forName(cm.getClass().getName());
            Method m = c.getDeclaredMethod("getMobileDataEnabled");
            m.setAccessible(true);
            return (Boolean)m.invoke(cm);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
        @Override
        public void onReceive(Context context, Intent intent) {
            int ring = 1;
            mContext = context;
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            switch( audioManager.getRingerMode() ){
                case AudioManager.RINGER_MODE_SILENT:
                    ring = 0;
                    break;
                case AudioManager.RINGER_MODE_VIBRATE:
                    ring = 1;
                    break;
                case AudioManager.RINGER_MODE_NORMAL:
                    ring = 2;
                    break;
            }
            NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(0);
            String name = intent.getStringExtra("string_name");
            boolean repeating = intent.getBooleanExtra("repeating", true);
            PreferenceManaging preferenceManaging = new PreferenceManaging(context);
            Realm mRealm = RealmManager.getInstance().getRealm(context);
            PreferenceVars preferenceVars = new PreferenceVars();
            preferenceVars.getContext(context);
            if(mRealm.where(Reminder.class).equalTo("description", name).findFirst() != null && preferenceVars.getDuring() != 2) {
                //mute
                if (preferenceVars.getDuring() == 0)
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);

                //vibrate
                if (preferenceVars.getDuring() == 1)
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                //data
                int data = 1;
                boolean state = isMobileDataEnabled();
                if (preferenceVars.getData() == 0){
                    turnOnDataConnection(context, false);
                    Log.i("state", String.valueOf(state));
                    data = 0;
                }
                //airplane mode
//                if (preferenceVars.getDuring() == 2) {
//                    boolean isEnabled = Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) == 1;
//                    audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
//                    if (Build.VERSION.SDK_INT < 4.2) {
//                        Settings.System.putInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, isEnabled ? 0 : 1);
//                        Intent intent1 = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
//                        intent.putExtra("state", !isEnabled);
//                        context.sendBroadcast(intent1);
//                    }
//                }

                int mId = intent.getIntExtra("id", 1);
                Intent intentM = new Intent(context, MuteOff.class);
                intentM.addCategory("android.intent.category.DEFAULT");
                intentM.putExtra("stringName", name);
                intentM.putExtra("repeating", repeating);
                intentM.putExtra("ring", ring);
                intentM.putExtra("state", state);
                intentM.putExtra("data", data);
                muteManagers = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                mutePendings = PendingIntent.getBroadcast(context, mId, intentM, PendingIntent.FLAG_CANCEL_CURRENT);

                int day = intent.getIntExtra("day", 1);
                int setHour = intent.getIntExtra("hour", 1);
                int setMinute = intent.getIntExtra("minute", 1);
                int hourStart = intent.getIntExtra("hourStart", 1);
                int minuteStart = intent.getIntExtra("minuteStart", 1);
                int time = ((hourStart * 60) + minuteStart) + ((setHour * 60) + setMinute);
                int hour = time / 60;
                int minute = time % 60;

                Calendar calendarM = Calendar.getInstance();
                calendarM.set(Calendar.DAY_OF_WEEK, day);
                calendarM.set(Calendar.HOUR_OF_DAY, hour);
                calendarM.set(Calendar.MINUTE, minute);
                calendarM.set(Calendar.SECOND, 0);
                calendarM.set(Calendar.MILLISECOND, 0);
                long times = calendarM.getTimeInMillis();
                if (times < System.currentTimeMillis()) {
                    times = times + (7 * AlarmManager.INTERVAL_DAY);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    muteManagers.setExact(AlarmManager.RTC_WAKEUP, times, mutePendings);
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    muteManagers.setExact(AlarmManager.RTC_WAKEUP, times, mutePendings);
                } else {
                    muteManagers.set(AlarmManager.RTC_WAKEUP, times, mutePendings);
                }
//                ReminderManager.getInstance().getNextAlarm();
            }

        }
}
