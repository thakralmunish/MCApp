package com.example.thakr.newspaper_testapp_1;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class SavedUpdate extends AsyncTask<String, Integer, String> {

    StorageReference mStorageRef;

    @Override
    protected void onPreExecute() {
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    protected String doInBackground(String... strings) {

        //StorageReference UploadRef = mStorageRef.child(MainActivity.UserIsThis + "/" + "saved_articles.dat");
        SavedArticlesAdapter.uri_articles = Uri.parse(MainActivity.fileVocab + "saved_articles.dat");

        //StorageReference UploadRef2 = mStorageRef.child(MainActivity.UserIsThis + "/" + "saved_newspapers.dat");
        SavedArticlesAdapter.uri_newspapers = Uri.parse(MainActivity.fileVocab + "saved_newspapers.dat");


        File file = new File(Environment.getExternalStorageDirectory(), "VOCAB_" + MainActivity.UserIsThis);
        Uri tempUri = Uri.parse(MainActivity.fileVocab + "saved_articles.dat");

        file = new File(tempUri.getPath());

        StorageReference UploadRef = mStorageRef.child(MainActivity.UserIsThis + "/" + "saved_articles.dat");

        Uri uriToUpload = Uri.fromFile(file);

        Log.d("OnStopCheck", "8");


        UploadRef.putFile(uriToUpload)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Log.d("OnStopCheck", "6");
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.d("OnStopCheck", "7");
                        // Handle unsuccessful uploads
                        // ...
                    }
                });


        tempUri = Uri.parse(MainActivity.fileVocab + "saved_newspapers.dat");

        file = new File(tempUri.getPath());

        StorageReference UploadRef2 = mStorageRef.child(MainActivity.UserIsThis + "/" + "saved_newspapers.dat");

        Uri uriToUpload2 = Uri.fromFile(file);

        Log.d("OnStopCheck", "8");


        UploadRef2.putFile(uriToUpload2)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Log.d("OnStopCheck", "6");
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.d("OnStopCheck", "7");
                        // Handle unsuccessful uploads
                        // ...
                    }
                });

        return null;
    }
}
