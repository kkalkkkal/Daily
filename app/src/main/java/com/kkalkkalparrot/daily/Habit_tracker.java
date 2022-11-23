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


    public ArrayList<Habit> habits= new ArrayList<>();
    public HashMap<LocalDate, ArrayList<Habit>> map = new HashMap<>();

    public void main() throws CloneNotSupportedException {
        habits.add(new Habit("습관1", Color.RED));
        habits.add(new Habit("습관2", Color.BLUE));
        habits.add(new Habit("습관3", Color.GRAY));
//        habits.add(new Habit("습관4", Color.GREEN));


        ////////////////////////////////////////////
        //이 사이는 서버로 옮겨도 필요한 부분
        //당일에 대한 맵 데이터 생성
        ArrayList<Habit> testHabits1 = new ArrayList<>();
        for(Habit habit : habits){
            Habit h = habit.clone();
            testHabits1.add(h);
        }

        map.put(date, testHabits1);
        ///////////////////////////////////////////

        ArrayList<Habit> testHabits2 = new ArrayList<>();
        for(Habit habit : habits){
            Habit h = habit.clone();
            testHabits2.add(h);
        }

//        testHabits.addAll(habits);

        testHabits2.get(2).set_did(true);
        date = date.withDayOfMonth(11);

        map.put(date, testHabits2);

        date = LocalDate.now();

    }

    public void habitAdded() throws CloneNotSupportedException {
        Set<LocalDate> keyset = map.keySet();
        for(LocalDate key : keyset){
            Habit newHabit = habits.get(habits.size()-1).clone();
//            map.get(key).add(newHabit);
            Objects.requireNonNull(map.get(key)).add(newHabit);
        }
    }

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

        RecycleAdapter adapter = new RecycleAdapter((ArrayList<Habit>) map.get(date), date);
        recycleView.setAdapter(adapter);

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
//                saveDiary(readDay);

                str = contextEditText.getText().toString();
                if (str.isEmpty()){
                    Toast.makeText(getActivity(), "습관을 입력해주세요.", Toast.LENGTH_LONG).show();
                    return;
                }

                Random rd = new Random();
                habits.add(new Habit(str, Color.rgb(rd.nextInt(256),rd.nextInt(256),rd.nextInt(256))));
                try {
                    habitAdded();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
//                map.put(date, habits);
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
                    ArrayList<Habit> testHabits = new ArrayList<>();
                    for(Habit habit : habits){
                        Habit h = new Habit();
                        try {
                            h = habit.clone();
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                        testHabits.add(h);
                    }
                    adapter.changeHabit((ArrayList<Habit>) testHabits);
                }
                else {
                    ArrayList<Habit> temp = (ArrayList<Habit>) map.get(tempDate);
                    adapter.changeHabit(temp);
                }
            }
        });

        return rootView;
    }
}