package com.example.thakr.newspaper_testapp_1;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static FragmentManager fragmentManager;

    private StorageReference mStorageRef;
    public static final String FB_STORAGE_PATH = "image/";
    public static final String NEWSPAPER_NOTES = "NOTES";

    private SharedData sharedData;

    private int PERMISSION_REQUEST = 1;

    public NewspaperSelect newspaperSelect;
    public static final String POSITION_CLICKED = "POSITION_CLICKED";

    public static NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PERMISSION_REQUEST);
        }

        File file = new File(Environment.getExternalStorageDirectory(), NEWSPAPER_NOTES);
        if (file.exists()) {
            Log.d("ABC", "1");
            deleteRecursive(file);
            Log.d("ABC", "2");
            Log.d("ABC", Boolean.toString(file.isDirectory()));
        }

        mStorageRef = FirebaseStorage.getInstance().getReference();

        sharedData = ViewModelProviders.of(this).get(SharedData.class);
        sharedData.setNewspaperSelected("TODAY");

        newspaperSelect = new NewspaperSelect();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.MainFrame, newspaperSelect).commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                /*
                String TempText = "HI THERE! SUCCESSFULLY CREATED A LOCAL TEXT FILE";

                Uri imgUri = Uri.fromFile(GenerateTempTextFile(getApplicationContext(), "TEMPTEXTFILE", TempText));

                StorageReference storageReference = mStorageRef.child(FB_STORAGE_PATH + System.currentTimeMillis() + ".txt");

                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo ActiveNetwork = connectivityManager.getActiveNetworkInfo();
                boolean IsConnected = (ActiveNetwork != null) && (ActiveNetwork.isConnected());

                if (IsConnected) {
                    storageReference.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getApplicationContext(), "SUCCESSFUL UPLOAD", Toast.LENGTH_SHORT).show();
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "SUCCESSFUL UPLOAD", Toast.LENGTH_SHORT).show();
                                }
                            });

                }
                */
                try {
                    File root = new File(Environment.getExternalStorageDirectory(), NEWSPAPER_NOTES);
                    if (!root.exists()) {
                        root.mkdirs();
                    }
                    File file = new File(root,"DOWNLOADEDFILE.txt");
                    //File localFile = File.createTempFile("DOWNLOADEDFILE", "txt");
                    StorageReference storageReference = mStorageRef.child(FB_STORAGE_PATH + "trydownload.txt");

                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo ActiveNetwork = connectivityManager.getActiveNetworkInfo();
                    boolean IsConnected = (ActiveNetwork != null) && (ActiveNetwork.isConnected());

                    if (IsConnected) {
                        storageReference.getFile(file)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(getApplicationContext(), "SUCCESSFUL DOWNLOAD", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "FAILED DOWNLOAD", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
                catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        File file = new File(Environment.getExternalStorageDirectory(), NEWSPAPER_NOTES);
        if (file.exists()) {
            Log.d("ABC", "1");
            deleteRecursive(file);
            Log.d("ABC", "2");
            Log.d("ABC", Boolean.toString(file.isDirectory()));
        }
    }

    void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
    }

    public File GenerateTempTextFile(Context C, String FileName, String Text) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), NEWSPAPER_NOTES);
            if (!root.exists()) {
                root.mkdirs();
            }
            File file = new File(root, FileName + ".txt");
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.append(Text);
            fileWriter.flush();
            fileWriter.close();
            //Toast.makeText(C, "TEXT FILE SAVED ON LOCAL STORAGE", Toast.LENGTH_SHORT).show();
            return file;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(getApplicationContext(), "TEXT_10 CLICKED", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Toast.makeText(getApplicationContext(), "TEXT_3 CLICKED", Toast.LENGTH_SHORT).show();
            fragmentManager.beginTransaction().replace(R.id.MainFrame, newspaperSelect).commit();
        }
        else if (id == R.id.nav_gallery) {
            Toast.makeText(getApplicationContext(), "TEXT_4 CLICKED", Toast.LENGTH_SHORT).show();
            sharedData.setNewspaperSelected("TODAY");
            SelectedNewspaper selectedNewspaper = new SelectedNewspaper();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.MainFrame, selectedNewspaper);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        else if (id == R.id.nav_slideshow) {
            Toast.makeText(getApplicationContext(), "TEXT_5 CLICKED", Toast.LENGTH_SHORT).show();
            SelectedNewspaper selectedNewspaper = new SelectedNewspaper();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.MainFrame, selectedNewspaper);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        else if (id == R.id.nav_manage) {
            Toast.makeText(getApplicationContext(), "TEXT_6 CLICKED", Toast.LENGTH_SHORT).show();
            Vocabulary vocabulary = new Vocabulary();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.MainFrame, vocabulary);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        else if (id == R.id.nav_share) {
            Toast.makeText(getApplicationContext(), "TEXT_8 CLICKED", Toast.LENGTH_SHORT).show();
            Intent SMSIntent = new Intent(Intent.ACTION_SENDTO);
            SMSIntent.setData(Uri.parse("smsto:8527720505"));
            startActivity(SMSIntent);
        }
        else if (id == R.id.nav_send) {
            Toast.makeText(getApplicationContext(), "TEXT_9 CLICKED", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }
}
