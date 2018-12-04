package com.example.thakr.newspaper_testapp_1;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
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
import java.util.HashMap;

public class SavedDownload extends AsyncTask<String, Integer, String> {

    StorageReference mStorageRef;

    @Override
    protected void onPreExecute() {
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    protected String doInBackground(String... strings) {

        StorageReference UploadRef = mStorageRef.child(MainActivity.UserIsThis + "/" + "saved_articles.dat");
        SavedArticlesAdapter.uri_articles = Uri.parse(MainActivity.fileVocab + "saved_articles.dat");

        UploadRef.getFile(SavedArticlesAdapter.uri_articles)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Log.d("SavedDownload", "SUCCESS");
                        SavedArticlesAdapter.uri_articles = Uri.parse(MainActivity.fileVocab + "saved_articles.dat");

                        try{
                            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(SavedArticlesAdapter.uri_articles.toString()));
                            SavedArticlesAdapter.Articles = (ArrayList<String>) objectInputStream.readObject();
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
                        Log.d("SavedDownload", "FAILED");
                    }
                });

        StorageReference UploadRef2 = mStorageRef.child(MainActivity.UserIsThis + "/" + "saved_newspapers.dat");
        SavedArticlesAdapter.uri_newspapers = Uri.parse(MainActivity.fileVocab + "saved_newspapers.dat");

        UploadRef2.getFile(SavedArticlesAdapter.uri_newspapers)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Log.d("SavedDownload", "SUCCESS 2");
                        SavedArticlesAdapter.uri_newspapers = Uri.parse(MainActivity.fileVocab + "saved_newspapers.dat");

                        try{
                            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(SavedArticlesAdapter.uri_newspapers.toString()));
                            SavedArticlesAdapter.Newspapers = (ArrayList<String>) objectInputStream.readObject();
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
                        Log.d("SavedDownload", "FAILED 2");
                    }
                });

        return null;
    }
}
