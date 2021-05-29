package com.example.exercise;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class FragmentHistory extends Fragment {
    //변수 선언
    private int mYear = 0, mMonth = 0, mDay = 0;
    TextView tv_time, tv_cal, tv_bmi, tv_rank;
    Button search;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_history,container,false);

        tv_time = v.findViewById(R.id.tv_time); //운동시간
        tv_cal = v.findViewById(R.id.tv_cal); //칼로리 소모량
        tv_bmi = v.findViewById(R.id.tv_bmi); //체지방량
        tv_rank = v.findViewById(R.id.tv_rank); //순위

        search = v.findViewById(R.id.search); //기록 검색 버튼

        //날짜 설정을 위한
        Calendar calendar = new GregorianCalendar();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePicker datePicker = v.findViewById(R.id.Date);
        datePicker.init(mYear, mMonth, mDay,mOnDateChangedListener);

        //Firebase를 위한
        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("exercise");

        //검색 버튼을 누를 시
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String 변수에 내용을 넣어줌
                String str_yy = String.valueOf(mYear);
                String str_mm = String.valueOf(mMonth+1);
                String str_dd = String.valueOf(mDay);
                String str_date = str_yy + "-" + str_mm + "-" + str_dd;

                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                //로그인이 되어 있는 상태일 경우
                if (firebaseUser != null) {
                    //데이터 불러오기
                    mDatabaseRef.child("UserData").child(firebaseUser.getUid()).child(str_date).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                HistoryData historyData = dataSnapshot.getValue(HistoryData.class);

                                String str_time = historyData.getTime();
                                String str_cal = historyData.getCal();
                                String str_bmi = historyData.getBodyfat();

                                tv_time.setText(str_time);
                                tv_bmi.setText(str_bmi);
                                tv_cal.setText(str_cal);
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                    //순위 매기기 쿼리
                    Query myMostViewedPostsQuery = mDatabaseRef.child("UserData")
                            .orderByChild(str_date + "/time");
                    myMostViewedPostsQuery.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {}
                        // [END_EXCLUDE]
                    });
                } else {
                    // No user is signed in
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
