package com.example.clark.newone.Task;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.clark.newone.R;

/**
 * Created by clark on 02.02.2018.
 */

public class TaskViewHolder extends RecyclerView.ViewHolder {

    View mView;

    TextView taskTitle;
    CheckBox checkBox;
    CardView taskCard;

    public TaskViewHolder(View itemView) {
        super(itemView);

        mView = itemView;

        taskTitle = mView.findViewById(R.id.taskTitle);
        checkBox = mView.findViewById(R.id.taskCheckBox);
        taskCard = mView.findViewById(R.id.taskCardView);
    }

    public void setTaskTitle(String title) {
        taskTitle.setText(title);
    }

    public void setCheckBox(String time) {
        checkBox.setText(time);
    }



}
