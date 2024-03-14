package com.nevadiatechnology.nevaquiz.Fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nevadiatechnology.nevaquiz.Common.Common;
import com.nevadiatechnology.nevaquiz.Model.SingleScore;
import com.nevadiatechnology.nevaquiz.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.nevadiatechnology.nevaquiz.Activity.SplashScreenActivity.online_person;
import static com.nevadiatechnology.nevaquiz.Activity.SplashScreenActivity.singleScore;

public class ScoreHomeFragment extends Fragment {

    private View view;
    private TextView scorePoint;
    private List<SingleScore> scoreList = new ArrayList<>();

    public ScoreHomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.score_home_fragment, container, false);

        initView();

        return view;
    }

    private void initView() {
        scorePoint =(TextView)view.findViewById(R.id.textView2);

        scoreRetreve();
    }

    private void scoreRetreve() {
        singleScore.child(Common.uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                scoreList.clear();
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        scoreList.add(snapshot.getValue(SingleScore.class));
                    }

                    Collections.sort(scoreList, new Comparator<SingleScore>() {
                        @Override
                        public int compare(SingleScore o1, SingleScore o2) {
                            if(o2.getScore() > o1.getScore()){
                                return 1;
                            }else{
                                return 0;
                            }
                        }
                    });

                    scorePoint.setText(scoreList.get(0).getScore()+"\nPoint");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onStart() {
        if (Common.uid != null) {
            online_person.child(Common.uid).child("active").setValue(true);
        }
        super.onStart();
    }
}
