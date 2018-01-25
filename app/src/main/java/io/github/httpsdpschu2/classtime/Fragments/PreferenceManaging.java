package io.github.httpsdpschu2.classtime.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import io.github.httpsdpschu2.classtime.Activities.AppCompatPreferenceActivity;

/**
 * Created by Dean on 4/16/2017.
 */

public class PreferenceManaging extends AppCompatPreferenceActivity {
    Context context;
    public PreferenceManaging(Context context){
        this.context = context;
    }
    public boolean getNot(){
        //PreferenceManager preferenceManager = getPreferenceManager();
        SharedPreferences listPreference = android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(context);
        return listPreference.getBoolean("notifications_new_message", true);
        //return preferenceManager.getSharedPreferences().getBoolean("notifications_new_message", true);
    }
}
