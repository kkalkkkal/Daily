package com.kkalkkalparrot.daily;

/*
* 담당자 : 한범진
* */


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.logging.LoggingMXBean;

public class Habit_tracker extends Fragment {
    public String str = null;
    public CalendarView calendarView;
    public Button cha_Btn, del_Btn, save_Btn, cancel_Btn;
    public TextView textView2, textView3;
    public EditText contextEditText;
    public FloatingActionButton fab;
    public RecyclerView recycleView;

    public LocalDate date = LocalDate.now();
    public int nowPosition;

    public Habit_tracker context;

    public String uid;

    protected final FirebaseFirestore db = FirebaseFirestore.getInstance();
    public DocumentReference memberDoc;

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

            System.out.println(habits.get(0).get_Color());
            System.out.println(habits.get(1).get_Color());
            System.out.println(habits.get(2).get_Color());
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

    public void dbHabitListChange(DocumentReference memberDoc){
        Map<String, Object> tempMap = new HashMap<>();
        for (Habit habit : habits) {
            tempMap.put(habit.get_name(), habit.get_Color());
        }
        memberDoc.update("habitList",tempMap);
    }

    public void dbHabitsChange(DocumentReference memberDoc){
        for(Map.Entry<LocalDate, ArrayList<HabitCheck>> pair : map.entrySet()){
            LocalDate tempdate = pair.getKey();
//                                                    java.sql.Timestamp tempTS = java.sql.Timestamp.valueOf(String.valueOf(tempdate.atStartOfDay()));
            Long TS = tempdate.toEpochDay() * 86400;

            ArrayList<HabitCheck> tempHCL = pair.getValue();

            HashMap<String, Boolean> innerMap = new HashMap<String, Boolean>();
            for (HabitCheck hc : tempHCL){
                innerMap.put(hc.getHabit().get_name(), hc.get_complete());
            }
            memberDoc.collection("Habits").document(String.valueOf(TS)).set(innerMap);
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_habit_tracker, container, false);

        uid = getArguments().getString("uid");
//        uid = "oCJNHZVDVtU4g4NQVnDEsdlCGFQ2";

        context = this;

//....................

//        ...............


        memberDoc = db.collection("Member").document(uid);
        memberDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> temp = document.getData();
                        HashMap<String, Long> ttempMap = (HashMap<String, Long>) temp.get("habitList");
                        if (habits.isEmpty()){
                            for (Map.Entry<String, Long> pairs : ttempMap.entrySet()){
                                habits.add(new Habit(pairs.getKey(), pairs.getValue().intValue()));
                            }
                        }

                        Log.d("맴버 접근", "DocumentSnapshot data: " + habits.get(0).get_name() );

                    } else {
                        Log.d("맴버 접근", "No such document");
                    }

                    memberDoc.collection("Habits")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Log.d("Habits 접근", document.getId() + " => " + document.getData());

                                            int tempTSInt = Integer.parseInt(document.getId());
                                            Date date = new Date(tempTSInt * 1000L);
                                            LocalDate tsLocalDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                                            Map<String, Object> tempInner = document.getData();

                                            ArrayList<HabitCheck> testHabitChecks = new ArrayList<>();

                                            for (Habit habit : habits) {
                                                HabitCheck hc = new HabitCheck(habit);
                                                testHabitChecks.add(hc);
                                                if((boolean) tempInner.get(habit.get_name())){
                                                    hc.set_complete(true);
                                                }
                                            }

                                            map.put(tsLocalDate, testHabitChecks);
                                        }

                                        if(!map.containsKey(date)) {
                                            ArrayList<HabitCheck> todayHabitChecks = new ArrayList<>();
                                            for (Habit habit : habits) {
                                                HabitCheck hc = new HabitCheck(habit);
                                                todayHabitChecks.add(hc);
                                            }
                                            //키가 들어있는지 확인. 있으면 덮어쓰지 않는다.
                                            map.put(date, todayHabitChecks);

                                            dbHabitsChange(memberDoc);
                                        }


                                        calendarView = rootView.findViewById(R.id.calendarView);
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

                                        RecycleAdapter adapter = new RecycleAdapter((ArrayList<HabitCheck>) map.get(date), date, context);
                                        recycleView.setAdapter(adapter);

                                        cha_Btn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                str = contextEditText.getText().toString();
                                                if (str.isEmpty()){
                                                    Toast.makeText(getActivity(), "Please enter your habit.", Toast.LENGTH_LONG).show();
                                                    return;
                                                }

                                                habits.get(nowPosition).set_name(str);

                                                dbHabitListChange(memberDoc);

//                                                Map<String, Object> tempMap = new HashMap<>();
//                                                for (Habit habit : habits) {
//                                                    tempMap.put(habit.get_name(), habit.get_Color());
//                                                }
//                                                memberDoc.update("habitList",tempMap);

                                                try {
                                                    habitChanged();
                                                    adapter.changeHabit(map.get(date));
                                                } catch (CloneNotSupportedException e) {
                                                    e.printStackTrace();
                                                }

                                                dbHabitsChange(memberDoc);

//                                                for(Map.Entry<LocalDate, ArrayList<HabitCheck>> pair : map.entrySet()){
//                                                    LocalDate tempdate = pair.getKey();
////                                                    java.sql.Timestamp tempTS = java.sql.Timestamp.valueOf(String.valueOf(tempdate.atStartOfDay()));
//                                                    Long TS = tempdate.toEpochDay() * 86400;
//
//                                                    ArrayList<HabitCheck> tempHCL = pair.getValue();
//
//                                                    HashMap<String, Boolean> innerMap = new HashMap<String, Boolean>();
//                                                    for (HabitCheck hc : tempHCL){
//                                                        innerMap.put(hc.getHabit().get_name(), hc.get_complete());
//                                                    }
//                                                    memberDoc.collection("Habits").document(String.valueOf(TS)).set(innerMap);
//                                                }



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
                                                dbHabitListChange(memberDoc);


//                                                Map<String, Object> tempMap = new HashMap<>();
//                                                for (Habit habit : habits) {
//                                                    tempMap.put(habit.get_name(), habit.get_Color());
//                                                }
//
//                                                memberDoc.update("habitList",tempMap);

                                                try {
                                                    habitDeleted();
                                                    adapter.changeHabit(map.get(date));
                                                } catch (CloneNotSupportedException e) {
                                                    e.printStackTrace();
                                                }

                                                dbHabitsChange(memberDoc);

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
                                                    Toast.makeText(getActivity(), "Please enter your habit.", Toast.LENGTH_LONG).show();
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
                                                dbHabitListChange(memberDoc);

                                                adapter.notifyItemInserted(adapter.getItemCount());

                                                dbHabitsChange(memberDoc);


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
                                    } else {
                                        Log.d("Habits 접근", "Error getting documents: ", task.getException());
                                    }
                                }
                            });






                } else {
                    Log.d("habits 접근", "get failed with ", task.getException());
                }
            }
        });

//        try {
//            main();
//        } catch (CloneNotSupportedException e) {
//            e.printStackTrace();
//        }




        return rootView;
    }
}