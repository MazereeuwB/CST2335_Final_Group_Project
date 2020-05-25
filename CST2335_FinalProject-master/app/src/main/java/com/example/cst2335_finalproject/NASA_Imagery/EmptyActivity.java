package com.example.cst2335_finalproject.NASA_Imagery;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.cst2335_finalproject.R;

public class EmptyActivity extends AppCompatActivity {
    /**
     * Acts as the phone version of the DetailsFragment, as the database is passed to this new activity as opposed to a fragment
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        Bundle dataToPass = getIntent().getExtras();
        DetailsFragment dFragment = new DetailsFragment(); //add a DetailFragment
        dFragment.setArguments( dataToPass ); //pass it a bundle for information
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frame2, dFragment)
                .addToBackStack("anyname")//Add the fragment in FrameLayout
                .commit(); //actually load the fragment.
    }
}
