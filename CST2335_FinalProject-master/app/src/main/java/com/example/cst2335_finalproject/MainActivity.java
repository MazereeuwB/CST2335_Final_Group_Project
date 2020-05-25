/**
 * 	Course Name	: CST2335
 * 	Student Name: Ben, Brian, Jaewoo, Wendy
 * 	Class Name	: MainActivity
 * 	Date		: Mar. 6. 2020
 */
package com.example.cst2335_finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageButton;


import com.example.cst2335_finalproject.NASA_Imagery.NASA_ImageryActivity;
import com.example.cst2335_finalproject.GuardianNews.GuardianNewActivity;
import com.example.cst2335_finalproject.NASA_Search.Nasa_SearchPage;
import com.example.cst2335_finalproject.bbc.BBC_Main;

import com.google.android.material.navigation.NavigationView;

/**
 * Launch main application activity.
 * Displays four shortcuts to execute each program
 */
public class MainActivity extends AppCompatActivity {
    ImageButton toGuardianNews;
    Toolbar tb;
    DrawerLayout navDrawer;

    /**
     * Construct title page with a toolbar and shortcuts
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set Action toolbar
        tb = findViewById(R.id.mainToolbar);
        setSupportActionBar(tb);

        findViewById(R.id.imgBtn_BBC_news).setOnClickListener( v -> {
            startActivity(new Intent(MainActivity.this, BBC_Main.class));
        });

        findViewById(R.id.imgBtn_nasa_earthImage).setOnClickListener( v -> {
            startActivity(new Intent(MainActivity.this, NASA_ImageryActivity.class));
        });

        findViewById(R.id.imgBtn_nasa_imageOfDay).setOnClickListener( v -> {
            startActivity(new Intent(MainActivity.this, Nasa_SearchPage.class));
        });

        toGuardianNews = findViewById(R.id.imgBtn_guardian);
        toGuardianNews.setOnClickListener(v->{
            Intent i = new Intent(MainActivity.this, GuardianNewActivity.class);
            startActivity(i);
        });

        //set onclick event to items of nav drawer
        navDrawer = findViewById(R.id.nav_drawer);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
            navDrawer, tb, R.string.open, R.string.close);
        navDrawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_drawer_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if(itemId == R.id.nav_GNews_Button) { //if the item of guardian news is selected, go to guardian news activity
                    //Toast.makeText(getApplicationContext(),"Go to Guardian news from drawer",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MainActivity.this, GuardianNewActivity.class);
                    startActivity(i);
                } else if(itemId == R.id.nav_bbc_Button) {
                    startActivity(new Intent(MainActivity.this, BBC_Main.class));
                } else if(itemId == R.id.nav_nasa_imagery_Button) {
                    startActivity(new Intent(MainActivity.this, NASA_ImageryActivity.class));
                } else if(itemId == R.id.nav_nasa_day_Button){
                    startActivity(new Intent(MainActivity.this, Nasa_SearchPage.class));
                }

                return false;
            }
        });
    }

    /**
     * Attach option menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_toolbar_icons,menu);
        return true;
    }

    /**
     * Start a next activity based on the selected item
     * @param item  MenuItem which is selected
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.guardianNew){
            Intent i = new Intent(MainActivity.this, GuardianNewActivity.class);
            startActivity(i);
        } else if(itemId == R.id.bbcNew){
            startActivity(new Intent(MainActivity.this, BBC_Main.class));
        } else if(itemId == R.id.nasaImage){
            startActivity(new Intent(MainActivity.this, NASA_ImageryActivity.class));
        } else if(itemId == R.id.nasaEarth){
            startActivity(new Intent(MainActivity.this, Nasa_SearchPage.class));
        }
        return true;

    }
}
