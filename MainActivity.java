package com.example.thakr.newspaper_test1;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static FragmentManager fm;

    private StorageReference mStorageRef;

    public static final String FB_STORAGE_PATH = "image/";
    public static final String FB_DATABASE_PATH = "image";

    private SharedData Model;

    private int My_Permission_Request = 1;

    NewspaperSelect NS;

    public static NavigationView navigationView;

    public static final String POSITION_CLICKED = "POSITION_CLICKED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, My_Permission_Request);
                Log.i("ABC", "18");
            }
            else {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, My_Permission_Request);
                Log.i("ABC", "19");
            }
        }
        else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, My_Permission_Request);
            Log.i("ABC", "20");
        }

        mStorageRef = FirebaseStorage.getInstance().getReference();

        fm = getSupportFragmentManager();

        NS = new NewspaperSelect();

        Model = ViewModelProviders.of(this).get(SharedData.class);

        fm.beginTransaction().add(R.id.MainFrame, NS).commit();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                /*
                Vocabulary V = new Vocabulary();
                //FragmentManager fm = getSupportFragmentManager();
                */
                //Temp1 V = new Temp1();
                //FragmentTransaction ft = fm.beginTransaction().replace(R.id.MainFrame, V);
                //ft.addToBackStack(null);
                //ft.commit();

                Uri imgUri = Uri.fromFile(new File("/storage/emulated/0/DCIM/Camera/IMG_20180929_183532.jpg"));

                StorageReference ref = mStorageRef.child(FB_STORAGE_PATH + System.currentTimeMillis() + ".jpg");

                ConnectivityManager CM = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo ActiveNetwork = CM.getActiveNetworkInfo();
                boolean IsConnected = (ActiveNetwork != null) && (ActiveNetwork.isConnected());

                if (IsConnected) {
                    ref.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getApplicationContext(), "SUCCESS 1", Toast.LENGTH_SHORT).show();
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "FAILURE 1", Toast.LENGTH_SHORT).show();
                                    Log.d("ERRORMESSAGE", e.toString());

                                }
                            });
                }


                navigationView.getMenu().getItem(3).setChecked(true);


            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(0).setChecked(true);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //Toast.makeText(getApplicationContext(), Integer.toString(fm.getBackStackEntryAt(0).getId()), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "TEXT_3 CLICKED", Toast.LENGTH_SHORT).show();
            fm.beginTransaction().replace(R.id.MainFrame, NS).commit();
        } else if (id == R.id.nav_gallery) {
            Toast.makeText(this, "TEXT_4 CLICKED", Toast.LENGTH_SHORT).show();
            Model.setNewspaperSelected("TODAY");
            SelectedNewspaper SN = new SelectedNewspaper();
            //FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction().replace(R.id.MainFrame, SN);
            ft.addToBackStack(null);
            ft.commit();
        } else if (id == R.id.nav_slideshow) {
            Toast.makeText(this, "TEXT_5 CLICKED", Toast.LENGTH_SHORT).show();
            SelectedNewspaper SN = new SelectedNewspaper();
            //FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction().replace(R.id.MainFrame, SN);
            ft.addToBackStack(null);
            ft.commit();
        } else if (id == R.id.nav_manage) {
            Toast.makeText(this, "TEXT_6 CLICKED", Toast.LENGTH_SHORT).show();
            Vocabulary V = new Vocabulary();
            //FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction().replace(R.id.MainFrame, V);
            ft.addToBackStack(null);
            ft.commit();
        } else if (id == R.id.nav_share) {
            Toast.makeText(this, "TEXT_8 CLICKED", Toast.LENGTH_SHORT).show();
            Intent SMSIntent = new Intent(Intent.ACTION_SENDTO);
            SMSIntent.setData(Uri.parse("smsto:8527720505"));
            startActivity(SMSIntent);
        } else if (id == R.id.nav_send) {
            Toast.makeText(this, "TEXT_9 CLICKED", Toast.LENGTH_SHORT).show();
            fm.beginTransaction().replace(R.id.MainFrame, new TempFragment9()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
