package com.example.thakr.newspaper_testapp_1;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SelectedNewspaper extends Fragment {
    private SharedData sharedData;

    public View view;
    public TextView textView;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    public SelectedNewspaper() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedData = ViewModelProviders.of(getActivity()).get(SharedData.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_selected_newspaper, container, false);

        textView = view.findViewById(R.id.PositionSelected);
        textView.setText(sharedData.getNewspaperSelected());

        return view;
    }

}
