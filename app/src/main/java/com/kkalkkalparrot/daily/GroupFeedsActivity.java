package com.kkalkkalparrot.daily;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class GroupFeedsActivity extends AppCompatActivity {
    private boolean setting = false;
    protected final FirebaseFirestore db = FirebaseFirestore.getInstance();
    protected Timestamp lastTime = new Timestamp(0,0);
    protected ArrayList<DataModel> dataModels = new ArrayList();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_feeds);
        Intent intent = getIntent();
        String gid = intent.getStringExtra("gid");

        Log.d("GroupFeedsActivity",gid);

        View.OnClickListener listener = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showDialog(v.getTag().toString());
//                Intent intent = new Intent(getApplicationContext(), GroupFeedsActivity.class);
//                intent.putExtra("id", v.getTag().toString());
//                startActivity(intent);
            }

        };

        //그룹 추가버튼 리스너
        ((ImageButton)findViewById(R.id.feedsList_bt1)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                showDialog("그룹 조작 버튼 누름");
                if (setting == false) {
                    v.setRotation(45);
                    ((View) findViewById(R.id.feedsListBackground)).setVisibility(View.VISIBLE);
                    ((View) findViewById(R.id.feedsList_opt1_t)).setVisibility(View.VISIBLE);
                    ((View) findViewById(R.id.feedsList_opt1_b)).setVisibility(View.VISIBLE);
                    ((View) findViewById(R.id.feedsList_opt2_t)).setVisibility(View.VISIBLE);
                    ((View) findViewById(R.id.feedsList_opt2_b)).setVisibility(View.VISIBLE);
                }else{
                    v.setRotation(0);
                    ((View) findViewById(R.id.feedsListBackground)).setVisibility(View.INVISIBLE);
                    ((View) findViewById(R.id.feedsList_opt1_t)).setVisibility(View.INVISIBLE);
                    ((View) findViewById(R.id.feedsList_opt1_b)).setVisibility(View.INVISIBLE);
                    ((View) findViewById(R.id.feedsList_opt2_t)).setVisibility(View.INVISIBLE);
                    ((View) findViewById(R.id.feedsList_opt2_b)).setVisibility(View.INVISIBLE);
                }
                setting = !setting;
            }
        });



        myRecyclerViewAdapter adapter;
        RecyclerView recyclerView;
        //데이터 모델리스트


        recyclerView = findViewById(R.id.recyclerview);
        adapter = new myRecyclerViewAdapter(this,dataModels);
        recyclerView.setAdapter(adapter);

        getFeedsDB(adapter,gid,lastTime,10);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (!recyclerView.canScrollVertically(1)) {
                    // 스크롤 마지막일때 새로 데이터추가 새로 로딩

                    getFeedsDB(adapter,gid,lastTime,10);

                }
//                if (!recyclerView.canScrollVertically(-1)) {
//                    Log.i(TAG, "Top of list");
//                } else if (!recyclerView.canScrollVertically(1)) {
//                    Log.i(TAG, "End of list");
//                } else {
//                    Log.i(TAG, "idle");
//                }
            }
        });
        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog("test");
            }
        });
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));

        
    }

    private void showDialog(String msg) {
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(GroupFeedsActivity.this)
                .setTitle("alert")
                .setMessage(msg);
        AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();
    }

    private void getFeedsDB(myRecyclerViewAdapter adapter,String gid, Timestamp start, int limit){

        Query   collection = db.collection("Group").document(gid).collection("feeds").orderBy("create_at").startAfter(start).limit(limit);
        collection.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d("Feeds DB","data size: "+task.getResult().size());
                            if (task.getResult().size()!=0) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Map<String, Object> data = document.getData();
                                    lastTime = (Timestamp) data.get("create_at");
                                    if (((ArrayList<String>) data.get("images")).size() == 0) {
                                        dataModels.add(new DataModel((String) data.get("content"), "", gid, document.getId()));
                                    }else{
                                        dataModels.add(new DataModel((String) data.get("content"), ((ArrayList<String>) data.get("images")).get(0), gid, document.getId()));
                                    }

                                    Log.d("Feeds DB", document.getId() + " => " + data);
                                }
                                adapter.notifyItemInserted(0);
//                                showDialog("추가로딩");
                            }
                        } else {
                            Log.d("Feeds DB", "Error getting documents: ", task.getException());
                        }

                    }
                });
    }


}
