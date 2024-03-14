package com.nevadiatechnology.nevaquiz.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.nevadiatechnology.nevaquiz.Common.Common;
import com.nevadiatechnology.nevaquiz.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.nevadiatechnology.nevaquiz.Activity.SplashScreenActivity.online_person;

public class ProfileActivity extends AppCompatActivity {

    private CircleImageView user_image;
    private EditText user_name, user_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();

        initView();
    }

    private void initView() {
        user_image = (CircleImageView) findViewById(R.id.user_image);

        user_name = (EditText) findViewById(R.id.user_name);
        user_email = (EditText) findViewById(R.id.user_email);

        Picasso.get().load(Common.currentUser.getImage()).placeholder(R.drawable.boy).into(user_image);

        user_name.setText(Common.currentUser.getName());
        user_email.setText(Common.currentUser.getEmail());
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
