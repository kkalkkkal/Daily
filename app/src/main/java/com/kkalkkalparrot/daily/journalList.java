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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.snapshot.Index;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
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
    String documentName;
    String uid;
    String date;
    FirebaseFirestore db;

    private ArrayList<JournalInfo> journalList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent secondIntent = getIntent();
        uid = secondIntent.getStringExtra("uid");
        int year = secondIntent.getIntExtra("year",2022);
        int month = secondIntent.getIntExtra("month",1);
        int dayOfMonth = secondIntent.getIntExtra("dayOfMonth", 1);
        date = secondIntent.getStringExtra("TimeStamp");

        this.InitializeData();

        setContentView(R.layout.journal_list);
        mRecyclerView = (RecyclerView) findViewById(R.id.JournalRecyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        //layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);

        //mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(new JournalAdapter(journalList));  // Adapter 등록

    }

    public void InitializeData() {
        journalList = new ArrayList<>();

        db = FirebaseFirestore.getInstance();
        db.collection("Member").document(uid).collection("Journal")
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                journalList.add(new JournalInfo(document.get("Title").toString()));
                                Log.d(TAG, "Title : " + document.get("Title").toString());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });


    }


}
