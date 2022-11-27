package com.kkalkkalparrot.daily;

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

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.List;


public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.ViewHolder> {


    public JournalAdapter(LookJournal lookJournal, List<JournalInfo> recipeList, Context applicationContext) {
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView mTitleTv;
        ImageView mImgTv;
        View mView;
        protected TextView journalName;
        protected String documentName;

        public ViewHolder(View itemView){
            super(itemView);
            mView = itemView;

            //뷰를 초기화
            mTitleTv = itemView.findViewById(R.id.rTitleTv);
            mImgTv = (ImageView) itemView.findViewById(R.id.smallImg);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), LookJournal.class);
                    Log.e("JournalAdpater", documentName);
                    intent.putExtra("journalName", documentName);
                    v.getContext().startActivity(intent);
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
    List<JournalInfo> JournalList;
    Context journal_content;
    FirebaseStorage fs;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.searcheditem_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //bind view = 데이터를 세팅
        holder.mTitleTv.setText(JournalList.get(position).getJournalName());
        holder.documentName = JournalList.get(position).getDocumentName();
        StorageReference gsReference = fs.getReferenceFromUrl(JournalList.get(position).getJournal_IMG());
        Log.e("error:", gsReference.toString());
        holder.imageSetter(gsReference, journal_content);
    }

    @Override
    public int getItemCount() {
        return JournalList.size();
    }
}
