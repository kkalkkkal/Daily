package com.kkalkkalparrot.daily;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.JournalViewHolder> {

    private ArrayList<JournalInfo> mList;

    public class RecipeViewHolder extends RecyclerView.ViewHolder{
        protected TextView recipeName;
        protected String documentName;

        public RecipeViewHolder(View view){
            super(view);
            this.recipeName = (TextView)view.findViewById(R.id.JournalName);
            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), LookJournal.class);
                    Log.e("RecipeAdpater", documentName);
                    intent.putExtra("recipeName", documentName);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
    public JournalAdapter(ArrayList<JournalInfo> list){
        this.mList=list;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_info, parent, false);
        RecipeViewHolder viewHolder = new RecipeViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Log.e("RecipeAdapter", mList.get(position).getRecipeName());
        holder.recipeName.setText(mList.get(position).getRecipeName());
        holder.documentName = mList.get(position).getDocumentName();
    }

    @Override
    public int getItemCount() {
        return (null != mList?mList.size() : 0);
    }
}
