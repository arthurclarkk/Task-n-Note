package com.example.clark.newone;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.clark.newone.R;

import java.util.AbstractCollection;
import java.util.HashSet;

/**
 * Created by clark on 22.01.2018.
 */

public class NoteViewHolder extends RecyclerView.ViewHolder {

    View mView;

    TextView textTitle, textContent, textTime;
    CardView noteCard;

    public NoteViewHolder(View itemView) {
        super(itemView);

        mView = itemView;

        textTitle = mView.findViewById(R.id.note_title);
        textTime = mView.findViewById(R.id.note_time);
        textContent = mView.findViewById(R.id.note_content);
        noteCard = mView.findViewById(R.id.note_card);



    }

    public void setNoteTitle(String title) {
        textTitle.setText(title);
    }

    public void setNoteTime(String time) {
        textTime.setText(time);
    }

    public void setNoteContent(String content) { textContent.setText(content);}


}
