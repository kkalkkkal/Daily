package com.kkalkkalparrot.daily;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class MakeJournal extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CODE = 101;
    private ImageView imageView;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private Button save_btn;
    private EditText edit_content;
    private EditText edit_title;


    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            // 위치 리스너는 위치정보를 전달할 때 호출되므로 onLocationChanged()메소드 안에 위지청보를 처리를 작업을 구현 해야합니다.
            String provider = location.getProvider();  // 위치정보
            double longitude = location.getLongitude(); // 위도
            double latitude = location.getLatitude(); // 경도
            double altitude = location.getAltitude(); // 고도
            //txtResult.setText("위치정보 : " + provider + "\n" + "위도 : " + longitude + "\n" + "경도 : " + latitude + "\n" + "고도 : " + altitude);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        public void onProviderEnabled(String provider) {

        }

        public void onProviderDisabled(String provider) {

        }
    };

    //사진찍기
    public void takePicture(){

        Intent imageTakeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(imageTakeIntent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(imageTakeIntent, REQUEST_IMAGE_CODE);
        }
    }

    //결과값 가져오기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CODE && resultCode == RESULT_OK) {

            Bundle extras = data.getExtras();

            Bitmap imageBitmap = (Bitmap) extras.get("data");

            imageView.setImageBitmap(imageBitmap);
            imageView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.journal_make);

        Intent secondIntent = getIntent();
        int camera_state = secondIntent.getIntExtra("camera", 1);
        String uid = secondIntent.getStringExtra("uid");
        int year = secondIntent.getIntExtra("year", 2022);
        int month = secondIntent.getIntExtra("month", 1);
        int dayOfMonth = secondIntent.getIntExtra("dayOfMonth", 1);
        String date = secondIntent.getStringExtra("TimeStamp");

        Log.d("camera_state", "camera_state : " + camera_state);
        imageView = findViewById(R.id.journal_img);

        if (camera_state == 1) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        MY_CAMERA_REQUEST_CODE);
            }
            takePicture();
        }

        save_btn = (Button) findViewById(R.id.journal_save_btn);
        edit_content = (EditText) findViewById(R.id.make_journal_context);
        edit_title = (EditText) findViewById(R.id.make_journal_title);


        final String[] journal_context = new String[1];
        final String[] journal_title = new String[1];

        //   현재 위치 저장하기

        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                journal_context[0] = String.valueOf(edit_content.getText().toString());
                journal_title[0] = String.valueOf(edit_content.getText().toString());

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Map<String, Object> user = new HashMap<>();
                user.put("Title", journal_title[0]);
                user.put("uid", uid);
                user.put("Content", journal_context[0]);

                user.put("Timestamp", date);

                // 위치정보를 원하는 시간, 거리마다 갱신해준다.
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        1000,
                        1,
                        gpsLocationListener);
                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        1000,
                        1,
                        gpsLocationListener);

                user.put("GPS", String.valueOf(location));
                Log.d("GPS", "GPS : " + String.valueOf(location));

                // 이미지가 있으면 storage에 이미지 업로드
                if (imageView.getVisibility() == View.VISIBLE) {
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReference();

                    StorageReference mountainsRef = storageRef.child(year+month+dayOfMonth+".jpg");

                    // Create a reference to 'images/mountains.jpg'
                    StorageReference mountainImagesRef = storageRef.child("imgs/"+uid+"/"+ year+month+dayOfMonth+".jpg");

                    // While the file names are the same, the references point to different files
                    mountainsRef.getName().equals(mountainImagesRef.getName());    // true
                    mountainsRef.getPath().equals(mountainImagesRef.getPath());    // false

                    user.put("image", "imgs/"+uid+"/"+ year+month+dayOfMonth+".jpg");

                }

                final String[] documentid = {""};
                // Add a new document with a generated ID
                db.collection("Member").document(uid).collection("Journal")
                        .add(user)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                documentid[0] = documentReference.getId().toString();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });

                // To do : 생명 주기 종료하고
                Intent intent = new Intent(getApplicationContext(), LookJournal.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("uid", uid);
                intent.putExtra("documentname", documentid[0]);
                startActivity(intent);

            }

        });





    }




}
