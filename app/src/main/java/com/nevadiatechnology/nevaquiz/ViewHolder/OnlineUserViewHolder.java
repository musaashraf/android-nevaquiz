package com.nevadiatechnology.nevaquiz.ViewHolder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nevadiatechnology.nevaquiz.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class OnlineUserViewHolder extends RecyclerView.ViewHolder {

    public CircleImageView profileImage;
    public TextView name, point;
    public LinearLayout relativeLayout;

    public OnlineUserViewHolder(@NonNull View itemView) {
        super(itemView);

        profileImage = (CircleImageView) itemView.findViewById(R.id.profileImage);
        name = (TextView) itemView.findViewById(R.id.userName);
        //point = (TextView) itemView.findViewById(R.id.textView6);
        relativeLayout = (LinearLayout) itemView.findViewById(R.id.relativeLayout);
    }
}
