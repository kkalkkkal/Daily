package com.kkalkkalparrot.daily;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.yalantis.library.Koloda;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Finder extends Fragment {
    Koloda koloda;
    SwipeAdapter swipeAdapter=null;
    String uid;
    protected final FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<Map<String,Object>> userdatas = new ArrayList<>();
    Map<String,Object> myInfo = new HashMap<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_finder, container, false);
        uid=getArguments().getString("uid");
        koloda = rootView.findViewById(R.id.koloda);

        ImageButton acceptBtn = rootView.findViewById(R.id.acceptBtn);
        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (koloda.getChildCount() != 0){
                    int index = userdatas.size()-koloda.getChildCount();
//                    Toast.makeText(getContext(),"now "+userdatas.get(index).get("nickName"),Toast.LENGTH_SHORT).show();
                    addFriend((String)userdatas.get(index).get("uid"));
                }
            }
        });

        getUserData(rootView);
        return rootView;
    }
    private void getUserData(ViewGroup rootView){
        userdatas =  new ArrayList<>();
        db.collection("Member").document(uid)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("Finder DB", "DocumentSnapshot data: " + document.getData());
                            myInfo = document.getData();
                            db.collection("Member")
                                    .limit(10)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                int i=0;
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    Map<String, Object> userData = document.getData();
                                                    userData.put("uid",document.getId());
                                                    if(!(document.getId().equals(uid)) && !(((ArrayList<String>)myInfo.get("friends")).contains(document.getId()))){
                                                        userdatas.add(userData);
                                                    }
                                                    Log.d("Finder DB", i+" "+document.getId() + " => " + document.getData());
                                                    i++;
                                                }
                                                swipeAdapter = new SwipeAdapter(getActivity().getParent(), userdatas);
                                                koloda.setAdapter(swipeAdapter);

                                            } else {
                                                Log.d("Finder DB", "Error getting documents: ", task.getException());
                                            }
                                        }
                                    });
                        } else {
                            Log.d("Finder DB", "No such document");
                        }
                    } else {
                        Log.d("Finder DB", "get failed with ", task.getException());
                    }
                }
            });


    }

    private void addFriend(String friendUid){
        db.collection("Member").document(uid)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("Finder DB", "DocumentSnapshot data: " + document.getData());
                                myInfo = document.getData();
                                if(((ArrayList<String>)myInfo.get("friends")).contains(friendUid)){
                                    Toast.makeText(getContext(),"This friend is already registered.",Toast.LENGTH_SHORT).show();
                                }else{
                                    db.collection("Member").document(uid).update("friends", FieldValue.arrayUnion(friendUid));
                                    Toast.makeText(getContext(),"You have been registered as a friend.",Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.d("Finder DB", "No such document");
                            }
                        } else {
                            Log.d("Finder DB", "get failed with ", task.getException());
                        }
                    }
                });
    }
}