package com.kkalkkalparrot.daily;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.accessibilityservice.AccessibilityService;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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

    public static final int PERMISSIONS_REQUEST = 1000;

    public String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid"); // 로그인한 유저의 ID

        Bundle bundle = new Bundle();
        bundle.putString("uid",uid);

        fragment1 = new Journal();
        fragment1.setArguments(bundle);
        fragment2 = new Community();
        fragment2.setArguments(bundle);
        fragment3 = new Finder();
        fragment3.setArguments(bundle);
        fragment4 = new Habit_tracker();
        fragment4.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment1).commit();

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(this, new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION },
                    PERMISSIONS_REQUEST);
        }

        tabs = findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("Journal"));
        tabs.addTab(tabs.newTab().setText("group"));
        tabs.addTab(tabs.newTab().setText("Find Friends"));
        tabs.addTab(tabs.newTab().setText("habit"));


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