package es.euphrat.clover.imagecaturer;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


public class MainActivity extends Activity {
    public static String TAG = MainActivity.class.getSimpleName();
    public static String DIRECTORY = Environment.getExternalStorageDirectory().toString() + "/Image Capturer";
    public static String DIRECTORY2 = Environment.getExternalStorageDirectory().toString() + "/Image Capturer2";
    private String[] perms = {"android.permission.READ_EXTERNAL_STORAGE","android.permission.WRITE_EXTERNAL_STORAGE" };
    Button button;
    ProgressDialog progressdialog;
    public static final int Progress_Dialog_Progress = 0;
    URL url;
    URLConnection urlconnection ;
    int FileSize;
    InputStream inputstream;
    OutputStream outputstream;
    byte dataArray[] = new byte[1024];
    long totalSize = 0;
    ImageView imageview;
    String GetPath ;
    String imageAddress;
    Calendar c = Calendar.getInstance();
    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
    String formattedDate = df.format(c.getTime());
    String fileName;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button)findViewById(R.id.button1);
        imageview = (ImageView)findViewById(R.id.imageView1);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
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


                new ImageDownloadWithProgressDialog().execute(imageAddress);

            }
        });

        Log.d(TAG, DIRECTORY);
        fileName = "Image-" + formattedDate + ".jpg";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permsRequest = 200;
            requestPermissions(perms , permsRequest);
        }

        Util.makeDirectory(DIRECTORY);
        Util.alarmManager(this);
        Util.makeDirectory(DIRECTORY2);



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
                        toString() + "/Image Capturer2/" + fileName);


                while ((count = inputstream.read(dataArray)) != -1) {

                    totalSize += count;

                    publishProgress(""+(int)((totalSize*100)/FileSize));

                    outputstream.write(dataArray, 0, count);
                }

                outputstream.flush();
                outputstream.close();
                inputstream.close();

            } catch (Exception e) {}
            return null;

        }
        protected void onProgressUpdate(String... progress) {

            progressdialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {

            dismissDialog(Progress_Dialog_Progress);

            GetPath = Environment.getExternalStorageDirectory().toString() + "/Image Capturer2/" + fileName ;

            imageview.setImageDrawable(Drawable.createFromPath(GetPath));

            Toast.makeText(MainActivity.this, "Image Downloaded Successfully", Toast.LENGTH_LONG).show();
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




