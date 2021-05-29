package com.example.exercise;

import android.os.Bundle;
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
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class FragmentData extends Fragment {
    // 변수 선언
    private int mYear = 0, mMonth = 0, mDay = 0;
    EditText edit_time;
    TextView tv_cal, tv_bmi;
    Button btn_save;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_data,container,false);

        tv_cal = v.findViewById(R.id.tv_cal); //칼로리 소모량
        tv_bmi = v.findViewById(R.id.tv_bmi); //체지방량

        edit_time = v.findViewById(R.id.edit_time); //운동시간 입력

        btn_save = v.findViewById(R.id.save); //기록저장 버튼

        //날짜 설정
        Calendar calendar = new GregorianCalendar();
        mYear = calendar.get(Calendar.YEAR); //년도
        mMonth = calendar.get(Calendar.MONTH); //월
        mDay = calendar.get(Calendar.DAY_OF_MONTH); //일

        DatePicker datePicker = v.findViewById(R.id.Date);
        datePicker.init(mYear, mMonth, mDay,mOnDateChangedListener);

        //Firebase를 위한
        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("exercise");

        //기록저장 버튼을 누를 시
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String 변수에 내용을 넣어줌
                String str_yy = String.valueOf(mYear);
                String str_mm = String.valueOf(mMonth+1);
                String str_dd = String.valueOf(mDay);
                String str_time = edit_time.getText().toString();
                String str_date = str_yy + "-" + str_mm + "-" + str_dd;

                //운동시간 입력 란이 공백이 아닐 경우
                if(str_time.length() != 0) {
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    //로그인이 되어 있는 상태일 경우
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
                                    double bmi = kg/((cm/100)*(cm/100)); //체지방량 계산
                                    String str_bmi = String.format("%.2f", bmi); //소수점 2자리까지
                                    int cal = (int) (0.035*kg*time); //칼로리 소모량 계산

                                    //TextView에 넣어줌
                                    tv_bmi.setText(str_bmi);
                                    tv_cal.setText(String.valueOf(cal));

                                    //HistoryData에 넣어주고 Realtime Database에 넣어줌
                                    HistoryData historyData = new HistoryData();
                                    historyData.setDate(str_date);
                                    historyData.setTime(str_time);
                                    historyData.setCal(String.valueOf(cal));
                                    historyData.setBodyfat(str_bmi);
                                    mDatabaseRef.child("UserData").child(firebaseUser.getUid()).child(str_date).setValue(historyData);
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) { }
                        });
                        Toast.makeText(getContext(), "기록하였습니다.", Toast.LENGTH_SHORT).show();
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
