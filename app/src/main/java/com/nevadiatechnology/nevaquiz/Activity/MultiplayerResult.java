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
import com.nevadiatechnology.nevaquiz.Model.Information;
import com.nevadiatechnology.nevaquiz.Model.User;
import com.nevadiatechnology.nevaquiz.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.nevadiatechnology.nevaquiz.Activity.SplashScreenActivity.online_person;
import static com.nevadiatechnology.nevaquiz.Activity.SplashScreenActivity.user_info;

public class MultiplayerResult extends AppCompatActivity {

    private CircleImageView user_image_one, user_image_two;
    private Button button;
    private TextView yourScore, friendScore, messageTv, user_one_name, user_two_name;
    private String score, key, friendUid, currentDate, frndScore, friendScoretext;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_result);
        getSupportActionBar().hide();

        score = getIntent().getStringExtra("score");
        key = getIntent().getStringExtra("key");
        friendUid = getIntent().getStringExtra("friendUid");
        friendScoretext = getIntent().getStringExtra("friendScore");

        user_one_name = (TextView) findViewById(R.id.user_one_name);
        user_two_name = (TextView) findViewById(R.id.user_two_name);
        user_image_one = (CircleImageView) findViewById(R.id.user_image_one);
        user_image_two = (CircleImageView) findViewById(R.id.user_image_two);
        yourScore = (TextView) findViewById(R.id.user_score_one);
        friendScore = (TextView) findViewById(R.id.user_score_two);
        messageTv = (TextView) findViewById(R.id.messageTv);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteScore();
                startActivity(new Intent(MultiplayerResult.this, HomeActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
        });

        if (score != null) {
            yourScore.setText(score);
        }

        friendScore.setText(friendScoretext);

        Picasso.get().load(Common.currentUser.getImage()).placeholder(R.drawable.boy).into(user_image_one);
        user_one_name.setText(Common.currentUser.getName());

        user_info.child(friendUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    Picasso.get().load(user.getImage()).placeholder(R.drawable.boy).into(user_image_two);
                    user_two_name.setText(user.getName());
                    showScore();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showScore() {
        SplashScreenActivity.scoreDatabase.child(key).child(friendUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    frndScore = dataSnapshot.getValue().toString();
                    //friendScore.setText(frndScore);
                    playerInfoData(frndScore);
                    if (Integer.parseInt(score) > Integer.parseInt(frndScore)) {
                        messageTv.setText("you are win");
                    } else {
                        messageTv.setText("you are los");
                    }
                } else {
                    SplashScreenActivity.scoreDatabase.child(key).child(friendUid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                frndScore = dataSnapshot.getValue().toString();
                                //friendScore.setText(frndScore);
                                playerInfoData(frndScore);
                                if (Integer.parseInt(score) > Integer.parseInt(frndScore)) {
                                    messageTv.setText("you are win");
                                } else {
                                    messageTv.setText("you are los");
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void playerInfoData(final String frndScore) {
        SplashScreenActivity.user_info.child(friendUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        if (frndScore != null) {
                            Calendar calendarToDate = Calendar.getInstance();
                            SimpleDateFormat currentDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            currentDate = currentDateFormat.format(calendarToDate.getTime());

                            Information information = new Information(Common.uid, score, Common.currentUser.getName(),
                                    Common.currentUser.getImage(), friendUid, frndScore, user.getName(), user.getImage(), currentDate);

                            SplashScreenActivity.playingInformation.child(String.valueOf(System.currentTimeMillis())).setValue(information).isSuccessful();

                            Picasso.get().load(Common.currentUser.getImage()).placeholder(R.drawable.boy).into(user_image_one);
                            Picasso.get().load(user.getImage()).placeholder(R.drawable.boy).into(user_image_two);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (friendUid != null && key != null) {
            showScore();
        }
        if (Common.uid != null) {
            online_person.child(Common.uid).child("active").setValue(true);
        }
    }

    @Override
    public void onBackPressed() {
        deleteScore();
        startActivity(new Intent(MultiplayerResult.this, HomeActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }

    private void deleteScore() {
        if (key != null) {
            SplashScreenActivity.quiteData.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        SplashScreenActivity.quiteData.child(key).removeValue();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            SplashScreenActivity.currentAnsData.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        SplashScreenActivity.currentAnsData.child(key).removeValue();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            SplashScreenActivity.scoreDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(key).exists()) {
                        SplashScreenActivity.scoreDatabase.child(key).removeValue();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        deleteScore();
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
