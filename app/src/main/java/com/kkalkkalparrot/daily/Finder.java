package com.kkalkkalparrot.daily;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.yalantis.library.Koloda;

import java.util.ArrayList;
import java.util.List;

public class Finder extends Fragment {
    Koloda koloda;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_finder, container, false);

        koloda = rootView.findViewById(R.id.koloda);
        List<Integer> list = new ArrayList<>();

        SwipeAdapter swipeAdapter = new SwipeAdapter(getActivity().getParent(), list);
        koloda.setAdapter(swipeAdapter);


        return rootView;
    }
}