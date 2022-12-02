package com.kkalkkalparrot.daily;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PopupAdapter extends RecyclerView.Adapter<PopupAdapter.ViewHolder>{

    private List<Popup_Group_Data> GroupList = null;

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

                        // Firebase에 그룹을 해당 그룹을 넣는다.



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
        //holder.mImgTv.setImageResource(item.getJournal_IMG());

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



