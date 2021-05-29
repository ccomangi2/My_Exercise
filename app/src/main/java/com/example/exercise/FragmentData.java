package com.example.exercise;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
                String str_mm = String.valueOf(mMonth+1);
                String str_dd = String.valueOf(mDay);
                String str_time = edit_time.getText().toString();
                String str_date = str_yy + "-" + str_mm + "-" + str_dd;

                if(str_time.length() != 0) {
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    if (firebaseUser != null) {
                        mDatabaseRef.child("UserData").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    UserData userData = dataSnapshot.getValue(UserData.class);
                                    int time = Integer.parseInt(str_time);
                                    assert userData != null;
                                    double cm = Double.parseDouble(userData.getCm());
                                    double kg = Double.parseDouble(userData.getKg());
                                    double bmi = kg/((cm/100)*(cm/100));
                                    String str_bmi = String.format("%.2f", bmi);
                                    int cal = (int) (0.035*kg*time);

                                    tv_bmi.setText(str_bmi);
                                    tv_cal.setText(String.valueOf(cal));

                                    HistoryData historyData = new HistoryData();
                                    historyData.setDate(str_date);
                                    historyData.setTime(str_time);
                                    historyData.setCal(String.valueOf(cal));
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
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    } else {
                        // No user is signed in
                    }
                } else {
                    Toast.makeText(getContext(), "운동 시간을 입력해 주세요.", Toast.LENGTH_SHORT).show();
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
