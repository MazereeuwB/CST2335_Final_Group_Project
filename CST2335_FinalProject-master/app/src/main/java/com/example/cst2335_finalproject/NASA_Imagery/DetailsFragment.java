package com.example.cst2335_finalproject.NASA_Imagery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.cst2335_finalproject.R;

/**
 * DetailsFragment activity is a fragment activity that extends off of the listview from NASA_favPage.
 * When an item from that listview is clicked by the user, this activity fragment is brought up, and it displays
 * four textviews containing the direction, time, latitude, and longitude of the selected image item, along with a button
 * to return to the previous activity.  These details are taken from an intent passed from the previous activity.
 */
public class DetailsFragment extends Fragment {
    /**
     * This Bundle contains information passed from the previous class as an intent, such as
     * direction, time, latitude, and longitude variables.
     */
    private Bundle dataFromActivity;
    /**
     * Allows for communication between fragments if possible.
     */
    private OnFragmentInteractionListener mListener;
    /**
     * Base class used for activities that use the support library action bar features.
     */
    private AppCompatActivity parentActivity;
    /**
     *Identifies whether the user is accessing the app from a tablet or not.
     */
    private boolean tablet;
    /**
     * A simple button to either hide the fragment in a tablet view, or back out of the fragment activity if on a phone
     * or smaller device.
     */
    private Button hide;
    /**
     * a textview displaying the direction from which the image was taken.
     */
    private TextView fragDirection;
    /**
     * displays the latitude of the image.
     */
    private TextView fragLat;
    /**
     * displays the longitude of the image.
     */
    private TextView fragLong;
    /**
     *OnCreateView is the method that dictates what view will return to the fragment's UI.  The creator, me, has added
     * four textviews and a button for the user to see.
     * @param inflater is a LayoutInflater that inflates any views in the fragment.
     * @param container is a ViewGroup, which, if not null, is the parent view thar the fragment's UI should be attached to.
     * @param savedInstanceState is a Bundle, if not null, this fragment is being reconstructed from a previous state.
     * @return the view for the fragment's UI.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dataFromActivity = getArguments();
        View result =inflater.inflate(R.layout.activity_details_fragment, container, false);

        fragDirection = result.findViewById(R.id.nasa_imagery_fragDirection);
        fragDirection.setText(getResources().getString(R.string.nasa_imagery_displayDirection)+ " " + dataFromActivity.getString("direction"));

        fragLat =result.findViewById(R.id.nasa_imagery_fragLat);
        fragLat.setText(getResources().getString(R.string.nasa_imagery_displayLat)+" " + dataFromActivity.getDouble("latitude"));

        fragLong =result.findViewById(R.id.nasa_imagery_fragLong);
        fragLong.setText(getResources().getString(R.string.nasa_imagery_displayLong)+ " " + dataFromActivity.getDouble("longitude"));

        tablet = dataFromActivity.getBoolean("istablet");
        hide = result.findViewById(R.id.nasa_imagery_fragBtn);
        hide.setOnClickListener( clk -> {
            parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
            if (!tablet)  parentActivity.finish();
        });
        return result;
    }

    /**
     * called when a fragment is first attached to its context
     * @param context Context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentActivity = (AppCompatActivity)context;
    }

    /**
     * called when a fragment is no longer attached to its activity
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
