package com.example.thakr.newspaper_test1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class Temp1 extends Fragment {

    public View FragmentView;
    public ImageView IV;

    public Temp1() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentView = inflater.inflate(R.layout.fragment_temp1, container, false);

        IV = FragmentView.findViewById(R.id.TempImageView);

        //IV.setImageResource(R.drawable.temp_letter_n);

        Uri file = Uri.fromFile(new File("/storage/emulated/0/DCIM/Camera/IMG_20180929_183532.jpg"));
        File imgFile = new  File("/storage/emulated/0/DCIM/Camera/IMG_20180929_183532.jpg");

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            IV.setImageBitmap(myBitmap);
            Toast.makeText(getContext(), "DISPLAY IMG", Toast.LENGTH_SHORT).show();
        };


        //IV.setImageURI(null);
        //IV.setImageURI(file);

        return FragmentView;
    }

}
