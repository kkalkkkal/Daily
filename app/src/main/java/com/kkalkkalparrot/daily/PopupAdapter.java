package com.kkalkkalparrot.daily;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PopupAdapter extends RecyclerView.Adapter<PopupAdapter.ViewHolder>{

    private List<Popup_Group_Data> GroupList = null;
    protected final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public PopupAdapter(List<Popup_Group_Data> grouplist) {
        this.GroupList = grouplist;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView group_name_view;
        TextView group_description_view;
        View mView;
        //protected TextView journalName;
        protected String documentName = "";

        public ViewHolder(View itemView){
            super(itemView);
            mView = itemView;

            //뷰를 초기화
            group_name_view = itemView.findViewById(R.id.group_name);
            group_description_view = itemView.findViewById(R.id.group_description);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if ( pos != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(v.getContext(), MainActivity.class);
                        //Log.e("JournalAdpater", documentName);
                        intent.putExtra("documentname", GroupList.get(pos).getDocument_id());
                        intent.putExtra("uid",GroupList.get(pos).getUid());

                        Timestamp timestamp = new Timestamp(new Date());
                        Map<String, Object> user = new HashMap<>(); // Firebase에 쏠 해시맵

                        // Firebase에서 Member에서 우선 Journal을 찾아 저장할 데이터를 모은다.
                        DocumentReference docRef = db.collection("Member").document(GroupList.get(pos).getUid()).collection("Journal").document(GroupList.get(pos).getDocument_id());

                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                                        user.put("content" , document.get("Content"));
                                        user.put("create_at", timestamp);
                                        user.put("heart", Arrays.asList());
                                        user.put("images", Arrays.asList(document.get("image")));
                                        user.put("uid", GroupList.get(pos).getUid());

                                        db.collection("Group").document(GroupList.get(pos).Group_id).collection("feeds")
                                                .add(user)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "Error adding document", e);
                                                    }
                                                });


                                    } else {
                                        Log.d(TAG, "No such document");
                                    }
                                } else {
                                    Log.d(TAG, "get failed with ", task.getException());
                                }

                            }
                        });

                        //그룹을 해당 그룹을 넣는다.

                        v.getContext().startActivity(intent);
                    }

                }
            });


        } // 생성자 끝

    }


    @NonNull
    @Override
    public PopupAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.popup_group_item, parent, false);
        PopupAdapter.ViewHolder viewHolder = new PopupAdapter.ViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull PopupAdapter.ViewHolder holder, int position) {
        //bind view = 데이터를 세팅

        Popup_Group_Data item = GroupList.get(position);

        holder.group_name_view.setText(GroupList.get(position).getGroup_name());
        holder.group_description_view.setText(GroupList.get(position).getGroup_Description());

        int p = position;

    }

    @Override
    public int getItemCount() {
        return GroupList.size();
    }


    private String toTimeStamp(Timestamp timestamp){
        Date toTimeStamp = new Date(timestamp.getSeconds()*1000+Math.floorDiv(timestamp.getNanoseconds(),1000));
        SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        return datef.format(toTimeStamp) ;
    }
}



