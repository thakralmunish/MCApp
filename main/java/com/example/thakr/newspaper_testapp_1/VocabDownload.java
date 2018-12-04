package com.example.thakr.newspaper_testapp_1;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

import static android.content.Context.ALARM_SERVICE;

public class VocabDownload extends AsyncTask<String, Integer, String> {

    StorageReference mStorageRef;

    @Override
    protected void onPreExecute() {
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    protected String doInBackground(String... strings) {

        StorageReference UploadRef = mStorageRef.child(MainActivity.UserIsThis + "/" + "Vocab_Array.dat");
        Vocabulary.uri_array = Uri.parse(MainActivity.fileVocab + "binary_array.dat");

        UploadRef.getFile(Vocabulary.uri_array)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Log.d("VocabDownload", "SUCCESS");
                        Vocabulary.uri_array = Uri.parse(MainActivity.fileVocab + "binary_array.dat");

                        try{
                            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(Vocabulary.uri_array.toString()));
                            Vocabulary.items = (ArrayList<String>) objectInputStream.readObject();
                            objectInputStream.close();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("VocabDownload", "FAILED");
                    }
                });

        StorageReference UploadRef2 = mStorageRef.child(MainActivity.UserIsThis + "/" + "Vocab_Hash.dat");
        Vocabulary.uri_hash = Uri.parse(MainActivity.fileVocab + "binary_hash.dat");

        UploadRef2.getFile(Vocabulary.uri_hash)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Log.d("VocabDownload", "SUCCESS 2");
                        Vocabulary.uri_hash = Uri.parse(MainActivity.fileVocab + "binary_hash.dat");

                        try{
                            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(Vocabulary.uri_hash.toString()));
                            Vocabulary.meanings = (HashMap<String,String>) objectInputStream.readObject();
                            objectInputStream.close();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }

                        int Length = Vocabulary.items.size();
                        String Word = "DEFAULT";
                        String Meaning = "DEFAULT";

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("VocabDownload", "FAILED 2");
                    }
                });

        return null;
    }

}
