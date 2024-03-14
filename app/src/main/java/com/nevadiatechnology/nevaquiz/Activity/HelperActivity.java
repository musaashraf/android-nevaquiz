package com.nevadiatechnology.nevaquiz.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.nevadiatechnology.nevaquiz.R;

public class HelperActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper);

        textView = (TextView) findViewById(R.id.textView);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int) (width * 16), (int) (height * 12));

        if (getIntent() != null) {
            String name = getIntent().getStringExtra("content");
            textView.setText(name + " not play with you");
        }
    }
}
