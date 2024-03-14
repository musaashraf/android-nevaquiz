package com.nevadiatechnology.nevaquiz.Activity;

import android.graphics.drawable.ColorDrawable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import com.nevadiatechnology.nevaquiz.Adpter.WrongQAdapter;
import com.nevadiatechnology.nevaquiz.Common.Common;
import com.nevadiatechnology.nevaquiz.Model.WrongQ;
import com.nevadiatechnology.nevaquiz.R;

import java.util.ArrayList;

import static com.nevadiatechnology.nevaquiz.Activity.SplashScreenActivity.online_person;

public class ShowAnsActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<String> wrongQuests = new ArrayList<String>();
    private ArrayList<String> selectedAnswers = new ArrayList<String>();
    private ArrayList<String> actualAnswers = new ArrayList<String>();
    private ArrayList<WrongQ> wrongQS = new ArrayList<>();
    private WrongQAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ans);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.bg_color)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        wrongQuests = getIntent().getStringArrayListExtra("wrongQuestions");
        selectedAnswers = getIntent().getStringArrayListExtra("selectedAnswer");
        actualAnswers = getIntent().getStringArrayListExtra("actualAnswer");

        for (int i = 0; i < wrongQuests.size(); i++) {
            wrongQS.add(new WrongQ(wrongQuests.get(i), selectedAnswers.get(i), actualAnswers.get(i)));
        }

        init();

        adapter = new WrongQAdapter(this, wrongQS, R.layout.wrong_ans_item);
        listView.setAdapter(adapter);

    }

    private void init() {
        listView = (ListView) findViewById(R.id.listView);
        this.setTitle("Correct Answer");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
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
