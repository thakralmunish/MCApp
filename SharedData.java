package com.example.thakr.newspaper_test1;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class SharedData extends ViewModel{
    private final MutableLiveData<String> NewspaperSelected = new MutableLiveData<String>();

    public String getNewspaperSelected() {
        return NewspaperSelected.getValue();
    }

    public void setNewspaperSelected(String S) {
        NewspaperSelected.setValue(S);
    }

    public void setNewspaperSelected(int I) {
        NewspaperSelected.setValue(Integer.toString(I));
    }
}
