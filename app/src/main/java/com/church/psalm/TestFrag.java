package com.church.psalm;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Darren Gu on 9/30/2015.
 */
public class TestFrag extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflater.inflate(R.layout.fragment_lists,container, false);
        return super.onCreateView(inflater, container, savedInstanceState);

    }
}
