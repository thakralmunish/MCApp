package com.example.thakr.newspaper_testapp_1;

import android.accounts.NetworkErrorException;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class SavedArticles extends Fragment {
    View view;
    ListView listView;

    public static Downloader downloader;

    SharedData sharedData;

    public static String POS_ARTICLE_SELECTED = "";
    public static String POS_NESPAPER_SELECTED = "";

    public static ArrayList<String> items = new ArrayList<>();

    public SavedArticles() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedData = ViewModelProviders.of(getActivity()).get(SharedData.class);

        SavedArticlesAdapter.uri_articles = Uri.parse(MainActivity.fileVocab + "saved_articles.dat");
        SavedArticlesAdapter.uri_newspapers = Uri.parse(MainActivity.fileVocab + "saved_newspapers.dat");

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



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_list_of_articles, container, false);

        listView = (ListView) view.findViewById(R.id.ArticlesListView);
        final SavedArticlesAdapter savedArticlesAdapter = new SavedArticlesAdapter(getContext());
        listView.setAdapter(savedArticlesAdapter);

        //final ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, items);
        //listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo ActiveNetwork = connectivityManager.getActiveNetworkInfo();
                boolean IsConnected = (ActiveNetwork != null) && (ActiveNetwork.isConnected());

                String NetCondition = "";
                if (IsConnected) {
                    NetCondition = "True";
                }
                else {
                    NetCondition = "False";
                }


                POS_ARTICLE_SELECTED = savedArticlesAdapter.getArticle(position);
                NewspaperSelect.POS_NEWSPAPER_SELECTED = savedArticlesAdapter.getNewspaper(position);
                Log.d("SaveCheck", "*******");
                Log.d("SaveCheck", POS_ARTICLE_SELECTED);
                Log.d("SaveCheck", NewspaperSelect.POS_NEWSPAPER_SELECTED);
                ListOfArticles.POS_ARTICLE_SELECTED = POS_ARTICLE_SELECTED;
                Log.d("ABC", "*1");
                Log.d("ABC", NewspaperSelect.POS_NEWSPAPER_SELECTED);
                Log.d("ABC", POS_ARTICLE_SELECTED);

                downloader = new Downloader();
                Log.d("ABC", "*2");


                sharedData.setNewspaperSelected("Loading...");
                Log.d("ABC", "*3");

                Article article = new Article();
                Log.d("ABC", "*4");
                FragmentTransaction fragmentTransaction = MainActivity.fragmentManager.beginTransaction().replace(R.id.MainFrame, article);
                Log.d("ABC", "*5");
                fragmentTransaction.addToBackStack(null);
                Log.d("ABC", "*6");
                fragmentTransaction.commit();
                Log.d("ABC", "*7");

                downloader.execute(NetCondition);
                Log.d("ABC", "*8");
            }
        });

        return view;
    }

}
