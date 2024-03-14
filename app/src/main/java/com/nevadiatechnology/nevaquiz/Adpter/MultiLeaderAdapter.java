package com.nevadiatechnology.nevaquiz.Adpter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nevadiatechnology.nevaquiz.Model.LeaderScore;
import com.nevadiatechnology.nevaquiz.R;
import com.nevadiatechnology.nevaquiz.ViewHolder.SingleLeaderViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MultiLeaderAdapter extends RecyclerView.Adapter<SingleLeaderViewHolder> {

    private Context context;
    private List<LeaderScore> singleScoreList;

    public MultiLeaderAdapter(Context context, List<LeaderScore> singleScoreList) {
        this.context = context;
        this.singleScoreList = singleScoreList;
    }

    @NonNull
    @Override
    public SingleLeaderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.single_leader_item, viewGroup, false);
        return new SingleLeaderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleLeaderViewHolder holder, int p) {
        holder.scorePoint.setText(singleScoreList.get(p).getScore()+"\nPoint");
        Picasso.get().load(singleScoreList.get(p).getImage()).placeholder(R.drawable.boy).into(holder.profileImage);
        holder.userName.setText(singleScoreList.get(p).getName());
    }

    @Override
    public int getItemCount() {
        return singleScoreList.size();
    }
}
