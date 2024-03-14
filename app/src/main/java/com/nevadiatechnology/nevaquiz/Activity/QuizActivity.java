package com.nevadiatechnology.nevaquiz.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.CountDownTimer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nevadiatechnology.nevaquiz.Common.Common;
import com.nevadiatechnology.nevaquiz.Model.LeaderScore;
import com.nevadiatechnology.nevaquiz.Model.QuestionItems;
import com.nevadiatechnology.nevaquiz.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

import static com.nevadiatechnology.nevaquiz.Activity.SplashScreenActivity.online_person;

public class QuizActivity extends AppCompatActivity {

    private String category, categoryName, key, friendUid, frndCurrentAns, leaderScore, frndScore;
    private TextView question_number_textView, timer_textView, question_textView;
    private RadioGroup radioGroup;
    private RadioButton optionA, optionB, optionC, optionD;
    private Button submit_button;
    private RelativeLayout quiz_layout;

    private List<QuestionItems> questionItemsList = new ArrayList<>();
    private QuestionItems questionItems;
    private Random random = new Random();
    private ArrayList<String> list = new ArrayList<String>();
    private ArrayList<String> randomList = new ArrayList<>();
    private ArrayList<String> wrongQuestListCompFunda = new ArrayList<String>();
    private ArrayList<String> selectedAnsCompFunda = new ArrayList<String>();
    private ArrayList<String> actualAnswerCompFunda = new ArrayList<String>();
    private int wrongNumber = 0, len = 0, currentAns = 1;
    private int score = 0, progress = 0;

    private CounterClass timer;

    private ACProgressFlower dialog;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private boolean secondPerson = false;

    private DatabaseReference Quiz_Question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        getSupportActionBar().hide();

        Quiz_Question = FirebaseDatabase.getInstance().getReference("Quiz_Question");

        category = getIntent().getStringExtra("category");
        categoryName = getIntent().getStringExtra("categoryName");
        key = getIntent().getStringExtra("key");
        friendUid = getIntent().getStringExtra("friendUid");

        init();

        if (key != null) {
            editor.putString("key", key).apply();
            Map<String, Object> map = new HashMap<>();
            map.put("status", 1);
            SplashScreenActivity.requests.child(key).updateChildren(map).isSuccessful();
        }

        if (key != null) {
            SplashScreenActivity.quiteData.child(key).child(Common.uid).setValue(true);
        }

        if (friendUid != null) {
            editor.putString("FriendUID", friendUid).apply();
        }

        if (categoryName != null) {
            retriveData(categoryName);
        }

