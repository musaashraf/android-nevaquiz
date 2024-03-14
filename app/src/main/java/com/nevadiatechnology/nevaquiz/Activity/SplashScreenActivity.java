package com.nevadiatechnology.nevaquiz.Activity;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nevadiatechnology.nevaquiz.Common.Common;
import com.nevadiatechnology.nevaquiz.Model.User;
import com.nevadiatechnology.nevaquiz.R;

public class SplashScreenActivity extends AppCompatActivity {

    public static FirebaseUser user;

    public static DatabaseReference user_info, online_person, leaderBroadScore, scoreDatabase, subjectCategory,
            playingInformation, currentAnsData, quiteData, data_question, requests, randomNumberData,
    singleScore, userToken;

    private SharedPreferences sharedPreferences;

    public SplashScreenActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        firebasedataRetrofit();

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        View hide = getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.STATUS_BAR_HIDDEN | View.SYSTEM_UI_FLAG_FULLSCREEN;
        hide.setSystemUiVisibility(option);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                doWork();
                startApp();
            }
        });
        thread.start();

    }

    private void firebasedataRetrofit() {
        user = FirebaseAuth.getInstance().getCurrentUser();

        user_info = FirebaseDatabase.getInstance().getReference("User");
        online_person = FirebaseDatabase.getInstance().getReference("Online_Person");
        subjectCategory = FirebaseDatabase.getInstance().getReference("subjectCategory");
        leaderBroadScore = FirebaseDatabase.getInstance().getReference("Leader_Broad_Score");
        scoreDatabase = FirebaseDatabase.getInstance().getReference("Score_MultiPlayer");
        playingInformation = FirebaseDatabase.getInstance().getReference("Playing_Information");
        currentAnsData = FirebaseDatabase.getInstance().getReference("Current_Ans");
        quiteData = FirebaseDatabase.getInstance().getReference("Quite_Game");
        data_question = FirebaseDatabase.getInstance().getReference("Quiz_Question");
        requests = FirebaseDatabase.getInstance().getReference("Player_Request");
        randomNumberData = FirebaseDatabase.getInstance().getReference("Random_Number");
        singleScore = FirebaseDatabase.getInstance().getReference("Single_Score");
        userToken = FirebaseDatabase.getInstance().getReference("Token");

        user_info.keepSynced(true);
        online_person.keepSynced(true);
        subjectCategory.keepSynced(true);
        leaderBroadScore.keepSynced(true);
        scoreDatabase.keepSynced(true);
        playingInformation.keepSynced(true);
        currentAnsData.keepSynced(true);
        quiteData.keepSynced(true);
        data_question.keepSynced(true);
        requests.keepSynced(true);
        randomNumberData.keepSynced(true);
        singleScore.keepSynced(true);
        userToken.keepSynced(true);
    }

    public void doWork() {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startApp() {
        String uid = sharedPreferences.getString("UID", null);
        Common.uid = uid;
        if (uid != null) {
            user_info.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        User user = dataSnapshot.getValue(User.class);
                        Common.currentUser = user;
                        Common.uid = user.getUid();
                        startActivity(new Intent(SplashScreenActivity.this, HomeActivity.class));
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
            finish();
        }
    }
}
