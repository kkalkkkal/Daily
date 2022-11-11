package com.kkalkkalparrot.daily;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Journal extends Fragment {

    CalendarView calendarView;
    private ImageButton camera_btn;
    private ImageButton journal_btn;


    // TextView today;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_journal, container, false);

        camera_btn = rootView.findViewById(R.id.camera_btn);
        journal_btn = rootView.findViewById(R.id.new_journal_btn);

        camera_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),MakeJournal.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("camera", 1);
                startActivity(intent);

            }
        });

        journal_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),MakeJournal.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("camera", 0);
                startActivity(intent);
            }
        });

        calendarView = rootView.findViewById(R.id.calendarView);        //날짜변환
        DateFormat formatter = new SimpleDateFormat("yyyy년MM월dd일");
        Date date = new Date(calendarView.getDate());
        //today.setText(formatter.format(date));
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String day;
                day = year + "년" + (month+1) + "월" + dayOfMonth + "일";
                //today.setText(day);
                }
            });



        return rootView;
    }


}