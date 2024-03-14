package com.nevadiatechnology.nevaquiz.Activity;

import android.graphics.drawable.ColorDrawable;

import com.google.android.material.tabs.TabLayout;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.nevadiatechnology.nevaquiz.Adpter.LeaderBroadViewPageAdapter;
import com.nevadiatechnology.nevaquiz.Common.Common;
import com.nevadiatechnology.nevaquiz.Fragment.MultiLeaderBroadFragment;
import com.nevadiatechnology.nevaquiz.Fragment.SingleLeaderBroadFragment;
import com.nevadiatechnology.nevaquiz.R;

import static com.nevadiatechnology.nevaquiz.Activity.SplashScreenActivity.online_person;

public class LeaderBroadActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private LeaderBroadViewPageAdapter adapter;

    private String category, categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_broad);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.bg_color)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        this.setTitle("Ranking Broad");

        if (getIntent() != null) {
            category = getIntent().getStringExtra("category");
            categoryName = getIntent().getStringExtra("categoryName");
        }

        initView();
    }

    private void initView() {
        tabLayout = (TabLayout) findViewById(R.id.tab_layout_id);
        viewPager = (ViewPager) findViewById(R.id.viewpager_id);

        adapter = new LeaderBroadViewPageAdapter(getSupportFragmentManager());

        if (category != null) {
            adapter.addFragment(new SingleLeaderBroadFragment(category, categoryName), "Single");
            adapter.addFragment(new MultiLeaderBroadFragment(category, categoryName), "Multiplayer");
        }

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
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
