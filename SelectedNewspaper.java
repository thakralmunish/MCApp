package com.example.thakr.newspaper_test1;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SelectedNewspaper extends Fragment {

    private SharedData Model;

    View FragmentView;
    TextView PositionDisplay;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    int PositionToDisplay;

    public SelectedNewspaper() {

    }

    public static SelectedNewspaper newInstance(String param1, String param2) {
        SelectedNewspaper fragment = new SelectedNewspaper();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Model = ViewModelProviders.of(getActivity()).get(SharedData.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentView = inflater.inflate(R.layout.fragment_selected_newspaper, container, false);
        PositionDisplay = FragmentView.findViewById(R.id.PositionOfNewspaperSelected);

        PositionDisplay.setText(Model.getNewspaperSelected());

        return FragmentView;
    }
    
}
