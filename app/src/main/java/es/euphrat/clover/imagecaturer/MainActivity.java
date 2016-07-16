package es.euphrat.clover.imagecaturer;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;


public class MainActivity extends FragmentActivity {
    public static String TAG = MainActivity.class.getSimpleName();
    public static String DIRECTORY = Environment.getExternalStorageDirectory().toString() + "/Image Capturer";
    public static String DIRECTORY2 = Environment.getExternalStorageDirectory().toString() + "/Image Capturer2";
    private String[] perms = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};


    ProgressDialog progressdialog;
    public static final int Progress_Dialog_Progress = 0;
    private URL url;
    private URLConnection urlconnection;
    private int FileSize;
    private InputStream inputstream;
    private OutputStream outputstream;
    private byte dataArray[] = new byte[1024];
    private long totalSize = 0;
    private ImageView imageview;
    private String GetPath;
    private String imageAddress;
    private Calendar c = Calendar.getInstance();
    private SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
    private String formattedDate = df.format(c.getTime());
    private String fileName;
    private int mHour = 12;
    private int mMinute = 00;

    public int getMinute() {
        return mMinute;
    }

    public void setMinute(int minute) {
        mMinute = minute;
    }

    public int getHour() {
        return mHour;
    }

    public void setHour(int hour) {
        mHour = hour;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message m) {
            /** Creating a bundle object to pass currently set Time to the fragment */
            Bundle b = m.getData();

            mHour = b.getInt("set_hour");
            mMinute = b.getInt("set_minute");


            setHour(mHour);
            setMinute(mMinute);

            Util.alarmManager(getApplicationContext());

            /** Displaying a short time message containing time set by Time picker dialog fragment */
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageview = (ImageView) findViewById(R.id.imageView1);
        GetImageURL getImageadres = new GetImageURL();
        try {
            imageAddress = getImageadres.execute("http://apod.nasa.gov/apod/astropix.html").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            Log.e(MainActivity.TAG, "we got an error in MainActivity: ", e);
        } catch (Exception e) {
            Log.e(MainActivity.TAG, "we got error in MainActivity: ", e);
        }
        if (!imageAddress.isEmpty() && imageAddress != null) {

            ImageDownloadWithProgressDialog imageCapture = new ImageDownloadWithProgressDialog();

            Log.d(MainActivity.TAG, imageAddress);

            imageCapture.execute("http://apod.nasa.gov/apod/" + imageAddress);


        } else {

            Log.d(MainActivity.TAG, "something went wrong :(, ImageURL is empty");
            Toast.makeText(getApplicationContext() , "Sorry We Aint got an image today", Toast.LENGTH_LONG).show();
        }

        new ImageDownloadWithProgressDialog().execute(imageAddress);

        Log.d(TAG, DIRECTORY);
        fileName = "Image-" + formattedDate + ".jpg";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permsRequest = 200;
            requestPermissions(perms, permsRequest);
        }
//     TODO       // man inaro felan gheire faal kardam //
        Util.makeDirectory(DIRECTORY);
        Util.alarmManager(this);
        Util.makeDirectory(DIRECTORY2);
        /** Click Event Handler for button */
        OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View v) {

                /** Creating a bundle object to pass currently set time to the fragment */
                Bundle b = new Bundle();

                /** Adding currently set hour to bundle object */
                b.putInt("set_hour", mHour);

                /** Adding currently set minute to bundle object */
                b.putInt("set_minute", mMinute);

                /** Instantiating TimePickerDialogFragment */
                TimePickerDialogFragment timePicker = new TimePickerDialogFragment(mHandler);

                /** Setting the bundle object on timepicker fragment */
                timePicker.setArguments(b);

                /** Getting fragment manger for this activity */
                FragmentManager fm = getSupportFragmentManager();

                /** Starting a fragment transaction */
                FragmentTransaction ft = fm.beginTransaction();

                /** Adding the fragment object to the fragment transaction */
                ft.add(timePicker, "time_picker");

                /** Opening the TimePicker fragment */
                ft.commit();

            }
        };

        /** Getting an instance of Set button */
        Button btnSet = (Button) findViewById(R.id.btnSet);

        /** Setting click event listener for the button */
        btnSet.setOnClickListener(listener);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public class ImageDownloadWithProgressDialog extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            showDialog(Progress_Dialog_Progress);
        }

        @Override
        protected String doInBackground(String... aurl) {

            int count;

            try {

                url = new URL(aurl[0]);
                urlconnection = url.openConnection();
                urlconnection.connect();

                FileSize = urlconnection.getContentLength();

                inputstream = new BufferedInputStream(url.openStream());
                outputstream = new FileOutputStream(Environment.getExternalStorageDirectory().
                        toString() + "/Image Capturer/" + fileName);


                while ((count = inputstream.read(dataArray)) != -1) {

                    totalSize += count;

                    publishProgress("" + (int) ((totalSize * 100) / FileSize));

                    outputstream.write(dataArray, 0, count);
                }

                outputstream.flush();
                outputstream.close();
                inputstream.close();

            } catch (Exception e) {
            }
//            return null;
            return fileName;
        }

        protected void onProgressUpdate(String... progress) {

            progressdialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);

            dismissDialog(Progress_Dialog_Progress);

            GetPath = Environment.getExternalStorageDirectory().toString() + "/Image Capturer/" + fileName;

            imageview.setImageDrawable(Drawable.createFromPath(GetPath));

            Toast.makeText(MainActivity.this, "Image Downloaded and Set to Wallpaper Successfully", Toast.LENGTH_LONG).show();

//            Util.setWall(getApplicationContext(), s);


        }

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case Progress_Dialog_Progress:

                progressdialog = new ProgressDialog(MainActivity.this);
                progressdialog.setMessage("Downloading Image From Server...");
                progressdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressdialog.setCancelable(false);
                progressdialog.show();
                return progressdialog;

            default:

                return null;
        }
    }


}






