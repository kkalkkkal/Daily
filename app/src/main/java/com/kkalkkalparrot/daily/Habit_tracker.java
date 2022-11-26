package com.kkalkkalparrot.daily;

/*
* 담당자 : 한범진
* */


import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public class Habit_tracker extends Fragment {
    public String str = null;
    public CalendarView calendarView;
    public Button cha_Btn, del_Btn, save_Btn, cancel_Btn;
    public TextView textView2, textView3;
    public EditText contextEditText;
    public FloatingActionButton fab;
    //    public ScrollView scrollView;
    public RecyclerView recycleView;
    //    public Date date = new Date();
    public LocalDate date = LocalDate.now();
    public int nowPosition;


    public ArrayList<Habit> habits= new ArrayList<>();
    public HashMap<LocalDate, ArrayList<HabitCheck>> map = new HashMap<>();

    public void habitAdded(Habit hb) throws CloneNotSupportedException {
        habits.add(hb);
        //        ArrayList<HabitCheck> existingHC;
        for (LocalDate key : map.keySet()) {
            ArrayList<HabitCheck> tempHCS = new ArrayList<>();
            for (HabitCheck hc : map.get(key)) {
                HabitCheck tempHC = (HabitCheck) hc.clone();
                tempHCS.add(tempHC);
            }
//            Habit hb = new Habit(str, Color.rgb(rd.nextInt(256),rd.nextInt(256),rd.nextInt(256)));
//            ArrayList<HabitCheck> hcs = map.get(date);
            HabitCheck newHC = new HabitCheck(hb);
            tempHCS.add(newHC);
            map.put(key, tempHCS);
        }
    }

    public void habitChanged() throws CloneNotSupportedException {
        for (LocalDate key : map.keySet()) {
            ArrayList<HabitCheck> tempHCS = new ArrayList<>();
            for (HabitCheck hc : map.get(key)) {
                HabitCheck tempHC = (HabitCheck) hc.clone();
                tempHCS.add(tempHC);
            }

            map.put(key, tempHCS);
        }
    }

    public void habitDeleted() throws CloneNotSupportedException {

        for (LocalDate key : map.keySet()) {
            ArrayList<HabitCheck> tempHCS = new ArrayList<>();
            int index = 0;
            for (HabitCheck hc : map.get(key)) {
                if (index != nowPosition) {
                    HabitCheck tempHC = (HabitCheck) hc.clone();
                    tempHCS.add(tempHC);
                }
                index += 1;
            }

            map.put(key, tempHCS);
        }
    }

    public void main() throws CloneNotSupportedException {
        if(habits.size() ==0) {
            habits.add(new Habit("습관1", Color.RED));
            habits.add(new Habit("습관2", Color.BLUE));
            habits.add(new Habit("습관3", Color.GRAY));
//        habits.add(new Habit("습관4", Color.GREEN));


            ////////////////////////////////////////////
            //이 사이는 서버로 옮겨도 필요한 부분
            //당일에 대한 맵 데이터 생성
            ArrayList<HabitCheck> testHabitChecks = new ArrayList<>();
            for (Habit habit : habits) {
                HabitCheck hc = new HabitCheck(habit);
                testHabitChecks.add(hc);
            }

            map.put(date, testHabitChecks);
            ///////////////////////////////////////////

            ArrayList<HabitCheck> testHabitChecks2 = new ArrayList<>();
            for (Habit habit : habits) {
                HabitCheck hc = new HabitCheck(habit);
                testHabitChecks2.add(hc);
            }

//        testHabits.addAll(habits);

            testHabitChecks2.get(2).set_complete(true);
            date = date.withDayOfMonth(11);

            map.put(date, testHabitChecks2);

            date = LocalDate.now();
        }

    }

//    public void habitAdded() throws CloneNotSupportedException {
//        Set<LocalDate> keyset = map.keySet();
//        for(LocalDate key : keyset){
//            Habit newHabit = habits.get(habits.size()-1).clone();
////            map.get(key).add(newHabit);
//            Objects.requireNonNull(map.get(key)).add(newHabit);
//        }
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_habit_tracker, container, false);

        try {
            main();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }


        calendarView = rootView.findViewById(R.id.calendarView);
//        diaryTextView = findViewById(R.id.diaryTextView);
        save_Btn = rootView.findViewById(R.id.save_Btn);
        cancel_Btn = rootView.findViewById(R.id.cancel_Btn);
        del_Btn = rootView.findViewById(R.id.del_Btn);
        cha_Btn = rootView.findViewById(R.id.cha_Btn);
        textView2 = rootView.findViewById(R.id.textView2);
        textView3 = rootView.findViewById(R.id.textView3);
        contextEditText = rootView.findViewById(R.id.contextEditText);
        fab = rootView.findViewById(R.id.floatingActionButton);

        recycleView = rootView.findViewById(R.id.recycleView);
        recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));

        RecycleAdapter adapter = new RecycleAdapter((ArrayList<HabitCheck>) map.get(date), date, this);
        recycleView.setAdapter(adapter);

