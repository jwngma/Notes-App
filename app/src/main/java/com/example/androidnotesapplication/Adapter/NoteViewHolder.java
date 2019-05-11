package com.example.androidnotesapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.androidnotesapplication.AddNoteActivity;
import com.example.androidnotesapplication.R;

import org.w3c.dom.Text;

public class NoteViewHolder extends RecyclerView.ViewHolder {

    Context context;

    public NoteViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;
    }

    TextView title, desc, time;

    View mView;

    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);

        mView = itemView;
        title = mView.findViewById(R.id.main_single_title);
        desc = mView.findViewById(R.id.main_single_desc);
        time = mView.findViewById(R.id.main_single_time);

    }

    public void setTitle(String titles) {
        title.setText(titles);
    }

    public void setDesc(String descs) {
        desc.setText(descs);
    }

    public  void  setTime(String times){
        time.setText(times);
    }

}
