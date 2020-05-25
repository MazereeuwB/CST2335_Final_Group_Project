package com.example.cst2335_finalproject.bbc;

import com.example.cst2335_finalproject.R;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.widget.Button;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Main activity class for BBC News Reader
 * Contains a fragment for displaying articles and favorites
 *
 * @author Jaewoo Kim
 */
public class BBC_Main extends AppCompatActivity {
    /**
     * the Integers which stores fragment information
     */
    static final int CURRENT_LIST = 1;
    static final int FAVORITES = 2;
    static int currentFragment = CURRENT_LIST;

    /**
     * Initialize variables for Main activity,
     * including Drawer and Fragments
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbc_main);

        Toolbar tBar = findViewById(R.id.bbc_toolbar);
        tBar.setTitle(getString(R.string.bbc_articleList_title));
        setSupportActionBar(tBar);

        DrawerLayout drawer = findViewById(R.id.bbc_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        BBC_Utils.start(this);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.bbc_fragment_main, new BBC_Fragment_ArticleList())
                .commit();
        currentFragment = CURRENT_LIST;

        NavigationView navigationView = findViewById(R.id.bbc_nav_view);
        navigationView.setNavigationItemSelectedListener( item -> {
            switch (item.getItemId())
            {
                case R.id.bbc_menu_help:
                    AlertDialog.Builder myBuilder = new AlertDialog.Builder(this);
                    myBuilder.setTitle(getString(R.string.bbc_help));
                    myBuilder.setMessage(getString(R.string.bbc_help_detail_1)+'\n'+getString(R.string.bbc_help_detail_2)+'\n'+getString(R.string.bbc_help_detail_3));
                    myBuilder.setPositiveButton(getString(R.string.bbc_cm_ok), (click, arg) -> {});
                    myBuilder.create().show();
                    break;
                case R.id.bbc_menu_back:
                    finish();
                    break;
            }

            return false;
        });

        findViewById(R.id.bbc_btn_articleList).setOnClickListener(view -> {
            if( currentFragment == CURRENT_LIST ) {
                Toast.makeText(this, getString(R.string.bbc_already_in), Toast.LENGTH_SHORT).show();
                return;
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.bbc_fragment_main, new BBC_Fragment_ArticleList())
                    .commit();

            ((Toolbar)findViewById(R.id.bbc_toolbar)).setTitle(getString(R.string.bbc_articleList_title));
            currentFragment = CURRENT_LIST;
        });

        findViewById(R.id.bbc_btn_favorites).setOnClickListener(view -> {
            if( currentFragment == FAVORITES ) {
                Toast.makeText(this, getString(R.string.bbc_already_in), Toast.LENGTH_SHORT).show();
                return;
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.bbc_fragment_main, new BBC_Fragment_Favorites())
                    .commit();

            ((Toolbar)findViewById(R.id.bbc_toolbar)).setTitle(getString(R.string.bbc_favorites_title));
            currentFragment = FAVORITES;
        });
    }
}
