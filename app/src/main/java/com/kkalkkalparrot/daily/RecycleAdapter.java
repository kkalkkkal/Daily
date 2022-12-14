package com.kkalkkalparrot.daily;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {
    private ArrayList<HabitCheck> mData = null ;
    private LocalDate mDate;
    private Habit_tracker habitContext;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout habitView;
        Switch habitSwitch;
        Button habitEditAndDeleteBtn;

        public void setOnMyClickListener(View.OnClickListener oCL){
            habitEditAndDeleteBtn.setOnClickListener(oCL);
        }

        public int getNowPosition(){
            return getAdapterPosition();
        }

        ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            habitView = itemView.findViewById(R.id.habits);

            habitSwitch = habitView.findViewById(R.id.habitSW);
            habitEditAndDeleteBtn = habitView.findViewById(R.id.habitEditAndDelete);

//            if(!mDate.isEqual(LocalDate.now())){
//                habitSwitch.setEnabled(false);
//            }
            habitSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    int pos = getAdapterPosition();

                    Long TS = LocalDate.now().toEpochDay() * 86400;
                    Map<String, Object> tempMap = new HashMap<>();

                    habitContext.memberDoc.collection("Habits").document(String.valueOf(TS)).update(habitContext.habits.get(pos).get_name(), b);

                    if(pos != RecyclerView.NO_POSITION){
                        mData.get(pos).set_complete(b);
                    }
                }
            });

            habitEditAndDeleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getNowPosition();//                                                    java.sql.Timestamp tempTS = java.sql.Timestamp.valueOf(String.valueOf(tempdate.atStartOfDay()));

                    habitContext.nowPosition = pos;

                    habitContext.contextEditText.setText(habitContext.habits.get(pos).get_name());

                    habitContext.del_Btn.setVisibility(View.VISIBLE);
                    habitContext.cha_Btn.setVisibility(View.VISIBLE);
                    habitContext.contextEditText.setVisibility(View.VISIBLE);
                    habitContext.textView2.setVisibility(View.VISIBLE);
                    habitContext.recycleView.setVisibility(View.INVISIBLE);

                }
            });

//            itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    final int position = getAbsoluteAdapterPosition();
//                    if (position != RecyclerView.NO_POSITION){
//                        a
//                    }
//
//                    return false;
//                }
//            });
        }

//        ViewHolder(View a_itemView, final OnItemLongClickEventListener a_itemLongClickListener) {
//            super(a_itemView);
//
//            // long Click event
//            a_itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View a_view) {
//                    final int position = getAdapterPosition();
//                    if (position != RecyclerView.NO_POSITION) {
//                        a_itemLongClickListener.onItemLongClick(a_view, position);
//                    }
//
//                    return false;
//                }
//            });
//        }
    }

    public void changeHabit(ArrayList<HabitCheck> habitChecks){
        mData = habitChecks;
        notifyDataSetChanged();
    }

    public void changeDate(LocalDate date){
        mDate = date;
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    RecycleAdapter(ArrayList<HabitCheck> habitChecks, LocalDate date,Habit_tracker context) {
        mData = habitChecks ;
        mDate = date;
        habitContext = context;
        notifyDataSetChanged();
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public RecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.sub, parent, false);
        RecycleAdapter.ViewHolder vh = new RecycleAdapter.ViewHolder(view);
        System.out.println("onCreateViewHolder 실행됨");

        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String habitName = mData.get(position).getHabit().get_name();
        int habitColor = mData.get(position).getHabit().get_Color();
        Switch tempSw = holder.habitView.findViewById(R.id.habitSW);
        TextView tempC = holder.habitView.findViewById(R.id.habitText);
        tempSw.setText(habitName);
        tempC.setTextColor(habitColor);
        tempSw.setChecked(mData.get(position).get_complete());

        if(mDate.isBefore(LocalDate.now())){
            tempSw.setEnabled(false);
            if(mData.get(position).get_complete()) {
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

        System.out.println("onBindViewHolder 실행됨");
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }


//    public interface OnItemLongClickEventListener {
//        void onItemLongClick(View a_view, int a_position);
//    }
//
//    private OnItemLongClickEventListener mItemLongClickListener;
//
//    public void setOnItemLongClickListener(OnItemLongClickEventListener a_listener) {
//        mItemLongClickListener = a_listener;
//    }
}

