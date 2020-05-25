package com.example.cst2335_finalproject.GuardianNews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cst2335_finalproject.R;


import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {

    private ArrayList<GuardianNews> news;
    private Context ctx;

    public MyAdapter(ArrayList<GuardianNews> news, Context ctx){
        this.news = news;
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return news.size();
    }

    @Override
    public GuardianNews getItem(int position) {
        return news.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(ctx).inflate(R.layout.guardian_news_item,null);
            TextView textView = convertView.findViewById(R.id.gnewsTitle);
            GuardianNews gnews = (GuardianNews)getItem(position);
            textView.setText(gnews.getWebTitle());
        }
        return convertView;
    }
}
