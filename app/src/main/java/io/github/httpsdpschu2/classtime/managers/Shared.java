package io.github.httpsdpschu2.classtime.managers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;

import io.github.httpsdpschu2.classtime.Activities.AppCompatPreferenceActivity;

import static io.github.httpsdpschu2.classtime.Fragments.SetVolume.MyPREFERENCESS;

/**
 * Created by Dean on 4/7/2017.
 */

public class Shared extends AppCompatPreferenceActivity {
    public Shared(String key, String item, Activity activity){
        SharedPreferences sharedpreferences = activity.getSharedPreferences("MyPREFERENCESS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, item);
        editor.commit();
    }
}
