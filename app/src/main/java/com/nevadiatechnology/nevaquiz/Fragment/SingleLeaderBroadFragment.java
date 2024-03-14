package com.nevadiatechnology.nevaquiz.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nevadiatechnology.nevaquiz.Adpter.SingleLeaderAdapter;
import com.nevadiatechnology.nevaquiz.Common.Common;
import com.nevadiatechnology.nevaquiz.Model.SingleScore;
import com.nevadiatechnology.nevaquiz.Model.User;
import com.nevadiatechnology.nevaquiz.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.nevadiatechnology.nevaquiz.Activity.SplashScreenActivity.singleScore;
import static com.nevadiatechnology.nevaquiz.Activity.SplashScreenActivity.user_info;

public class SingleLeaderBroadFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private SingleLeaderAdapter adapter;
    private List<SingleScore> singleScoreList = new ArrayList<>();
    private List<User> userList = new ArrayList<>();

    private String categoryName, category;

    private DatabaseReference single_Score;

    public SingleLeaderBroadFragment(String category, String categoryName) {
        this.category = category;
        this.categoryName = categoryName;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        single_Score = FirebaseDatabase.getInstance().getReference("Single_Score");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.single_leader_fragment, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        scoreRetreve();

        return view;
    }

    private void scoreRetreve() {
        if (category != null) {
            single_Score.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    singleScoreList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        for (DataSnapshot data : snapshot.getChildren()) {
                            if (data.getValue(SingleScore.class).getId().equals(category)) {
                                singleScoreList.add(data.getValue(SingleScore.class));
                            }
                        }
                    }

                    Collections.sort(singleScoreList, new Comparator<SingleScore>() {
                        @Override
                        public int compare(SingleScore o1, SingleScore o2) {
                            if (o1.getScore() > o2.getScore()) {
                                return -1;
                            } else {
                                return 0;
                            }
                        }
                    });

                    getUserInfo(singleScoreList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void getUserInfo(final List<SingleScore> singleScoreList) {
        for (int i = 0; i < singleScoreList.size(); i++) {
            user_info.child(singleScoreList.get(i).getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                userList.add(dataSnapshot.getValue(User.class));
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

            adapter = new SingleLeaderAdapter(getContext(), singleScoreList, userList);
            recyclerView.setAdapter(adapter);
        }
    }
}