        timer = new CounterClass(1800000 / 18, 1000);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.optionA || i == R.id.optionB || i == R.id.optionC || i == R.id.optionD) {
                    submit_button.setEnabled(true);
                    if (currentAns >= 11) {
                        checkAns();
                    }
                }
            }
        });

        submit_button.setEnabled(false);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton answer = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                if (questionItems.getAnswer().equals(answer.getText())) {
                    score++;
                } else {
                    wrongQuestListCompFunda.add(wrongNumber, questionItems.getQuestion());
                    selectedAnsCompFunda.add(wrongNumber, answer.getText().toString());
                    actualAnswerCompFunda.add(wrongNumber, questionItems.getAnswer());
                    wrongNumber++;
                }

                radioGroup.clearCheck();

                submit_button.setEnabled(false);
                if (currentAns < 11) {
                    if (currentAns == 10) {
                        submit_button.setText("End Game");
                    }

                    if (key != null && secondPerson) {
                        questionItems = questionItemsList.get(Integer.parseInt(randomList.get(currentAns)));
                    } else {
                        questionItems = questionItemsList.get(Integer.parseInt(list.get(currentAns)));
                    }

                    setQuestionView();
                } else {
                    timer.onFinish();
                    timer.cancel();
                }
                if (key != null) {
                    SplashScreenActivity.currentAnsData.child(key).child(Common.uid).setValue(currentAns).isSuccessful();
                    SplashScreenActivity.scoreDatabase.child(key).child(Common.uid).setValue(score).isSuccessful();
                }
            }
        });

        leaderScoreSync();

        quiteGame();

    }


    private void quiteGame() {
        if (key != null) {
            SplashScreenActivity.quiteData.child(key).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if (dataSnapshot.exists()) {
                        String bol = dataSnapshot.getValue().toString();
                        if (Boolean.parseBoolean(bol)) {

                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(QuizActivity.this);
                            builder.setTitle("Quite Game");
                            builder.setIcon(R.drawable.book);
                            builder.setMessage("Your Friend quite this game, you win the match");
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteData();
                                    finish();
                                    startActivity(new Intent(QuizActivity.this, HomeActivity.class));
                                }
                            });

                            builder.show();
                        }
                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void init() {
        question_number_textView = (TextView) findViewById(R.id.question_number_textView);
        timer_textView = (TextView) findViewById(R.id.timer_textView);
        question_textView = (TextView) findViewById(R.id.question_textView);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        optionA = (RadioButton) findViewById(R.id.optionA);
        optionB = (RadioButton) findViewById(R.id.optionB);
        optionC = (RadioButton) findViewById(R.id.optionC);
        optionD = (RadioButton) findViewById(R.id.optionD);

        submit_button = (Button) findViewById(R.id.submit_button);

        quiz_layout = (RelativeLayout) findViewById(R.id.quiz_layout);

        dialog = new ACProgressFlower.Builder(this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .fadeColor(Color.DKGRAY).build();
        dialog.show();
        quiz_layout.setVisibility(View.GONE);

        sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    private void checkAns() {
        String k = sharedPreferences.getString("key", "");
        String s = sharedPreferences.getString("FriendUID", "");
        SplashScreenActivity.currentAnsData.child(k).child(s).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    frndCurrentAns = dataSnapshot.getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void retriveData(final String category) {
        Quiz_Question.orderByChild("category").equalTo(category).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    questionItemsList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.getValue(QuestionItems.class).isActive()) {
                            questionItemsList.add(snapshot.getValue(QuestionItems.class));
                        }
                    }


                    len = questionItemsList.size();
                    for (int j = 0; j < len; j++) {
                        while (true) {
                            int nextTwo = random.nextInt(len);
                            if (!list.contains(nextTwo)) {
                                list.add(String.valueOf(nextTwo));
                                break;
                            }
                        }
                    }

                    if (key != null) {
                        randomNumberMethods(key, list);
                    }

                    if (key != null && secondPerson) {
                        questionItems = questionItemsList.get(Integer.parseInt(randomList.get(0)));
                    } else {
                        questionItems = questionItemsList.get(Integer.parseInt(list.get(0)));
                    }


                    if (questionItems != null) {
                        dialog.dismiss();
                        quiz_layout.setVisibility(View.VISIBLE);
                        timer.start();
                        setQuestionView();
                    }
                } else {
                    onBackPressed();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(QuizActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void randomNumberMethods(final String key, final ArrayList<String> list) {
        SplashScreenActivity.randomNumberData.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    randomList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        randomList.add(snapshot.getValue().toString());
                    }
                    secondPerson = true;
                } else {
                    SplashScreenActivity.randomNumberData.child(key).setValue(list).isSuccessful();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setQuestionView() {
        question_textView.setText(questionItems.getQuestion());
        optionA.setText(questionItems.getOpta());
        optionB.setText(questionItems.getOptb());
        optionC.setText(questionItems.getOptc());
        optionD.setText(questionItems.getOptd());

        if (currentAns < 10) {
            question_number_textView.setText("" + currentAns + "/" + "10");
        } else {
            question_number_textView.setText("" + currentAns + "/" + "10");
        }
        currentAns++;
    }

    public class CounterClass extends CountDownTimer {
        public CounterClass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long millis = millisUntilFinished;
            String hms = String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
            timer_textView.setText(hms);
        }

        @Override
        public void onFinish() {
            showResult();
        }
    }

    private void showResult() {
        if (key == null) {
            if (currentAns >= 11) {
                Intent intent = new Intent(QuizActivity.this, ScoreActivity.class);
                intent.putExtra("score", String.valueOf(score));
                intent.putExtra("category", category);
                intent.putStringArrayListExtra("wrongQuestions", wrongQuestListCompFunda);
                intent.putStringArrayListExtra("selectedAnswer", selectedAnsCompFunda);
                intent.putStringArrayListExtra("actualAnswer", actualAnswerCompFunda);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                showAlert();
            }
        } else {
            if (currentAns >= 11) {
                SplashScreenActivity.currentAnsData.child(key).child(Common.uid).setValue(currentAns).isSuccessful();
                SplashScreenActivity.scoreDatabase.child(key).child(Common.uid).setValue(score).isSuccessful();
                if (frndCurrentAns != null && Integer.parseInt(frndCurrentAns) == 11) {
                    if (leaderScore != null) {
                        if (score > Integer.parseInt(leaderScore)) {
                            updateLeaderScore();
                        }
                    } else {
                        LeaderScore leaderScore1 = new LeaderScore(String.valueOf(score), Common.currentUser.getName(), Common.currentUser.getImage(), category, categoryName);
                        SplashScreenActivity.leaderBroadScore.child(Common.uid).setValue(leaderScore1).isSuccessful();
                    }
                    deleteData();
                    editor.remove("key").apply();
                    editor.remove("FriendUID").apply();
                    Intent intent = new Intent(QuizActivity.this, MultiplayerResult.class);
                    intent.putExtra("score", String.valueOf(score));
                    intent.putExtra("key", key);
                    intent.putExtra("friendScore", getScore(friendUid));
                    intent.putExtra("friendUid", friendUid);
                    intent.putStringArrayListExtra("wrongQuestions", wrongQuestListCompFunda);
                    intent.putStringArrayListExtra("selectedAnswer", selectedAnsCompFunda);
                    intent.putStringArrayListExtra("actualAnswer", actualAnswerCompFunda);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Please wait your friend not answering all", Toast.LENGTH_SHORT).show();
                }
            } else {
                showAlert();
            }
        }
    }

    private String getScore(String friendUid) {
        SplashScreenActivity.scoreDatabase.child(key).child(friendUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    frndScore = dataSnapshot.getValue().toString();
                } else {
                    frndScore = "0";
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return frndScore;
    }

    private void updateLeaderScore() {
        Map<String, Object> map = new HashMap<>();
        map.put("score", String.valueOf(score));
        SplashScreenActivity.leaderBroadScore.child(Common.uid).updateChildren(map).isSuccessful();
    }

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Warning");
        builder.setIcon(R.drawable.book);
        builder.setMessage("your not answering all question");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (key != null) {
                    deleteData();
                }
                finish();
                startActivity(new Intent(QuizActivity.this, HomeActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });
        builder.show();
    }

    private void leaderScoreSync() {
        SplashScreenActivity.leaderBroadScore.child(Common.uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    leaderScore = dataSnapshot.getValue(LeaderScore.class).getScore();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("If you close all your progress would not be saved... Do you wish to exit ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (key != null) {
                            SplashScreenActivity.quiteData.child(key).child(Common.uid).setValue(false);
                            deleteData();
                        }
                        finish();
                        startActivity(new Intent(QuizActivity.this, HomeActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void deleteData() {
        if (key != null) {
            SplashScreenActivity.requests.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        SplashScreenActivity.requests.child(key).removeValue();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            SplashScreenActivity.randomNumberData.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        SplashScreenActivity.randomNumberData.child(key).removeValue();
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
        if (Common.uid != null) {
            online_person.child(Common.uid).child("active").setValue(false);
        }
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        deleteData();
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