//        RecycleAdapter.ViewHolder vh;
//        vh = adapter.getViewHolder();
//
//        View.OnClickListener clickListener;
//        clickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                nowPosition = vh.getNowPosition();
//
//
//
//                //습관 이름 빼오기
//
//                contextEditText.setText(habits.get(nowPosition).get_name());
//
//
//
//
//                del_Btn.setVisibility(View.VISIBLE);
//                cha_Btn.setVisibility(View.VISIBLE);
//                contextEditText.setVisibility(View.VISIBLE);
//                textView2.setVisibility(View.VISIBLE);
//                recycleView.setVisibility(View.INVISIBLE);
//
//            }
//        };
//        vh.setOnMyClickListener(clickListener);

        cha_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str = contextEditText.getText().toString();
                if (str.isEmpty()){
                    Toast.makeText(getActivity(), "습관을 입력해주세요.", Toast.LENGTH_LONG).show();
                    return;
                }
                habits.get(nowPosition).set_name(str);
                try {
                    habitChanged();
                    adapter.changeHabit(map.get(date));
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }

//                diaryTextView.setVisibility(View.INVISIBLE);
                cha_Btn.setVisibility(View.INVISIBLE);
                del_Btn.setVisibility(View.INVISIBLE);
                contextEditText.setVisibility(View.INVISIBLE);
                textView2.setVisibility(View.INVISIBLE);
                recycleView.setVisibility(View.VISIBLE);

                contextEditText.setText("");
            }
        });

        del_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                habits.remove(nowPosition);

                try {
                    habitDeleted();
                    adapter.changeHabit(map.get(date));
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }

                cha_Btn.setVisibility(View.INVISIBLE);
                del_Btn.setVisibility(View.INVISIBLE);
                contextEditText.setVisibility(View.INVISIBLE);
                textView2.setVisibility(View.INVISIBLE);
                recycleView.setVisibility(View.VISIBLE);

                contextEditText.setText("");
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                diaryTextView.setVisibility(View.VISIBLE);
                save_Btn.setVisibility(View.VISIBLE);
                cancel_Btn.setVisibility(View.VISIBLE);
                contextEditText.setVisibility(View.VISIBLE);
                textView2.setVisibility(View.VISIBLE);
//                scrollView.setVisibility(View.INVISIBLE);

                recycleView.setVisibility(View.INVISIBLE);
            }
        });

        cancel_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                diaryTextView.setVisibility(View.INVISIBLE);
                save_Btn.setVisibility(View.INVISIBLE);
                cancel_Btn.setVisibility(View.INVISIBLE);
                contextEditText.setVisibility(View.INVISIBLE);
                textView2.setVisibility(View.INVISIBLE);
//                scrollView.setVisibility(View.INVISIBLE);

                recycleView.setVisibility(View.VISIBLE);

                contextEditText.setText("");
            }
        });

        save_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                str = contextEditText.getText().toString();
                if (str.isEmpty()){
                    Toast.makeText(getActivity(), "습관을 입력해주세요.", Toast.LENGTH_LONG).show();
                    return;
                }

                Random rd = new Random();
                Habit hb = new Habit(str, Color.rgb(rd.nextInt(256),rd.nextInt(256),rd.nextInt(256)));
                try {
                    habitAdded(hb);
                    adapter.changeHabit(map.get(date));
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
//                habits.add(hb);
////                try {
////                    habitAdded();
////                } catch (CloneNotSupportedException e) {
////                    e.printStackTrace();
////                }
//                ArrayList<HabitCheck> hcs = map.get(date);
//                HabitCheck newHC = new HabitCheck(hb);
//                hcs.add(newHC);
//                map.put(date, hcs);


                adapter.notifyItemInserted(adapter.getItemCount());


                save_Btn.setVisibility(View.INVISIBLE);
                cancel_Btn.setVisibility(View.INVISIBLE);
                contextEditText.setVisibility(View.INVISIBLE);
                textView2.setVisibility(View.INVISIBLE);
//                scrollView.setVisibility(View.INVISIBLE);

                recycleView.setVisibility(View.VISIBLE);
                contextEditText.setText("");

            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth)
            {
                recycleView.setVisibility(View.VISIBLE);

                LocalDate tempDate = LocalDate.of(year, month+1, dayOfMonth);

                adapter.changeDate(tempDate);

                if(!map.containsKey(tempDate)){
                    // 관련 날짜 데이터 없으므로 빈 습관 출력
                    ArrayList<HabitCheck> testHabits = new ArrayList<>();
                    for(Habit habit : habits){
                        HabitCheck hc = new HabitCheck(habit);
                        testHabits.add(hc);
                    }
                    adapter.changeHabit((ArrayList<HabitCheck>) testHabits);
                }
                else {
                    ArrayList<HabitCheck> temp = (ArrayList<HabitCheck>) map.get(tempDate);
                    adapter.changeHabit(temp);
                }
            }
        });

        return rootView;
    }
}