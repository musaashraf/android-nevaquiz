package com.nevadiatechnology.nevaquiz.Activity;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nevadiatechnology.nevaquiz.Common.Common;
import com.nevadiatechnology.nevaquiz.R;

import static com.nevadiatechnology.nevaquiz.Activity.SplashScreenActivity.online_person;

public class PlayConditionActivity extends AppCompatActivity implements View.OnClickListener {

    private String categoryId, categoryName;
    private RelativeLayout single_player, multi_player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_condition);
        getSupportActionBar().hide();

        categoryId = getIntent().getStringExtra("category");
        categoryName = getIntent().getStringExtra("categoryName");

        single_player = (RelativeLayout) findViewById(R.id.single_player);
        multi_player = (RelativeLayout) findViewById(R.id.multi_player);

        single_player.setOnClickListener(this);
        multi_player.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.single_player:
                startActivity(new Intent(PlayConditionActivity.this, QuizActivity.class)
                        .putExtra("category", categoryId)
                        .putExtra("categoryName", categoryName));
                break;
            case R.id.multi_player:
                startActivity(new Intent(PlayConditionActivity.this, OnlineUserActivity.class)
                        .putExtra("category", categoryId)
                        .putExtra("categoryName", categoryName));
                break;
            default:
                break;
        }
    }

    @Override
    protected void onStart() {
        if (Common.uid != null) {
            online_person.child(Common.uid).child("active").setValue(true);
        }
        super.onStart();
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
