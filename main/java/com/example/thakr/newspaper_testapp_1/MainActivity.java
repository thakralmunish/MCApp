package com.example.thakr.newspaper_testapp_1;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Random;

import static com.example.thakr.newspaper_testapp_1.NotificationHelper.ALARM_TYPE_RTC;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static FragmentManager fragmentManager;

    public static int Counter = 0;
    public static int New = 0;

    private View mProgressView;
    public View MainView;

    private PendingIntent pendingIntent;
    private AlarmManager manager;

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private StorageReference mStorageRef;
    public static final String FIREBASE_PATH = "test1/";
    public static final String NEWSPAPER_NOTES = "NOTES";
    public static final String NEWS_IMAGES = "NEWS_IMAGES";

    public static File fileVocab;

    //public String CHANNEL_ID = "NOTIFICATION_CHANNEL_ID_1";

    public static SharedData sharedData;

    private int PERMISSION_REQUEST = 1;

    public static File TryImg1;

    public NewspaperSelect newspaperSelect;

    public static File file;

    public static String UserIsThis;

    public static String CHANNEL_ID = "0";

    public static String Word = "DEFAULT WORD";
    public static String Meaning = "DEFAULT MEANING";

    public static Starter starter;

    public static NotificationManager notificationManager;

    //public static File articlesFile, newspapersFile, hashFile, arrayFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.d("LoginCheck", "Permission 2 F");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.d("LoginCheck", "Permission 1 F");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
        }
        Log.d("LoginCheck", "Permission 1 T");


        Log.d("LoginCheck", "Permission 2 T");

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            Log.d("LoginCheck", "Permission 3 F");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1);
        }
        Log.d("LoginCheck", "Permission 3 T");

        notificationManager = getSystemService(NotificationManager.class);


        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        UserIsThis = user.getEmail();
        Log.d("UserCheck", UserIsThis);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ActiveNetwork = connectivityManager.getActiveNetworkInfo();
        boolean IsConnected = (ActiveNetwork != null) && (ActiveNetwork.isConnected());

        String NetCondition = "";
        if (IsConnected) {
            NetCondition = "True";
        }
        else {
            NetCondition = "False";
        }


        starter = new Starter();
        starter.execute(NetCondition);

        VocabDownload vocabDownload = new VocabDownload();
        vocabDownload.execute();

        SavedDownload savedDownload = new SavedDownload();
        savedDownload.execute();

        File temp = new File(Environment.getExternalStorageDirectory(), "TRY_IMAGE");
        TryImg1 = new File(temp, "TestImg1.jpg");

        fileVocab = new File(Environment.getExternalStorageDirectory(), "VOCAB_" + UserIsThis);

        if (!fileVocab.exists()) {
            fileVocab.mkdirs();
        }

        Vocabulary.uri_array = Uri.parse(fileVocab + "binary_array.dat");
        Vocabulary.uri_hash = Uri.parse(fileVocab + "binary_hash.dat");

        try{
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(Vocabulary.uri_array.toString()));
            Vocabulary.items = (ArrayList<String>) objectInputStream.readObject();
            objectInputStream.close();

            objectInputStream = new ObjectInputStream(new FileInputStream(Vocabulary.uri_hash.toString()));
            Vocabulary.meanings = (HashMap<String,String>) objectInputStream.readObject();
            objectInputStream.close();

            /*
            int Size = Vocabulary.items.size();
            Random rand = new Random();
            int n = rand.nextInt(Size);
            Word = Vocabulary.items.get(n);
            Meaning = Vocabulary.meanings.get(Word);
            */


            /*
            Intent alarmIntent = new Intent(this, AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

            startAlarm(MainView);
            */




        }
        catch (Exception e) {
            e.printStackTrace();
        }

        SavedArticlesAdapter.uri_articles = Uri.parse(fileVocab + "saved_articles.dat");
        SavedArticlesAdapter.uri_newspapers = Uri.parse(fileVocab + "saved_newspapers.dat");


        try{
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(SavedArticlesAdapter.uri_articles.toString()));
            SavedArticlesAdapter.Articles = (ArrayList<String>) objectInputStream.readObject();
            objectInputStream.close();

            objectInputStream = new ObjectInputStream(new FileInputStream(SavedArticlesAdapter.uri_newspapers.toString()));
            SavedArticlesAdapter.Newspapers = (ArrayList<String>) objectInputStream.readObject();
            objectInputStream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }




        Intent intent = getIntent();
        String currentuserdisplay = intent.getStringExtra(LoginActivity.CURRENTUSER);

        //TextView Current_User = (TextView) findViewById(R.id.currentuser);
        //Current_User.setText(currentuser.split("@")[0]);

        //TextView UserEmail = (TextView) findViewById(R.id.textView);
        //UserEmail.setText(currentuser);

        /*

        ComponentName receiver = new ComponentName(getApplicationContext(), AlarmBootReceiver.class);
        PackageManager pm = getApplicationContext().getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        scheduleRepeatingRTCNotification(getApplicationContext(), "17", "22");
        */

        /*
        File file = new File(Environment.getExternalStorageDirectory(), NEWSPAPER_NOTES);
        if (file.exists()) {
            deleteRecursive(file);
        }
        */

        sharedData = ViewModelProviders.of(this).get(SharedData.class);
        sharedData.setNewspaperSelected("TODAY");

        newspaperSelect = new NewspaperSelect();
        fragmentManager = getSupportFragmentManager();

        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                New += 1;
                Log.d("BACK", String.valueOf(fragmentManager.getBackStackEntryCount()));
            }

        });

        //fragmentManager.beginTransaction().add(R.id.MainFrame, newspaperSelect).commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vocabulary vocabulary = new Vocabulary();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.MainFrame, vocabulary);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        TextView Current_User = (TextView) header.findViewById(R.id.currentuser);
        Current_User.setText(user.getEmail().split("@")[0].toUpperCase());
        TextView UserEmail = (TextView) header.findViewById(R.id.textView);
        UserEmail.setText(user.getEmail());

    }

    @Override
    protected void onResume() {
        super.onResume();
        //NewspaperSelect.POS_NEWSPAPER_SELECTED = null;
        //ListOfArticles.POS_ARTICLE_SELECTED = null;
    }

    public void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "TEST_CHANNEL";//getString(R.string.channel_name);
            String description = "TEST_DESCRIPTION";//getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static void scheduleRepeatingRTCNotification(Context context, String hour, String min) {
        //get calendar instance to be able to select what time notification should be scheduled
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        //Setting time of the day (8am here) when notification will be sent every day (default)
        calendar.set(Calendar.HOUR_OF_DAY,
                Integer.getInteger(hour, 8),
                Integer.getInteger(min, 0));

        //Setting intent to class where Alarm broadcast message will be handled
        Intent intent = new Intent(context, AlarmReceiver.class);
        //Setting alarm pending intent
        PendingIntent alarmIntentRTC = PendingIntent.getBroadcast(context, ALARM_TYPE_RTC, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //getting instance of AlarmManager service
        AlarmManager alarmManagerRTC = (AlarmManager)context.getSystemService(ALARM_SERVICE);

        //Setting alarm to wake up device every day for clock time.
        //AlarmManager.RTC_WAKEUP is responsible to wake up device for sure, which may not be good practice all the time.
        // Use this when you know what you're doing.
        //Use RTC when you don't need to wake up device, but want to deliver the notification whenever device is woke-up
        //We'll be using RTC.WAKEUP for demo purpose only
        alarmManagerRTC.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntentRTC);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Counter = 1;
        New = 1;

        File file = new File(Environment.getExternalStorageDirectory(), NEWSPAPER_NOTES);

        /*
        if (file.exists()) {
            deleteRecursive(file);
        }

        file = new File(Environment.getExternalStorageDirectory(), NEWSPAPER_NOTES + "_START");
        if (file.exists()) {
            deleteRecursive(file);
        }
        */

    }

    public void startAlarm(View view) {
        manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        int interval = 5000;

        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
    }

    public void cancelAlarm(View view) {
        if (manager != null) {
            manager.cancel(pendingIntent);
            Toast.makeText(this, "Alarm Canceled", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        /*
        File file = new File(Environment.getExternalStorageDirectory(), NEWSPAPER_NOTES);
        if (file.exists()) {
            deleteRecursive(file);
        }

        file = new File(Environment.getExternalStorageDirectory(), NEWSPAPER_NOTES + "_START");
        if (file.exists()) {
            deleteRecursive(file);
        }
        */


        //Vocabulary.uri_array = Uri.parse(fileVocab + "binary_array.dat");
        //Vocabulary.uri_hash = Uri.parse(fileVocab + "binary_hash.dat");

    }

    /*
    void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteRecursive(child);
            }
        }
        fileOrDirectory.delete();
    }
    */

    @Override
    public void onBackPressed() {
        Counter -= 1;
        New -= 1;
        Log.d("BACK", String.valueOf(fragmentManager.getBackStackEntryCount()));
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (fragmentManager.getBackStackEntryCount() == 0) {
                startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME));
            }
            else {
                super.onBackPressed();
            }

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

        int id = item.getItemId();
        if (id == R.id.action_settings) {

            SavedArticlesAdapter.uri_articles = Uri.parse(fileVocab + "saved_articles.dat");
            SavedArticlesAdapter.uri_newspapers = Uri.parse(fileVocab + "saved_newspapers.dat");

            try{
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(SavedArticlesAdapter.uri_articles.toString()));
                SavedArticlesAdapter.Articles = (ArrayList<String>) objectInputStream.readObject();
                objectInputStream.close();

                objectInputStream = new ObjectInputStream(new FileInputStream(SavedArticlesAdapter.uri_newspapers.toString()));
                SavedArticlesAdapter.Newspapers = (ArrayList<String>) objectInputStream.readObject();
                objectInputStream.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            if (NewspaperSelect.POS_NEWSPAPER_SELECTED == null || ListOfArticles.POS_ARTICLE_SELECTED == null) {
                // NOTHING
            }
            else {
                SavedArticlesAdapter.AddNewspaper(NewspaperSelect.POS_NEWSPAPER_SELECTED);
                SavedArticlesAdapter.AddArticle(ListOfArticles.POS_ARTICLE_SELECTED);
                SavedUpdate savedUpdate = new SavedUpdate();
                savedUpdate.execute();
                Log.d("SaveCheck", "##########");
                Log.d("SaveCheck", NewspaperSelect.POS_NEWSPAPER_SELECTED);
                Log.d("SaveCheck", ListOfArticles.POS_ARTICLE_SELECTED);

            }
            return true;
        }

        else if (id == R.id.action_savedarticlesview) {
            SavedArticles savedArticles = new SavedArticles();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.MainFrame, savedArticles);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
            for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
                fragmentManager.popBackStack();
            }
            fragmentManager.beginTransaction().replace(R.id.MainFrame, newspaperSelect).commit();
            Counter = 1;
            New = 1;
        }
        else if (id == R.id.nav_gallery) {
            NewspaperSelect.POS_NEWSPAPER_SELECTED = GridViewAdapter.GridViewItems.get(0);
            final ListOfArticles listOfArticles = new ListOfArticles();
            FragmentTransaction fragmentTransaction = MainActivity.fragmentManager.beginTransaction().replace(R.id.MainFrame, listOfArticles);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            //sharedData.setNewspaperSelected("");
            //SelectedNewspaper selectedNewspaper = new SelectedNewspaper();
            //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.MainFrame, selectedNewspaper);
            //fragmentTransaction.addToBackStack(null);
            //fragmentTransaction.commit();
        }
        else if (id == R.id.nav_slideshow) {
            SavedArticles savedArticles = new SavedArticles();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.MainFrame, savedArticles);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        else if (id == R.id.nav_manage) {
            Vocabulary vocabulary = new Vocabulary();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.MainFrame, vocabulary);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        else if (id == R.id.nav_share) {
            Toast.makeText(getApplicationContext(), "WRITE TO US", Toast.LENGTH_SHORT).show();
            Intent SMSIntent = new Intent(Intent.ACTION_SENDTO);
            SMSIntent.setData(Uri.parse("smsto:8527720505"));
            startActivity(SMSIntent);
        }
        else if (id == R.id.nav_send) {
            Toast.makeText(getApplicationContext(), "SIGNED OUT", Toast.LENGTH_SHORT).show();
            GridViewAdapter.Clear();
            Vocabulary.items.clear();
            SavedArticlesAdapter.Clear();
            cancelAlarm(MainView);
            //GridViewAdapter.Images.clear();
            mAuth.signOut();
            finish();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            File file = new File(Environment.getExternalStorageDirectory(), NEWSPAPER_NOTES);
            /*
            if (file.exists()) {
                deleteRecursive(file);
            }
            */
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            MainView.setVisibility(show ? View.GONE : View.VISIBLE);
            MainView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    MainView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            MainView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    public class Starter extends AsyncTask<String, Integer, String> {

        private StorageReference mStorageRef;
        public File ImgDirectory;
        public String Data = "DEFAULT";
        public ProgressDialog progDailog;
        File file;
        AlertDialog.Builder builder;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progDailog = new ProgressDialog(MainActivity.this);
            progDailog.setMessage("Loading...");
            progDailog.setIndeterminate(false);

            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            //progDailog.setCancelable(false);
            progDailog.show();

            builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Error");
            builder.setMessage("An Unknown Error occurred. Kindly sign in again.");
            builder.setCancelable(false);
            builder.setPositiveButton("Ok, Sign Out", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mAuth.signOut();
                    finish();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    File file = new File(Environment.getExternalStorageDirectory(), NEWSPAPER_NOTES);
                    /*
                    if (file.exists()) {
                        deleteRecursive(file);
                    }
                    */
                }
            });

            ImgDirectory = new File(Environment.getExternalStorageDirectory(), MainActivity.NEWS_IMAGES);
            if (!ImgDirectory.exists()) {
                ImgDirectory.mkdirs();
            }

            //GridViewAdapter.Images.clear();
            /*
            builder.setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    builder.setCancelable(true);
                    finish();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    Intent i = getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage( getBaseContext().getPackageName());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });
            */



        }

        @Override
        protected String doInBackground(String... strings) {

            Log.d("StartCheck", "1");

            FirebaseStorage storage = FirebaseStorage.getInstance();
            mStorageRef = storage.getReferenceFromUrl("gs://newspapertestapp1.appspot.com");
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
            }

            File root = new File(Environment.getExternalStorageDirectory(), MainActivity.NEWSPAPER_NOTES + "_START");



            if (!root.exists()) {
                root.mkdirs();
            }

            file = new File(root,"TRYSTART.txt");


            if (false) {
                Log.d("StartCheck", "2");
                StringBuilder stringBuilder = new StringBuilder();
                try {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line;

                    while ((line = br.readLine()) != null) {
                        stringBuilder.append(line);
                        String NewspaperName = line;
                        GridViewAdapter.AddItem(line);
                        stringBuilder.append('\n');
                        //line = br.readLine();
                        //String ArticleNames = line;
                    }
                    br.close();
                }
                catch (IOException e) {
                    // ERROR HANDLING
                }

                Data = stringBuilder.toString();
                Log.d("GHI", Data);
                Log.d("GHI", "1");
                newspaperSelect = new NewspaperSelect();
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().add(R.id.MainFrame, newspaperSelect).commit();


                for (int i = 0; i < GridViewAdapter.GridViewItems.size(); i++) {
                    StorageReference storageReference2 = mStorageRef.child("image/" + GridViewAdapter.GridViewItems.get(i) + ".bmp");
                    File file2 = new File(ImgDirectory, GridViewAdapter.GridViewItems.get(i) + "_IMAGE" + ".jpg");
                    Log.d("IMGDOWNLOAD", GridViewAdapter.GridViewItems.get(i));
                    storageReference2.getFile(file2)
                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Log.d("IMGDOWNLOAD", "1");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("IMGDOWNLOAD", "2");
                                }
                            });
                }


                progDailog.dismiss();
            }

            else {
                Log.d("StartCheck", "3");
                try {
                    file.createNewFile();
                    Log.d("StartCheck", "4");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("StartCheck", "5");
                }
                StorageReference storageReference = mStorageRef.child("start/newspapers.txt");
                Log.d("StartCheck", "6");

                boolean IsConnected = false;
                Log.d("StartCheck", "7");

                if (strings[0].equals("True")) {
                    IsConnected = true;
                    Log.d("StartCheck", "8");
                }

                if (IsConnected) {
                    Log.d("StartCheck", "9");
                    storageReference.getFile(file)
                            .addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Log.d("StartCheck", "Progressing");
                                }
                            })
                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Log.d("StartCheck", "10");
                                    StringBuilder stringBuilder = new StringBuilder();
                                    try {
                                        BufferedReader br = new BufferedReader(new FileReader(file));
                                        String line;

                                        while ((line = br.readLine()) != null) {
                                            stringBuilder.append(line);
                                            String NewspaperName = line;
                                            stringBuilder.append('\n');
                                            GridViewAdapter.AddItem(line);
                                        }
                                        br.close();
                                    }
                                    catch (IOException e) {
                                        // ERROR HANDLING
                                    }

                                    Data = stringBuilder.toString();
                                    Log.d("GHI", Data);
                                    //Log.d("GHI", D.toString());
                                    Log.d("GHI", "2");

                                    for (int i = 0; i < GridViewAdapter.GridViewItems.size(); i++) {
                                        Log.d("IMGDOWNLOAD", "0B*");
                                        StorageReference storageReference2 = mStorageRef.child("image/" + GridViewAdapter.GridViewItems.get(i) + ".bmp");
                                        Log.d("IMGDOWNLOAD", "0B");
                                        final File file2 = new File(ImgDirectory, GridViewAdapter.GridViewItems.get(i) + "_IMAGE" + ".jpg");
                                        Log.d("IMGDOWNLOAD", GridViewAdapter.GridViewItems.get(i));

                                        storageReference2.getFile(file2)
                                                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                        Log.d("IMGDOWNLOAD", "1B");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.d("IMGDOWNLOAD", "2B");
                                                    }
                                                });
                                    }

                                    newspaperSelect = new NewspaperSelect();
                                    fragmentManager = getSupportFragmentManager();
                                    fragmentManager.beginTransaction().add(R.id.MainFrame, newspaperSelect).commit();


                                    progDailog.dismiss();


                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("StartCheck", "11");
                                    Log.d("StartCheck", e.toString());
                                    Log.d("StartCheck", e.getMessage());

                                    Data = "FAILED";
                                    Log.d("GHI", Data);
                                    Log.d("GHI", "3");
                                    Log.d("GHI", e.toString());
                                    //newspaperSelect = new NewspaperSelect();
                                    //fragmentManager = getSupportFragmentManager();
                                    //fragmentManager.beginTransaction().add(R.id.MainFrame, newspaperSelect).commit();
                                    progDailog.dismiss();
                                    builder.show();

                                }

                            });



                }

            }

            return "";

        }

    }

}