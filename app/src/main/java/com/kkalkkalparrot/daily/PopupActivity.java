package com.kkalkkalparrot.daily;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



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
                                journalList.add(new JournalInfo(R.drawable.icons_heart,document.get("Title").toString(), document.getId(), uid));
                                Log.d(TAG, "Title : " + document.get("Title").toString());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }

                        journalAdapter.notifyItemInserted(0);
                    }
                });


    }
}
