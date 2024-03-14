package com.nevadiatechnology.nevaquiz.ViewHolder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nevadiatechnology.nevaquiz.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestViewHolder extends RecyclerView.ViewHolder {

    public CircleImageView request_image;
    public TextView request_name;
    public Button accept_btn, delete_btn;

    public RequestViewHolder(@NonNull View itemView) {
        super(itemView);

        request_image = (CircleImageView) itemView.findViewById(R.id.request_image);
        request_name = (TextView) itemView.findViewById(R.id.request_name);
        accept_btn = (Button) itemView.findViewById(R.id.accept_btn);
        delete_btn = (Button) itemView.findViewById(R.id.delete_btn);
    }
}
