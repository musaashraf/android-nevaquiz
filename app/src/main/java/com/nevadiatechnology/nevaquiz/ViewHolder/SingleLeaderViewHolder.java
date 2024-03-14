package com.nevadiatechnology.nevaquiz.ViewHolder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.nevadiatechnology.nevaquiz.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class SingleLeaderViewHolder extends RecyclerView.ViewHolder {

    public CircleImageView profileImage;
    public TextView position, userName, scorePoint;

    public SingleLeaderViewHolder(@NonNull View itemView) {
        super(itemView);

        profileImage=(CircleImageView)itemView.findViewById(R.id.profileImage);
        position =(TextView)itemView.findViewById(R.id.position);
        userName =(TextView)itemView.findViewById(R.id.userName);
        scorePoint =(TextView)itemView.findViewById(R.id.scorePoint);
    }
}
