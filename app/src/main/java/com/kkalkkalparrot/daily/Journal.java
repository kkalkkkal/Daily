package com.kkalkkalparrot.daily;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Journal extends Fragment {

    CalendarView calendarView;
    TextView today;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_journal, container, false);

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