package com.kkalkkalparrot.daily;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Journal extends Fragment {

    CalendarView calendarView;
    private ImageButton camera_btn;
    private ImageButton journal_btn;


    protected String uid;
    // TextView today;

    int select_year = 2022;
    int select_month = 1;
    int select_dayOfMonth = 1;

    Date date;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_journal, container, false);
        setHasOptionsMenu(true);

        uid = getArguments().getString("uid");

        camera_btn = rootView.findViewById(R.id.camera_btn);
        journal_btn = rootView.findViewById(R.id.new_journal_btn);
        calendarView = rootView.findViewById(R.id.calendarView);

        DateFormat formatter = new SimpleDateFormat("yyyy년 MM월 dd일 a hh시 mm분 ss초 zzzZZZZ");

        camera_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MakeJournal.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("camera", 1);
                intent.putExtra("uid", uid);
                intent.putExtra("year", select_year);
                intent.putExtra("month", select_month);
                intent.putExtra("dayOfMonth", select_dayOfMonth);
                intent.putExtra("TimeStamp", formatter.format(date));
                startActivity(intent);

            }
        });

        journal_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MakeJournal.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("camera", 0);
                intent.putExtra("uid", uid);

                intent.putExtra("year", select_year);
                intent.putExtra("month", select_month);
                intent.putExtra("dayOfMonth", select_dayOfMonth);
                startActivity(intent);
            }
        });

        //Todo : 날짜변환 (캘린더 뷰는 클릭 이벤트가 발생하지 않아 팝업 창이 생기지 않는다. -> 별도의 리스트 확인 버튼을 만들고 캘린더는 어디까지나 날짜 세팅용으로만 둔다)


        //today.setText(formatter.format(date));
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Log.i("디버깅", year + "년" + (month + 1) + "월" + dayOfMonth + "일");
                select_year = year;
                select_month = month;
                select_dayOfMonth = dayOfMonth;
                Toast.makeText(getContext(), year + "년 " + (month + 1) + "월 " + dayOfMonth + "일 ", Toast.LENGTH_SHORT).show();
                date = new Date(calendarView.getDate());


            }
        });



        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.calendar_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(getActivity(), "BB:" + item.getTitle(), Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);


    }

}