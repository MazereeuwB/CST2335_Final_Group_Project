package com.example.cst2335_finalproject.GuardianNews;

import android.os.AsyncTask;

public class NewsLoader extends AsyncTask {

    public static final String URL_BASE = "https://content.guardianapis.com/search?api-key=1fb36b70-1588-4259-b703-2570ea1fac6a&q=";
    private String search;

    public NewsLoader(String search){
        this.search = search;
    }

    @Override
    protected String doInBackground(Object[] objects) {
        String res = "";
        String url = URL_BASE + this.search;
        return res;
    }
}
