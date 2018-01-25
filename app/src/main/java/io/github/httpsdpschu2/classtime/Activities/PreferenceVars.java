package io.github.httpsdpschu2.classtime.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

/**
 * Created by Dean on 4/5/2017.
 */

public class PreferenceVars extends PreferenceFragment {
    private Context context;
    public PreferenceVars(){
    }
    public void getContext(Context context){
        this.context = context;
    }
    public int getNot() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int not = Integer.parseInt(prefs.getString("notification_dialog", "2"));
        return not;
    }
    public int getDuring() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int during = Integer.parseInt(prefs.getString("duringClass", "0"));
        return during;
    }
    public int getTheme() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int theme = Integer.parseInt(prefs.getString("themeKey", "0"));
        return theme;
    }
    public int getData() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int data = Integer.parseInt(prefs.getString("data", "0"));
        return data;
    }
}
