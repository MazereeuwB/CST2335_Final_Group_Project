package com.example.cst2335_finalproject.NASA_Imagery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cst2335_finalproject.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class NASA_imagePage extends AppCompatActivity {
    /**
     * Holds double value of latitude retrieved from fromNasaInput intent object
     */
    private double latitude;
    /**
     * Database object from MyOpener class
     */
    static SQLiteDatabase db;
    /**
     * Instantiating an object from my database class
     */
    MyOpener dbHelper = new MyOpener(this);
    /**
     * arrayList of NASA_Images objects
     */
    ArrayList<NASA_Images> imagesArrayList = new ArrayList<>();
    /**
     * Holds double value of latitude retrieved from fromNasaInput intent object
     */
    private double longitude;
    /**
     * Holds the id value returned by a database insertion command
     */
    private long newId;
    /**
     * stores a byte array containing the image information retrieved from the async task
     */
    private byte[] nasaBArray;
    /**
     * Holds data retrieved from NASA_ImageryActivity activity, which sent Double values latitude and longitude.
     **/
    private Intent fromNasaInput;
    /**
     * A simple progress bar to visually display progress into displaying retrieved data from the async task onto the layout
     */
    private ProgressBar bar;
    /**
     * Holds the string data parsed from latitude, to display as a textview
     */
    private String latString;
    /**
     * Holds the string data parsed from longitude, to display as a textview
     */
    private String longString;
    /**
     * A file object to use in fileExists method to see if the given file does exist
     */
    private File file;
    /**
     * Used to hold a double value containing the latitude of an item retrieved from a database search for the isDuplicate method
     */
    private Double dbLat;
    /**
     * Used to hold a double value containing the longitude of an item retrieved from a database search for the isDuplicate method
     */
    private Double dbLong;
    /**
     * A textview to display the latitude of the retrieved image
     */
    private TextView latText;
    /**
     * longitude column index in the database search
     */
    private int longColIndex;
    /**
     * a textview to display the longitude of the retrieved image
     */
    private TextView longText;
    /**
     * Contains url string to be used in the async task to retrieve data from a website
     */
    private String url;
    /**
     * latitude column index in the database search
     */
    private int latColIndex;
    /**
     * An ImageQuery object to execute an async task
     */
    private ImageQuery iq;
    /**
     * a string containing the direction and time of a retrieved image taken from a JSON object
     */
    private String dateTime;
    /**
     * A string containing the url to retrieve the image
     */
    private String imageUrl;
    /**
     * Holds the image data as a bitmap to be displayed in the layout
     */
    private Bitmap nasaImage;
    /**
     * URL object that holds the url passed to the async task
     */
    private URL url1;
    /**
     * A string array to hold the two strings after a single string is split into two, which contains the direction, and time.
     */
    private String[] datesTimes;
    /**
     * Stores the first string, the direction, of datesTimes
     */
    private String date;
    /**
     * Stores the second string, the time, of datesTimes
     */
    private String time;
    /**
     * An HttpURLConnection object
     */
    private HttpURLConnection urlConnection;
    /**
     * An InputStream object
     */
    private InputStream response;
    /**
     * A BufferedReader object
     */
    private BufferedReader reader;
    /**
     * A string builder holding the JSON data as a string
     */
    private StringBuilder sb;
    /**
     * an individual line in StringBuilder sb.
     */
    private String line;
    /**
     * String holding stringbuilder string
     */
    private String result;
    /**
     * JSONObject containing data retrieved from given url
     */
    private JSONObject nasaReport;
    /**
     * Full url of the stored image
     */
    private String imageFile;
    /**
     * String array holding two strings of a single spit string
     */
    private String[] imageFiles;
    /**
     * Trimmed image url, into an appropriate file name
     */
    private String imageFileName;
    /**
     * FileInputStream object to read from a file
     */
    private FileInputStream fis;
    /**
     * URL object to pass to retrieve the image from the url website
     */
    private URL url2;
    /**
     * An HttpURLConnection object
     */
    private HttpURLConnection connection;
    /**
     * A FileOutputStream object to write to a file
     */
    private FileOutputStream outputStream;
    /**
     * Textview to display the direction of an image
     */
    private TextView directionText;

    /**
     * Imageview to display the retrieved image
     */
    private ImageView map;
    /**
     * Button to add the current image and its data to a "favourites" database to display in the NASA_favPage activity
     */
    private Button favBtn;
    /**
     * A ByteArrayOutputStream object used in the conversion of byte array to bitmap
     */
    private ByteArrayOutputStream bos;
    /**
     * Holds database query results
     */
    private Cursor results;
    /**
     * Holds the direction value
     */
    private String direction;
    /**
     * API key to access the database
     */
    private final String MY_KEY = "AnfgXkXBgufbJGRLr5BsTCtOSjEFFcfXcFU4OSxTT87huK7__SyKZkuaDLaKJ5AD";
    /**
     *  Imports data from previous intent via 'fromNasaInput', which is displayed through various TextViews, and then
     *  an AsyncTask is created to retrieve a bitmap image from an api database.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasa_image_page);
        bar = findViewById(R.id.nasa_imagery_displayBar);
        bar.setVisibility(View.INVISIBLE);
        fromNasaInput = getIntent();
        latitude = fromNasaInput.getDoubleExtra("latitude", 0.00);
        longitude = fromNasaInput.getDoubleExtra("longitude", 0.00);
        direction = fromNasaInput.getStringExtra("direction");
        latString = Double.toString(latitude);
        longString = Double.toString(longitude);
        latText = findViewById(R.id.nasa_imagery_displayLat);
        latText.setText(getResources().getString(R.string.nasa_imagery_displayLat) +latString);
        longText = findViewById(R.id.nasa_imagery_displayLong);
        longText.setText(getResources().getString(R.string.nasa_imagery_displayLong) + longString);
        directionText = findViewById(R.id.nasa_imagery_displayDirection);
        directionText.setText(getResources().getString(R.string.nasa_imagery_displayDirection)+ " " + direction);

        url = "http://dev.virtualearth.net/REST/V1/Imagery/Map/Birdseye/"+ latString + "," + longString + "/20?dir="+direction+"&ms=500,500&key="+MY_KEY;
        iq = new ImageQuery();
        iq.execute(url);
    }

    /**
     * This AsyncTask class provides the structure to allow this application to reach out to the internet through a url
     * and retrieve data via an API to bring back, store, and display.
     */
    private class ImageQuery extends AsyncTask<String, Integer, String> {
        /**
         * This shows the progress of retrieving data from the API
         */
        ProgressBar bar = findViewById(R.id.nasa_imagery_displayBar);

        /**
         * Takes the image file name from the details entered by the user, and either loads the image from a stored file in
         * the local system, or, if the local file does not exist, retrieves it from the API, and then stores it as a local file.
         * @param strings -  a single string passed as a url to access the API for data
         * @return completion status
         */
        @Override
        protected String doInBackground(String... strings) {
            try {
                url1 = new URL(strings[0]);
                publishProgress(33);
                SystemClock.sleep(500);
                imageFiles = url.split("/");
               String imageFileName1 = imageFiles[imageFiles.length-2];
               String imageFileName2 = imageFiles[imageFiles.length-1];
                imageFileName = imageFileName1 + imageFileName2;
                imageFileName = imageFileName.replace("/", "");
                if(fileExists(imageFileName)){
                    Log.i("nasaImage", "image obtained from internal file" + imageFileName);
                    fis = null;
                    try {    fis = openFileInput(imageFileName);   }
                    catch (FileNotFoundException e) {e.printStackTrace();}
                    nasaImage = BitmapFactory.decodeStream(fis);
                    publishProgress(66);
                    SystemClock.sleep(500);
                }
                else {
                    nasaImage = null;
                    Log.i("nasaImage", "image downloaded externally");

                    connection = (HttpURLConnection) url1.openConnection();
                    response = connection.getInputStream();
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        nasaImage = BitmapFactory.decodeStream(connection.getInputStream());
                    }

                    outputStream = openFileOutput(imageFileName, Context.MODE_PRIVATE);
                    nasaImage.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                    outputStream.flush();
                    outputStream.close();
                    publishProgress(66);
                    SystemClock.sleep(500);
                }

                publishProgress(100);


            } catch (Exception ex) {
                ex.printStackTrace();
                Log.i("values", "error null image search");
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(NASA_imagePage.this, getResources().getString(R.string.nasa_imagery_Toast1), Toast.LENGTH_SHORT).show();
                    finish();}
                });
                return null;
            }
            return "Done";
        }

        /**
         * Updates the progress bar value with given arg value
         * @param args - amount to update progress by called in doInBackground
         */
        public void onProgressUpdate(Integer... args) {
            bar.setVisibility(View.VISIBLE);
                bar.setProgress(args[0]);
            }

        /**
         * Takes the bitmap data retrieved from doInBackground and sets it as the bitmap image view in the layout.
         * If the user wishes to store this image as a favourite, when the button is clicked, isDuplicate() will run to confirm
         * that this image is not already in the database.  Then once confirmed unique, the image and its relevant data will be
         * stored into the database.
         * @param fromDoInBackground
         */
        public void onPostExecute(String fromDoInBackground) {
            map = findViewById(R.id.nasa_imagery_displayImage);
            map.setImageBitmap(nasaImage);
            favBtn = findViewById(R.id.nasa_imagery_displayFav);
            bar.setVisibility(View.INVISIBLE);
            favBtn.setOnClickListener( click -> {
                db = dbHelper.getWritableDatabase();
                /*
                if(isDuplicate()) {
                    Snackbar.make(favBtn, getResources().getString(R.string.nasa_imagery_Snackbar1), Snackbar.LENGTH_SHORT).show();
                    return;
                }

                 */
                bos = new ByteArrayOutputStream();
                nasaImage.compress(Bitmap.CompressFormat.PNG, 100, bos);
                nasaBArray = bos.toByteArray();
                ContentValues newValues = new ContentValues();
                newValues.put(MyOpener.COL_DIRECTION, direction);
                newValues.put(MyOpener.COL_LAT, latitude);
                newValues.put(MyOpener.COL_LONG, longitude);
                newValues.put(MyOpener.COL_IMAGE, nasaBArray);
                newId = db.insert(MyOpener.TABLE_NAME, null, newValues);
                nasaImage = BitmapFactory.decodeByteArray(nasaBArray, 0, nasaBArray.length);
                imagesArrayList.add(new NASA_Images(direction, latitude,longitude, nasaImage, newId));
                Snackbar.make(favBtn, getResources().getString(R.string.nasa_imagery_Snackbar2), Snackbar.LENGTH_SHORT).show();
                                          });
            findViewById(R.id.nasa_imagery_displayBack).setOnClickListener(click -> finish());
        }

        /**
         * The currently selected image data is checked against the existing database to check if there are duplicates.
         * The latitude, longitude, and direction of an image make it unique, so those three items are checked against those items
         * form each image in the database via a search query.
         * @return boolean which indicates whether the given image data already exists in the database, true if yes, false if no.
         */
        private boolean isDuplicate() {
            db = dbHelper.getWritableDatabase();
            results = db.query(false, MyOpener.TABLE_NAME,
                    new String[]{MyOpener.COL_LAT, MyOpener.COL_LONG, MyOpener.COL_DIRECTION},
                    null, null, null, null, null, null);
            latColIndex = results.getColumnIndex(MyOpener.COL_LAT);
            longColIndex = results.getColumnIndex((MyOpener.COL_LONG));
           int directionColIndex = results.getColumnIndex(MyOpener.COL_DIRECTION);
            while (results.moveToNext()) {
                Double dbDirection = Double.valueOf(results.getString(directionColIndex));
                dbLat = Double.valueOf(results.getString(latColIndex));
                dbLong = Double.valueOf(results.getString(longColIndex));
                    Log.i("values", "dbLat: " + dbLat + "\ndbLong " + dbLong + "\nlat: " + latitude + "\n long: " + longitude);
                if (Double.compare(dbLat, latitude) == 0)
                    if (Double.compare(dbLong, longitude) == 0)
                        Log.i("values", "dbDirection: " + dbDirection + "\ndirection: "+ direction);
                        if(Double.compare(dbDirection, Double.valueOf(direction)) == 0) return true;

                }
            return false;
            }

        /**
         * Checks to make sure given filename exists in the local system
         * @param fName given file name to test
         * @return boolean, true is file exists, false if not
         */
        private boolean fileExists(String fName) {
            file = getBaseContext().getFileStreamPath(fName);
            return file.exists();
        }


    }
}
