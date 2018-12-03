package com.example.thakr.newspaper_testapp_1;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter {
    Context context;

    public ListViewAdapter(Context C) {
        context = C;
    }

    public ListViewAdapter(Context C, String N) {
        context = C;
        ListViewItems = GridViewAdapter.GetArticlesList(N);
    }

    public String[] ListViewItems = new String[40];

    @Override
    public int getCount() {
        return ListViewItems.length;
    }

    @Override
    public String getItem(int position) {
        return ListViewItems[position];
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
        textView.setText(ListViewItems[position]);
        return textView;
    }
}
