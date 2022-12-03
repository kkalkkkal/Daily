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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.media.Image;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Community extends Fragment {

    protected boolean setting = false;

    protected String uid;
    protected Map<String, Object> userdata;

    public ImageButton GL_Btn1;
    public LinearLayout con;

    protected final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_community, container, false);
        uid=getArguments().getString("uid");


        GL_Btn1 = rootView.findViewById(R.id.groupList_bt1);

        View.OnClickListener listener = new View.OnClickListener(){
            @Override
            public void onClick(View v){
//                showDialog(v.getTag().toString());
                Intent intent = new Intent(getContext(), GroupFeedsActivity.class);
                intent.putExtra("gid", v.getTag().toString());
                startActivity(intent);
            }
        };

        //그룹 추가버튼 리스너
        GL_Btn1.setOnClickListener(new View.OnClickListener(){
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
                groupJoinDialog(rootView,inflater);
            }
        });

        ((ImageButton)rootView.findViewById(R.id.groupList_opt1_b)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupCreateDialog(rootView,inflater);
            }
        });

        // 그룹 목록 생성 및 표시


        getUserDB(rootView,inflater,uid);
        Log.d("Community","DocumentSnapshot data: "+ userdata);

        return rootView;
    }

    private void showDialog(String msg) {
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(getActivity())
                .setTitle("alert")
                .setMessage(msg);
        AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();
    }

    private void groupJoinDialog(ViewGroup rootView, LayoutInflater inflater) {
        final EditText joinEditText = new EditText(getContext());
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(getActivity())
                .setTitle("Join Group")
                .setMessage("Please enter your group code")
                .setView(joinEditText).setPositiveButton("Join", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String gid = String.valueOf(joinEditText.getText());
                        Log.d("Community editText",gid);
//                        showDialog(gid);
                        putGroup(rootView, inflater,uid,gid);
                    }
                });
        AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();
    }
    private void groupCreateDialog(ViewGroup rootView, LayoutInflater inflater) {
        final EditText CreateEditText_name = new EditText(getContext());
        CreateEditText_name.setHint("name");
        final EditText CreateEditText_description = new EditText(getContext());
        CreateEditText_description.setHint("description");
//        final EditText CreateEditText_image = new EditText(getContext());
//        CreateEditText_image.setHint("image");
        final EditText CreateEditText_tag = new EditText(getContext());
        CreateEditText_tag.setHint("#tag1 #tag2 #tag3");
        LinearLayout ll = new LinearLayout(getContext());
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setLayoutParams(param);
        ll.addView(CreateEditText_name);
        ll.addView(CreateEditText_description);
//        ll.addView(CreateEditText_image);
        ll.addView(CreateEditText_tag);
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(getActivity())
                .setTitle("Join Group")
                .setMessage("Please enter your group code")
                .setView(ll)
                .setPositiveButton("Join", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = String.valueOf(CreateEditText_name.getText());
                        String description = String.valueOf(CreateEditText_description.getText());
                        String tag = String.valueOf(CreateEditText_tag.getText());
                        ArrayList<String> tags = new ArrayList<>();
                        if (!tag.equals("") && !name.equals("") && !name.equals("")) {
                            if (tag.substring(0,1).equals("#")) {
                                for (String t : (tag.substring(1).trim().split("#"))) {
                                    tags.add(t);
                                }

                                Map<String, Object> data = new HashMap<>();
                                data.put("name",name);
                                data.put("description",description);
                                data.put("image","https://firebasestorage.googleapis.com/v0/b/daily-eeae6.appspot.com/o/imgs%2Ftest_image2.jpg?alt=media&token=0560bcdb-e8c7-4058-8764-900e7dab2bd9");
                                data.put("tag",tags);
                                createGroup(rootView,inflater,uid,data);
                            }else{
                                showDialog("Invalid input");
                            }
                        }else{
                            showDialog("Invalid input");
                        }
                    }
                });
        AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();
    }

    private void getUserDB(ViewGroup rootView, LayoutInflater inflater, String uid){
        DocumentReference docRef = db.collection("Member").document(uid);
        View.OnClickListener listener = new View.OnClickListener(){
            @Override
            public void onClick(View v){
//                showDialog(v.getTag().toString());
                Intent intent = new Intent(getActivity(), GroupFeedsActivity.class);
                intent.putExtra("uid", uid);
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

                con = rootView.findViewById(R.id.groupList);
                con.removeAllViews();
                if (userdata != null) {

                    for (int i = 0; i < ((ArrayList<String>) userdata.get("groups")).size(); i++) {

                        LinearLayout ll = new LinearLayout(getContext());
                        ll.setPadding(0,50,0,50);
                        Log.d("Community UserDB Group","-"+((ArrayList<String>) userdata.get("groups")).get(i)+"-");
                        getGroupInfo(rootView,inflater,listener,ll,i,((ArrayList<String>) userdata.get("groups")).get(i));

                        con.addView(ll);
                    }
                }
            }

        });

    }

    private void getGroupInfo(ViewGroup rootView, LayoutInflater inflater,View.OnClickListener listener,LinearLayout ll,int i,String gid){
        DocumentReference docRef = db.collection("Group").document(gid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Map<String, Object> groupData = null;
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        groupData = document.getData();

                        Log.d("Community GroupDB", "Group " + gid + " data: " + groupData);
                    } else {
                        Log.d("Community GroupDB", "Group " + gid +"No such document");
                    }
                } else {
                    Log.d("Community GroupDB", "Group " + gid +"get failed with ", task.getException());
                }


                if (groupData!=null) {
                    String groupName = (String) groupData.get("name");
                    String groupDescription = (String) groupData.get("description");
                    String groupTags = "#"+String.join(" #",(ArrayList<String>)groupData.get("tag"));

                    LinearLayout groupLayout = (LinearLayout) inflater.inflate(R.layout.group_list_sub, null);

                    Glide.with(groupLayout.findViewById(R.id.groupImage)).load((String) groupData.get("image")).into((ImageView) groupLayout.findViewById(R.id.groupImage));
                    groupLayout.setTag(gid);
                    ((TextView) groupLayout.findViewById(R.id.groupName)).setText(groupName);
                    ((TextView) groupLayout.findViewById(R.id.groupDescription)).setText(groupDescription);
                    ((TextView) groupLayout.findViewById(R.id.groupTags)).setText(groupTags);
                    groupLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    groupLayout.setOnClickListener(listener);

                    groupLayout.findViewById(R.id.del_Btn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d("Community", "del!!");
                            deleteGroup(rootView,inflater,uid,gid);
                        }
                    });

                    ll.addView(groupLayout);
                }
            }
        });
    }

    private void deleteGroup(ViewGroup rootView, LayoutInflater inflater,String uid, String gid){
        DocumentReference userRef = db.collection("Member").document(uid);
        userRef.update("groups", FieldValue.arrayRemove(gid));
        getUserDB(rootView,inflater,uid);
    }

    private void createGroup(ViewGroup rootView, LayoutInflater inflater,String uid, Map data){
        db.collection("Group")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        db.collection("Member").document(uid).update("groups", FieldValue.arrayUnion(
                                documentReference.getId()));
                        getUserDB(rootView,inflater,uid);
                        Log.d("Community GroupDB add", "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Community GroupDB add", "Error adding document", e);
                    }
                });

        getUserDB(rootView,inflater,uid);
    }

    private void putGroup(ViewGroup rootView, LayoutInflater inflater,String uid, String gid){
        DocumentReference userRef = db.collection("Member").document(uid);
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        boolean u_ck=false;
                        ArrayList<String> groups=(ArrayList<String>)document.getData().get("groups");
                        for (String group: groups){
                            if(group.equals(gid)){
                                u_ck=true;
                                break;
                            }
                        }
                        if (u_ck){
                            showDialog(gid+"\nThis is a group you have already joined.");
                        }else{
                            db.collection("Group")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                boolean g_ck = false;
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    if (document.getId().equals(gid)){
                                                        userRef.update("groups", FieldValue.arrayUnion(gid));
                                                        g_ck = true;
                                                        break;
                                                    }
//                                Log.d("Community GrorpDB", document.getId() + " => " + document.getData());
                                                }
                                                if (g_ck){
                                                    showDialog(gid+"\nJoined the group.");
                                                    getUserDB(rootView,inflater,uid);
                                                }
                                                else{
                                                    showDialog(gid+"\nThis group does not exist.");
                                                }
                                            } else {
                                                Log.d("Community GroupDB", "Error getting documents: ", task.getException());
                                            }
                                        }
                                    });
                        }
                        Log.d("Community UserDB", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("Community UserDB", "No such document");
                    }
                } else {
                    Log.d("Community UserDB", "get failed with ", task.getException());
                }
            }
        });

    }
}