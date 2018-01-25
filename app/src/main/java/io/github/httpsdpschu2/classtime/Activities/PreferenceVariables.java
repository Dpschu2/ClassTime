package io.github.httpsdpschu2.classtime.Activities;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.view.View;
import android.widget.SeekBar;

import io.github.httpsdpschu2.classtime.Fragments.MenuFragment;
import io.github.httpsdpschu2.classtime.Fragments.SetVolume;
import io.github.httpsdpschu2.classtime.R;

import static android.content.Context.MODE_PRIVATE;
import static io.github.httpsdpschu2.classtime.Fragments.SetVolume.MyPREFERENCESS;
import static io.github.httpsdpschu2.classtime.R.id.seekBar;

/**
 * Created by Dean on 4/4/2017.
 */

public class PreferenceVariables extends SetVolume {
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    int volume;
    public PreferenceVariables(){
    }

    public void setContext(Activity context){
        sharedPref = context.getSharedPreferences("MyPREFERENCESS", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        volume = sharedPref.getInt("volume", 0);
    }

    public void SavePreferences(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }
    public int getVolume(){
        return volume;
    }
}
