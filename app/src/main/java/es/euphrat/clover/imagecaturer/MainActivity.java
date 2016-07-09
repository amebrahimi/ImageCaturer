package es.euphrat.clover.imagecaturer;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

public class MainActivity extends Activity {
    public static String TAG = MainActivity.class.getSimpleName();
    public static String DIRECTORY = Environment.getExternalStorageDirectory().toString() + "/Image Capturer";
    private String[] perms = {"android.permission.READ_EXTERNAL_STORAGE","android.permission.WRITE_EXTERNAL_STORAGE" };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, DIRECTORY);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permsRequest = 200;
            requestPermissions(perms , permsRequest);
        }

        Util.makeDirectory(DIRECTORY);
        Util.alarmManager(this);


        // man ovaisam

    }


}
