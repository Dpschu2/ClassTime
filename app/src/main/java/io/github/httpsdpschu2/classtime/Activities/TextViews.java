package io.github.httpsdpschu2.classtime.Activities;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.HashMap;

import io.github.httpsdpschu2.classtime.Fragments.SetVolume;
import io.github.httpsdpschu2.classtime.R;
import io.github.httpsdpschu2.classtime.managers.ReminderManager;

/**
 * Created by Dean on 2/22/2017.
 */

public final class TextViews extends AppCompatActivity {
    private String classNameMain;
    private String classNameMainAdjust;
    private String classTimeMain;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    public void setCollapsingToolbarLayout(CollapsingToolbarLayout collapsingToolbarLayout) {
        this.collapsingToolbarLayout = collapsingToolbarLayout;
    }
    private static TextViews mInstance;
    public TextViews(){}
    public static TextViews getInstance() {
        if(mInstance == null) {
            mInstance = new TextViews();
        }
        return mInstance;
    }
    public void setClassMain() {
        if(!classNameMain.equals("No classes"))
            collapsingToolbarLayout.setTitle(classNameMain + " at " + classTimeMain);
        else
            collapsingToolbarLayout.setTitle(classNameMain);
        if(classNameMain.length() > 12) {
            classNameMainAdjust = classNameMain.substring(0, 9) + "...";
            collapsingToolbarLayout.setTitle(classNameMainAdjust + " at " + classTimeMain);
            if(classNameMainAdjust.length() > 6 && classNameMainAdjust.length() < 9)
                collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.Toolbar_TitleText1);
            else if(classNameMainAdjust.length() > 8 && classNameMainAdjust.length() < 13)
                collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.Toolbar_TitleText2);
        }
        if(classNameMain.length() > 6 && classNameMain.length() < 9)
            collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.Toolbar_TitleText1);
        else if(classNameMain.length() > 8 && classNameMain.length() < 13 && !classNameMain.equals("No classes"))
            collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.Toolbar_TitleText2);
    }
    public void setClassNameMain(String classNameMain) {
        this.classNameMain = classNameMain;
    }
    public void setClassTimeMain(String classTimeMain) {
        this.classTimeMain = classTimeMain;
    }
}
