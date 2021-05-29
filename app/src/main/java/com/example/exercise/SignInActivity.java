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
    EditText edit_id, edit_pw;
    Button btn_signin, btn_signup;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        edit_id = findViewById(R.id.edit_id);
        edit_pw = findViewById(R.id.edit_pw);

        btn_signin = findViewById(R.id.btn_signin);
        btn_signup = findViewById(R.id.btn_signup);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("exercise");

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strPw = edit_pw.getText().toString();
                String strEmail = edit_id.getText().toString();

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
            }
        });
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoActivity(SignUpActivity.class);
            }
        });
    }
    public void gotoActivity(Class c) {
        Intent intent = new Intent(getApplicationContext(), c);
        startActivity(intent);
    }
}
