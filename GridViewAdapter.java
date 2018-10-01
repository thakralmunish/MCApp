package com.example.thakr.newspaper_testapp_1;

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
            R.drawable.sample_image,
            R.drawable.sample_image,
            R.drawable.sample_image,
            R.drawable.sample_image,
            R.drawable.sample_image,
            R.drawable.sample_image,
            R.drawable.sample_image,
            R.drawable.sample_image,
            R.drawable.sample_image,
            R.drawable.sample_image,
            R.drawable.sample_image,
            R.drawable.sample_image,
            R.drawable.sample_image,
            R.drawable.sample_image,
            R.drawable.sample_image,
            R.drawable.sample_image,
            R.drawable.sample_image,
            R.drawable.sample_image,
            R.drawable.sample_image,
            R.drawable.sample_image,
            R.drawable.sample_image,
            R.drawable.sample_image,
            R.drawable.sample_image,
            R.drawable.sample_image,
            R.drawable.sample_image,
            R.drawable.sample_image,
            R.drawable.sample_image,
            R.drawable.sample_image,
            R.drawable.sample_image,
            R.drawable.sample_image,
            R.drawable.sample_image,
            R.drawable.sample_image,
            R.drawable.sample_image,
            R.drawable.sample_image,
            R.drawable.sample_image,
            R.drawable.sample_image,
            R.drawable.sample_image,
            R.drawable.sample_image,
            R.drawable.sample_image,
            R.drawable.sample_image
    };

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView ==  null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(370, 370));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        }
        else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageResource(ItemsOfGridView[position]);
        return imageView;
    }

    @Override
    public int getCount() {
        return ItemsOfGridView.length;
    }

    @Override
    public Object getItem(int position) {
        return ItemsOfGridView[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
