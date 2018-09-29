package com.example.thakr.newspaper_test1;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class GridViewAdapter extends BaseAdapter {
    private Context context;

    public GridViewAdapter(Context C) {
        context = C;
    }

    public Integer[] ItemsOfGridView = {
            R.drawable.temp_letter_n,
            R.drawable.temp_letter_n,
            R.drawable.temp_letter_n,
            R.drawable.temp_letter_n,
            R.drawable.temp_letter_n,
            R.drawable.temp_letter_n,
            R.drawable.temp_letter_n,
            R.drawable.temp_letter_n,
            R.drawable.temp_letter_n,
            R.drawable.temp_letter_n,
            R.drawable.temp_letter_n,
            R.drawable.temp_letter_n,
            R.drawable.temp_letter_n,
            R.drawable.temp_letter_n,
            R.drawable.temp_letter_n,
            R.drawable.temp_letter_n,
            R.drawable.temp_letter_n,
            R.drawable.temp_letter_n,
            R.drawable.temp_letter_n,
            R.drawable.temp_letter_n,
            R.drawable.temp_letter_n,
            R.drawable.temp_letter_n,
            R.drawable.temp_letter_n,
            R.drawable.temp_letter_n,
            R.drawable.temp_letter_n,
            R.drawable.temp_letter_n,
            R.drawable.temp_letter_n,
            R.drawable.temp_letter_n,
            R.drawable.temp_letter_n,
            R.drawable.temp_letter_n,
            R.drawable.temp_letter_n,
            R.drawable.temp_letter_n,
            R.drawable.temp_letter_n,
            R.drawable.temp_letter_n,
            R.drawable.temp_letter_n,
            R.drawable.temp_letter_n,
            R.drawable.temp_letter_n,
            R.drawable.temp_letter_n,
            R.drawable.temp_letter_n,
            R.drawable.temp_letter_n
    };

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(370, 370));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageResource(ItemsOfGridView[position]);
        return imageView;
    }

    public int getCount() {
        return ItemsOfGridView.length;
    }

    public Object getItem(int position) {
        return ItemsOfGridView[position];
    }

    public long getItemId(int position) {
        return 0;
    }

}

