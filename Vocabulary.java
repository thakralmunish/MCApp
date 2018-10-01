package com.example.thakr.newspaper_testapp_1;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.HashMap;

public class Vocabulary extends Fragment {
    public View view;
    public SearchView searchView;
    public ListView listView;

    String products[] = {"Dell Inspiron", "HTC One X", "HTC Wildfire S", "HTC Sense", "HTC Sensation XE",
            "iPhone 4S", "Samsung Galaxy Note 800",
            "Samsung Galaxy S3", "MacBook Air", "Mac Mini", "MacBook Pro"};

    public static String[] VOCAB_LIST =  {};
    public ArrayAdapter<String> adapter;
    ArrayList<HashMap<String, String>> words;

    public Vocabulary() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ArrayAdapter<String>(getContext(), R.layout.fragment_vocabulary, R.id.SearchView, products);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_vocabulary, container, false);

        searchView = view.findViewById(R.id.SearchView);
        listView = view.findViewById(R.id.VocabularyListView);
        listView.setAdapter(adapter);

        return view;
    }

}
