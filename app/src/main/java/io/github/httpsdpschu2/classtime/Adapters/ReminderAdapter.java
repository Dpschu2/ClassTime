package io.github.httpsdpschu2.classtime.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Vibrator;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import io.github.httpsdpschu2.classtime.Activities.PreferenceVars;
import io.github.httpsdpschu2.classtime.R;
import io.github.httpsdpschu2.classtime.Activities.MainActivity;
import io.github.httpsdpschu2.classtime.Fragments.NotificationDialog;
import io.github.httpsdpschu2.classtime.Fragments.Reminders;
import io.github.httpsdpschu2.classtime.managers.ReminderManager;
import io.github.httpsdpschu2.classtime.managers.Shared;
import io.github.httpsdpschu2.classtime.models.Reminder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static io.github.httpsdpschu2.classtime.Fragments.SetVolume.MyPREFERENCESS;

/**
 * Created by Dean on 2/18/17.
 */
public final class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> implements Reminders.ItemTouchHelperAdapter {

    List<Reminder> mReminders;
    SimpleDateFormat mFormatter, mInputFormatter;
    Context context;
    PreferenceVars preferenceVars = new PreferenceVars();
    static MainActivity mActivity;
    private static ReminderAdapter mInstance;


    public static ReminderAdapter getInstance() {
        if(mInstance == null) {
            mInstance = new ReminderAdapter();
        }
        return mInstance;
    }
    public List<Reminder> getList(){
        return mReminders;
    }
    public ReminderAdapter(){ }
    public ReminderAdapter(List<Reminder> data, MainActivity activity){
        this.mReminders = data;
        this.mActivity = activity;
        new Shared("itemCount", String.valueOf(mReminders.size()), mActivity);
        mInputFormatter = new SimpleDateFormat("H:mm");
        mFormatter = new SimpleDateFormat("h:mm a", Locale.getDefault());
        // manually initialize every view because the recycler does not initialize all the items
        for(int i=0; i<mReminders.size(); i++) {
            Reminder r = mReminders.get(i);
            ReminderManager.getInstance().setActive(r.isActive(), r.getDescription());
        }
    }

    @Override
    public int getItemCount() {
        if (mReminders == null) { return 0; }
        return mReminders.size();
    }

