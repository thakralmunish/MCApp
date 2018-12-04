package com.example.thakr.newspaper_testapp_1;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class GridViewAdapter extends BaseAdapter {
    private Context context;
    public static String[][] L = new String[40][40];

    public GridViewAdapter(Context C) {
        context = C;
    }

    public static ArrayList<String> GridViewItems = new ArrayList<String>();

    //public static ArrayList<Uri> Images = new ArrayList<>();


    public static void AddItem(String Str) {
        String S = Str.split(";;;")[0];
        String[] List = Str.split(";;;");
        String[] TempArticles = Arrays.copyOfRange(List, 0, List.length - 1);

        String[] Temp = new String[List.length - 1];

        for (int i = 1; i < List.length; i++) {
            Log.d("Check", "A");
            Log.d("Check", List[i]);
            Temp[i-1] = List[i];
        }

        GridViewItems.add(S);
        L[GridViewItems.size() - 1] = Temp;
    }

    public File ImgDirectory = new File(Environment.getExternalStorageDirectory(), MainActivity.NEWS_IMAGES);

    public static void Clear() {
        GridViewItems.clear();
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
        /*
        TextView textView;
        if (convertView == null) {
            textView = new TextView(context);
            textView.setLayoutParams(new GridView.LayoutParams(370, 370));
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            textView.setPadding(8,8,8,8);
            textView.setTextSize(40);
        }
        else {
            textView = (TextView) convertView;
        }
        textView.setText(GridViewItems.get(position));
        return textView;
        */


        View gridViewAndroid;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            gridViewAndroid = new View(context);
            gridViewAndroid = inflater.inflate(R.layout.try_gridview_layout, null);
            TextView textViewAndroid = (TextView) gridViewAndroid.findViewById(R.id.android_gridview_text);
            ImageView imageViewAndroid = (ImageView) gridViewAndroid.findViewById(R.id.android_gridview_image);
            textViewAndroid.setText(GridViewItems.get(position));

            //imageViewAndroid.setImageResource(ItemsOfGridView[position]);
            imageViewAndroid.setImageURI(Uri.fromFile(new File(ImgDirectory, GridViewItems.get(position) + "_IMAGE" + ".jpg")));
            /*
            if (Images.get(position) == null) {
                imageViewAndroid.setImageResource(R.drawable.sample_image);
            }
            else {
                imageViewAndroid.setImageURI(Uri.fromFile(ImgDirectory, GridViewItems.get(i) + "_IMAGE" + ".jpg"));
            }
            */

            //imageViewAndroid.setImageURI(Uri.fromFile(MainActivity.TryImg1));
        } else {
            gridViewAndroid = (View) convertView;
        }

        return gridViewAndroid;
    }

    /*
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
    */


    public static String[] GetArticlesList (String N) {
        for (int i = 0; i < GridViewItems.size(); i++) {
            if (GridViewItems.get(i).equals(N)) {
                return L[i];
            }
        }
        return null;
    }

    public static String[] GetArticlesList (int N) {
        return L[N];
    }



    @Override
    public int getCount() {
        return GridViewItems.size();
        //return ItemsOfGridView.length;
    }

    @Override
    public String getItem(int position) {
        return GridViewItems.get(position);
        //return ItemsOfGridView[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
