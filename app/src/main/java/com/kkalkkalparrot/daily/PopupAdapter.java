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
        TextView mTitleTv;
        ImageView mImgTv;
        View mView;
        //protected TextView journalName;
        protected String documentName = "";

        public ViewHolder(View itemView){
            super(itemView);
            mView = itemView;

            //뷰를 초기화
            mTitleTv = itemView.findViewById(R.id.rTitleTv);
            mImgTv = (ImageView) itemView.findViewById(R.id.smallImg);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if ( pos != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(v.getContext(), LookJournal.class);
                        //Log.e("JournalAdpater", documentName);
                        intent.putExtra("documentname", JournalList.get(pos).getDocumentName());
                        intent.putExtra("uid",JournalList.get(pos).getUid());
                        v.getContext().startActivity(intent);
                    }

                }
            });


        } // 생성자 끝

        public void imageSetter(StorageReference srf, Context ct){
            GlideApp
                    .with(ct)
                    .load(srf)
                    .into(mImgTv);

        }
    }

    LookJournal lookJournal;

    Context journal_content;
    FirebaseStorage fs;

    @NonNull
    @Override
    public JournalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View view = LayoutInflater.from(parent.getContext())
        //        .inflate(R.layout.searcheditem_layout, parent, false);
        View view = inflater.inflate(R.layout.searcheditem_layout, parent, false);
        JournalAdapter.ViewHolder viewHolder = new JournalAdapter.ViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull PopupAdapter.ViewHolder holder, int position) {
        //bind view = 데이터를 세팅

        Popup_Group_Data item = GroupList.get(position);

        holder.mTitleTv.setText(GroupList.get(position).getJournalName());
        //holder.mImgTv.setImageResource(item.getJournal_IMG());

        int p = position;

        //holder.documentName = JournalList.get(position).getDocumentName();
        //StorageReference gsReference = fs.getReferenceFromUrl(JournalList.get(position).getJournal_IMG());
        //Log.e("error:", gsReference.toString());
        //holder.imageSetter(gsReference, journal_content);
    }

    @Override
    public int getItemCount() {
        return JournalList.size();
    }


    private String toTimeStamp(Timestamp timestamp){
        Date toTimeStamp = new Date(timestamp.getSeconds()*1000+Math.floorDiv(timestamp.getNanoseconds(),1000));
        SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        return datef.format(toTimeStamp) ;
    }
}



