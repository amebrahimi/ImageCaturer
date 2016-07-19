package es.euphrat.clover.imagecaturer;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class GetImageURL extends AsyncTask<String, Void, String> {



    @Override
    protected String doInBackground(String... params) {
        String imageSource = null;
        try {
            imageSource = getImageSource(params[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imageSource;

    }

    private String getImageSource(String... params) throws IOException {
        String result;
        URL url;
        HttpURLConnection httpURLConnection;
        String pictureAdress;
        ArrayList<String> resultString = null;
        String lastResult = null;

        try {
            url = new URL(params[0]);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream in = httpURLConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) total.append(line);
            /*
            int contentLength = httpURLConnection.getContentLength();
            char[] charArray = new char[contentLength];
            reader.read(charArray);
            result = new String(charArray);
            */
            result = total.toString();
            Log.d(MainActivity.TAG, result);



            Pattern p = Pattern.compile("<a href=\"(.*?)\">");
            Matcher m = p.matcher(result);

            resultString = new ArrayList<>();

            while (m.find()) {
                pictureAdress = m.group(1);

                resultString.add(pictureAdress);


            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (resultString != null) {
            for (String m : resultString) {

                if (m.matches(".*jpg$")) {

                    lastResult = m;

                    break;
                } else {
                    lastResult = "";
                }
            }
        } else {
            Log.d(MainActivity.TAG, "resultSting is empty");
        }

        Log.d(MainActivity.TAG, lastResult);
        return lastResult;
    }





}
