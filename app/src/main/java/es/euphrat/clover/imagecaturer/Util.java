package es.euphrat.clover.imagecaturer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Random;

public class Util {

    public static void makeDirectory(String directory) {

        File myDir = new File(directory);

        if (!myDir.exists()) {
            myDir.mkdirs();
        }
    }


    public static void alarmManager(Context context) {

        AlarmManager mAlarmManager;
        PendingIntent mPendingIntent;
        Random random;

        random = new Random(5);
        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent mIntent = new Intent(context, MainActivity.ImageDownloadWithProgressDialog.class);
        mPendingIntent = PendingIntent.getBroadcast(context, random.nextInt(), mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Log.i(MainActivity.TAG, random.nextInt() + "");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 10);
        calendar.set(Calendar.SECOND, 0);

    mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
    AlarmManager.INTERVAL_DAY, mPendingIntent);

        mAlarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 1000 * 4, mPendingIntent);

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
