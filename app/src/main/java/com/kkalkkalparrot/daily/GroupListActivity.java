package com.kkalkkalparrot.daily;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class GroupListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        View.OnClickListener listener = new View.OnClickListener(){
            @Override
            public void onClick(View v){
//                showDialog(v.getTag().toString());
                Intent intent = new Intent(getApplicationContext(), GroupFeedsActivity.class);
                intent.putExtra("id", v.getTag().toString());
                startActivity(intent);
            }
        };

        LinearLayout con = findViewById(R.id.groupList);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for(int i =0; i<10; i++) {

            String groupName = "그룹" + Integer.toString(i);
            String groupDescription = "그룹" + Integer.toString(i)+"에 대한 설명입니다.";
            String groupTags = "#태그1";
            for(int j=0;j<i;j++){
                groupTags+="#태그"+Integer.toString(j+2);
            }

            LinearLayout ll = new LinearLayout(this);
            ll.setPadding(0,50,0,50);
            LinearLayout groupLayout = (LinearLayout) inflater.inflate(R.layout.group_list_sub,null);
            groupLayout.setTag(Integer.toString(i));
            ((TextView)groupLayout.findViewById(R.id.groupName)).setText(groupName);
            ((TextView)groupLayout.findViewById(R.id.groupDescription)).setText(groupDescription);
            ((TextView)groupLayout.findViewById(R.id.groupTags)).setText(groupTags);
            groupLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            groupLayout.setOnClickListener(listener);

            ll.addView(groupLayout);
            con.addView(ll);
        }

    }
    private void showDialog(String msg) {
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(GroupListActivity.this)
                .setTitle("alert")
                .setMessage(msg);
        AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();
    }

    public void startFeedActivity(String id){
        Intent intent = new Intent(getApplicationContext(), GroupFeedsActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

}