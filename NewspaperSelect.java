package com.example.thakr.newspaper_test1;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class NewspaperSelect extends Fragment {

    private SharedData Model;
    View FragmentView;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public NewspaperSelect() {

    }

    public static NewspaperSelect newInstance(String param1, String param2) {
        NewspaperSelect fragment = new NewspaperSelect();
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
        FragmentView = inflater.inflate(R.layout.fragment_newspaper_select, container, false);

        GridView gridView = (GridView) FragmentView.findViewById(R.id.Grid_View);
        gridView.setAdapter(new GridViewAdapter(getContext()));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(parent.getContext(), "POSITION " + Integer.toString(position), Toast.LENGTH_SHORT).show();
                Model.setNewspaperSelected(position);
                SelectedNewspaper SN = new SelectedNewspaper();
                //FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = MainActivity.fm.beginTransaction().replace(R.id.MainFrame, SN);
                ft.addToBackStack(null);
                ft.commit();
                MainActivity.navigationView.getMenu().getItem(2).setChecked(true);
            }
        });


        return FragmentView;
    }

}
