package com.kkalkkalparrot.daily;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GroupFeedsActivity extends AppCompatActivity {
    private boolean setting = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_feeds);
        Intent intent = getIntent();
        String gid = intent.getStringExtra("gid");

        Log.d("GroupFeedsActivity",gid);

        View.OnClickListener listener = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showDialog(v.getTag().toString());
//                Intent intent = new Intent(getApplicationContext(), GroupFeedsActivity.class);
//                intent.putExtra("id", v.getTag().toString());
//                startActivity(intent);
            }

        };

        //그룹 추가버튼 리스너
        ((ImageButton)findViewById(R.id.feedsList_bt1)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                showDialog("그룹 조작 버튼 누름");
                if (setting == false) {
                    v.setRotation(45);
                    ((View) findViewById(R.id.feedsListBackground)).setVisibility(View.VISIBLE);
                    ((View) findViewById(R.id.feedsList_opt1_t)).setVisibility(View.VISIBLE);
                    ((View) findViewById(R.id.feedsList_opt1_b)).setVisibility(View.VISIBLE);
                    ((View) findViewById(R.id.feedsList_opt2_t)).setVisibility(View.VISIBLE);
                    ((View) findViewById(R.id.feedsList_opt2_b)).setVisibility(View.VISIBLE);
                }else{
                    v.setRotation(0);
                    ((View) findViewById(R.id.feedsListBackground)).setVisibility(View.INVISIBLE);
                    ((View) findViewById(R.id.feedsList_opt1_t)).setVisibility(View.INVISIBLE);
                    ((View) findViewById(R.id.feedsList_opt1_b)).setVisibility(View.INVISIBLE);
                    ((View) findViewById(R.id.feedsList_opt2_t)).setVisibility(View.INVISIBLE);
                    ((View) findViewById(R.id.feedsList_opt2_b)).setVisibility(View.INVISIBLE);
                }
                setting = !setting;
            }
        });

        myRecyclerViewAdapter adapter;
        RecyclerView recyclerView;

        String tempImg = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAOEAAADhCAMAAAAJbSJIAAAATlBMVEWRkZH///+Ojo6Li4uIiIj4+PjIyMiVlZXx8fHp6enc3NzX19ednZ20tLSwsLCSkpKoqKjAwMDj4+PKysqjo6O7u7vR0dHz8/Pg4OCCgoIFKEJWAAAEzUlEQVR4nO3a65KjKhQFYAKIIqigeOn3f9GzAU1MYk91TdWZ1tT6/kTRWK6gXDSMAQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA/GM8++3T+AEuD/GjDfc8XIjQNiR0UqRS8c1Rfh8Pt+JIEOptw21cz1iEqbhlhVZWMFbqo4Pc/Akicnc75Cjhm+2Eh2JfWhjLy/nwKEr8brroLxKK/r28rD4oIZ9ei7VgJ65DZodMpVrQal21OWHV7IyBpRs3xxpiyaTjr8FZ59ej1HFbsR5kaE9wH9JFl8l0bkauqywn1JLvpN1zFQ6diCWCuX6KFcXXo7Tpd9kOeoqAG5ET3i+rLeHrfp3Ov8S2TvW328qblLD8n0/2r/w0oU133HeXHxL+ph8nTC3S8FaefUJCnu7DW8MOm5ErJnxpStnWT2ofqCl9TXm9hIXeUSmPvQ/Zin5w4rlPv17CJ6njY7zdlxWD3dfjRySkiE9DtGI/gbh4wm0jt8NTxukR8XoJZ68eHp2gEM4vjynUY4B9vYTv49IVtaOyG5c14j3RFRP+6TuchzxXvN+Kn5aQEnVpiFN/bsJ1MtV/WML9/SiGz0vIrdq1Oflry2cl7G99u467uciPeYbtaxdMyMs9tmWgcXdctyp3iuG6dfj8VLgW2ww4bam2Pv9R0xdM+GQRL+PubH4E+oCEjIflpVSH13HpfImE/JuENJBxZjfwnlV5kbkFDy3ZVUdaf+byVi46p8xSVXXtqVV9OoxNO3bsjA4G129224SQUoj3F4hXeasIF5KuqZKFvJyK1hcXceJgbXe/4ujesrlRKWmB0VCAVkLci60LabewHtPaf57lWDP6xupy7uLzl9SSlMNETFyvtNbpvS8fR153sm8tBRA6LK2oaZOwVScHrR0tMJv2TAtEmukMb9ZIpcaYsGJxmhAfF/JQNfEU48lyKdks1k1V2WpHn1zqUDs1SVFZq7mvhS1Kq5nTKaHmg6ZfSJrhJAm1FWJNKE0tU8LRE6rM3jvndTxRuUzsRstOeeHaKiYcJNVlzONpwcWE/ZaQSrg4T0KtfT9TQt6ppVfxwcQc321OnImymKbZxXFMWy/L0PM6JlSmoIRWT/SDpKrzSovnhDzOjGf929ky3QYW65C32nSd0Y7qMPbZTWm9n8exGhTderW1/suHlFB89aFueelHa4OnOh9oVGMNd318Z5oS0vBVmMmeo1vUgYUQE45Oxr+ReDrZOrK2aUZjfDNSoyi4V4NSkx0m17QpoV3ig/45zhdLKcqeO+2cGwMlVCw4d5qr1PR938T7kLOOxB5jHb5wPvTT1Of3aF01Nk2jXWuMyQl5S82Rr6gNrmpjampC6UhmooQNLdSKEvJTZIxhSkro7q9f1s+qLSuxtaWs06WUX3Wb3tXrmJBaYWqPqIfoqJ2xTnfptT7dkGmBU28hR3OO65QazbqsSrt6LDBjRm/qXBE0yq5NlTpMUduUcIjPwelmW+swo4TpM7alofmdPAcEc4fFjVLNWg0uzRnyrEjwQPdm+l9bY9dN2wG6bYF2OdUI/PhcdtOE938i3kueN50pFQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAXNh/mDxB0bZfCNkAAAAASUVORK5CYII=";
        //데이터 모델리스트
        ArrayList<DataModel> dataModels = new ArrayList();

        dataModels.add(new DataModel("015B ,오왠 : 세월의 흔적 다 버리고",tempImg));
        dataModels.add(new DataModel("새벽공방 : 다섯밤",tempImg));
        dataModels.add(new DataModel("폴킴 : 우리 만남이",tempImg));
        dataModels.add(new DataModel("안녕하신가영 : 니가 좋아",tempImg));
        dataModels.add(new DataModel("넬 : 멀어지다",tempImg));
        dataModels.add(new DataModel("꽃잠프로젝트 : 그대는 어디 있나요",tempImg));
        dataModels.add(new DataModel("검정치마 : 기다린 만큼 ,더",tempImg));
        dataModels.add(new DataModel("어쿠루브 : 그게 뭐라고",tempImg));
        dataModels.add(new DataModel("우효 : Grace",tempImg));


        recyclerView = findViewById(R.id.recyclerview);
        adapter = new myRecyclerViewAdapter(this,dataModels);
        recyclerView.setAdapter(adapter);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (!recyclerView.canScrollVertically(1)) {
                    // 스크롤 마지막일때 새로 데이터추가 새로 로딩
                    dataModels.add(new DataModel("015B ,오왠 : 세월의 흔적 다 버리고",tempImg));
                    dataModels.add(new DataModel("새벽공방 : 다섯밤",tempImg));
                    dataModels.add(new DataModel("폴킴 : 우리 만남이",tempImg));
                    dataModels.add(new DataModel("안녕하신가영 : 니가 좋아",tempImg));
                    dataModels.add(new DataModel("넬 : 멀어지다",tempImg));
                    dataModels.add(new DataModel("꽃잠프로젝트 : 그대는 어디 있나요",tempImg));
                    dataModels.add(new DataModel("검정치마 : 기다린 만큼 ,더",tempImg));
                    dataModels.add(new DataModel("어쿠루브 : 그게 뭐라고",tempImg));
                    dataModels.add(new DataModel("우효 : Grace",tempImg));
                    adapter.notifyItemInserted(0);
                    showDialog("추가로딩");
                }
//                if (!recyclerView.canScrollVertically(-1)) {
//                    Log.i(TAG, "Top of list");
//                } else if (!recyclerView.canScrollVertically(1)) {
//                    Log.i(TAG, "End of list");
//                } else {
//                    Log.i(TAG, "idle");
//                }
            }
        });
        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog("test");
            }
        });
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));

        
    }

    private void showDialog(String msg) {
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(GroupFeedsActivity.this)
                .setTitle("alert")
                .setMessage(msg);
        AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();
    }


}
