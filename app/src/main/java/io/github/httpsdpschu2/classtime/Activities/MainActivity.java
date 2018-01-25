package io.github.httpsdpschu2.classtime.Activities;

import android.app.ActionBar;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.httpsdpschu2.classtime.Fragments.NotificationDialog;
import io.github.httpsdpschu2.classtime.MyService;
import io.github.httpsdpschu2.classtime.R;
import io.github.httpsdpschu2.classtime.Fragments.Reminders;
import io.github.httpsdpschu2.classtime.managers.RealmManager;
import io.github.httpsdpschu2.classtime.managers.ReminderManager;

public class MainActivity extends AppCompatActivity implements DialogInterface.OnDismissListener{
    ArrayList<Fragment> mFragments;
    private static MainActivity mInstance;
    private CardView card;
    public static MainActivity getInstance() {
        if(mInstance == null) {
            mInstance = new MainActivity();
        }
        return mInstance;
    }

    public MainActivity(){
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //PreferenceManaging.setDefaultValues(this, R.xml.pref_notification, true);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        stopService(new Intent(this, MyService.class));

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextViews.getInstance().setCollapsingToolbarLayout((CollapsingToolbarLayout) findViewById(R.id.toolbar_layout));

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        card = (CardView) findViewById(R.id.cardBack);
        NestedScrollView relativeLayout = (NestedScrollView) findViewById(R.id.nested);
        PreferenceVars preferenceVars = new PreferenceVars();
        preferenceVars.getContext(getBaseContext());

        if(preferenceVars.getTheme() == 0){
            collapsingToolbarLayout.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#03a7ff")));
            fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1dd9f6")));
            relativeLayout.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        if(preferenceVars.getTheme() == 1){
            collapsingToolbarLayout.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f53500")));
            fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff523f")));
            relativeLayout.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        if(preferenceVars.getTheme() == 2){
            collapsingToolbarLayout.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#07d300")));
            fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00fc8f")));
            relativeLayout.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        if(preferenceVars.getTheme() == 3){
            collapsingToolbarLayout.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3f3f3f")));
            fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#757575")));
            relativeLayout.setBackgroundColor(Color.parseColor("#222222"));
        }

        // Configure Realm for the application
        RealmManager.getInstance().getRealm(this);

        mFragments = new ArrayList<>();
        mFragments.add(new Reminders());

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.layout, mFragments.get(0));
        fragmentTransaction.commit();

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && !notificationManager.isNotificationPolicyAccessGranted()) {
            Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivity(intent);
        }
//displays icon
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        TextViews.getInstance().setClassNameMain("No classes");
        TextViews.getInstance().setClassTimeMain("");
        TextViews.getInstance().setClassMain();
//FAB
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Add class dialog
                Vibrator vb = (Vibrator) MainActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                vb.vibrate(25);
                createNotificationDialog();
            }
        });
        startService(new Intent(this, MyService.class));
    }
//Back button press
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
//Settings
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Vibrator vb = (Vibrator) MainActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
            vb.vibrate(15);
            Intent i = new Intent(this, HelperActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void createNotificationDialog() {
        FragmentManager fm = getSupportFragmentManager();
        NotificationDialog notificationDialog = NotificationDialog.newInstance(null);
        notificationDialog.show(fm, "fragment_edit_name");
    }
    public ArrayList<Fragment> getmFragments(){
        return mFragments;
    }
    @Override
    public void onDismiss(DialogInterface dialog) {
        // refresh the Reminder fragment when the notification dialog dismisses
        Reminders reminderFragment = (Reminders) mFragments.get(0);
        if(reminderFragment != null)
            reminderFragment.refresh();
    }
}

