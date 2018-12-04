package com.example.thakr.newspaper_testapp_1;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class SavedArticlesAdapter extends BaseAdapter {
    Context context;
    public static Uri uri_articles;
    public static Uri uri_newspapers;

    public SavedArticlesAdapter(Context C) {
        context = C;
    }

    public static ArrayList<String> Articles = new ArrayList<String>();
    public static ArrayList<String> Newspapers = new ArrayList<String>();

    public static void Clear () {
        Articles.clear();
        Newspapers.clear();
    }

    public static void AddArticle(String S) {
        Articles.add(S);
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(uri_articles.toString()));
            out.writeObject(Articles);
            out.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void AddNewspaper (String S) {
        Newspapers.add(S);
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(uri_newspapers.toString()));
            out.writeObject(Newspapers);
            out.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        return Articles.size();
    }

    @Override
    public Object getItem(int position) {
        return Articles.get(position);
    }

    public int getArticle(String S) {
        return Articles.indexOf(S);
    }

    public String getArticle (int N) {
        return Articles.get(N);
    }

    public int getNewspaper (String S) {
        return Newspapers.indexOf(S);
    }

    public String getNewspaper (int N) {
        return Newspapers.get(N);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        if (convertView == null) {
            textView = new TextView(context);
            textView.setPadding(8,8,8,8);
            textView.setTextSize(30);
        }
        else {
            textView = (TextView) convertView;
        }
        textView.setText(Articles.get(position));
        return textView;
    }
}
