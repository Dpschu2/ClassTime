package io.github.httpsdpschu2.classtime.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import io.github.httpsdpschu2.classtime.Fragments.MenuFragment;
import io.github.httpsdpschu2.classtime.R;
import io.github.httpsdpschu2.classtime.managers.RealmManager;

/**
 * Created by Dean on 3/28/2017.
 */

public class HelperActivity extends AppCompatActivity  {


    public HelperActivity(){ }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        PreferenceVars preferenceVars = new PreferenceVars();
        preferenceVars.getContext(getBaseContext());
        if(preferenceVars.getTheme() == 0){
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#03a7ff")));
            //this.setTheme(R.style.PreferencesThemelight);
        }
        if(preferenceVars.getTheme() == 1){
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f53500")));
            //this.setTheme(R.style.PreferencesThemelight);
        }
        if(preferenceVars.getTheme() == 2){
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#07d300")));
            //this.setTheme(R.style.PreferencesThemelight);
        }
        if(preferenceVars.getTheme() == 3){
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3f3f3f")));
//            this.setTheme(R.style.PreferencesThemedark);
        }
        RealmManager.getInstance().getRealm(this);
        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new MenuFragment())
                .commit();
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(HelperActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Intent myIntent = new Intent(HelperActivity.this, MainActivity.class);
            startActivityForResult(myIntent, 0);
            Vibrator vb = (Vibrator) HelperActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
            vb.vibrate(15);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
