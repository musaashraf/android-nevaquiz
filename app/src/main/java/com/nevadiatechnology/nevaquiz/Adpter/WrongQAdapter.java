package com.nevadiatechnology.nevaquiz.Adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nevadiatechnology.nevaquiz.Model.WrongQ;
import com.nevadiatechnology.nevaquiz.R;

import java.util.ArrayList;

public class WrongQAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<WrongQ> wrongQArrayList;
    private int layout;

    public WrongQAdapter(Context context, ArrayList<WrongQ> wrongQArrayList, int layout) {
        this.context = context;
        this.wrongQArrayList = wrongQArrayList;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return wrongQArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return wrongQArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {
        TextView text_question, text_given_ans, text_correct_ans;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder = new ViewHolder();

        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(layout, null);

            viewHolder.text_question = (TextView) view.findViewById(R.id.text_question);
            viewHolder.text_given_ans = (TextView) view.findViewById(R.id.text_given_ans);
            viewHolder.text_correct_ans = (TextView) view.findViewById(R.id.text_correct_ans);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.text_question.setText("Question : " + wrongQArrayList.get(position).getQuestion());
        viewHolder.text_given_ans.setText("Given Ans : " + wrongQArrayList.get(position).getSelectedAnswer());
        viewHolder.text_correct_ans.setText("Correct Ans : " + wrongQArrayList.get(position).getActualAnswer());

        return view;
    }
}
