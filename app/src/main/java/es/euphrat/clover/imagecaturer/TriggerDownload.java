package es.euphrat.clover.imagecaturer;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

public class TriggerDownload extends BroadcastReceiver {
    private String imageURL;




    @Override
    public void onReceive(Context context, Intent intent) {

        GetImageURL getImageURL = new GetImageURL();

        Log.i(MainActivity.TAG, "We Are getting the broadcast...");

        Toast.makeText(context, "we are FINALLY here !!!!!", Toast.LENGTH_LONG).show();

        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(2000);


        try {

            imageURL = getImageURL.execute("http://apod.nasa.gov/apod/astropix.html").get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            Log.e(MainActivity.TAG, "we got an error: ", e);
        } catch (Exception e) {
            Log.e(MainActivity.TAG, "we got error: ", e);
        }


        if (!imageURL.isEmpty() && imageURL != null) {

            ImageCapture imageCapture = new ImageCapture(context);

            Log.d(MainActivity.TAG, imageURL);

            imageCapture.execute("http://apod.nasa.gov/apod/" + imageURL);


        } else {

            Log.d(MainActivity.TAG, "something went wrong :(, ImageURL is empty");
            Toast.makeText(context, "Sorry We Aint got an image today", Toast.LENGTH_LONG).show();
        }



    }
}
