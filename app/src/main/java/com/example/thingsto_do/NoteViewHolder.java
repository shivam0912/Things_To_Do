package com.example.thingsto_do;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

class NoteViewHolder extends RecyclerView.ViewHolder {

    private TextView textTitle, textTime,textDescription;
     LinearLayout root;

    NoteViewHolder(View view){
        super(view);

        root = view.findViewById(R.id.list_root);
        textTitle = view.findViewById(R.id.title_note);
        textTime = view.findViewById(R.id.update_time);
        textDescription = view.findViewById(R.id.note_description);
    }
    void setNoteTitle(String title){
        textTitle.setText(title);

    }
    void setDescription(String description){ textDescription.setText(description);}

    void setTime(String time){
        textTime.setText(time);
    }

}
