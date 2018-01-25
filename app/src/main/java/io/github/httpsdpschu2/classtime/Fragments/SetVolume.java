package io.github.httpsdpschu2.classtime.Fragments;

/**
 * Created by Dean on 3/23/2017.
 */
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import io.github.httpsdpschu2.classtime.Activities.PreferenceVariables;
import io.github.httpsdpschu2.classtime.Activities.PreferenceVars;
import io.github.httpsdpschu2.classtime.R;

public class SetVolume extends DialogFragment implements OnSeekBarChangeListener {

    public SeekBar sb;
    AudioManager am;
    public int Volume;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCESS = "MyPrefss";
    public static final String VOLUME = "volume";

    public int getVolume(){ return Volume;}

    public SetVolume(){
    }
    public static SetVolume newInstance(String key) {
        SetVolume dialog = new SetVolume();
        Bundle args = new Bundle();
        args.putString("key", key);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.volume_main, container);
        PreferenceVars preferenceVars = new PreferenceVars();
        preferenceVars.getContext(root.getContext());
        TextView name = (TextView) root.findViewById(R.id.volumeName);
        sb = (SeekBar) root.findViewById(R.id.seekBar);
        View line = (View) root.findViewById(R.id.volumeLine);
        if(preferenceVars.getTheme() == 0){
            name.setTextColor(Color.parseColor("#1dd9f6"));
            line.setBackgroundColor(Color.parseColor("#1dd9f6"));
            sb.getThumb().setColorFilter(Color.parseColor("#1dd9f6"), PorterDuff.Mode.MULTIPLY);
            sb.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(Color.parseColor("#1dd9f6"), PorterDuff.Mode.MULTIPLY));
        }
        if(preferenceVars.getTheme() == 1){
            name.setTextColor(Color.parseColor("#ff523f"));
            line.setBackgroundColor(Color.parseColor("#ff523f"));
            sb.getThumb().setColorFilter(Color.parseColor("#ff523f"), PorterDuff.Mode.MULTIPLY);
            sb.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(Color.parseColor("#ff523f"), PorterDuff.Mode.MULTIPLY));
        }
        if(preferenceVars.getTheme() == 2){
            name.setTextColor(Color.parseColor("#00fc8f"));
            line.setBackgroundColor(Color.parseColor("#00fc8f"));
            sb.getThumb().setColorFilter(Color.parseColor("#00fc8f"), PorterDuff.Mode.MULTIPLY);
            sb.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(Color.parseColor("#00fc8f"), PorterDuff.Mode.MULTIPLY));
        }
        if(preferenceVars.getTheme() == 3){
            name.setTextColor(Color.parseColor("#3f3f3f"));
            line.setBackgroundColor(Color.parseColor("#3f3f3f"));
            sb.getThumb().setColorFilter(Color.parseColor("#3f3f3f"), PorterDuff.Mode.MULTIPLY);
            sb.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(Color.parseColor("#3f3f3f"), PorterDuff.Mode.MULTIPLY));
        }
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCESS, Context.MODE_PRIVATE);
        am = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_RING);
        sb.setMax(maxVolume);
        sb.setOnSeekBarChangeListener(this);
        sb.setProgress(sharedpreferences.getInt(VOLUME, 0));
        return root;
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(VOLUME, sb.getProgress());
        editor.commit();
        PreferenceVariables preferenceVariables = new PreferenceVariables();
        preferenceVariables.setContext(getActivity());
        preferenceVariables.SavePreferences(VOLUME, sb.getProgress());
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public void onProgressChanged(SeekBar seekb, int progress, boolean arg2) {
        am.setStreamVolume(AudioManager.STREAM_RING, progress, 0);
        Volume = progress;

    }

    public void onStartTrackingTouch(SeekBar arg0) {

    }

    public void onStopTrackingTouch(SeekBar arg0) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getContext());

        String alarms = sharedPreferences.getString("notifications_new_message_ringtone",
                "default ringtone");

        Uri uri = Uri.parse(alarms);

        Ringtone r = RingtoneManager.getRingtone(getContext(), uri);
        r.play();
    }
}
