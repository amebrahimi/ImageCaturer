package es.euphrat.clover.imagecaturer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.MainThread;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

public class Util {

    public static int mHour;
    public static int mMinute;




    public static void makeDirectory(String directory) {

        File myDir = new File(directory);

        if (!myDir.exists()) {
            myDir.mkdirs();
        }
    }


    public static  Handler mHandler = new Handler(){
//        @Override
//        public void handleMessage(Message m){
//            /** Creating a bundle object to pass currently set Time to the fragment */
//            Bundle b = m.getData();
//
//            /** Getting the Hour of day from bundle */
//            mHour = b.getInt("set_hour");
//
//            /** Getting the Minute of the hour from bundle */
//            mMinute = b.getInt("set_minute");





            /** Displaying a short time message containing time set by Time picker dialog fragment */
//        }
    };


    public static void alarmManager(Context context) {
        SharedPreference sharedPreference = new SharedPreference();
        MainActivity mainActivity = new MainActivity();
        AlarmManager mAlarmManager;
        PendingIntent mPendingIntent;
        Random random;
        random = new Random(5);
        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent mIntent = new Intent(context, TriggerDownload.class);
        mPendingIntent = PendingIntent.getBroadcast(context, random.nextInt(), mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Log.i(MainActivity.TAG, random.nextInt() + "");
        mHour = sharedPreference.getValue(context);
        mMinute = sharedPreference.getValue(context);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, mHour);
        calendar.set(Calendar.MINUTE, mMinute);
        calendar.set(Calendar.SECOND, 0);
        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
        24*60*60*1000, mPendingIntent);
        Log.d ("HOUR", String.valueOf(mHour));
        Log.d ("MINUTE", String.valueOf(mMinute));

//        mAlarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000 * 4, mPendingIntent);
        Log.i(MainActivity.TAG, "We Are getting the broadcast...");
//        Toast.makeText(context, "we are FINALLY here !!!!!", Toast.LENGTH_LONG).show();
//        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
//        vibrator.vibrate(2000);



//        Calendar cur_cal = new GregorianCalendar();
//        cur_cal.setTimeInMillis(System.currentTimeMillis());//set the current time and date for this calendar
//        Calendar cal = new GregorianCalendar();
//        cal.set(Calendar.HOUR_OF_DAY, 12);
//        cal.set(Calendar.MINUTE, 52);
//        Intent intent = new Intent(context, MainActivity.ImageDownloadWithProgressDialog.class);
//        PendingIntent pintent = PendingIntent.getService(context, 0, intent, 0);
//        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pintent);
//        Log.i(MainActivity.TAG, "We Are getting the broadcast...");
//        Toast.makeText(context, "we are FINALLY here !!!!!", Toast.LENGTH_LONG).show();
//        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
//        vibrator.vibrate(2000);

    }

    public static void setWall(Context context, String fileName) {

        FileInputStream is;
        BufferedInputStream bis;
        WallpaperManager wallpaperManager;
        String root = Environment.getExternalStorageDirectory().toString();
        DisplayMetrics displayMetrics;

        displayMetrics = new DisplayMetrics();

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        int Dheight = displayMetrics.heightPixels;
        int Dwidth = displayMetrics.widthPixels;


        try {
            is = new FileInputStream(new File(root + "/Image Capturer/" + fileName));

            Log.d(MainActivity.TAG, fileName);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;

            bis = new BufferedInputStream(is);
            Bitmap bitmap = BitmapFactory.decodeStream(bis, null, options);

            int imageHeight = options.outHeight;
            int imageWidth = options.outWidth;

            float maxFloat = getMax(imageWidth, imageHeight, Dwidth, Dheight);
            Log.d(MainActivity.TAG, maxFloat + "");

            Bitmap useThisBitmap = Bitmap.createScaledBitmap(bitmap, (int) (imageWidth * maxFloat), (int) (imageHeight * maxFloat), true);

            wallpaperManager = WallpaperManager.getInstance(context);
            wallpaperManager.setBitmap(useThisBitmap);

        } catch (FileNotFoundException e) {
            Log.e(MainActivity.TAG, "the file isn't there!!!", e);
        } catch (IOException e) {
            Log.e(MainActivity.TAG, "Couldn't use wallPaper", e);
        }

    }


    private static float getMax(int bWidth, int bHeight, int dWidth, int dHeight) {

        float Rw;
        float Rh;
        float fMax;

        Rw = ((float) dWidth / bWidth);
        Rh = ((float) dHeight / bHeight);

        fMax = Math.max(Rw, Rh);

        return fMax;
    }



}
