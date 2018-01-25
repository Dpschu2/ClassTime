package io.github.httpsdpschu2.classtime.Fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.List;

import io.github.httpsdpschu2.classtime.Activities.HelperActivity;
import io.github.httpsdpschu2.classtime.Activities.MainActivity;
import io.github.httpsdpschu2.classtime.Adapters.ReminderAdapter;
import io.github.httpsdpschu2.classtime.R;
import io.github.httpsdpschu2.classtime.managers.RealmManager;
import io.github.httpsdpschu2.classtime.managers.ReminderManager;
import io.github.httpsdpschu2.classtime.models.Reminder;
import io.realm.Realm;

/**
 * Created by Dean on 3/30/2017.
 */

public class MenuFragment extends PreferenceFragment {
    private FragmentActivity myContext;

    private static MenuFragment mInstance;
    public static MenuFragment getInstance() {
        if(mInstance == null) {
            mInstance = new MenuFragment();
        }
        return mInstance;
    }
    public MenuFragment(){ }
    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load preferences from an XML resource
        addPreferencesFromResource(R.xml.pref_notification);
        //bindPreferenceSummaryToValue(findPreference("notifications_new_message_ringtone"));

        //Clear alarms
        Preference clear = (Preference) findPreference("clearSchedule");
        clear.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("Delete");
                alertDialogBuilder
                        .setMessage("Are you sure you want to delete your schedule?")
                        .setCancelable(true)
                        .setNegativeButton("Cancel",new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id) {
//                                mAdapter = Reminders.getInstance().getmAdapter();
//                                mAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                            }
                        })
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Realm mRealm = RealmManager.getInstance().getRealm(myContext);
                                SharedPreferences sharedPref = getActivity().getSharedPreferences("MyPREFERENCESS", Context.MODE_PRIVATE);
                                int size = Integer.parseInt(sharedPref.getString("itemCount", "0"));
                                List<Reminder> rem = ReminderManager.getInstance().getReminders();
                                if (size > 0) {
                                    mRealm.beginTransaction();
                                    rem.clear();
                                    mRealm.where(Reminder.class).findAll().clear();
                                    mRealm.commitTransaction();
                                }
                                ReminderManager.getInstance().getNextAlarm();
                                Toast.makeText(getActivity(), "schedule deleted", Toast.LENGTH_LONG).show();
                            }
                        });

                // create alert dialog
                android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                Button b = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                Button a = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                a.setTextColor(Color.parseColor("#0288D1"));
                b.setTextColor(Color.parseColor("#0288D1"));
                return true;
            }
        });

        final ListPreference list = (ListPreference)findPreference("themeKey");
        list.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                int index = list.findIndexOfValue(newValue.toString());

                if (index != -1)
                    Toast.makeText(myContext, "theme: " + list.getEntries()[index], Toast.LENGTH_LONG).show();

                Intent intent = getActivity().getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                getActivity().overridePendingTransition(0, 0);
                getActivity().finish();
                getActivity().overridePendingTransition(0, 0);
                startActivity(intent);
                return true;
            }
        });

        //Volume
        Preference volume = (Preference) findPreference("key_volume");
        volume.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                FragmentManager fm = myContext.getSupportFragmentManager();
                SetVolume notificationDialog = SetVolume.newInstance(null);
                notificationDialog.show(fm, "dialog");
                return true;
            }
        });

    }

}
