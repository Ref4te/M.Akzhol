package com.example.notesapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    public interface OnNoteClickListener {
        void onNoteClick(Note note);
    }

    private ArrayList<Note> noteList;
    private final OnNoteClickListener listener;

    public NoteAdapter(ArrayList<Note> noteList, OnNoteClickListener listener) {
        this.noteList = noteList;
        this.listener = listener;
    }

    public void setNoteList(ArrayList<Note> noteList) {
        this.noteList = noteList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = noteList.get(position);

        holder.tvTitle.setText(note.getTitle());

        String category = note.getCategory();
        if (category == null || category.trim().isEmpty()) {
            holder.tvCategory.setText("Категория: -");
        } else {
            holder.tvCategory.setText("Категория: " + category);
        }

        String time = note.getTime();
        if (time == null || time.trim().isEmpty()) {
            holder.tvTime.setText("Время: -");
        } else {
            holder.tvTime.setText("Время: " + time);
        }

        String description = note.getDescription();
        if (description == null || description.trim().isEmpty()) {
            holder.tvDescription.setText("Описание отсутствует");
        } else {
            holder.tvDescription.setText(description);
        }

        holder.cardView.setOnClickListener(v -> listener.onNoteClick(note));
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tvTitle, tvCategory, tvTime, tvDescription;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardViewNote);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }
    }
}