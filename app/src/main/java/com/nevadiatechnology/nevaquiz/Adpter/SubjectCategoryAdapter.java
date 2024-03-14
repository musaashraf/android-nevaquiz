package com.nevadiatechnology.nevaquiz.Adpter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nevadiatechnology.nevaquiz.Activity.PlayConditionActivity;
import com.nevadiatechnology.nevaquiz.Common.Common;
import com.nevadiatechnology.nevaquiz.Model.SubjectCategory;
import com.nevadiatechnology.nevaquiz.R;
import com.nevadiatechnology.nevaquiz.ViewHolder.SubjectCategoryViewHolder;

import java.util.List;

public class SubjectCategoryAdapter extends RecyclerView.Adapter<SubjectCategoryViewHolder> {

    private Context context;
    private List<SubjectCategory> categoryList;

    public SubjectCategoryAdapter(Context context, List<SubjectCategory> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public SubjectCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.subject_item, viewGroup, false);
        return new SubjectCategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectCategoryViewHolder holder, final int i) {
        holder.subject_name.setText(categoryList.get(i).getName());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.categoryName = categoryList.get(i).getName();
                context.startActivity(new Intent(context, PlayConditionActivity.class)
                        .putExtra("category", String.valueOf(categoryList.get(i).getId()))
                        .putExtra("categoryName", String.valueOf(categoryList.get(i).getName()))
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}
