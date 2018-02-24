package com.example.clark.newone.Task;

import android.widget.CheckBox;

/**
 * Created by clark on 02.02.2018.
 */

public class TaskModel {

    public String taskTitle;
    public CheckBox checkBox;

    public TaskModel() {

    }

    public TaskModel(String taskTitle, CheckBox checkBox) {
        this.taskTitle = taskTitle;
        this.checkBox = checkBox;

    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public CheckBox getCheckBox(){
        return checkBox;
    }

    public void setCheckBox() {
        this.checkBox = checkBox;
    }
}
