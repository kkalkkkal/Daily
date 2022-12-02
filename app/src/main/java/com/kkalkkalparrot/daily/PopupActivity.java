package com.kkalkkalparrot.daily;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PopupActivity extends AppCompatActivity {

    protected final FirebaseFirestore db = FirebaseFirestore.getInstance();

    ArrayList<Popup_Group_Data> GroupList;

    String uid;
    String document_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_group);

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid"); // 로그인한 유저의 ID
        document_id = intent.getStringExtra("documentname");





    }

    public void InitializeData() {
        GroupList = new ArrayList<>();

        db.collection("Member").document(uid).collection("Journal")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                GroupList.add(new Popup_Group_Data(document.getId(),document.get("description"), document.get("name"), uid, document_id));
                                Log.d(TAG, "Title : " + document.get("Title").toString());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }

                        PopupAdapter.notifyItemInserted(0);
                    }
                });


    }
}
