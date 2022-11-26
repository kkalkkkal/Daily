package com.kkalkkalparrot.daily;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.CarrierConfigManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
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
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.journal_make);

        Intent secondIntent = getIntent();
        int camera_state = secondIntent.getIntExtra("camera", 0);
        String uid = secondIntent.getStringExtra("uid");
        int year = secondIntent.getIntExtra("year",2022);
        int month = secondIntent.getIntExtra("month",1);
        int dayOfMonth = secondIntent.getIntExtra("dayOfMonth", 1);
        String date = secondIntent.getStringExtra("TimeStamp");



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
        editText = (EditText) findViewById(R.id.make_journal_view);


        String journal_context = String.valueOf(editText.getText().toString());
        String journal_image = "";

     //   현재 위치 저장하기
        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        save_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Map<String, Object> user = new HashMap<>();
                user.put("uid",uid);
                user.put("Content",journal_context);
                user.put("GPS", location);
                Log.d("GPS","GPS : " + location);
                user.put("Timestamp",date);

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

                // Add a new document with a generated ID
                db.collection("Journal")
                        .add(user)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });

                // To do : 생명 주기 종료하고

            }

        });





    }


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

}
