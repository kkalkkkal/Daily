package com.kkalkkalparrot.daily;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

// 선택한 날짜의 저널 리스트 보기
public class journalList extends Fragment {

    private ArrayList<JournalInfo> mArrayList;
    private JournalAdapter mAdapter;
    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.journal_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.JournalRecyclerView);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mArrayList = new ArrayList<>();

        //여기에 mArrayList에 데이터 넣는거 필요함
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Journal").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    int count = 0;
                    for (DocumentSnapshot document : task.getResult()){
                        String JournalName = (String)document.get("TITLE");
                        JournalInfo inputJournal = new JournalInfo(JournalName, document.getId());
                        mArrayList.add(inputJournal);
                        Log.e("JournalFragment", JournalName + " input!");
                    }
                    mAdapter = new JournalAdapter(mArrayList);
                    setmAdapter();
                }
            }
        });
    }

    void setmAdapter(){
        mRecyclerView.setAdapter(mAdapter);
    }


}
