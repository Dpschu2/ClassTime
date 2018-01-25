package io.github.httpsdpschu2.classtime.Alarm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import io.github.httpsdpschu2.classtime.Activities.MainActivity;
import io.github.httpsdpschu2.classtime.Activities.PreferenceVariables;
import io.github.httpsdpschu2.classtime.Activities.PreferenceVars;
import io.github.httpsdpschu2.classtime.Fragments.PreferenceManaging;
import io.github.httpsdpschu2.classtime.R;
import io.github.httpsdpschu2.classtime.managers.RealmManager;
import io.github.httpsdpschu2.classtime.managers.ReminderManager;
import io.github.httpsdpschu2.classtime.models.Reminder;
import io.realm.Realm;

/**
 * Created by Dean on 2/20/2017.
 */

public class AlarmReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int mId = 0;
            PowerManager pm = (PowerManager) context.getApplicationContext().getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
            wakeLock.acquire();

            String name = intent.getStringExtra("string_name");
            String time = intent.getStringExtra("string_time");
            boolean reminder = intent.getBooleanExtra("reminder", false);
            int setBack = 0;
            int back;
            back = intent.getIntExtra("setBack", setBack);
            Log.i("setBack", String.valueOf(back));
            //ReminderManager.getInstance().remove(name);
            PreferenceManaging preferenceManaging = new PreferenceManaging(context);
            Realm mRealm = RealmManager.getInstance().getRealm(context);
            if(mRealm.where(Reminder.class).equalTo("description", name).findFirst() != null && preferenceManaging.getNot() && reminder) {
                Intent i = new Intent();
                i.setClassName("io.github.httpsdpschu2.classtime", "io.github.httpsdpschu2.classtime.Alarm.AlarmSound");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("stringName", name);
                i.putExtra("stringTime", time);
                context.startActivity(i);
            }

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_notifications_active_black_24dp)
                            .setContentTitle("Next class:    " + name)
                            .setContentText("              Time:    " + time)
                            .setAutoCancel(true)
                            .setDefaults(-1);

            // Creates an explicit intent for an Activity in your app
            Intent resultIntent = new Intent(context, MainActivity.class);
            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            PreferenceVars preferenceVars = new PreferenceVars();
            preferenceVars.getContext(context);
            Log.i("notification!!!", String.valueOf(preferenceVars.getNot()));
            if(preferenceVars.getNot() == 1 && preferenceManaging.getNot() && reminder)
                vibrator.vibrate(1000);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            // Adds the back stack for the Intent (but not the Intent itself)
            stackBuilder.addParentStack(MainActivity.class);
            // Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);

            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            // mId allows you to update the notification later on.
            if(preferenceManaging.getNot() && back != 0 && reminder)
                mNotificationManager.notify(mId, mBuilder.build());

            wakeLock.release();
        }

}
