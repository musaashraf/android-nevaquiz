package com.nevadiatechnology.nevaquiz.Fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nevadiatechnology.nevaquiz.Activity.SplashScreenActivity;
import com.nevadiatechnology.nevaquiz.Adpter.SubjectCategoryAdapter;
import com.nevadiatechnology.nevaquiz.Common.Common;
import com.nevadiatechnology.nevaquiz.Model.SubjectCategory;
import com.nevadiatechnology.nevaquiz.R;

import java.util.ArrayList;
import java.util.List;

import static com.nevadiatechnology.nevaquiz.Activity.SplashScreenActivity.online_person;

public class SubjectHomeFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private SubjectCategoryAdapter adapter;
    private DatabaseReference subjectCategory, online_person;
    private List<SubjectCategory> items = new ArrayList<>();

    public SubjectHomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.subject_home_fragment, container, false);

        subjectCategory = FirebaseDatabase.getInstance().getReference("subjectCategory");
        online_person = FirebaseDatabase.getInstance().getReference("Online_Person");

        initView();

        return view;
    }

    private void initView() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        subjectCategory.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                items.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    items.add(snapshot.getValue(SubjectCategory.class));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        adapter = new SubjectCategoryAdapter(getContext(), items);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        if (Common.uid != null) {
            online_person.child(Common.uid).child("active").setValue(true);
        }
        super.onStart();
    }
}
