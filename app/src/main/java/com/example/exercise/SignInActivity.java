package com.example.exercise;

import android.content.Intent;
import android.content.pm.SigningInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {
    //변수 선언
    EditText edit_id, edit_pw;
    Button btn_signin, btn_signup;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        edit_id = findViewById(R.id.edit_id); //이메일 아이디 입략란
        edit_pw = findViewById(R.id.edit_pw); //비밀번호 입력란

        btn_signin = findViewById(R.id.btn_signin); //로그인 버튼
        btn_signup = findViewById(R.id.btn_signup); //회원가입 버튼

        //파이어베이스를 위한
        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("exercise");

        //로그인 버튼 누를 시
        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strPw = edit_pw.getText().toString();
                String strEmail = edit_id.getText().toString();
                //아이디와 비밀번호 둘 다 공백이 아닐 경우
                if (strPw.length() != 0 && strEmail.length() !=0) {
                    mAuth.signInWithEmailAndPassword(strEmail, strPw).addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                gotoActivity(MainActivity.class);
                                finish();
                            } else {
                                Toast.makeText(SignInActivity.this, "아이디와 비밀번호가 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(SignInActivity.this, "아이디와 비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //회원가입 버튼 누를시
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoActivity(SignUpActivity.class);
            }
        });
    }
    //액티비티 이동 메서드
    public void gotoActivity(Class c) {
        Intent intent = new Intent(getApplicationContext(), c);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
