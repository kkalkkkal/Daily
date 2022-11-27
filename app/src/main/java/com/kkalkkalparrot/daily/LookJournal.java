package com.kkalkkalparrot.daily;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.google.firebase.firestore.FirebaseFirestore;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

// 선택한 저널 보기
public class LookJournal extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    String searchWord;
    String documentName;
    String uid;
    protected final FirebaseFirestore db = FirebaseFirestore.getInstance();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.journal_look);

        Intent secondIntent = getIntent();
        uid = secondIntent.getStringExtra("uid");
        documentName = secondIntent.getStringExtra("documentname");





        db.collection("board");

    }



}
