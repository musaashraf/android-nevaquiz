package com.nevadiatechnology.nevaquiz.Activity;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nevadiatechnology.nevaquiz.Common.Common;
import com.nevadiatechnology.nevaquiz.Model.SingleScore;
import com.nevadiatechnology.nevaquiz.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.nevadiatechnology.nevaquiz.Activity.SplashScreenActivity.online_person;
import static com.nevadiatechnology.nevaquiz.Activity.SplashScreenActivity.singleScore;

public class ScoreActivity extends AppCompatActivity {

    private double perc;
    private int score = 0, id;
    private String re;
    private ArrayList<String> wrongQuests = new ArrayList<String>();
    private ArrayList<String> selectedAnswers = new ArrayList<String>();
    private ArrayList<String> actualAnswers = new ArrayList<String>();

    private TextView scoreTv;
    private Button getMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        getSupportActionBar().hide();

        re = getIntent().getStringExtra("score");
        id = Integer.parseInt(getIntent().getStringExtra("category"));
        score = Integer.parseInt(re);
        wrongQuests = getIntent().getStringArrayListExtra("wrongQuestions");
        selectedAnswers = getIntent().getStringArrayListExtra("selectedAnswer");
        actualAnswers = getIntent().getStringArrayListExtra("actualAnswer");

        scoreTv = (TextView) findViewById(R.id.scoreTv);
        getMore = (Button) findViewById(R.id.getMore);

        if (score != 0 || wrongQuests != null || selectedAnswers != null || actualAnswers != null) {
            perc = score * 3.33;
            scoreTv.setText(re + "\nPoint");
        }

        getMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScoreActivity.this, ShowAnsActivity.class);
                intent.putStringArrayListExtra("wrongQuestions", wrongQuests);
                intent.putStringArrayListExtra("selectedAnswer", selectedAnswers);
                intent.putStringArrayListExtra("actualAnswer", actualAnswers);
                startActivity(intent);
                finish();
            }
        });

        if (score != 0 && id != 0) {
            singleScore.child(Common.uid).child(String.valueOf(id)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        SingleScore ss = dataSnapshot.getValue(SingleScore.class);
                        if (ss.getId() == String.valueOf(id)) {
                            if (ss.getScore() < score) {
                                final Map<String, Object> map = new HashMap<>();
                                map.put("score", score);
                                singleScore.child(Common.uid).child(String.valueOf(id)).updateChildren(map).isSuccessful();
                                Common.categoryName = "";
                            }
                        }
                    } else {
                        SingleScore singleScore1 = new SingleScore(score, String.valueOf(id), Common.categoryName, Common.uid);
                        singleScore.child(Common.uid).child(String.valueOf(id)).setValue(singleScore1).isSuccessful();
                        Common.categoryName = "";
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Common.uid != null) {
            online_person.child(Common.uid).child("active").setValue(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Common.uid != null) {
            online_person.child(Common.uid).child("active").setValue(false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Common.uid != null) {
            online_person.child(Common.uid).child("active").setValue(false);
        }
    }
}
