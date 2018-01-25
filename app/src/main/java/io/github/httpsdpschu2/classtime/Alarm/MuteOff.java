package io.github.httpsdpschu2.classtime.Alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import io.github.httpsdpschu2.classtime.Activities.MainActivity;
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

public class MuteOff extends BroadcastReceiver{

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
        @Override
        public void onReceive(Context context, Intent intent) {

            boolean repeating = intent.getBooleanExtra("repeating", true);
            Log.i("repeating", String.valueOf(repeating));
            String name = intent.getStringExtra("stringName");
            int data = intent.getIntExtra("data", 1);
            int ring = intent.getIntExtra("ring", 1);
            boolean state = intent.getBooleanExtra("state", true);

            if(data == 0){
                if(state)
                    turnOnDataConnection(context, true);
                else
                    turnOnDataConnection(context, false);
            }
            //unmute
            AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
            if(ring == 0)
                audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            if(ring == 1)
                audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            if(ring == 2)
                audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

            if(!repeating){
                Toast.makeText(context, name + " removed", Toast.LENGTH_LONG).show();
                List<Reminder> rem = ReminderManager.getInstance().getReminders();
                ReminderManager.getInstance().cancelAlarms(name);
                Realm mRealm = RealmManager.getInstance().getRealm(context);
                mRealm.beginTransaction();
                if(rem.size() == 1){
                    rem.clear();
                    mRealm.where(Reminder.class).findAll().clear();
                }
                else {
                    Reminder result = mRealm.where(Reminder.class).equalTo("description",name).findFirst();
                    result.removeFromRealm();
                }
                mRealm.commitTransaction();
                ReminderManager.getInstance().getNextAlarm();
                Intent i=new Intent(context,MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
//            //disables airplane mode
//            boolean isEnabled = Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) == 1;
//            if (isEnabled && Build.VERSION.SDK_INT < 4.2) {
//                Settings.System.putInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, isEnabled ? 1 : 0);
//                Intent intent1 = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
//                intent.putExtra("state", !isEnabled);
//                context.sendBroadcast(intent1);
//            }

        }

}
