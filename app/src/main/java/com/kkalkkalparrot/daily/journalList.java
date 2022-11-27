package com.kkalkkalparrot.daily;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.Query;
import com.google.firebase.database.snapshot.Index;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.channels.CompletionHandler;
import java.util.ArrayList;
import java.util.List;

// 선택한 날짜의 저널 리스트 보기
public class journalList extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    String searchWord;
    String documentName;
    FirebaseFirestore db;
    List<String> arr = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent secondIntent = getIntent();
        int camera_state = secondIntent.getIntExtra("camera", 0);
        String uid = secondIntent.getStringExtra("uid");
        int year = secondIntent.getIntExtra("year",2022);
        int month = secondIntent.getIntExtra("month",1);
        int dayOfMonth = secondIntent.getIntExtra("dayOfMonth", 1);
        String date = secondIntent.getStringExtra("TimeStamp");

        setContentView(R.layout.journal_list);
        mRecyclerView = (RecyclerView) findViewById(R.id.JournalRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        searchWord = getIntent().getStringExtra("SearchWord");



    }



}
