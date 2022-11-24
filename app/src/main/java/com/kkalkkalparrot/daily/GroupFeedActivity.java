package com.kkalkkalparrot.daily;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.audiofx.EnvironmentalReverb;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.zip.Inflater;

public class GroupFeedActivity extends AppCompatActivity {

    protected final FirebaseFirestore db = FirebaseFirestore.getInstance();
    protected Map<String, Object> feedData;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_feed);
        Intent intent = getIntent();
        String gid = intent.getStringExtra("gid");
        Log.d("GroupFeedActivity",gid);
        String fid = intent.getStringExtra("fid");
        Log.d("GroupFeedActivity",fid);

        TextView textView = findViewById(R.id.feedId);
        textView.setText(gid+"->"+fid);

        getFeedDB(gid,fid);

        //피드정보 받아오기
        String feedUserName = "유저이름입니다.";
        String feedUserImg = "https://cdn-icons-png.flaticon.com/512/3135/3135789.png";
        String feedTime = "10"+"분전";
        String feedContent = "내려온 동력은 바로 이것이다 이성은 투명하되 얼음과 같으며 지혜는 날카로우나 갑 속에 든 칼이다 청춘의 끓는 피가 아니더면 인간이 얼마나 쓸쓸하랴?";
        String heartCount = "130";
        String commentCount = "25";

//        ((TextView)findViewById(R.id.feedUserName)).setText(feedUserName);
//        Glide.with(this).load(feedUserImg).circleCrop().into(((ImageView)findViewById(R.id.feedUserImg)));

        ((TextView)findViewById(R.id.feedTime)).setText(feedTime);
        ((TextView)findViewById(R.id.feedContent)).setText(feedContent);
        ((TextView)findViewById(R.id.heartCount)).setText(heartCount);

        ((TextView)findViewById(R.id.commentCount)).setText(commentCount);


//        LinearLayout commentContainer = findViewById(R.id.commentArea);
//        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//        for(int i=0;i<5;i++) {
//
//            //답글 정보 저장
//            String commentUserId = "user" + Integer.toString(i);
//            String commentUserImg = "https://heurm-tutorial.vlpt.us/images/default_thumbnail.png";
//            String commentTime = "10" + "분전";
//            String commentContent = "댓글입니다 ";
//            for(int j=0;j<i;j++){
//                commentContent+="댓글입니다 ";
//            }
//
//            LinearLayout commentLayout = (LinearLayout) inflater.inflate(R.layout.comment_set, null);
//            ((TextView)commentLayout.findViewById(R.id.commentUserName)).setText(commentUserId);
//            Glide.with(this).load(commentUserImg).circleCrop().into(((ImageView)commentLayout.findViewById(R.id.commentUserImg)));
//            ((TextView)commentLayout.findViewById(R.id.commentTime)).setText(commentTime);
//            ((TextView)commentLayout.findViewById(R.id.commentContent)).setText(commentContent);
//            commentContainer.addView(commentLayout);
//        }
//        inflater.inflate(R.layout.comment_set,findViewById(R.id.feed),true);
//        inflater.inflate(R.layout.comment_set,findViewById(R.id.feed),true);
//        inflater.inflate(R.layout.comment_set,findViewById(R.id.feed),true);
//        inflater.inflate(R.layout.comment_set,findViewById(R.id.feed),true);

    }

    private void getFeedDB(String gid, String fid){
        DocumentReference docRef = db.collection("Group").document(gid).collection("feeds").document(fid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        feedData = document.getData();
                        Log.d("Feed DB", "DocumentSnapshot data: " + feedData);

                        getUserDB((ImageView)findViewById(R.id.feedUserImg),(TextView)findViewById(R.id.feedUserName),(String)feedData.get("uid"));

                        ((TextView)findViewById(R.id.feedTime)).setText(toTimeStamp((Timestamp) feedData.get("create_at")));
                        ArrayList<String> feedImgs = (ArrayList<String>) feedData.get("images");
                        LinearLayout l_view = (LinearLayout) findViewById(R.id.feedImages);
                        for (String feedImg : feedImgs) {
                            ImageView feedImageView = new ImageView(getApplicationContext());
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                            DisplayMetrics dm = getResources().getDisplayMetrics();
                            layoutParams.leftMargin = Math.round(5 * dm.density);
                            layoutParams.rightMargin = Math.round(5 * dm.density);
                            feedImageView.setLayoutParams(layoutParams);
                            feedImageView.setAdjustViewBounds(true);
                            Glide.with(getApplicationContext()).load(feedImg).into(feedImageView);
                            l_view.addView(feedImageView);
                        }



                                ((TextView)findViewById(R.id.feedContent)).setText((String)feedData.get("content"));
                        ((TextView)findViewById(R.id.heartCount)).setText(Integer.toString(((ArrayList<String>)feedData.get("heart")).size()));

                        LinearLayout commentContainer = findViewById(R.id.commentArea);
                        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                        getCommentDB(inflater,commentContainer,gid,fid);
                    } else {
                        Log.d("Feed DB", "No such document");
                    }
                } else {
                    Log.d("Feed DB", "get failed with ", task.getException());
                }
            }
        });
    }

    private void getCommentDB(LayoutInflater inflater, LinearLayout commentContainer, String gid, String fid){
        db.collection("Group").document(gid).collection("feeds").document(fid).collection("comment")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            ((TextView)findViewById(R.id.commentCount)).setText(Integer.toString(task.getResult().size()));

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> commentData = document.getData();
                                LinearLayout commentLayout = (LinearLayout) inflater.inflate(R.layout.comment_set, null);
                                TextView uName = (TextView) commentLayout.findViewById(R.id.commentUserName);
                                ImageView uImg = (ImageView)commentLayout.findViewById(R.id.commentUserImg);

                                getUserDB(uImg,uName,(String)commentData.get("uid"));

                                ((TextView)commentLayout.findViewById(R.id.commentTime)).setText(toTimeStamp((Timestamp) commentData.get("create_at")));
                                ((TextView)commentLayout.findViewById(R.id.commentContent)).setText((String)commentData.get("content"));
                                commentContainer.addView(commentLayout);
                                Log.d("Comments DB", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("Comments DB", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void getUserDB(ImageView imageView,TextView textView, String uid){
        DocumentReference docRef = db.collection("Member").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> feedData = document.getData();
                        Log.d("User DB", "DocumentSnapshot data: " + feedData);
                        textView.setText((String)feedData.get("nickName"));
                        Glide.with(getApplicationContext()).load((String)feedData.get("image")).circleCrop().into(imageView);
                    } else {
                        Log.d("User DB", "No such document");
                    }
                } else {
                    Log.d("User DB", "get failed with ", task.getException());
                }
            }
        });
    }
    private String toTimeStamp(Timestamp timestamp){
        Date toTimeStamp = new Date(timestamp.getSeconds()*1000+timestamp.getNanoseconds()*1000);
        SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        return datef.format(toTimeStamp) ;
    }

}
