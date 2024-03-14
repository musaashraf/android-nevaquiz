package com.nevadiatechnology.nevaquiz.ViewHolder;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.nevadiatechnology.nevaquiz.Interface.ItemClickListener;
import com.nevadiatechnology.nevaquiz.R;

public class SubjectCategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView subject_name;
    private ItemClickListener itemClickListener;
    public CardView layout;

    public SubjectCategoryViewHolder(@NonNull View itemView) {
        super(itemView);

        subject_name = (TextView) itemView.findViewById(R.id.subject_name);
        layout = (CardView) itemView.findViewById(R.id.layout);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}