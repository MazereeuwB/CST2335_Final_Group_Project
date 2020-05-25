package com.example.cst2335_finalproject.NASA_Search;
//package com.example.cst2335_finalproject.NASA_Search;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.session.PlaybackState;
import android.os.AsyncTask;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

//import com.example.cst2335_finalproject.MainActivity;
//import com.example.cst2335_finalproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Nasa_Search extends AsyncTask <Void,Void,Void> {
    String dataReceived;
    String dataParsed;
    String singleParsed;


    String titleData;
    String explanationData;
    String urlData;
    String hdurlData;
    String dateData;

    Bitmap image = null;


    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL url = new URL("https://api.nasa.gov/planetary/apod?api_key=DgPLcIlnmN0Cwrzcg3e9NraFaYLIDI68Ysc6Zh3d&date=" + Nasa_SearchPage.formattedDate);
            //Our connection to the website
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            //We are getting the input stream so we can read the data
            InputStream inputStream = httpURLConnection.getInputStream();
            //BufferedReader allows us to read the data
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);

            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = bufferedReader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            dataReceived = sb.toString();





            JSONObject json = new JSONObject(dataReceived);    // create JSON obj from string

            titleData = json.getString("title");
            explanationData = json.getString("explanation");
            urlData = json.getString("url");
            hdurlData = json.getString("hdurl");
            dateData = json.getString("date");

            URL getImageFromUrl = new URL(urlData);
            HttpURLConnection connection = (HttpURLConnection) getImageFromUrl.openConnection();
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                Log.i("Creating Bitmap", "Response Code: " + responseCode);
                image = BitmapFactory.decodeStream(connection.getInputStream());
            }



        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        Nasa_SearchPage.addData(image, titleData,explanationData,dateData,hdurlData);

    }
}