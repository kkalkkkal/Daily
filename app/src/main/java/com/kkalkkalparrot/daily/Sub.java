package com.kkalkkalparrot.daily;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
//import android.widget.Switch;


public class Sub extends LinearLayout{

//    public Switch switchView = findViewById(R.id.habitSW);

    public Sub(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public Sub(Context context) {
        super(context);
        init(context);
    }
    private void init(Context context){
        LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.sub,this,true);
    }
}