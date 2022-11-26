package com.kkalkkalparrot.daily;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.JournalViewHolder> {


    public JournalAdapter(ArrayList<JournalInfo> mArrayList) {
    }

    @NonNull
    @Override
    public JournalAdapter.JournalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull JournalAdapter.JournalViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return 0;
    }


    public class JournalViewHolder extends RecyclerView.ViewHolder{
        public JournalViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
