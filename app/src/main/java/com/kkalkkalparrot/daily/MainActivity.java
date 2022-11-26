package com.kkalkkalparrot.daily;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.accessibilityservice.AccessibilityService;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;


import java.util.Objects;


public class MainActivity extends FragmentActivity {

    TabLayout tabs;
    Journal fragment1; //화면1
    Community fragment2; //화면2
    Finder fragment3; //화면3
    Habit_tracker fragment4; // 화면4
    
    int position; // 현재 화면


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        String uid = intent.getStringExtra("uid");

        Bundle bundle = new Bundle();
        bundle.putString("uid",uid);

        fragment1 = new Journal();
        fragment2 = new Community();
        fragment2.setArguments(bundle);
        fragment3 = new Finder();
        fragment4 = new Habit_tracker();
        fragment4.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment1).commit();

        tabs = findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("저널"));
        tabs.addTab(tabs.newTab().setText("그룹"));
        tabs.addTab(tabs.newTab().setText("주변"));
        tabs.addTab(tabs.newTab().setText("감정"));


        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Fragment selected = null;
                if(position == 0)
                    selected = fragment1;
                else if(position == 1)
                    selected = fragment2;
                else if(position == 2)
                    selected = fragment3;
                else if(position == 3)
                    selected = fragment4;
                getSupportFragmentManager().beginTransaction().replace(R.id.container, selected).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
}