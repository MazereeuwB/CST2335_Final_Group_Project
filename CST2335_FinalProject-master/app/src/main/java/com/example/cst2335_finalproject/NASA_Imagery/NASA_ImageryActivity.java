package com.example.cst2335_finalproject.NASA_Imagery;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cst2335_finalproject.R;

/**
 * NASA_ImageryActivity is the home page activity for this project.  The layout displays two edit text fields, in which the user is prompted
 * to enter in the latitude and longitude of the image they would like to see.  The user may choose to enter those details into the program
 * with the "Find Image" button, to retrieve the image with the given latitude and longitude.  The user may, instead, choose
 * to see a list of their pre-selected favourite images with the "See Your Favourite Images!" button.
 */
public class NASA_ImageryActivity extends AppCompatActivity {
    /**
     * Takes user input as the latitude of the image to retrieve
     */
    private EditText editLat;
    /**
     * Takes user input as the longitude of the image to retrieve
     */
    private EditText editLong;
    /**
     * Takes user input as the direction of the image to retrieve
     */
    private EditText editDirection;
    /**
     * Holds the latitude string value retrieved from "prefs"
     */
    private String latString;
    /**
     * Holds the longitude string value retrieved from "prefs"
     */
    private String longString;
    /**
     * Holds the direction string value retrieved from "prefs"
     */
    private String direction;
    /**
     * holds the double value of latitude, retrieved from the editText editLat
     */
    private Double latitude;
    /**
     * holds the double value of longitude, retrieved from the editText editLong
     */
    private Double longitude;
    /**
     * An intent object, to take the user to the NASA_favPage upon the click of the "See Your Favourite Images!" button.
     */
    private Intent nasaFavPage;
    /**
     * Saves and holds the editLat and editLong data entered by the user,
     * when the activity is left, so it is there waiting patiently, upon our user's return.
     */
    private SharedPreferences prefs;
    /**
     * An intent object, containing the latitude and longitude values, to pass the user to the NASA_imagePage activity
     * to retrieve an image.
     */
    private Intent nasaImagePage;

    /**
     * This method is called when the activity is stopped and another is about to be launched, which is the moment that the
     * prefs object needs to be saved for the next time this activity is launched.
     * prefs stores two string variables, under ReserveLat and ReserveLong, which contain the editLat text and the editLong text, respectively.
     */
    @Override
    protected void onPause() {
        super.onPause();
        prefs = getSharedPreferences("nasaFile", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("ReserveDirection", editDirection.getText().toString());
        edit.putString("ReserveLat", editLat.getText().toString());
        edit.putString("ReserveLong", editLong.getText().toString());
        edit.commit();
    }

    /**
     * Launched when an activity is launched, as it performs the initialization of all fragments.
     * This particular onCreate method displays the related layout (R.layout.activity_nasa_imagery_main).
     * The user input from the two editTexts are saved as a string, and then parsed into two Double variables.  The user
     * can then choose to click one of two buttons, which will take them to the NASA_favPage activity, or the NASA_imagePage activity.
     * @param savedInstanceState is a Bundle, if not null, this fragment is being reconstructed from a previous state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasa_imagery_main);

        Toolbar myToolBar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolBar);
        editLat = findViewById(R.id.nasa_imagery_editLat);
        editLong = findViewById(R.id.nasa_imagery_editLong);
        editDirection = findViewById(R.id.nasa_imagery_editDirection);
        prefs = getSharedPreferences("nasaFile", MODE_PRIVATE);
        latString = prefs.getString("ReserveLat", "");
        editLat.setText(latString);
        longString = prefs.getString("ReserveLong", "");
        editLong.setText(longString);
        direction = prefs.getString("ReserveDirection", "");
        editDirection.setText(direction);
        nasaImagePage = new Intent(NASA_ImageryActivity.this, NASA_imagePage.class);
        findViewById(R.id.nasa_imagery_inputButton).setOnClickListener( click -> {
            try {
                latitude = Double.valueOf(editLat.getText().toString());
                longitude = Double.valueOf(editLong.getText().toString());
                nasaImagePage.putExtra("direction", direction);
                nasaImagePage.putExtra("latitude", latitude);
                nasaImagePage.putExtra("longitude", longitude);
                startActivityForResult(nasaImagePage, 1);
            }catch(Exception ex){
                Toast.makeText(NASA_ImageryActivity.this, getResources().getString(R.string.nasa_imagery_Toast2), Toast.LENGTH_SHORT).show();}
        });
        nasaFavPage = new Intent(NASA_ImageryActivity.this, NASA_favPage.class);
        findViewById(R.id.nasa_imagery_goToFavBtn).setOnClickListener( click -> {
         startActivity(nasaFavPage);
        });
    }

    /**
     * Creates a menu view to store items for a toolbar
     * @param menu menu object to hold items
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);
        return true;
    }

    /**
     * If the help item is selected, an alert dialog is shown to help the user navigate the application
     * @param item MenuItem object to store a menu item
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.nasa_imagery_help:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle(getResources().getString(R.string.nasa_imagery_helpStringTitle));
                alert.setMessage(getResources().getString(R.string.nasa_imagery_helpString1) + "\n" +
                        getResources().getString(R.string.nasa_imagery_helpString2) + "\n" +
                        getResources().getString(R.string.nasa_imagery_helpString3));
                alert.setNeutralButton("Okay", (click, arg) -> {});
                alert.create().show();
                break;
        }

        return true;
    }
}
