package com.nevadiatechnology.nevaquiz.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nevadiatechnology.nevaquiz.Adpter.MultiLeaderAdapter;
import com.nevadiatechnology.nevaquiz.Model.LeaderScore;
import com.nevadiatechnology.nevaquiz.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.nevadiatechnology.nevaquiz.Activity.SplashScreenActivity.leaderBroadScore;

public class MultiLeaderBroadFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private List<LeaderScore> scoreList = new ArrayList<>();
    ;
    private MultiLeaderAdapter adapter;

    private String categoryName, category;

    public MultiLeaderBroadFragment(String category, String categoryName) {
        this.category = category;
        this.categoryName = categoryName;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.multi_player_leader_fragment, container, false);

        initView();

        return view;
    }

    private void initView() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onStart() {
        super.onStart();

        adapter = new MultiLeaderAdapter(getContext(), scoreList);
        recyclerView.setAdapter(adapter);

        if(category !=null){
            leaderBroadScore.orderByChild("id").equalTo(category).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    scoreList.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        scoreList.add(snapshot.getValue(LeaderScore.class));
                    }

                    Collections.sort(scoreList, new Comparator<LeaderScore>() {
                        @Override
                        public int compare(LeaderScore o1, LeaderScore o2) {
                            if (Integer.parseInt(o1.getScore()) > Integer.parseInt(o2.getScore())) {
                                return -1;
                            } else {
                                return 0;
                            }
                        }
                    });

                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
