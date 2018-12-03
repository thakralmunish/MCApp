package com.example.thakr.newspaper_testapp_1;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Downloader extends AsyncTask <String, Integer, String> {

    File root, Newspaper, file;
    String Data;
    StorageReference mStorageRef;
    boolean ExitCondition = false;


    @Override
    protected void onPreExecute() {
        ExitCondition = false;

        MainActivity.sharedData.setNewspaperSelected("Loading...");

        Log.d("ABC", "*9");
        root = new File(Environment.getExternalStorageDirectory(), MainActivity.NEWSPAPER_NOTES);
        Log.d("ABC", "*10");
        if (!root.exists()) {
            Log.d("ABC", "*11");
            root.mkdirs();
        }

        Log.d("ABC", "*12");
        Newspaper = new File(root, NewspaperSelect.POS_NEWSPAPER_SELECTED);
        Log.d("ABC", "*13");
        if (! Newspaper.exists()) {
            Log.d("ABC", "*14");
            Newspaper.mkdirs();
            Log.d("ABC", "*15");
        }

        file = new File(Newspaper, ListOfArticles.POS_ARTICLE_SELECTED + ".txt");
        Log.d("ABC", "*16");

        mStorageRef = FirebaseStorage.getInstance().getReference();
        Log.d("ABC", "*17");
    }

    @Override
    protected String doInBackground(String... strings) {
        Log.d("ABC", "*18");

        if (file.exists()) {
            Log.d("ABC", "*19");
            StringBuilder stringBuilder = new StringBuilder();
            Log.d("ABC", "*20");
            try {
                Log.d("ABC", "*21");
                BufferedReader br = new BufferedReader(new FileReader(file));
                Log.d("ABC", "*22");
                String line;
                Log.d("ABC", "*23");

                while ((line = br.readLine()) != null) {
                    Log.d("ABC", "*24");
                    stringBuilder.append(line);
                    Log.d("ABC", "*25");
                    stringBuilder.append('\n');
                    Log.d("ABC", "*26");
                }
                br.close();
                Log.d("ABC", "*27");
                ExitCondition = true;

                Data = stringBuilder.toString();
                Log.d("ABC", Data);


                StorageReference Directory = mStorageRef.child(MainActivity.FIREBASE_PATH);
                Log.d("ABC", "*30");
                StorageReference storageReference = Directory.child("NOT THERE" + "/" + "NOT THERE" + ".txt");

                boolean IsConnected = true;

                if (IsConnected) {
                    storageReference.getFile(file)
                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    //Toast.makeText(getContext(), "SUCCESSFUL DOWNLOAD", Toast.LENGTH_SHORT).show();
                                    Log.d("ABC", "*35");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    MainActivity.sharedData.setNewspaperSelected(Data);
                                }

                                //Toast.makeText(getContext(), "FAILED DOWNLOAD", Toast.LENGTH_SHORT).show();
                            });
                }

                //MainActivity.sharedData.setNewspaperSelected(Data);
                //MainActivity.sharedData.setNewspaperSelected(Data);
                Log.d("ABC", "*28");

            } catch (IOException e) {
                ExitCondition = true;
                Data = "FAILED";
                MainActivity.sharedData.setNewspaperSelected("An Error Occurred");
                Log.d("ABC", "*29");
                // ERROR HANDLING
            }

            //article.TEXT_TO_DISPLAY = stringBuilder.toString();
            //sharedData.setNewspaperSelected(stringBuilder.toString());
        }
        else {
            StorageReference Directory = mStorageRef.child(MainActivity.FIREBASE_PATH);
            Log.d("ABC", "*30");
            StorageReference storageReference = Directory.child(NewspaperSelect.POS_NEWSPAPER_SELECTED + "/" + ListOfArticles.POS_ARTICLE_SELECTED + ".txt");
            Log.d("ABC", "*31");

            boolean IsConnected = false;
            Log.d("ABC", "*32");

            if (strings[0].equals("True")) {
                Log.d("ABC", "*33");
                IsConnected = true;
            }

            if (IsConnected) {
                Log.d("ABC", "*34");
                storageReference.getFile(file)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                //Toast.makeText(getContext(), "SUCCESSFUL DOWNLOAD", Toast.LENGTH_SHORT).show();

                                StringBuilder stringBuilder = new StringBuilder();
                                Log.d("ABC", "*35");
                                try {
                                    BufferedReader br = new BufferedReader(new FileReader(file));
                                    Log.d("ABC", "*36");
                                    String line;
                                    Log.d("ABC", "*37");

                                    while ((line = br.readLine()) != null) {
                                        Log.d("ABC", "*38");
                                        stringBuilder.append(line);
                                        Log.d("ABC", "*39");
                                        stringBuilder.append('\n');
                                        Log.d("ABC", "*40");
                                    }
                                    br.close();
                                    Log.d("ABC", "*41");
                                    ExitCondition = true;
                                    Data = stringBuilder.toString();
                                    MainActivity.sharedData.setNewspaperSelected(Data);
                                    Log.d("ABC", "*42");
                                    Log.d("ABC", Data);
                                }
                                catch (IOException e) {
                                    ExitCondition = true;
                                    Data = "FAILED";
                                    MainActivity.sharedData.setNewspaperSelected("An Error Occurred");
                                    Log.d("ABC", "*43");
                                    // ERROR HANDLING
                                }

                                //article.TEXT_TO_DISPLAY = stringBuilder.toString();

                                //sharedData.setNewspaperSelected(stringBuilder.toString());
                                //article.textView.setText(stringBuilder.toString());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("ABC", "*44");
                                ExitCondition = true;
                                Data = "FAILED";
                                MainActivity.sharedData.setNewspaperSelected("An Error Occurred");
                                //Toast.makeText(getContext(), "FAILED DOWNLOAD", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
        if (Data == null) {
            Data = "Loading...";
        }
        Log.d("ABC", "*45");
        return Data;

    }

}
