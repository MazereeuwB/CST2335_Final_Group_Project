package com.example.cst2335_finalproject.GuardianNews;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cst2335_finalproject.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GuardianNewsDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GuardianNewsDetailFragment extends Fragment {

    String newsTitle;
    String newsLink;
    String newsSection;
    GuardianNewsDBHelper dbHelper;
    SQLiteDatabase database;

    public GuardianNewsDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle dataToPass = getArguments();
        newsTitle = dataToPass.getString(GuardianNewActivity.NEWS_TITLE);
        newsLink = dataToPass.getString(GuardianNewActivity.NEWS_Link);
        newsSection = dataToPass.getString(GuardianNewActivity.NEWS_SECTION_NAME);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_guardian_news_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView titleView = getView().findViewById(R.id.fagment_gnews_title);
        titleView.setText(newsTitle);
        TextView linkView = getView().findViewById(R.id.fagment_gnews_link);
        linkView.setText(newsLink);
        TextView sectionView = getView().findViewById(R.id.fagment_gnews_Section);
        sectionView.setText(newsSection);
        Button saveBtn = getView().findViewById(R.id.saveBtnInFragment);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper = new GuardianNewsDBHelper
                        (getContext(),GuardianNewsDBHelper.DB_NAME,null,GuardianNewsDBHelper.DEFAULT_VERSION);
                database = dbHelper.getWritableDatabase();

                ContentValues cv = new ContentValues();
                cv.put(GuardianNewsDBHelper.TITLE,newsTitle);
                cv.put(GuardianNewsDBHelper.URL,newsLink);
                cv.put(GuardianNewsDBHelper.SECTION_NAME,newsSection);
                Long id = database.insert(GuardianNewsDBHelper.TABLE_FAVORITE_GUARDIAN_NEWS,null,cv);

                if(id == -1){
                    Toast.makeText(getContext(),"Failed to save this news",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(),"Successfully save this news",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
