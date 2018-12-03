package com.example.thakr.newspaper_testapp_1;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;

public class NewspaperSelect extends Fragment {
    private SharedData sharedData;
    public View view;

    private StorageReference mStorageRef;

    public static String POS_NEWSPAPER_SELECTED = "";

    public NewspaperSelect() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedData = ViewModelProviders.of(getActivity()).get(SharedData.class);
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_newspaper_select, container, false);

        GridView gridView = view.findViewById(R.id.GridView);
        final GridViewAdapter gridViewAdapter = new GridViewAdapter(getContext());
        gridView.setAdapter(gridViewAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                POS_NEWSPAPER_SELECTED = gridViewAdapter.getItem(position);

                /*
                if (position == 1) {
                    sharedData.setNewspaperSelected("Loading...");

                    final Article article = new Article();
                    FragmentTransaction fragmentTransaction = MainActivity.fragmentManager.beginTransaction().replace(R.id.MainFrame, article);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                    File root = new File(Environment.getExternalStorageDirectory(), MainActivity.NEWSPAPER_NOTES);
                    if (!root.exists()) {
                        root.mkdirs();
                    }
                    final File file = new File(root,"DOWNLOADEDFILE.txt");

                    if (file.exists()) {
                        StringBuilder stringBuilder = new StringBuilder();
                        try {
                            BufferedReader br = new BufferedReader(new FileReader(file));
                            String line;

                            while ((line = br.readLine()) != null) {
                                stringBuilder.append(line);
                                stringBuilder.append('\n');
                            }
                            br.close();
                        }
                        catch (IOException e) {
                            // ERROR HANDLING
                        }

                        //article.TEXT_TO_DISPLAY = stringBuilder.toString();
                        sharedData.setNewspaperSelected(stringBuilder.toString());
                    }
                    else {
                        //File localFile = File.createTempFile("DOWNLOADEDFILE", "txt");
                        StorageReference storageReference = mStorageRef.child("image/trydownload.txt");

                        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo ActiveNetwork = connectivityManager.getActiveNetworkInfo();
                        boolean IsConnected = (ActiveNetwork != null) && (ActiveNetwork.isConnected());

                        if (IsConnected) {
                            storageReference.getFile(file)
                                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                            Toast.makeText(getContext(), "SUCCESSFUL DOWNLOAD", Toast.LENGTH_SHORT).show();

                                            StringBuilder stringBuilder = new StringBuilder();
                                            try {
                                                BufferedReader br = new BufferedReader(new FileReader(file));
                                                String line;

                                                while ((line = br.readLine()) != null) {
                                                    stringBuilder.append(line);
                                                    stringBuilder.append('\n');
                                                }
                                                br.close();
                                            }
                                            catch (IOException e) {
                                                // ERROR HANDLING
                                            }

                                            //article.TEXT_TO_DISPLAY = stringBuilder.toString();
                                            sharedData.setNewspaperSelected(stringBuilder.toString());
                                            //article.textView.setText(stringBuilder.toString());
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getContext(), "FAILED DOWNLOAD", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }





                }
                else {
                */
                    final ListOfArticles listOfArticles = new ListOfArticles();
                    FragmentTransaction fragmentTransaction = MainActivity.fragmentManager.beginTransaction().replace(R.id.MainFrame, listOfArticles);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                //}
            }
        });

        return view;
    }

}
