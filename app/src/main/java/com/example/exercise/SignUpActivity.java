package com.example.exercise;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignUpActivity extends AppCompatActivity {
    //변수 선언
    EditText edit_name, edit_pw, edit_email, edit_cm, edit_kg;
    Button btn_signup;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edit_name = findViewById(R.id.edit_name); //이름
        edit_pw = findViewById(R.id.edit_pw); //비밀번호
        edit_email = findViewById(R.id.edit_email); //아이디이자 이메일
        edit_cm = findViewById(R.id.edit_cm); //키
        edit_kg = findViewById(R.id.edit_kg); //몸무게

        btn_signup = findViewById(R.id.btn_signup); //회원가입 버튼

        //파이어베이스를 위한
        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("exercise");

        //회원가입 버튼 누를 시
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String 변수에 내용을 넣어줌
                String strName = edit_name.getText().toString();
                String strPw = edit_pw.getText().toString();
                String strEmail = edit_email.getText().toString();
                String strCm = edit_cm.getText().toString();
                String strKg = edit_kg.getText().toString();
                
                //사용자 데이터베이스에 등록
                mAuth.createUserWithEmailAndPassword(strEmail, strPw).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //성공시
                        if(task.isSuccessful()) {
                            //사용자 정보 저장
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            UserData userData = new UserData();
                            userData.setIdToken(firebaseUser.getUid());
                            userData.setName(strName);
                            userData.setEmail(firebaseUser.getEmail());
                            userData.setCm(strCm);
                            userData.setKg(strKg);

                            mDatabaseRef.child("UserData").child(firebaseUser.getUid()).setValue(userData);
                            Toast.makeText(SignUpActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                            gotoActivity(SignInActivity.class);
                        } 
                        //실패시
                        else {
                            Toast.makeText(SignUpActivity.this, "비밀번호를 6자 이상 입력해 주세요.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    //액티비티 이동 메서드
    public void gotoActivity(Class c) {
        Intent intent = new Intent(getApplicationContext(), c);
        startActivity(intent);
    }
}
