package com.example.reseller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentForMyStorageThi extends Fragment {

    public static FragmentForMyStorageSec newInstance() {
        return new FragmentForMyStorageSec();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_for_my_storage_sec, container, false);
        return view;
    }
}