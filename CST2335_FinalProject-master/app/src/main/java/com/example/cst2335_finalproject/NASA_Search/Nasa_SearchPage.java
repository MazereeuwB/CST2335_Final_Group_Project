package com.example.cst2335_finalproject.NASA_Search;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

//import com.example.cst2335_finalproject.R;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.StrictMode;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cst2335_finalproject.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

public class Nasa_SearchPage extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private TextView nDisplayDate;
    private DatePickerDialog.OnDateSetListener nDataSetListener;
    public String downloadURLRec="";
    ListView listView;
    static public String formattedDate;
    private static customAdapter myAdapter;
    ImageButton favListBtn;
    static ArrayList<singleRow> list;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasa__search_page);

        listView = findViewById(R.id.listView);
        list = new ArrayList<singleRow>();
        //list.add(new singleRow("","","","",""));
        favListBtn = findViewById(R.id.favButton);
        favListBtn.setOnClickListener(click -> {
            Intent nextActivity = new Intent(Nasa_SearchPage.this, Nasa_FavList.class);
            startActivity(nextActivity);
            toast(getResources().getString(R.string.Nasa_Search_FavListToast));
            //make the transition
        });

        listView.setOnItemLongClickListener(((parent, view, position, id) -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Do you want to add to your favorites?");

            alertDialogBuilder.setPositiveButton("yes", (click, arg) -> {
                Bitmap image = list.get(position).getImage();
                String title = list.get(position).getTitle();
                String explanation = list.get(position).getExplanation();
                String date = list.get(position).getDateOfPic();
                String url = list.get(position).getHdPicLink();
                ContentValues newValues = new ContentValues();
                newValues.put(nasaDB.COL_IMAGE, String.valueOf(image));
                newValues.put(nasaDB.COL_TITLE, title);
                newValues.put(nasaDB.COL_EXPLANATION, explanation);
                newValues.put(nasaDB.COL_DATE, date);
                newValues.put(nasaDB.COL_HDURL, url);

                saveDatabase(newValues);

            });
            alertDialogBuilder.setNegativeButton("No", (click, arg) -> {
            });
            alertDialogBuilder.create().show();
            return true;
        }));
        final Nasa_Search getData = new Nasa_Search();

        Button button = (Button) findViewById(R.id.openDateBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");

            }
        });

        Button download = (Button)findViewById(R.id.searchDateBtn);
        download.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                getData.execute();
                toast(getResources().getString(R.string.Nasa_Search_SearchDateToast));
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(downloadURLRec));
//                startActivity(browserIntent);
//                updateImages();
            }
        });

        listView = (ListView)findViewById(R.id.listView);
        myAdapter = new customAdapter(this);
        listView.setAdapter(myAdapter);

    }
    public void toast(String msg){
        Context context = getApplicationContext();
        CharSequence text = msg;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

//    public void toastMsg(String msg) {
//
//        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
//        toast.show();
//    }
//
//    public void displayToastMsg(View v) {
//
//        toastMsg(R.id.Nasa);
//
//    }

    public static boolean addData(Bitmap image, String title ,String explanation,String date, String picUrl) {
        list.add(new singleRow(image, title, explanation, date, picUrl ));
        myAdapter.notifyDataSetChanged();
        return true;
    }

    //now when we choose a date in our DatePickerFragment, we get the year,month
    // and day we choose passed to here
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        // This variable is created to take the Calendar variable above with year,month,day
        // and creates a string with it.
        SimpleDateFormat formatterOut = new SimpleDateFormat("yyyy-MM-dd");
        formattedDate = formatterOut.format(c.getTime());
        TextView textView = (TextView) findViewById(R.id.textView);

        textView.setText(formattedDate);
    }

    private void saveDatabase(ContentValues cv){
        nasaDB nasaOpener = new nasaDB(this);
        db = nasaOpener.getWritableDatabase();
        db.insert(nasaDB.TABLE_NAME,null, cv);
    }


    static class customAdapter extends BaseAdapter {


        Context c;

        customAdapter(Context context){
            c = context;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View row = layoutInflater.inflate(R.layout.singlerow,parent,false);

            ImageView image = (ImageView)row.findViewById(R.id.imageLocation);
            TextView title = (TextView)row.findViewById(R.id.titleOfPOTD);
            TextView explanation = (TextView)row.findViewById(R.id.explanationOfPOTD);
//            TextView url = (TextView)row.findViewById(R.id.url);
            TextView dateOfPic = (TextView)row.findViewById(R.id.dateOfPic);
            TextView linkToHDPic = (TextView)row.findViewById(R.id.linkPicHD);
            // linkToHDPic.setMovementMethod(LinkMovementMethod.getInstance());

            singleRow tmp = list.get(position);

            image.setImageBitmap(tmp.Image);
            title.setText(tmp.Title);
            explanation.setText(tmp.Explanation);
//            url.setText(tmp.Url);
            dateOfPic.setText(tmp.DateOfPic);
            linkToHDPic.setText(tmp.HdPicLink);
            //linkToHDPic.setText(Html.fromHtml(tmp.HdPicLink));


            return row;
        }
    }
}

class singleRow {

    Bitmap Image;
    String Title;
    String Explanation;
    //    String Url;
    String DateOfPic;
    String HdPicLink;

    singleRow(Bitmap image, String title, String explanation, String dateOfPic, String hdPicLink) {
        this.Image = image;
        this.Title = title;
        this.Explanation = explanation;
//        this.Url = url;
        this.DateOfPic = dateOfPic;
        this.HdPicLink = hdPicLink;
    }
    public Bitmap getImage(){return Image;}
    public String getTitle(){return Title;}
    public String getExplanation(){return Explanation;}
    public String getDateOfPic(){return DateOfPic;}
    public String getHdPicLink(){return HdPicLink;}
}
