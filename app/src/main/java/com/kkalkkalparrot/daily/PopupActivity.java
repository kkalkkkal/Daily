package com.kkalkkalparrot.daily;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PopupActivity extends AppCompatActivity {

    protected final FirebaseFirestore db = FirebaseFirestore.getInstance();

    ArrayList<Popup_Group_Data> popup_group_data;
    ArrayList<String> GroupList;

    String uid;
    String document_id;
    RecyclerView mRecyclerView;
    PopupAdapter popupAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_group);

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid"); // 로그인한 유저의 ID
        document_id = intent.getStringExtra("documentname");

        //UI 객체생성
        TextView txtText = (TextView) findViewById(R.id.txtText);
        txtText.setText("Test");

        mRecyclerView = (RecyclerView) findViewById(R.id.group_recyclerview);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(manager);

        //mRecyclerView.setHasFixedSize(true);
        GroupList = new ArrayList<>();


        // 유저의 그룹을 찾기
        DocumentReference docRef = db.collection("Member").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        GroupList = (ArrayList<String>) document.get("groups");

                        InitializeData2();

                        popupAdapter = new PopupAdapter(popup_group_data);
                        mRecyclerView.setAdapter(popupAdapter);  // Adapter 등록

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });




    }

    //확인 버튼 클릭
    public void mOnClose(View v){
        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        setResult(RESULT_OK, intent);

        //액티비티(팝업) 닫기
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()== MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }

    // 그룹 컬렉션에서 선택한 콘렉션 찾기
    public void InitializeData2() {
        popup_group_data = new ArrayList<>();
        DocumentReference docRef;
        for (String group_id : GroupList){
            docRef = db.collection("Group").document(group_id);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            popup_group_data.add(new Popup_Group_Data(document.getId(),document.get("description").toString(), document.get("name").toString(), uid, document_id));


                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                    popupAdapter.notifyItemInserted(0);
                }
            });

        }

    }


}
