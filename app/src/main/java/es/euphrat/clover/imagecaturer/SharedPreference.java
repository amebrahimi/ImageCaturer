package es.euphrat.clover.imagecaturer;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import static es.euphrat.clover.imagecaturer.Util.mHour;
import static es.euphrat.clover.imagecaturer.Util.mMinute;

public class SharedPreference {

//    public static final String PREFS_NAME = "AOP_PREFS";
//    public static final String PREFS_KEY = "AOP_PREFS_String";
    public static final int set_Hour = mHour;
    public static final int set_minute = mMinute;

    public SharedPreference() {
        super();
    }

    public int save(Context context, int text) {
        SharedPreferences settings;
        Editor editor;

        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences("set_Hour", Context.MODE_PRIVATE); //1
        settings = context.getSharedPreferences("set_Minute", Context.MODE_PRIVATE); //1

        editor = settings.edit(); //2

        editor.putInt("set_Hour", mHour); //3
        editor.putInt("set_Minute", mMinute); //3

        editor.commit(); //4
        return text;
    }

    public int getValue(Context context) {
        SharedPreferences settings;


        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences("set_Hour", Context.MODE_PRIVATE);
        settings = context.getSharedPreferences("set_Minute", Context.MODE_PRIVATE);
        mHour = settings.getInt("set_Hour", mHour);
        mMinute = settings.getInt("set_Minute", mMinute);
        return mHour;
    }
}