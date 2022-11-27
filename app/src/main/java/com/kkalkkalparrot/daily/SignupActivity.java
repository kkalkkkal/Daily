package com.kkalkkalparrot.daily;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";//태그 생성함
    private  final int GALLERY_CODE = 10;
    private FirebaseAuth mAuth;//파이어베이스 인증기능
    private FirebaseStorage storage;//파이어베이스 스토리지
    private FirebaseFirestore db = FirebaseFirestore.getInstance();//파이어베이스 DB
    protected Uri userImagefile = null;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();

        //프로필 사진 불러오기 버튼 기능 구현
        Button getImageButton = (Button) findViewById(R.id.getUserImageButton);
        getImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getimage();
            }
        });//버튼을 클릭하면 레지스터 실행된다.

        //등록하기 버튼 기능 구현
        Button registerButton = (Button) findViewById((R.id.registerButton));
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });//버튼을 클릭하면 레지스터 실행된다.


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    protected  void onStop() {
        super.onStop();;
        if(dialog != null){
            dialog.dismiss();
            dialog = null;
        }
    }//다이얼로그 켠상태에서는 끌수 없다.

    private void register() {//회원가입 로직을구현

        final EditText emailText = (EditText) findViewById(R.id.emailText);
        final EditText passwordText = (EditText) findViewById(R.id.passwordText);
        final EditText passwordText2 = (EditText) findViewById(R.id.passwordText2);
        final EditText nicknameText = (EditText) findViewById(R.id.nicknameText);

        //입력값 받아오기

        String userEmail = emailText.getText().toString();
        String userPassword = passwordText.getText().toString();
        String userPassword2 = passwordText2.getText().toString();
        String userNickName = nicknameText.getText().toString();


        if (!userPassword.equals(userPassword2)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
            dialog = builder.setMessage("비밀번호가 일치하지 않습니다.")
                    .setNegativeButton("확인", null)
                    .create();
            dialog.show();
            return;

        }
        if (userEmail.equals("") || userPassword.equals("") || userPassword2.equals("") || userNickName.equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
            dialog = builder.setMessage("빈 칸 없이 입력해주세요.")
                    .setNegativeButton("확인", null)
                    .create();
            dialog.show();
            return;
        }

        if (userPassword.length() < 6) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
            dialog = builder.setMessage("비밀번호는 6자 이상 입력.")
                    .setNegativeButton("확인", null)
                    .create();
            dialog.show();
            return;
        }
        if (userImagefile == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
            dialog = builder.setMessage("프로필사진을 선택해주세요")
                    .setNegativeButton("확인", null)
                    .create();
            dialog.show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(userEmail, userPassword)//여기다가 이메일이랑 패스워드를 넘겨주면 간단하게 될거다.
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            // FirebaseUser Authentication 등록
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String uid = user.getUid();

                            // FirebaseUser Storage 유저이미지 저장
                            StorageReference storageRef = storage.getReference();
                            StorageReference userImgRef = storageRef.child("imgs/usr_"+uid+".png");
                            UploadTask uploadTask = userImgRef.putFile(userImagefile);
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
//                                    Toast.makeText(getApplicationContext(), "사진이 정상적으로 업로드 되지 않았습니다." ,Toast.LENGTH_SHORT).show();
                                    Log.w(TAG, "uploadUserIamge:failure", task.getException());
                                    AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                                    dialog = builder.setMessage("회원 등록에 실패했습니다.")
                                            .setNegativeButton("확인", null)
                                            .create();
                                    dialog.show();
                                    user.delete();
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                    Toast.makeText(getApplicationContext(), "사진이 정상적으로 업로드 되었습니다." ,Toast.LENGTH_SHORT).show();
                                    // 유저이미지 링크 받아오기
                                    userImgRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful()) {
                                                Uri downloadUri = task.getResult();
                                                String userImgUrl = downloadUri.toString();

                                                // FirebaseUser Cloud Firestore >> Member에 유저 추가
                                                Map<String, Object> userData = new HashMap<>();
                                                userData.put("id",userEmail);
                                                userData.put("nickName",userNickName);
                                                userData.put("image",userImgUrl);
                                                userData.put("groups",new ArrayList<String>());
                                                userData.put("level", new HashMap<>());
                                                userData.put("friends", new ArrayList<>());
                                                userData.put("habitList", new HashMap<>());
                                                db.collection("Member").document(uid).set(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                                                            dialog = builder.setMessage("회원 등록에 성공했습니다.")
                                                                    .setPositiveButton("확인", null)
                                                                    .create();
                                                            dialog.show();
                                                            finish();
                                                        } else {
//                                                            Log.d("Feed DB", "get failed with ", task.getException());
                                                            Log.w(TAG, "createUserDB:failure", task.getException());
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                                                            dialog = builder.setMessage("회원 등록에 실패했습니다.")
                                                                    .setNegativeButton("확인", null)
                                                                    .create();
                                                            dialog.show();
                                                            user.delete();
                                                        }
                                                    }
                                                });
                                            } else {

                                                Log.w(TAG, "downloadUserIamge:failure", task.getException());
                                                AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                                                dialog = builder.setMessage("회원 등록에 실패했습니다.")
                                                        .setNegativeButton("확인", null)
                                                        .create();
                                                dialog.show();
                                                user.delete();
                                            }
                                        }
                                    });

                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                            dialog = builder.setMessage("회원 등록에 실패했습니다.")
                                    .setNegativeButton("확인", null)
                                    .create();
                            dialog.show();
                        }

                        // ...
                    }
                });
    }

    private void getimage(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, GALLERY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, final Intent data){
//        Glide.with(this).load(feedUserImg).circleCrop().into(((ImageView)findViewById(R.id.userImage)));
        ImageView photo = findViewById(R.id.userImage);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE) {
            userImagefile = data.getData();
            StorageReference storageRef = storage.getReference();
            StorageReference userImgRef = storageRef.child("test/1.png");
            UploadTask uploadTask = userImgRef.putFile(userImagefile);
            try {
                InputStream in = getContentResolver().openInputStream(data.getData());
                Bitmap img = BitmapFactory.decodeStream(in);
                in.close();
                photo.setImageBitmap(img);
            } catch (Exception e) {
                e.printStackTrace();
            }

//            uploadTask.addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception exception) {
////                    Toast.makeText(ImgActivity.this, "사진이 정상적으로 업로드 되지 않았습니다." ,Toast.LENGTH_SHORT).show();
//                }
//            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
////                    Toast.makeText(ImgActivity.this, "사진이 정상적으로 업로드 되었습니다." ,Toast.LENGTH_SHORT).show();
//                    userImgRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Uri> task) {
//                            if (task.isSuccessful()) {
//                                Uri downloadUri = task.getResult();
//                                Log.d("사진 다운로드 결과", downloadUri.toString());
//                            } else {
//                                // Handle failures
//                                // ...
//                            }
//                        }
//                    });
//                }
//            });
        }
    }

}
