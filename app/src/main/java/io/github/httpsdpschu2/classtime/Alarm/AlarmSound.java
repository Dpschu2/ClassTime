package io.github.httpsdpschu2.classtime.Alarm;

/**
 * Created by Dean on 2/11/2017.
 */
import android.app.ActionBar;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toolbar;

import java.io.IOException;

import io.github.httpsdpschu2.classtime.Activities.AppCompatPreferenceActivity;
import io.github.httpsdpschu2.classtime.Activities.MainActivity;
import io.github.httpsdpschu2.classtime.Activities.PreferenceVariables;
import io.github.httpsdpschu2.classtime.Activities.PreferenceVars;
import io.github.httpsdpschu2.classtime.Fragments.PreferenceManaging;
import io.github.httpsdpschu2.classtime.R;

import static java.security.AccessController.getContext;

public class AlarmSound extends AppCompatPreferenceActivity {
    private MediaPlayer player;
    final Context context=this;

    View view;
    TextView name;
    TextView time;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.alarm);
        android.support.v7.app.ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        PreferenceVars preferenceVars = new PreferenceVars();
        preferenceVars.getContext(context);
        if(getActionBar() != null){
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if(preferenceVars.getTheme() == 0){
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#03a7ff")));
        }
        if(preferenceVars.getTheme() == 1){
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f53500")));
        }
        if(preferenceVars.getTheme() == 2){
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#07d300")));
        }
        if(preferenceVars.getTheme() == 3){
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3f3f3f")));
        }
        SharedPreferences listPreference = android.support.v7.preference.PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());//(SharedPreferences) findPreference("notification_dialog");
        String value = listPreference.getString("example_text",
                "Class Time!");
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(value);

        //ReminderManager.getInstance().getNextAlarm();
        name = (TextView) findViewById(R.id.className);
        time = (TextView) findViewById(R.id.classTime);
        String stringName = getIntent().getStringExtra("stringName");
        String stringTime = getIntent().getStringExtra("stringTime");
        name.setText(stringName);
        time.setText(stringTime);

        Button stop = (Button) findViewById(R.id.alarm_stop);
        stop.setPadding(0,0,0,0);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)  {
                Vibrator vb = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                vb.vibrate(10);
                Intent i = new Intent(context, MainActivity.class);
                startActivity(i);
                finish();
                KeyguardManager keyguardManager = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
                KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("TAG");
                keyguardLock.disableKeyguard();
            }
        });
        PreferenceVars preferenceVar = new PreferenceVars();
        preferenceVar.getContext(context);
        PreferenceManaging preferenceManaging = new PreferenceManaging(context);
        Log.i("notif", String.valueOf(preferenceVar.getNot()));
        if (preferenceManaging.getNot() && preferenceVar.getNot() == 2) {
            play(this, getAlarmSound());
            Log.i("played", "!");
        }

    }
    private void play(Context context, Uri alert) {
        player = new MediaPlayer();
        try {
            if(player.isPlaying())
            {
                player.stop();
            }
            player.reset();
            player.setDataSource(context, alert);
            final AudioManager audio = (AudioManager) context
                    .getSystemService(Context.AUDIO_SERVICE);
            if (audio.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                player.setAudioStreamType(AudioManager.STREAM_ALARM);
                player.prepare();
                PreferenceVariables preferenceVariables = new PreferenceVariables();
                preferenceVariables.setContext(this);
                Log.i("volume", String.valueOf(preferenceVariables.getVolume()));
                player.setVolume(preferenceVariables.getVolume() * 2, 50);
                player.start();
            }
        } catch (IOException e) {
            Log.e("Error....","Check code...");
        }
    }

    private Uri getAlarmSound() {
        SharedPreferences getAlarms = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String alarms = getAlarms.getString("notifications_new_message_ringtone", "default ringtone");
        Uri uri = Uri.parse(alarms);
        return uri;
    }
}

