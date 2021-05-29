package com.example.exercise;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class FragmentData extends Fragment {
    private int mYear = 0, mMonth = 0, mDay = 0;
    EditText edit_time;
    TextView tv_cal, tv_bmi;
    Button btn_save;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseData;
    private DatabaseReference mDatabaseRef;
    private ChildEventListener mChildEventListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_data,container,false);

        tv_cal = v.findViewById(R.id.tv_cal);
        tv_bmi = v.findViewById(R.id.tv_bmi);

        edit_time = v.findViewById(R.id.edit_time);

        btn_save = v.findViewById(R.id.save);

        Calendar calendar = new GregorianCalendar();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePicker datePicker = v.findViewById(R.id.Date);
        datePicker.init(mYear, mMonth, mDay,mOnDateChangedListener);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("exercise");
        mFirebaseData = FirebaseDatabase.getInstance();

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_yy = String.valueOf(mYear);
                String str_mm = String.valueOf(mMonth);
                String str_dd = String.valueOf(mDay);
                String str_time = edit_time.getText().toString();
                String str_cal = tv_cal.getText().toString();
                String str_bmi = tv_bmi.getText().toString();
                String str_date = str_yy + "-" + str_mm + "-" + str_dd;
                if(str_time != null) {
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    if (firebaseUser != null) {
                        HistoryData historyData = new HistoryData();
                        historyData.setDate(str_date);
                        historyData.setTime(str_time);
                        historyData.setCal(str_cal);
                        historyData.setBodyfat(str_bmi);
                        mDatabaseRef.child("UserData").child(firebaseUser.getUid()).child(str_date).setValue(historyData);
                        mChildEventListener = new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                            }

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        };
                    } else {
                        // No user is signed in
                    }
                }
            }
        });

        return v;
    }

    DatePicker.OnDateChangedListener mOnDateChangedListener = new DatePicker.OnDateChangedListener(){
        @Override
        public void onDateChanged(DatePicker datePicker, int yy, int mm, int dd) {
            mYear = yy;
            mMonth = mm;
            mDay = dd;
        }
    };
}
