package com.kkalkkalparrot.daily;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

// 선택한 저널 보기
public class LookJournal extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private String documentName;
    String uid;
    protected FirebaseFirestore db;

    Button share_btn;
    TextView writer_name;
    TextView journal_content;
    TextView journal_Title;
    TextView journal_timestamp;
    ImageView journal_image;





    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.journal_look);

        Intent secondIntent = getIntent();
        uid = secondIntent.getStringExtra("uid");
        documentName = secondIntent.getStringExtra("documentname");
        Log.d("Documentname",documentName);

        db = FirebaseFirestore.getInstance();

        share_btn = (Button) findViewById(R.id.share_btn);
        writer_name = (TextView) findViewById(R.id.journal_writer);
        journal_content = (TextView) findViewById(R.id.journal_content);
        journal_Title = (TextView) findViewById(R.id.journal_title);
        journal_timestamp = (TextView) findViewById(R.id.journal_Timestamp);
        journal_image = (ImageView) findViewById(R.id.journal_image);

        //Todo : share_btn 함수 작성
        InitializeData();

    }


    public void InitializeData() {
        Log.d("InitializeData()/ Document name" , documentName);
        DocumentReference docRef = db.collection("Member").document(uid).collection("Journal").document(documentName);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        writer_name.setText(document.get("uid").toString());
                        journal_Title.setText(document.get("Title").toString());
                        journal_content.setText(document.get("Content").toString());
                        //journal_image.setImageBitmap();
                        journal_timestamp.setText(document.get("Timestamp").toString());
                        document.get("GPS");
                        document.get("image");

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }


}
