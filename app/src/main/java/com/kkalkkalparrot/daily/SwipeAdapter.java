package com.kkalkkalparrot.daily;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Map;

public class SwipeAdapter extends BaseAdapter {

    private Context context;
    private List<Map<String, Object>> list;

    public SwipeAdapter(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Map<String, Object> getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_koloda, parent, false);
            Map<String,Object> tags = (Map<String,Object>)getItem(i).get("level");
            String tagStr = "관심분야: | ";
            for ( String key : tags.keySet()){
                tagStr+= key + " LV" + tags.get(key)+" | ";
            }
            ((TextView)view.findViewById(R.id.finder_userName)).setText((String)getItem(i).get("nickName"));
            ((TextView)view.findViewById(R.id.finder_userLevel)).setText(tagStr);
            Glide.with(parent).load(getItem(i).get("image")).into((ImageView)view.findViewById(R.id.finder_user_image));
        } else {
            view = convertView;
        }
        return view;
    }

}
