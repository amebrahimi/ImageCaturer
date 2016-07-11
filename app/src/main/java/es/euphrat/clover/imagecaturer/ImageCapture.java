package es.euphrat.clover.imagecaturer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;


public class ImageCapture extends AsyncTask<String, Void, String> {

    Context mContext;
    String fileName;

    public ImageCapture(Context context) {
        this.mContext = context;
    }


    @Override
    protected String doInBackground(String... params) {


        Bitmap bitmap = downloadFromCloud(params[0]);

        return saveImage(bitmap, MainActivity.DIRECTORY);


    }

    private Bitmap downloadFromCloud(String... urls) {

        URL url;
        HttpURLConnection urlConnection;
        Bitmap bitmap = null;

        try {

            url = new URL(urls[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();
            bitmap = BitmapFactory.decodeStream(in);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private String saveImage(Bitmap bitmap, String myDir) {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());



        fileName = "Image-" + formattedDate + ".jpg";
        File file = new File(myDir, fileName);
        if (file.exists()) file.delete();

        try {

            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileName;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        Util.setWall(mContext, s);
    }
}
