package io.github.httpsdpschu2.classtime.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;

import io.github.httpsdpschu2.classtime.Activities.MainActivity;
import io.github.httpsdpschu2.classtime.Activities.PreferenceVars;
import io.github.httpsdpschu2.classtime.Adapters.ReminderAdapter;
import io.github.httpsdpschu2.classtime.R;
import io.github.httpsdpschu2.classtime.managers.ReminderManager;
import io.github.httpsdpschu2.classtime.models.Reminder;

/**
 * Created by Dean on 2/18/17.
 */
public class NotificationDialog extends DialogFragment {

    TimePicker mTimePicker;
    TextView mGoalType;
    EditText mGoalText, classHour, classMinute;
    TextInputEditText mDescription;
    TextInputLayout mDescriptionLayout;
    ToggleButton mSun, mMon, mTue, mWed, mThu, mFri, mSat;
    SwitchCompat repeating;
    SwitchCompat reminder;
    String mKey;
    Reminder mReminder;
    String[] v;
    PreferenceVars preferenceVars = new PreferenceVars();
    int i = 0;
    public NotificationDialog() {
    }

    public static NotificationDialog newInstance(String key) {
        NotificationDialog dialog = new NotificationDialog();
        Bundle args = new Bundle();
        args.putString("key", key);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onDismiss(final DialogInterface dialog){
        super.onDismiss(dialog);
        ((DialogInterface.OnDismissListener)getActivity()).onDismiss(dialog);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.add_main, container);
        LinearLayout linearLayout = (LinearLayout) root.findViewById(R.id.not);
        PreferenceVars preferenceV = new PreferenceVars();
        preferenceV.getContext(getContext());
        if(preferenceV.getTheme() == 0){
            linearLayout.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        if(preferenceV.getTheme() == 1){
            linearLayout.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        if(preferenceV.getTheme() == 2){
            linearLayout.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        if(preferenceV.getTheme() == 3){
            linearLayout.setBackgroundColor(Color.parseColor("#BDBDBD"));
        }
        mTimePicker = (TimePicker) root.findViewById(R.id.notification_timepicker);
        mDescription = (TextInputEditText) root.findViewById(R.id.notification_description);
        mDescription.addTextChangedListener(new NotificationTextWatcher(mDescription));
        mDescriptionLayout = (TextInputLayout) root.findViewById(R.id.notification_description_layout);
        mSun = (ToggleButton) root.findViewById(R.id.sunBtn);
        mMon = (ToggleButton) root.findViewById(R.id.monBtn);
        mTue = (ToggleButton) root.findViewById(R.id.tueBtn);
        mWed = (ToggleButton) root.findViewById(R.id.wedBtn);
        mThu = (ToggleButton) root.findViewById(R.id.thuBtn);
        mFri = (ToggleButton) root.findViewById(R.id.friBtn);
        mSat = (ToggleButton) root.findViewById(R.id.satBtn);
        mGoalText = (EditText) root.findViewById(R.id.notification_goal);
        mGoalType = (TextView) root.findViewById(R.id.notification_goal_type);
        classHour = (EditText)root.findViewById(R.id.classDurationHours);
        classMinute = (EditText)root.findViewById(R.id.classDurationMinutes);
        repeating = (SwitchCompat) root.findViewById(R.id.repeating_switch);
        reminder = (SwitchCompat) root.findViewById(R.id.reminder_switch);
        ImageButton save = (ImageButton) root.findViewById(R.id.notification_submit);
        View line = (View)root.findViewById(R.id.line);
        TextView classInfo = (TextView) root.findViewById(R.id.classInfo);
        preferenceVars.getContext(getContext());
        if (preferenceVars.getTheme() == 0) {
            classInfo.setTextColor(Color.parseColor("#1dd9f6"));
            line.setBackgroundColor(Color.parseColor("#1dd9f6"));
            //save.setBackgroundResource(R.drawable.send_button);
            mDescription.setLinkTextColor(Color.parseColor("#1dd9f6"));
            if (Build.VERSION.SDK_INT >= 21) {
                save.setBackgroundResource(0);
                save.setImageResource(R.drawable.send_button);
                save.setScaleType(ImageView.ScaleType.FIT_XY);
                mDescription.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1dd9f6")));
            }
            else{
                save.setBackgroundDrawable( getResources().getDrawable(R.drawable.send_button) );
            }
        }
        if (preferenceVars.getTheme() == 1) {
            classInfo.setTextColor(Color.parseColor("#ff523f"));
            line.setBackgroundColor(Color.parseColor("#ff523f"));
            //save.setBackgroundResource(R.drawable.send_button_red);
            mDescription.setLinkTextColor(Color.parseColor("#ff523f"));
            if (Build.VERSION.SDK_INT >= 21) {
                save.setBackgroundResource(0);
                save.setImageResource(R.drawable.send_button_red);
                save.setScaleType(ImageView.ScaleType.FIT_XY);
                mDescription.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff523f")));
            }
            else{
                save.setBackgroundDrawable( getResources().getDrawable(R.drawable.send_button_red) );
            }
        }
        if (preferenceVars.getTheme() == 2) {
            classInfo.setTextColor(Color.parseColor("#00fc8f"));
            line.setBackgroundColor(Color.parseColor("#00fc8f"));
            //save.setBackgroundResource(R.drawable.send_button_green);
            mDescription.setLinkTextColor(Color.parseColor("#00fc8f"));
            if (Build.VERSION.SDK_INT >= 21) {
                save.setBackgroundResource(0);
                save.setImageResource(R.drawable.send_button_green);
                save.setScaleType(ImageView.ScaleType.FIT_XY);
                mDescription.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00fc8f")));
            }
            else{
                save.setBackgroundDrawable( getResources().getDrawable(R.drawable.send_button_green) );
            }
        }
        if (preferenceVars.getTheme() == 3) {
            classInfo.setTextColor(Color.parseColor("#3f3f3f"));
            line.setBackgroundColor(Color.parseColor("#3f3f3f"));
            //save.setBackgroundResource(R.drawable.send_button_green);
            mDescription.setLinkTextColor(Color.parseColor("#3f3f3f"));
            if (Build.VERSION.SDK_INT >= 21) {
                save.setBackgroundResource(0);
                save.setImageResource(R.drawable.send_button_dark);
                save.setScaleType(ImageView.ScaleType.FIT_XY);
                mDescription.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#3f3f3f")));
            }
            else{
                save.setBackgroundDrawable( getResources().getDrawable(R.drawable.send_button_dark) );
            }
        }
        if(getArguments().getString("key") != null) {
            if (ReminderManager.getInstance().find(getArguments().getString("key")).isRepeating()) {
                if (preferenceVars.getTheme() == 0) {
                    repeating.getThumbDrawable().setColorFilter(Color.parseColor("#03A9F4"), PorterDuff.Mode.MULTIPLY);
                    repeating.getTrackDrawable().setColorFilter(Color.argb(128, 3, 169, 244), PorterDuff.Mode.MULTIPLY);
                }
                if (preferenceVars.getTheme() == 1) {
                    repeating.getThumbDrawable().setColorFilter(Color.parseColor("#f53500"), PorterDuff.Mode.MULTIPLY);
                    repeating.getTrackDrawable().setColorFilter(Color.parseColor("#ff828f"), PorterDuff.Mode.MULTIPLY);
                }
                if (preferenceVars.getTheme() == 2) {
                    repeating.getThumbDrawable().setColorFilter(Color.parseColor("#07d300"), PorterDuff.Mode.MULTIPLY);
                    repeating.getTrackDrawable().setColorFilter(Color.parseColor("#92fb92"), PorterDuff.Mode.MULTIPLY);
                }
                if (preferenceVars.getTheme() == 3) {
                    repeating.getThumbDrawable().setColorFilter(Color.parseColor("#0b0b0b"), PorterDuff.Mode.MULTIPLY);
                    repeating.getTrackDrawable().setColorFilter(Color.parseColor("#353535"), PorterDuff.Mode.MULTIPLY);
                }
            }
        }
        else if(getArguments().getString("key") == null) {
                if (preferenceVars.getTheme() == 0) {
                    repeating.getThumbDrawable().setColorFilter(Color.parseColor("#03A9F4"), PorterDuff.Mode.MULTIPLY);
                    repeating.getTrackDrawable().setColorFilter(Color.argb(128, 3, 169, 244), PorterDuff.Mode.MULTIPLY);
                }
                if (preferenceVars.getTheme() == 1) {
                    repeating.getThumbDrawable().setColorFilter(Color.parseColor("#f53500"), PorterDuff.Mode.MULTIPLY);
                    repeating.getTrackDrawable().setColorFilter(Color.parseColor("#ff828f"), PorterDuff.Mode.MULTIPLY);
                }
                if (preferenceVars.getTheme() == 2) {
                    repeating.getThumbDrawable().setColorFilter(Color.parseColor("#07d300"), PorterDuff.Mode.MULTIPLY);
                    repeating.getTrackDrawable().setColorFilter(Color.parseColor("#92fb92"), PorterDuff.Mode.MULTIPLY);
                }
                if (preferenceVars.getTheme() == 3) {
                    repeating.getThumbDrawable().setColorFilter(Color.parseColor("#0b0b0b"), PorterDuff.Mode.MULTIPLY);
                    repeating.getTrackDrawable().setColorFilter(Color.parseColor("#353535"), PorterDuff.Mode.MULTIPLY);
                }
        }
        if(getArguments().getString("key") != null) {
            if (ReminderManager.getInstance().find(getArguments().getString("key")).isReminder()) {
                if (preferenceVars.getTheme() == 0) {
                    reminder.getThumbDrawable().setColorFilter(Color.parseColor("#03A9F4"), PorterDuff.Mode.MULTIPLY);
                    reminder.getTrackDrawable().setColorFilter(Color.argb(128, 3, 169, 244), PorterDuff.Mode.MULTIPLY);
                }
                if (preferenceVars.getTheme() == 1) {
                    reminder.getThumbDrawable().setColorFilter(Color.parseColor("#f53500"), PorterDuff.Mode.MULTIPLY);
                    reminder.getTrackDrawable().setColorFilter(Color.parseColor("#ff828f"), PorterDuff.Mode.MULTIPLY);
                }
                if (preferenceVars.getTheme() == 2) {
                    reminder.getThumbDrawable().setColorFilter(Color.parseColor("#07d300"), PorterDuff.Mode.MULTIPLY);
                    reminder.getTrackDrawable().setColorFilter(Color.parseColor("#92fb92"), PorterDuff.Mode.MULTIPLY);
                }
                if (preferenceVars.getTheme() == 3) {
                    reminder.getThumbDrawable().setColorFilter(Color.parseColor("#0b0b0b"), PorterDuff.Mode.MULTIPLY);
                    reminder.getTrackDrawable().setColorFilter(Color.parseColor("#353535"), PorterDuff.Mode.MULTIPLY);
                }
            }
        }
        else if(getArguments().getString("key") == null) {
            reminder.getThumbDrawable().setColorFilter(Color.parseColor("#FAFAFA"), PorterDuff.Mode.MULTIPLY);
            reminder.getTrackDrawable().setColorFilter(Color.argb(66, 0, 0, 0), PorterDuff.Mode.MULTIPLY);
        }
//Left off coding repeating switch
        repeating.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Vibrator vb = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                vb.vibrate(10);
                if (isChecked) {
                    if(preferenceVars.getTheme() == 0) {
                        repeating.getThumbDrawable().setColorFilter(Color.parseColor("#03A9F4"), PorterDuff.Mode.MULTIPLY);
                        repeating.getTrackDrawable().setColorFilter(Color.argb(128, 3, 169, 244), PorterDuff.Mode.MULTIPLY);
                    }
                    if(preferenceVars.getTheme() == 1) {
                        repeating.getThumbDrawable().setColorFilter(Color.parseColor("#f53500"), PorterDuff.Mode.MULTIPLY);
                        repeating.getTrackDrawable().setColorFilter(Color.parseColor("#ff828f"), PorterDuff.Mode.MULTIPLY);
                    }
                    if(preferenceVars.getTheme() == 2) {
                        repeating.getThumbDrawable().setColorFilter(Color.parseColor("#07d300"), PorterDuff.Mode.MULTIPLY);
                        repeating.getTrackDrawable().setColorFilter(Color.parseColor("#92fb92"), PorterDuff.Mode.MULTIPLY);
                    }
                    if(preferenceVars.getTheme() == 3) {
                        repeating.getThumbDrawable().setColorFilter(Color.parseColor("#0b0b0b"), PorterDuff.Mode.MULTIPLY);
                        repeating.getTrackDrawable().setColorFilter(Color.parseColor("#353535"), PorterDuff.Mode.MULTIPLY);
                    }
                } else {
                    repeating.getThumbDrawable().setColorFilter(Color.parseColor("#FAFAFA"), PorterDuff.Mode.MULTIPLY);
                    repeating.getTrackDrawable().setColorFilter(Color.argb(66, 0, 0, 0), PorterDuff.Mode.MULTIPLY);
                }
            }
        });
        final android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(getContext());
        reminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Vibrator vb = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                vb.vibrate(10);
                Log.i("i", String.valueOf(i));
                Log.i("isChecked", String.valueOf(isChecked) );
                if (isChecked && i > 0) {
                    Log.i("pass in checked", "" );
                    if(preferenceVars.getTheme() == 0) {
                        reminder.getThumbDrawable().setColorFilter(Color.parseColor("#03A9F4"), PorterDuff.Mode.MULTIPLY);
                        reminder.getTrackDrawable().setColorFilter(Color.argb(128, 3, 169, 244), PorterDuff.Mode.MULTIPLY);
                    }
                    if(preferenceVars.getTheme() == 1) {
                        reminder.getThumbDrawable().setColorFilter(Color.parseColor("#f53500"), PorterDuff.Mode.MULTIPLY);
                        reminder.getTrackDrawable().setColorFilter(Color.parseColor("#ff828f"), PorterDuff.Mode.MULTIPLY);
                    }
                    if(preferenceVars.getTheme() == 2) {
                        reminder.getThumbDrawable().setColorFilter(Color.parseColor("#07d300"), PorterDuff.Mode.MULTIPLY);
                        reminder.getTrackDrawable().setColorFilter(Color.parseColor("#92fb92"), PorterDuff.Mode.MULTIPLY);
                    }
                    if(preferenceVars.getTheme() == 3) {
                        reminder.getThumbDrawable().setColorFilter(Color.parseColor("#0b0b0b"), PorterDuff.Mode.MULTIPLY);
                        reminder.getTrackDrawable().setColorFilter(Color.parseColor("#353535"), PorterDuff.Mode.MULTIPLY);
                    }
                    final android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(getContext());
                    alertDialogBuilder.setTitle("Reminder");
                    LinearLayout layout = new LinearLayout(getContext());
                    layout.setOrientation(LinearLayout.VERTICAL);
                    alertDialogBuilder
                            .setMessage("Input number of minutes before class to be reminded");
                            final EditText input = new EditText(getContext());
                            input.setLines(1);
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
                    input.setInputType(InputType.TYPE_CLASS_NUMBER);
                    input.setRawInputType(Configuration.KEYBOARD_12KEY);
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(150, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layout.setGravity(Gravity.CENTER_HORIZONTAL);
                    input.setGravity(Gravity.CENTER_HORIZONTAL);
                    String s = "" + mGoalText.getText().toString();
                    if(getArguments().getString("key") != null)
                        input.setText(s);
                    input.setLayoutParams(lp);
                    layout.addView(input, lp);
                    alertDialogBuilder.setView(layout);
                    alertDialogBuilder.setCancelable(false)
                            .setNegativeButton("Cancel",new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int id) {
                                    reminder.setChecked(false);
                                    dialog.cancel();
                                }
                            })
                            .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    mGoalText.setText(String.valueOf(input.getText().toString()));
                                    if(mGoalText.getText().toString().equals("")) {
                                        reminder.setChecked(false);
                                    }
                                    dialog.cancel();
                                }
                            });

                    // create alert dialog
                    android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
