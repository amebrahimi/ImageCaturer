package es.euphrat.clover.imagecaturer;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class BootReceiver extends BroadcastReceiver {

    private static final String PREFS_FILE = "es.euphrat.clover.imagecaturer.preferences";
    private static final String KEY_HOUR = "key_hour";
    private static final String KEY_MINUTE = "key_minute";
    private SharedPreferences mSharedPreferences;



    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
                mSharedPreferences = context.getSharedPreferences(PREFS_FILE , Context.MODE_PRIVATE);
                Util.alarmManager(context, mSharedPreferences.getInt(KEY_HOUR , 0) , mSharedPreferences.getInt(KEY_MINUTE, 0));
        }
    }


}
