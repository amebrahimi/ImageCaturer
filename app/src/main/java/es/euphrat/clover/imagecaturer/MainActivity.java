package es.euphrat.clover.imagecaturer;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class
MainActivity extends FragmentActivity {
    private static final String PREFS_FILE = "es.euphrat.clover.imagecaturer.preferences";
    private static final String KEY_HOUR = "key_hour";
    private static final String KEY_MINUTE = "key_minute";

    public static String TAG = MainActivity.class.getSimpleName();
    public static String DIRECTORY = Environment.getExternalStorageDirectory().toString() + "/NASA Photo of the Day";
    private String[] perms = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};


//    private ProgressDialog progressdialog;
//    public static final int Progress_Dialog_Progress = 0;
//    private byte dataArray[] = new byte[1024];
//    private long totalSize = 0;
    private static ImageView imageview;
//    private String imageAddress;
    private Calendar c = Calendar.getInstance();
    private SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
    private String formattedDate = df.format(c.getTime());
    private String fileName;
    private int mHour;
    private int mMinute;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private TextView time;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message m) {
            //new
            Bundle b = m.getData();

            mHour = b.getInt("set_hour");
            mMinute = b.getInt("set_minute");

            mEditor.clear();
            mEditor.putInt(KEY_HOUR, mHour);
            mEditor.putInt(KEY_MINUTE, mMinute);
            mEditor.commit();

            Util.alarmManager(getApplicationContext(), mSharedPreferences.getInt(KEY_HOUR, 0), mSharedPreferences.getInt(KEY_MINUTE, 0));
            time.setText(getString(R.string.wallpaperChanging1) + " " + String.format("%02d", mSharedPreferences.getInt(KEY_MINUTE, 0)) + " : "
                    + String.format("%02d", mSharedPreferences.getInt(KEY_HOUR, 0)) + " " + (getString(R.string.wallpaperChanging2)));
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        time.setText(getString(R.string.wallpaperChanging1) + " " + String.format("%02d", mSharedPreferences.getInt(KEY_MINUTE, 0)) + " : "
                + String.format("%02d", mSharedPreferences.getInt(KEY_HOUR, 0)) + " " + (getString(R.string.wallpaperChanging2)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSharedPreferences = getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        time = (TextView) findViewById(R.id.textView2);
        imageview = (ImageView) findViewById(R.id.imageView1);
//        GetImageURL getImageAddress = new GetImageURL();
//
//        try {
//            imageAddress = getImageAddress.execute("http://apod.nasa.gov/apod/astropix.html").get();
//        } catch (Exception e) {
//            Log.e(MainActivity.TAG, "we got error in MainActivity: ", e);
//        }
//        if (!imageAddress.isEmpty() && imageAddress != null) {
//
//            ImageDownloadWithProgressDialog imageCapture = new ImageDownloadWithProgressDialog();
//
//            Log.d(MainActivity.TAG, imageAddress);
//
//            imageCapture.execute("http://apod.nasa.gov/apod/" + imageAddress);
//
//
//        } else {
//
//            Log.d(MainActivity.TAG, "something went wrong :(, ImageURL is empty");
//            Toast.makeText(getApplicationContext(), "Sorry We Aint got an image today", Toast.LENGTH_LONG).show();
//        }
//
//        new ImageDownloadWithProgressDialog().execute(imageAddress);
//
        Log.d(TAG, DIRECTORY);
        fileName = "Image-" + formattedDate + ".jpg";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permsRequest = 200;
            requestPermissions(perms, permsRequest);
        }
        File file = new File(Environment.getExternalStorageDirectory().toString() + "/NASA Photo of the Day/" + fileName);
        /** man inaro felan gheire faal kardam */
        Util.makeDirectory(DIRECTORY);

        if(!file.exists()) {
            Util.alarmManager(this, mSharedPreferences.getInt(KEY_HOUR, 0), mSharedPreferences.getInt(KEY_MINUTE, 0));
        }else{
            imageview.setImageDrawable(Drawable.createFromPath(String.valueOf(file)));
        }

        OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle b = new Bundle();

                b.putInt("set_hour", mHour);

                b.putInt("set_minute", mMinute);

                TimePickerDialogFragment timePicker = new TimePickerDialogFragment(mHandler);

                timePicker.setArguments(b);

                FragmentManager fm = getSupportFragmentManager();

                FragmentTransaction ft = fm.beginTransaction();

                ft.add(timePicker, "time_picker");

                ft.commit();

            }
        };

        Button btnSet = (Button) findViewById(R.id.btnSet);

        btnSet.setOnClickListener(listener);
//
//
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        return true;
//    }
//
//    public class ImageDownloadWithProgressDialog extends AsyncTask<String, String, String> {
//
//
//        @Override
//        protected void onPreExecute() {
//
//            super.onPreExecute();
//
//            showDialog(Progress_Dialog_Progress);
//        }
//
//        @Override
//        protected String doInBackground(String... aurl) {
//
//            int count;
//
//            try {
//
//                URL url = new URL(aurl[0]);
//                URLConnection urlconnection = url.openConnection();
//                urlconnection.connect();
//
//                int fileSize = urlconnection.getContentLength();
//
//                InputStream inputstream = new BufferedInputStream(url.openStream());
//                OutputStream outputstream = new FileOutputStream(Environment.getExternalStorageDirectory().
//                        toString() + "/Image Capturer/" + fileName);
//
//
//                while ((count = inputstream.read(dataArray)) != -1) {
//
//                    totalSize += count;
//
//                    publishProgress("" + (int) ((totalSize * 100) / fileSize));
//
//                    outputstream.write(dataArray, 0, count);
//                }
//
//                outputstream.flush();
//                outputstream.close();
//                inputstream.close();
//
//            } catch (Exception e) {
//            }
//            return fileName;
//        }
//
//        protected void onProgressUpdate(String... progress) {
//
//            progressdialog.setProgress(Integer.parseInt(progress[0]));
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//
//            super.onPostExecute(s);
//
//            dismissDialog(Progress_Dialog_Progress);
//
    }
            public static void imageDraw(String fileName) {
                String getPath = Environment.getExternalStorageDirectory().toString() + "/NASA Photo of the Day/" + fileName;

                imageview.setImageDrawable(Drawable.createFromPath(getPath));
            }

    public static void noNetworkImage(){

        imageview.setImageResource(R.drawable.no_network);
    }
    public static void noImageToday(){
        imageview.setImageResource(R.drawable.no_picture);
    }
//
//
//        }
//
//    }

//    @Override
//    protected Dialog onCreateDialog(int id) {
//        switch (id) {
//            case Progress_Dialog_Progress:
//
//                progressdialog = new ProgressDialog(MainActivity.this);
//                progressdialog.setMessage("Downloading Image From Server...");
//                progressdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//                progressdialog.setCancelable(false);
//                progressdialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Dismiss", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//                progressdialog.show();
//                return progressdialog;
//
//            default:
//
//                return null;
//        }
//    }


    }