//                  pulls up keyboard  alertDialog.getWindow().setSoftInputMode(
//                            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    alertDialog.show();
                }
                else if (isChecked && i == 0) {
                    Log.i("pass in checked", "");
                    if (preferenceVars.getTheme() == 0) {
                        reminder.getThumbDrawable().setColorFilter(Color.parseColor("#03A9F4"), PorterDuff.Mode.MULTIPLY);
                        reminder.getTrackDrawable().setColorFilter(Color.argb(128, 3, 169, 244), PorterDuff.Mode.MULTIPLY);
                    }
                    if (preferenceVars.getTheme() == 1) {
                        reminder.getThumbDrawable().setColorFilter(Color.parseColor("#f53500"), PorterDuff.Mode.MULTIPLY);
                        reminder.getTrackDrawable().setColorFilter(Color.parseColor("#ff828f"), PorterDuff.Mode.MULTIPLY);
                    }
                    if (preferenceVars.getTheme() == 2) {
                        reminder.getThumbDrawable().setColorFilter(Color.parseColor("#07d300"), PorterDuff.Mode.MULTIPLY);
                        reminder.getTrackDrawable().setColorFilter(Color.parseColor("#92fb92"), PorterDuff.Mode.MULTIPLY);
                    }
                    if (preferenceVars.getTheme() == 3) {
                        reminder.getThumbDrawable().setColorFilter(Color.parseColor("#0b0b0b"), PorterDuff.Mode.MULTIPLY);
                        reminder.getTrackDrawable().setColorFilter(Color.parseColor("#353535"), PorterDuff.Mode.MULTIPLY);
                    }
                }
                else {
                    reminder.getThumbDrawable().setColorFilter(Color.parseColor("#FAFAFA"), PorterDuff.Mode.MULTIPLY);
                    reminder.getTrackDrawable().setColorFilter(Color.argb(66, 0, 0, 0), PorterDuff.Mode.MULTIPLY);
                }
                i++;
            }

        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator vb = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                vb.vibrate(25);
                String desc = mDescription.getText().toString();
                int goal = 0;
                if(!mGoalText.getText().toString().equals("")) {
                    goal = Integer.parseInt(mGoalText.getText().toString());
                }
                if(desc.equals("")){
                    mDescriptionLayout.setError("This field is empty");
                    return;
                }
                if(!buttonsSelected()){
                    Toast.makeText(getContext(),"Please select a day to be reminded", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(classHour.getText().toString().equals("") || classMinute.getText().toString().equals("")){
                    Toast.makeText(getContext(),"Please input class length", Toast.LENGTH_SHORT).show();
                    return;
                }
                String value = classHour.getText().toString();
                String valueM = classMinute.getText().toString();
                String classDuration = value + ":" + valueM;
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR, mTimePicker.getCurrentHour());
                cal.set(Calendar.MINUTE, mTimePicker.getCurrentMinute());
                long mil = cal.getTimeInMillis();
                if(mKey == null) {
                    if(ReminderManager.getInstance().exists(desc)){
                        Toast.makeText(getContext(), "Please try a different description", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                        ReminderManager.getInstance().create(desc, mTimePicker.getHour(), mTimePicker.getMinute(), getDays(), goal, mil, classDuration, getRepeating(), getReminder());
                    } else {
                        ReminderManager.getInstance().create(desc, mTimePicker.getCurrentHour(), mTimePicker.getCurrentMinute(), getDays(), goal, mil, classDuration, getRepeating(), getReminder());
                    }
                } else {
                    if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                        ReminderManager.getInstance().update(mKey, desc, mTimePicker.getHour(), mTimePicker.getMinute(), getDays(), goal, mil, classDuration, getRepeating(), getReminder());
                    } else {
                        ReminderManager.getInstance().update(mKey, desc, mTimePicker.getCurrentHour(), mTimePicker.getCurrentMinute(), getDays(), goal, mil, classDuration, getRepeating(), getReminder());
                    }
                }
                AlertDialog dialog = alertDialogBuilder.create();
                dialog.cancel();
                Toast.makeText(getContext(), "alarm set", Toast.LENGTH_LONG).show();
                dismiss();
            }
        });

        mKey = getArguments().getString("key");
        if(mKey != null) {
            mReminder = ReminderManager.getInstance().find(mKey);
            if(!mReminder.isReminder())
                i++;
            fillFields(mReminder);
        }
        else
            i++;

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private boolean buttonsSelected() {
        return mSun.isChecked() || mMon.isChecked() || mTue.isChecked() || mWed.isChecked() || mThu.isChecked() || mFri.isChecked() || mSat.isChecked();
    }

    private void fillFields(Reminder r) {
        int value = r.getTimeGoal();
        String type = "minutes before class";
        mGoalText.setText(String.valueOf(value));
        mGoalType.setText(type);
        classHour.setText(r.getProgress().split(":")[0]);
        classMinute.setText(r.getProgress().split(":")[1]);
        mDescription.setText(r.getDescription());
        repeating.setChecked(r.isRepeating());
        reminder.setChecked(r.isReminder());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mTimePicker.setHour(r.getHour());
            mTimePicker.setMinute(r.getMin());
        } else {
            mTimePicker.setCurrentHour(r.getHour());
            mTimePicker.setCurrentMinute(r.getMin());
        }
        setDays(r.getDays());
    }

    private void setDays(String days) {
        char[] dayArray = days.toCharArray();
        v = new String[7];
        for(int ind=0; ind<dayArray.length; ind++) {
            if(Integer.parseInt(Character.toString(dayArray[ind])) == 1) {
                switch(ind){
                    case 0:
                        mSun.setChecked(true);
                        v[ind] = "0";
                        break;
                    case 1:
                        mMon.setChecked(true);
                        v[ind] = "1";
                        break;
                    case 2:
                        mTue.setChecked(true);
                        v[ind] = "2";
                        break;
                    case 3:
                        mWed.setChecked(true);
                        v[ind] = "3";
                        break;
                    case 4:
                        mThu.setChecked(true);
                        v[ind] = "4";
                        break;
                    case 5:
                        mFri.setChecked(true);
                        v[ind] = "5";
                        break;
                    case 6:
                        mSat.setChecked(true);
                        v[ind] = "6";
                        break;
                }
            }
        }
        if(!Arrays.asList(v).contains("0"))
            mSun.setChecked(false);
        if(!Arrays.asList(v).contains("1"))
            mMon.setChecked(false);
        if(!Arrays.asList(v).contains("2"))
            mTue.setChecked(false);
        if(!Arrays.asList(v).contains("3"))
            mWed.setChecked(false);
        if(!Arrays.asList(v).contains("4"))
            mThu.setChecked(false);
        if(!Arrays.asList(v).contains("5"))
            mFri.setChecked(false);
        if(!Arrays.asList(v).contains("6"))
            mSat.setChecked(false);

    }

    private boolean getRepeating(){
        if(repeating.isChecked())
            return true;
        else
            return false;
    }
    private boolean getReminder(){
        if(reminder.isChecked())
            return true;
        else
            return false;
    }
    private String getDays() {
        String ret = "";
        ret = mSun.isChecked() ? ret+"1" : ret+"0";
        ret = mMon.isChecked() ? ret+"1" : ret+"0";
        ret = mTue.isChecked() ? ret+"1" : ret+"0";
        ret = mWed.isChecked() ? ret+"1" : ret+"0";
        ret = mThu.isChecked() ? ret+"1" : ret+"0";
        ret = mFri.isChecked() ? ret+"1" : ret+"0";
        ret = mSat.isChecked() ? ret+"1" : ret+"0";
        return ret;
    }

    private class NotificationTextWatcher implements TextWatcher {

        private View view;

        private NotificationTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String value = charSequence.toString();
            if(value.equals("")){
                mDescriptionLayout.setError("This field is empty");
            } else {
                mDescriptionLayout.setErrorEnabled(false);
            }
        }

        public void afterTextChanged(Editable editable) {

        }
    }
}
