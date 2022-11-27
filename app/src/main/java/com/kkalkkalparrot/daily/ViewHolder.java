package com.kkalkkalparrot.daily;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {

    TextView mTitleTv, mDescriptionTv;
    View mView;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        mView = itemView;

        //아이템을 클릭했을때의 동작
        itemView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                mClickListener.onItemClick(v, getAdapterPosition());
            }
        });

        //아이템을 길게 클릭했을 때의 리스너
        itemView.setOnLongClickListener(new View.OnLongClickListener(){

            public boolean onLongClick(View v){
                mClickListener.onItemClick(v, getAdapterPosition());
                return true;
            }
        });

        //뷰를 초기화
        mTitleTv = itemView.findViewById(R.id.rTitleTv);


    }//생성자 끝.

    private ClickListener mClickListener;
    //클릭 리스너의 인터페이스??? 이건 뭔지??
    public interface ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }
    public void setOnclickListener(ClickListener clickListener){
        mClickListener = clickListener;
    }
}
