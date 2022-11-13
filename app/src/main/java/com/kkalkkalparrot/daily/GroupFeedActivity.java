package com.kkalkkalparrot.daily;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.zip.Inflater;

public class GroupFeedActivity extends AppCompatActivity {
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_feed);
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        TextView textView = findViewById(R.id.feedId);
        textView.setText(id);

        //피드정보 받아오기
        String feedUserName = "유저이름입니다.";
        String feedUserImg = "https://cdn-icons-png.flaticon.com/512/3135/3135789.png";
        String feedTime = "10"+"분전";
        String feedContent = "내려온 동력은 바로 이것이다 이성은 투명하되 얼음과 같으며 지혜는 날카로우나 갑 속에 든 칼이다 청춘의 끓는 피가 아니더면 인간이 얼마나 쓸쓸하랴?";
        String heartCount = "130";
        String commentCount = "25";

        ((TextView)findViewById(R.id.feedUserName)).setText(feedUserName);
        Glide.with(this).load(feedUserImg).circleCrop().into(((ImageView)findViewById(R.id.feedUserImg)));
        ((TextView)findViewById(R.id.feedTime)).setText(feedTime);
        ((TextView)findViewById(R.id.feedContent)).setText(feedContent);
        ((TextView)findViewById(R.id.heartCount)).setText(heartCount);
        ((TextView)findViewById(R.id.commentCount)).setText(commentCount);


        LinearLayout commentContainer = findViewById(R.id.commentArea);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for(int i=0;i<5;i++) {

            //답글 정보 저장
            String commentUserId = "user" + Integer.toString(i);
            String commentUserImg = "https://heurm-tutorial.vlpt.us/images/default_thumbnail.png";
            String commentTime = "10" + "분전";
            String commentContent = "댓글입니다 ";
            for(int j=0;j<i;j++){
                commentContent+="댓글입니다 ";
            }

            LinearLayout commentLayout = (LinearLayout) inflater.inflate(R.layout.comment_set, null);
            ((TextView)commentLayout.findViewById(R.id.commentUserName)).setText(commentUserId);
            Glide.with(this).load(commentUserImg).circleCrop().into(((ImageView)commentLayout.findViewById(R.id.commentUserImg)));
            ((TextView)commentLayout.findViewById(R.id.commentTime)).setText(commentTime);
            ((TextView)commentLayout.findViewById(R.id.commentContent)).setText(commentContent);
            commentContainer.addView(commentLayout);
        }
//        inflater.inflate(R.layout.comment_set,findViewById(R.id.feed),true);
//        inflater.inflate(R.layout.comment_set,findViewById(R.id.feed),true);
//        inflater.inflate(R.layout.comment_set,findViewById(R.id.feed),true);
//        inflater.inflate(R.layout.comment_set,findViewById(R.id.feed),true);

    }
}
