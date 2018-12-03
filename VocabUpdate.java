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

public class VocabUpdate extends AsyncTask<String, Integer, String> {

    StorageReference mStorageRef;

    @Override
    protected void onPreExecute() {
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    protected String doInBackground(String... strings) {


        File file = new File(Environment.getExternalStorageDirectory(), "VOCAB_" + MainActivity.UserIsThis);
        Uri tempUri = Uri.parse(MainActivity.fileVocab + "binary_array.dat");

        if (!MainActivity.fileVocab.exists()) {
            Log.d("OnStopCheck", "1");
        }


        if (tempUri == null) {
            Log.d("OnStopCheck", "2");
        }
        else {
            Log.d("OnStopCheck", "3");
        }

        file = new File(tempUri.getPath());

        if (file.exists()) {
            Log.d("OnStopCheck", "4");
        }
        else {
            Log.d("OnStopCheck", "5");
        }

        StorageReference UploadRef = mStorageRef.child(MainActivity.UserIsThis + "/" + "Vocab_Array.dat");

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


        tempUri = Uri.parse(MainActivity.fileVocab + "binary_hash.dat");

        if (!MainActivity.fileVocab.exists()) {
            Log.d("OnStopCheck", "1");
        }


        if (tempUri == null) {
            Log.d("OnStopCheck", "2");
        }
        else {
            Log.d("OnStopCheck", "3");
        }

        file = new File(tempUri.getPath());

        if (file.exists()) {
            Log.d("OnStopCheck", "4");
        }
        else {
            Log.d("OnStopCheck", "5");
        }

        StorageReference UploadRef2 = mStorageRef.child(MainActivity.UserIsThis + "/" + "Vocab_Hash.dat");

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
