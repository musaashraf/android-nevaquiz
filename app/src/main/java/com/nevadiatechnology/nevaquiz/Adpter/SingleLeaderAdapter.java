package com.nevadiatechnology.nevaquiz.Adpter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nevadiatechnology.nevaquiz.Common.Common;
import com.nevadiatechnology.nevaquiz.Model.SingleScore;
import com.nevadiatechnology.nevaquiz.Model.User;
import com.nevadiatechnology.nevaquiz.R;
import com.nevadiatechnology.nevaquiz.ViewHolder.SingleLeaderViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SingleLeaderAdapter extends RecyclerView.Adapter<SingleLeaderViewHolder> {

    private Context context;
    private List<SingleScore> singleScoreList;
    private List<User> userList;

    public SingleLeaderAdapter(Context context, List<SingleScore> singleScoreList, List<User> userList) {
        this.context = context;
        this.singleScoreList = singleScoreList;
        this.userList = userList;
    }

    @NonNull
    @Override
    public SingleLeaderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.single_leader_item, viewGroup, false);
        return new SingleLeaderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleLeaderViewHolder holder, int i) {
        if (userList.get(i).getImage() != null) {
            Picasso.get().load(userList.get(i).getImage()).placeholder(R.drawable.boy).into(holder.profileImage);
        }

        if (userList.get(i).getName() != null) {
            holder.userName.setText(userList.get(i).getName());
        }

        if (String.valueOf(singleScoreList.get(i).getScore()) != null) {
            holder.scorePoint.setText(singleScoreList.get(i).getScore() + "\nPoint");
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
