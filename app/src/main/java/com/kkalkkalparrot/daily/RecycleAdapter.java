package com.kkalkkalparrot.daily;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {
    private ArrayList<Habit> mData = null ;
    private LocalDate mDate;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout habitView;
        Switch habitSwitch;

        ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            habitView = itemView.findViewById(R.id.habits);

            habitSwitch = habitView.findViewById(R.id.habitSW);

//            if(!mDate.isEqual(LocalDate.now())){
//                habitSwitch.setEnabled(false);
//            }
            habitSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        mData.get(pos).set_did(b);
                    }
                }
            });

        }
    }

    public void changeHabit(ArrayList<Habit> habits){
        mData = habits;
        notifyDataSetChanged();
    }

    public void changeDate(LocalDate date){
        mDate = date;
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    RecycleAdapter(ArrayList<Habit> habits, LocalDate date) {
        mData = habits ;
        mDate = date;
        notifyDataSetChanged();
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public RecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.sub, parent, false);
        RecycleAdapter.ViewHolder vh = new RecycleAdapter.ViewHolder(view);

        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String habitName = mData.get(position).get_name();
        int habitColor = mData.get(position).get_Color();
        Switch tempSw = holder.habitView.findViewById(R.id.habitSW);
        TextView tempC = holder.habitView.findViewById(R.id.habitText);
        tempSw.setText(habitName);
        tempC.setTextColor(habitColor);
        tempSw.setChecked(mData.get(position).get_did());

        if(mDate.isBefore(LocalDate.now())){
            tempSw.setEnabled(false);
            if(mData.get(position).get_did()) {
                holder.habitView.setBackgroundColor(Color.GREEN);
            }
            else{
                holder.habitView.setBackgroundColor(Color.RED);
            }
        }
        else{
            if(mDate.isEqual(LocalDate.now())) {
                tempSw.setEnabled(true);
            }
            else{
                tempSw.setEnabled(false);
            }
            holder.habitView.setBackgroundResource(R.drawable.border);
        }

    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }
}

