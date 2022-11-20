package com.kkalkkalparrot.daily;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class Community extends Fragment {

    protected boolean setting = false;

    protected String uid;
    protected Map<String, Object> userdata;

    protected final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_community, container, false);

        uid=getArguments().getString("uid");

        Log.d("Community","Community 페이지!!");
        Log.d("Community","유저 UID: " + uid);

        ((ImageButton)rootView.findViewById(R.id.groupList_bt1)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                showDialog("그룹 조작 버튼 누름");
                if (setting == false) {
                    v.setRotation(45);
                    ((View) rootView.findViewById(R.id.groupListBackground)).setVisibility(View.VISIBLE);
                    ((View) rootView.findViewById(R.id.groupList_opt1_t)).setVisibility(View.VISIBLE);
                    ((View) rootView.findViewById(R.id.groupList_opt1_b)).setVisibility(View.VISIBLE);
                    ((View) rootView.findViewById(R.id.groupList_opt2_t)).setVisibility(View.VISIBLE);
                    ((View) rootView.findViewById(R.id.groupList_opt2_b)).setVisibility(View.VISIBLE);
                }else{
                    v.setRotation(0);
                    ((View) rootView.findViewById(R.id.groupListBackground)).setVisibility(View.INVISIBLE);
                    ((View) rootView.findViewById(R.id.groupList_opt1_t)).setVisibility(View.INVISIBLE);
                    ((View) rootView.findViewById(R.id.groupList_opt1_b)).setVisibility(View.INVISIBLE);
                    ((View) rootView.findViewById(R.id.groupList_opt2_t)).setVisibility(View.INVISIBLE);
                    ((View) rootView.findViewById(R.id.groupList_opt2_b)).setVisibility(View.INVISIBLE);
                }
                setting = !setting;
            }
        });

        ((ImageButton)rootView.findViewById(R.id.groupList_opt2_b)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupJoinDialog();
            }
        });

        // 그룹 목록 생성 및 표시


        getUserDB(rootView,inflater,uid);
        Log.d("Community","DocumentSnapshot data: "+ userdata);

//        View.OnClickListener listener = new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
////                showDialog(v.getTag().toString());
//                Intent intent = new Intent(getContext(), GroupFeedsActivity.class);
//                intent.putExtra("id", v.getTag().toString());
//                startActivity(intent);
//            }
//        };
//
//        LinearLayout con = rootView.findViewById(R.id.groupList);
//
//        for(int i =0; i<10; i++) {
//
//            String groupName = "그룹" + Integer.toString(i);
//            String groupDescription = "그룹" + Integer.toString(i)+"에 대한 설명입니다.";
//            String groupTags = "#태그1";
//            for(int j=0;j<i;j++){
//                groupTags+="#태그"+Integer.toString(j+2);
//            }
//
//            LinearLayout ll = new LinearLayout(getContext());
//            LinearLayout groupLayout = (LinearLayout) inflater.inflate(R.layout.group_list_sub,null);
//            groupLayout.setTag(Integer.toString(i));
//            ((TextView)groupLayout.findViewById(R.id.groupName)).setText(groupName);
//            ((TextView)groupLayout.findViewById(R.id.groupDescription)).setText(groupDescription);
//            ((TextView)groupLayout.findViewById(R.id.groupTags)).setText(groupTags);
//            groupLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
//            groupLayout.setOnClickListener(listener);
//
//            ll.addView(groupLayout);
//            con.addView(ll);
//        }

        return rootView;
    }

    private void showDialog(String msg) {
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(getActivity())
                .setTitle("alert")
                .setMessage(msg);
        AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();
    }

    private void groupJoinDialog() {
        final EditText editText = new EditText(getContext());
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(getActivity())
                .setTitle("그룹 가입")
                .setMessage("그룹 코드를 입력하세요")
                .setView(editText).setPositiveButton("가입", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBUpdate_joinGroup();
                    }
                });
        AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();
    }


    private void DBUpdate_joinGroup(){
        //유저의 소속그룹 업데이트
    }

    private void startFeedActivity(String id){
        Intent intent = new Intent(getContext(), GroupFeedsActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    private void getUserDB(ViewGroup rootView, LayoutInflater inflater, String uid){
        DocumentReference docRef = db.collection("Member").document(uid);
        View.OnClickListener listener = new View.OnClickListener(){
            @Override
            public void onClick(View v){
//                showDialog(v.getTag().toString());
                Intent intent = new Intent(getActivity(), GroupFeedsActivity.class);
                intent.putExtra("gid", v.getTag().toString());
                startActivity(intent);
            }
        };
        Task<DocumentSnapshot> test = docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        userdata = document.getData();

                        Log.d("Community UserDB", "DocumentSnapshot data: " + userdata.get("groups"));
                    } else {
                        Log.d("Community UserDB", "No such document");
                    }
                } else {
                    Log.d("Community UserDB", "get failed with ", task.getException());
                }

                LinearLayout con = rootView.findViewById(R.id.groupList);

                if (userdata != null) {

                    for (int i = 0; i < ((ArrayList<String>) userdata.get("groups")).size(); i++) {

                        LinearLayout ll = new LinearLayout(getContext());
                        ll.setPadding(0,50,0,50);

                        getGroupInfo(inflater,listener,ll,i,((ArrayList<String>) userdata.get("groups")).get(i).trim());

                        con.addView(ll);
                    }
                }
            }

        });

    }

    private void getGroupInfo(LayoutInflater inflater,View.OnClickListener listener,LinearLayout ll,int i,String guid){
        DocumentReference docRef = db.collection("Group").document(guid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Map<String, Object> groupData = null;
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        groupData = document.getData();

                        Log.d("Community GroupDB", "Group " + guid + " data: " + groupData);
                    } else {
                        Log.d("Community GroupDB", "Group " + guid +"No such document");
                    }
                } else {
                    Log.d("Community GroupDB", "Group " + guid +"get failed with ", task.getException());
                }


                if (groupData!=null) {
                    String groupName = (String) groupData.get("name");
                    String groupDescription = (String) groupData.get("description");
                    String groupTags = "#"+String.join(" #",(ArrayList<String>)groupData.get("tag"));

                    LinearLayout groupLayout = (LinearLayout) inflater.inflate(R.layout.group_list_sub, null);

                    groupLayout.setTag(guid);
                    ((TextView) groupLayout.findViewById(R.id.groupName)).setText(groupName);
                    ((TextView) groupLayout.findViewById(R.id.groupDescription)).setText(groupDescription);
                    ((TextView) groupLayout.findViewById(R.id.groupTags)).setText(groupTags);
                    groupLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    groupLayout.setOnClickListener(listener);

                    ll.addView(groupLayout);
                }
            }
        });

    }
}