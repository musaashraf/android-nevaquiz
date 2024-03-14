package com.nevadiatechnology.nevaquiz.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.nevadiatechnology.nevaquiz.Adpter.ViewPageAdapter;
import com.nevadiatechnology.nevaquiz.Common.Common;
import com.nevadiatechnology.nevaquiz.Fragment.ScoreHomeFragment;
import com.nevadiatechnology.nevaquiz.Fragment.SubjectHomeFragment;
import com.nevadiatechnology.nevaquiz.Model.MyToken;
import com.nevadiatechnology.nevaquiz.Model.User;
import com.nevadiatechnology.nevaquiz.R;
import com.squareup.picasso.Picasso;

import me.relex.circleindicator.CircleIndicator;

import static com.nevadiatechnology.nevaquiz.Activity.SplashScreenActivity.online_person;
import static com.nevadiatechnology.nevaquiz.Activity.SplashScreenActivity.userToken;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private View headerView;
    private TextView person_name, person_email;
    private ImageView profile_image;
    private LinearLayout profile_layout;
    private ViewPager viewPager;
    private ViewPageAdapter adapter;

    private FirebaseAuth auth;
    private SharedPreferences sharedPreferences;

    private DatabaseReference user_table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();

        setToken();

    }

    private void setToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        updateToken(task.getResult().getToken());
                        Log.d("TOKEN", task.getResult().getToken());
                    }
                });
    }

    public static void updateToken(final String s) {
        if (s != null) {
            userToken.child(Common.uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {

                    } else {
                        MyToken myToken = new MyToken();
                        myToken.setToken(s);

                        FirebaseDatabase.getInstance().getReference("Token").child(Common.uid).setValue(myToken).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void initView() {
        user_table = FirebaseDatabase.getInstance().getReference("User");

        sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        headerView = navigationView.getHeaderView(0);
        person_name = (TextView) headerView.findViewById(R.id.person_name);
        person_email = (TextView) headerView.findViewById(R.id.person_email);
        profile_image = (ImageView) headerView.findViewById(R.id.imageView);

        profile_layout = (LinearLayout) headerView.findViewById(R.id.profile_layout);
        profile_layout.setOnClickListener(this);

        if (Common.currentUser != null) {
            if (Common.currentUser.getEmail() != null) {
                person_email.setText(Common.currentUser.getEmail());
            }
            if (Common.currentUser.getName() != null) {
                person_name.setText(Common.currentUser.getName());
            }
            if (Common.currentUser.getImage() != null) {
                Picasso.get().load(Common.currentUser.getImage()).placeholder(R.drawable.boy).into(profile_image);
            }
        } else {
            readUserInfo();
        }

        viewPager = (ViewPager) findViewById(R.id.viewpager_id);
        adapter = new ViewPageAdapter(getSupportFragmentManager());

        //adapter.addFragment(new ScoreHomeFragment(), "");
        adapter.addFragment(new SubjectHomeFragment(), "");

        viewPager.setAdapter(adapter);

        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            //HomeActivity.this.startForegroundService(new Intent(HomeActivity.this, RequestService.class));
        } else {
            // startService(new Intent(HomeActivity.this, RequestService.class));
        }

        String uid = sharedPreferences.getString("UID", null);
        Common.uid = uid;
    }

    private void readUserInfo() {
        String uid = sharedPreferences.getString("UID", null);
        Common.uid = uid;
        if (uid != null) {
            user_table.child(uid)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                User user = dataSnapshot.getValue(User.class);
                                Common.currentUser = user;

                                if (Common.currentUser.getEmail() != null) {
                                    person_email.setText(Common.currentUser.getEmail());
                                }
                                if (Common.currentUser.getName() != null) {
                                    person_name.setText(Common.currentUser.getName());
                                }
                                if (Common.currentUser.getImage() != null) {
                                    Picasso.get().load(Common.currentUser.getImage()).placeholder(R.drawable.boy).into(profile_image);
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
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (viewPager.getCurrentItem() == 0) {
                super.onBackPressed();
            } else {
                viewPager.setCurrentItem(viewPager.getCurrentItem() - viewPager.getCurrentItem());
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(HomeActivity.this, HomeActivity.class));
        } else if (id == R.id.nav_request) {
            startActivity(new Intent(HomeActivity.this, RequestActivity.class));
        } else if (id == R.id.nav_leaderbroad) {
            startActivity(new Intent(HomeActivity.this, RankingSubjectActivity.class));
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logout) {
            SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear().apply();
            Common.currentUser = null;
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(HomeActivity.this, LoginActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_layout:
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                break;
            default:
                break;
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