    @Override
    public ReminderViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_main, viewGroup, false);
        context = viewGroup.getContext();

        preferenceVars.getContext(context);
        return new ReminderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ReminderViewHolder viewHolder, int i) {
        Reminder r = mReminders.get(i);
        String days = r.getDays();
        String[] v = new String[7];
        char[] dayArray = days.toCharArray();

        PreferenceVars preferenceV = new PreferenceVars();
        preferenceV.getContext(context);

        if(preferenceV.getTheme() == 0){
            ((CardView) viewHolder.itemView).setCardBackgroundColor(Color.parseColor("#FFFFFFFF"));
        }
        if(preferenceV.getTheme() == 1){
            ((CardView) viewHolder.itemView).setCardBackgroundColor(Color.parseColor("#FFFFFFFF"));
        }
        if(preferenceV.getTheme() == 2){
            ((CardView) viewHolder.itemView).setCardBackgroundColor(Color.parseColor("#FFFFFFFF"));
        }
        if(preferenceV.getTheme() == 3){
            ((CardView) viewHolder.itemView).setCardBackgroundColor(Color.parseColor("#a3a3a3"));
        }
        for(int ind=0; ind<dayArray.length; ind++) {
            if(Integer.parseInt(Character.toString(dayArray[ind])) == 1) {
                switch(ind+1){
                    case 1:
                        if(preferenceVars.getTheme() == 0)
                            viewHolder.mSun.setTextColor(Color.parseColor("#03A9F4"));
                        if(preferenceVars.getTheme() == 1)
                            viewHolder.mSun.setTextColor(Color.parseColor("#f53500"));
                        if(preferenceVars.getTheme() == 2)
                            viewHolder.mSun.setTextColor(Color.parseColor("#07d300"));
                        if(preferenceVars.getTheme() == 3)
                            viewHolder.mSun.setTextColor(Color.parseColor("#0b0b0b"));
                        v[ind] = "0";
                        break;
                    case 2:
                        if(preferenceVars.getTheme() == 0)
                            viewHolder.mMon.setTextColor(Color.parseColor("#03A9F4"));
                        if(preferenceVars.getTheme() == 1)
                            viewHolder.mMon.setTextColor(Color.parseColor("#f53500"));
                        if(preferenceVars.getTheme() == 2)
                            viewHolder.mMon.setTextColor(Color.parseColor("#07d300"));
                        if(preferenceVars.getTheme() == 3)
                            viewHolder.mMon.setTextColor(Color.parseColor("#0b0b0b"));
                        v[ind] = "1";
                        break;
                    case 3:
                        if(preferenceVars.getTheme() == 0)
                            viewHolder.mTue.setTextColor(Color.parseColor("#03A9F4"));
                        if(preferenceVars.getTheme() == 1)
                            viewHolder.mTue.setTextColor(Color.parseColor("#f53500"));
                        if(preferenceVars.getTheme() == 2)
                            viewHolder.mTue.setTextColor(Color.parseColor("#07d300"));
                        if(preferenceVars.getTheme() == 3)
                            viewHolder.mTue.setTextColor(Color.parseColor("#0b0b0b"));
                        v[ind] = "2";
                        break;
                    case 4:
                        if(preferenceVars.getTheme() == 0)
                            viewHolder.mWed.setTextColor(Color.parseColor("#03A9F4"));
                        if(preferenceVars.getTheme() == 1)
                            viewHolder.mWed.setTextColor(Color.parseColor("#f53500"));
                        if(preferenceVars.getTheme() == 2)
                            viewHolder.mWed.setTextColor(Color.parseColor("#07d300"));
                        if(preferenceVars.getTheme() == 3)
                            viewHolder.mWed.setTextColor(Color.parseColor("#0b0b0b"));
                        v[ind] = "3";
                        break;
                    case 5:
                        if(preferenceVars.getTheme() == 0)
                            viewHolder.mThu.setTextColor(Color.parseColor("#03A9F4"));
                        if(preferenceVars.getTheme() == 1)
                            viewHolder.mThu.setTextColor(Color.parseColor("#f53500"));
                        if(preferenceVars.getTheme() == 2)
                            viewHolder.mThu.setTextColor(Color.parseColor("#07d300"));
                        if(preferenceVars.getTheme() == 3)
                            viewHolder.mThu.setTextColor(Color.parseColor("#0b0b0b"));
                        v[ind] = "4";
                        break;
                    case 6:
                        if(preferenceVars.getTheme() == 0)
                            viewHolder.mFri.setTextColor(Color.parseColor("#03A9F4"));
                        if(preferenceVars.getTheme() == 1)
                            viewHolder.mFri.setTextColor(Color.parseColor("#f53500"));
                        if(preferenceVars.getTheme() == 2)
                            viewHolder.mFri.setTextColor(Color.parseColor("#07d300"));
                        if(preferenceVars.getTheme() == 3)
                            viewHolder.mFri.setTextColor(Color.parseColor("#0b0b0b"));
                        v[ind] = "5";
                        break;
                    case 7:
                        if(preferenceVars.getTheme() == 0)
                            viewHolder.mSat.setTextColor(Color.parseColor("#03A9F4"));
                        if(preferenceVars.getTheme() == 1)
                            viewHolder.mSat.setTextColor(Color.parseColor("#f53500"));
                        if(preferenceVars.getTheme() == 2)
                            viewHolder.mSat.setTextColor(Color.parseColor("#07d300"));
                        if(preferenceVars.getTheme() == 3)
                            viewHolder.mSat.setTextColor(Color.parseColor("#0b0b0b"));
                        v[ind] = "6";
                        break;
                }
            }
        }
        int textColor1 = viewHolder.reminderText.getCurrentTextColor();
        if(!Arrays.asList(v).contains("0"))
            viewHolder.mSun.setTextColor(textColor1);
        if(!Arrays.asList(v).contains("1"))
            viewHolder.mMon.setTextColor(textColor1);
        if(!Arrays.asList(v).contains("2"))
            viewHolder.mTue.setTextColor(textColor1);
        if(!Arrays.asList(v).contains("3"))
            viewHolder.mWed.setTextColor(textColor1);
        if(!Arrays.asList(v).contains("4"))
            viewHolder.mThu.setTextColor(textColor1);
        if(!Arrays.asList(v).contains("5"))
            viewHolder.mFri.setTextColor(textColor1);
        if(!Arrays.asList(v).contains("6"))
            viewHolder.mSat.setTextColor(textColor1);

        //set times on cardview
        int hour = r.getHour();
        int minute = r.getMin();
        int setBack = r.getTimeGoal();
        int setHour = hour;
        int setMinute = minute;
        if(setHour == 0){
            setHour = 12;
        }
        if(setBack > minute){
            for(int j = 0; j < setBack; j++){
                if(setMinute == 0){
                    setMinute = 60;
                    if(setHour == 1){
                        setHour = 13;
                    }
                    if(hour == 0){
                        hour = 25;
                    }
                    setHour--;
                    hour--;
                }
                setMinute--;
            }
        }
        else{
            setMinute -= setBack;
        }
        String hour_string = String.valueOf(setHour);
        String minute_string = String.valueOf(setMinute);
        if(setHour > 12){
            hour_string = String.valueOf(setHour - 12);
        }
        if(setMinute < 10){
            minute_string = "0" + String.valueOf(setMinute);
        }
        String AM_PM;
        Log.i("setHour", String.valueOf(setHour));
        if(hour >= 12) {
            AM_PM = "PM";
        } else {
            AM_PM = "AM";
        }
        String times = hour_string + ":" + minute_string + " " + AM_PM;
        String time = String.valueOf(r.getHour())+":"+String.valueOf(r.getMin());
        try {
            Date timeAsDate = mInputFormatter.parse(time);
            time = mFormatter.format(timeAsDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final String description = r.getDescription();
        viewHolder.mTime.setText(time);
        viewHolder.mTimeReminder.setText(times);
        viewHolder.mDescription.setText(description);

        if(preferenceV.getTheme() == 0){
            viewHolder.mDescription.setTextColor(Color.parseColor("#99000000"));
            viewHolder.mTime.setTextColor(Color.parseColor("#99000000"));
            viewHolder.mTimeReminder.setTextColor(Color.parseColor("#99000000"));
            viewHolder.reminderText.setTextColor(Color.parseColor("#757575"));
        }
        if(preferenceV.getTheme() == 1){
            viewHolder.mDescription.setTextColor(Color.parseColor("#99000000"));
            viewHolder.mTime.setTextColor(Color.parseColor("#99000000"));
            viewHolder.mTimeReminder.setTextColor(Color.parseColor("#99000000"));
            viewHolder.reminderText.setTextColor(Color.parseColor("#757575"));
        }
        if(preferenceV.getTheme() == 2){
            viewHolder.mDescription.setTextColor(Color.parseColor("#99000000"));
            viewHolder.mTime.setTextColor(Color.parseColor("#99000000"));
            viewHolder.mTimeReminder.setTextColor(Color.parseColor("#99000000"));
            viewHolder.reminderText.setTextColor(Color.parseColor("#757575"));
        }
        if(preferenceV.getTheme() == 3){
            viewHolder.mDescription.setTextColor(Color.parseColor("#ffffff"));
            viewHolder.mTime.setTextColor(Color.parseColor("#ffffff"));
            viewHolder.mTimeReminder.setTextColor(Color.parseColor("#ffffff"));
            viewHolder.reminderText.setTextColor(Color.parseColor("#ffffff"));
        }
        int use = 0;
        for(int p=0; p<mReminders.size(); p++) {
            String rem = mReminders.get(p).getDescription();
            if(rem.equals(viewHolder.mDescription.getText().toString())){
                use = p;
            }
        }
        if(mReminders.get(use).isReminder()) {
            viewHolder.mTimeReminder.setVisibility(View.VISIBLE);
            viewHolder.reminderText.setVisibility(View.VISIBLE);
        }
        else {
            viewHolder.mTimeReminder.setVisibility(View.GONE);
            viewHolder.reminderText.setVisibility(View.GONE);
        }
        viewHolder.mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                ReminderManager.getInstance().setActive(isChecked, description);
                if (isChecked) {
                    if(preferenceVars.getTheme() == 0) {
                        viewHolder.mSwitch.getThumbDrawable().setColorFilter(Color.parseColor("#03A9F4"), PorterDuff.Mode.MULTIPLY);
                        viewHolder.mSwitch.getTrackDrawable().setColorFilter(Color.argb(128, 3, 169, 244), PorterDuff.Mode.MULTIPLY);
                    }
                    if(preferenceVars.getTheme() == 1) {
                        viewHolder.mSwitch.getThumbDrawable().setColorFilter(Color.parseColor("#f53500"), PorterDuff.Mode.MULTIPLY);
                        viewHolder.mSwitch.getTrackDrawable().setColorFilter(Color.parseColor("#ff828f"), PorterDuff.Mode.MULTIPLY);
                    }
                    if(preferenceVars.getTheme() == 2) {
                        viewHolder.mSwitch.getThumbDrawable().setColorFilter(Color.parseColor("#07d300"), PorterDuff.Mode.MULTIPLY);
                        viewHolder.mSwitch.getTrackDrawable().setColorFilter(Color.parseColor("#92fb92"), PorterDuff.Mode.MULTIPLY);
                    }
                    if(preferenceVars.getTheme() == 3) {
                        viewHolder.mSwitch.getThumbDrawable().setColorFilter(Color.parseColor("#0b0b0b"), PorterDuff.Mode.MULTIPLY);
                        viewHolder.mSwitch.getTrackDrawable().setColorFilter(Color.argb(66, 0, 0, 0), PorterDuff.Mode.MULTIPLY);
                    }
                } else {
                    viewHolder.mSwitch.getThumbDrawable().setColorFilter(Color.parseColor("#FAFAFA"), PorterDuff.Mode.MULTIPLY);
                    viewHolder.mSwitch.getTrackDrawable().setColorFilter(Color.argb(66, 0, 0, 0), PorterDuff.Mode.MULTIPLY);
                }
            }
        });
        viewHolder.mSwitch.setChecked(r.isActive());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onItemDismiss(int position, Context context) {
        ReminderManager.getInstance().setActive(false, mReminders.get(position).getDescription());
        ReminderManager.getInstance().getRealm().beginTransaction();
        //ReminderManager.getInstance().cancelAlarms(mReminders.get(position).getDescription());
        mReminders.remove(position);
        ReminderManager.getInstance().getRealm().commitTransaction();
        notifyItemRemoved(position);
    }


    public static class ReminderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mDescription, mTime, mTimeReminder, reminderText, mSun, mMon, mTue, mWed, mThu, mFri, mSat;
        SwitchCompat mSwitch;

        ReminderViewHolder(View itemView) {
            super(itemView);
            mSwitch = (SwitchCompat) itemView.findViewById(R.id.reminder_switch1);
            mSwitch.getThumbDrawable().setColorFilter(Color.parseColor("#FAFAFA"), PorterDuff.Mode.MULTIPLY);
            mSwitch.getTrackDrawable().setColorFilter(Color.argb(66, 0, 0, 0), PorterDuff.Mode.MULTIPLY);
            mTime = (TextView) itemView.findViewById(R.id.reminder_time);
            mTimeReminder = (TextView) itemView.findViewById(R.id.reminder_time_alarm);
            mDescription = (TextView) itemView.findViewById(R.id.reminder_description);
            reminderText = (TextView) itemView.findViewById(R.id.reminder_text);
            mSun = (TextView)itemView.findViewById(R.id.sunText);
            mMon = (TextView)itemView.findViewById(R.id.monText);
            mTue = (TextView)itemView.findViewById(R.id.tueText);
            mWed = (TextView)itemView.findViewById(R.id.wedText);
            mThu = (TextView)itemView.findViewById(R.id.thuText);
            mFri = (TextView)itemView.findViewById(R.id.friText);
            mSat = (TextView)itemView.findViewById(R.id.satText);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick (View v) {
            FragmentManager fm = mActivity.getSupportFragmentManager();
            NotificationDialog notificationDialog = NotificationDialog.newInstance(mDescription.getText().toString());
            notificationDialog.show(fm, "fragment_edit_name");

        }
    }

}
