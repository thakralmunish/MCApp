package com.example.thakr.newspaper_testapp_1;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;

public class Article extends Fragment {
    public View view;
    public TextView textView;

    private SharedData sharedData;

    public String TEXT_TO_DISPLAY = "Loading";

    public Article() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedData = ViewModelProviders.of(getActivity()).get(SharedData.class);
        final Observer<String> observer = new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(sharedData.getNewspaperSelected());
            }
        };
        sharedData.getNewspaper().observe(this, observer);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_article, container, false);

        textView = view.findViewById(R.id.ArticleTextView);
        textView.setText(sharedData.getNewspaperSelected());

        return view;
    }

}
