package com.example.cst2335_finalproject.NASA_Imagery;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.cst2335_finalproject.R;

import java.util.ArrayList;

/**
 * NASA_favPage creates and displays a listview of the user's favourited images.  Each item in the list displays as the image
 * that was saved, with the option to click on each item and a fragment will appear displaying the other relevant data relating to
 * the selected image item.  The user is also able to to long click on a selected item to display an alert dialog which prompts the
 * user to delete the selected item if they wish.
 */
public class NASA_favPage extends AppCompatActivity {
    /**
     * Object created to access the MyOpener database
     */
    MyOpener dbHelper = new MyOpener(this);
    /**
     * A ListAdapter object created
     */
    private MyListAdapter myAdapter;
    /**
     * ArrayList which holds NASA_Images objects to display as a listview
     */
    private ArrayList<NASA_Images> imagesArrayList = new ArrayList<>();
    /**
     * Holds direction value of image
     */
    private String direction;
    /**
     * Holds latitude value of image
     */
    private Double latitude;
    /**
     * Holds longitude value of image
     */
    private Double longitude;
    /**
     * Holds bitmap value of image
     */
    private Bitmap image;
    /**
     * Holds byte array data of given image to store in database
     */
    private byte[] nasaBArray;
    /**
     * Holds int id value of image
     */
    private long id;

    /**
     * Displays a listview object which holds data from imagesArrayList.  It checks whether the user is using a phone or a tablet,
     * which will provide either a new activity or a fragment for when the user clicks on a selected list item.
     * The data is then passed to the given fragment/activity via Bundle object.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasa_fav_page);
        ListView myList =findViewById(R.id.nasa_imagery_list);
        DetailsFragment dFragment = new DetailsFragment();
        boolean isTablet = findViewById(R.id.frame) != null;
        loadFromDatabase();
        myAdapter = new MyListAdapter();
        myList.setAdapter(myAdapter);
        myList.setOnItemClickListener((list, item, position, id) -> {
            //Create a bundle to pass data to the new fragment
            Bundle dataToPass = new Bundle();
            dataToPass.putString("direction", imagesArrayList.get(position).getDirection() );
            dataToPass.putDouble("latitude", imagesArrayList.get(position).getLatitude());
            dataToPass.putDouble("longitude", imagesArrayList.get(position).getLongitude());

            if(isTablet)
            {
                //add a DetailFragment
                dFragment.setArguments( dataToPass ); //pass it a bundle for information
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame, dFragment)
                        .addToBackStack("anyname")//Add the fragment in FrameLayout
                        .commit(); //actually load the fragment.
            }
            else //isPhone
            {
                Intent nextActivity = new Intent(NASA_favPage.this, EmptyActivity.class);
                dataToPass.putBoolean("istablet", false);
                nextActivity.putExtras(dataToPass); //send data to next activity


                startActivity(nextActivity); //make the transition
            }
        });
        myList.setOnItemLongClickListener((parent, view, position, id) -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle(getResources().getString(R.string.nasa_imagery_alertTitle));
            alert.setMessage(getResources().getString(R.string.nasa_imagery_alertMessage1)+ position
                    + "\n"+getResources().getString(R.string.nasa_imagery_alertMessage2) + id);
            alert.setPositiveButton( getResources().getString(R.string.nasa_imagery_alertYes), (click, arg) -> {
                NASA_Images selectedImage = imagesArrayList.get(position);
                NASA_imagePage.db.delete(MyOpener.TABLE_NAME, MyOpener.COL_ID + "= ?", new String[] {Long.toString(selectedImage.getId())});
                imagesArrayList.remove(selectedImage);
                myAdapter.notifyDataSetChanged();
            });
            alert.setNegativeButton( getResources().getString(R.string.nasa_imagery_alertNo), (click, arg) -> { })
                    .create().show();
            return true;
        });
    }

    /**
     * Data stored in the database is queried and then the queried result is looped over to get each row from the database,
     * which is then stored as an object of NASA_Images.
     *
     */
    public void loadFromDatabase(){
        NASA_imagePage.db = dbHelper.getWritableDatabase();
        String [] columns = {MyOpener.COL_ID, MyOpener.COL_LAT, MyOpener.COL_LONG, MyOpener.COL_DIRECTION, MyOpener.COL_IMAGE};
        //query all the results from the database:
        Cursor results = NASA_imagePage.db.query(false, MyOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        //Now the results object has rows of results that match the query.
        //find the column indices:
        int directionColIndex = results.getColumnIndex(MyOpener.COL_DIRECTION);
        int imageColIndex = results.getColumnIndex(MyOpener.COL_IMAGE);
        int latColIndex = results.getColumnIndex(MyOpener.COL_LAT);;
        int longColIndex = results.getColumnIndex((MyOpener.COL_LONG));
        int idColIndex = results.getColumnIndex(MyOpener.COL_ID);

        //iterate over the results, return true if there is a next item:
        while(results.moveToNext())
        {

             direction = results.getString(directionColIndex);
             latitude = Double.valueOf(results.getString(latColIndex));
            longitude = Double.valueOf(results.getString(longColIndex));
            nasaBArray = results.getBlob(imageColIndex);
             id = results.getLong(idColIndex);
            image = BitmapFactory.decodeByteArray(nasaBArray, 0, nasaBArray.length);

            imagesArrayList.add(new NASA_Images(direction, latitude, longitude, image, id));
        }
    }

    /**
     * Gives the base methods to display a listview for the user
     */
    private class MyListAdapter extends BaseAdapter{
        /**
         *
         * @return size of the arraylist
         */
        @Override
        public int getCount() {
           return imagesArrayList.size();

        }

        /**
         *
         * @param position of the currently selected item
         * @return the item associated with the position given
         */
        @Override
        public NASA_Images getItem(int position) {
            return imagesArrayList.get(position);
        }

        /**
         *
         * @param position of the currently selected item
         * @return the item id within the database
         */
        @Override
        public long getItemId(int position) {
            return imagesArrayList.get(position).getId();
        }

        /**
         * inflates the view for each list item
         * @param position of the given item
         * @param convertView
         * @param parent
         * @return the inflated view
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();

            View newView = inflater.inflate(R.layout.nasa_imagery_listitemview, parent, false);

            ImageView imageViewImage = newView.findViewById(R.id.nasa_imagery_favImage);
            imageViewImage.setImageBitmap(getItem(position).getImage());

            return newView;
        }
    }
}
