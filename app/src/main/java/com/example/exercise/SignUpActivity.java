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

import java.sql.DatabaseMetaData;

public class SignUpActivity extends AppCompatActivity {
    EditText edit_name, edit_pw, edit_email;
    Button btn_signup;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("회원가입");
        actionBar.setDisplayHomeAsUpEnabled(true);

        edit_name = findViewById(R.id.edit_name);
        edit_pw = findViewById(R.id.edit_pw);
        edit_email = findViewById(R.id.edit_email);

        btn_signup = findViewById(R.id.btn_signup);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("exercise");

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strName = edit_name.getText().toString();
                String strPw = edit_pw.getText().toString();
                String strEmail = edit_email.getText().toString();

                mAuth.createUserWithEmailAndPassword(strEmail, strPw).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            UserData userData = new UserData();
                            userData.setIdToken(firebaseUser.getUid());
                            userData.setName(strName);
                            userData.setEmail(firebaseUser.getEmail());
                            userData.setPw(strPw);

                            mDatabaseRef.child("UserData").child(firebaseUser.getUid()).setValue(userData);
                            Toast.makeText(SignUpActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                            gotoActivity(SignInActivity.class);
                        } else {
                            Toast.makeText(SignUpActivity.this, "비밀번호를 6자 이상 입력해 주세요.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    public void gotoActivity(Class c) {
        Intent intent = new Intent(getApplicationContext(), c);
        startActivity(intent);
    }
}
