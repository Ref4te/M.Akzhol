package com.example.notesapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText etSearch;
    private RecyclerView recyclerView;
    private FloatingActionButton fabAdd;

    private DBHelper dbHelper;
    private NoteAdapter adapter;
    private ArrayList<Note> fullNoteList;

    private final ActivityResultLauncher<Intent> launcher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> loadNotes()
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etSearch = findViewById(R.id.etSearch);
        recyclerView = findViewById(R.id.recyclerViewNotes);
        fabAdd = findViewById(R.id.fabAdd);

        dbHelper = new DBHelper(this);
        fullNoteList = new ArrayList<>();

        adapter = new NoteAdapter(new ArrayList<>(), note -> {
            Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
            intent.putExtra("note_id", note.getId());
            launcher.launch(intent);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
            launcher.launch(intent);
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterNotes(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        loadNotes();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotes();
    }

    private void loadNotes() {
        ArrayList<Note> list = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                DBHelper.TABLE_NOTES,
                null,
                null,
                null,
                null,
                null,
                DBHelper.COLUMN_ID + " DESC"
        );

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.COLUMN_ID);
            int titleIndex = cursor.getColumnIndex(DBHelper.COLUMN_TITLE);
            int categoryIndex = cursor.getColumnIndex(DBHelper.COLUMN_CATEGORY);
            int timeIndex = cursor.getColumnIndex(DBHelper.COLUMN_TIME);
            int descriptionIndex = cursor.getColumnIndex(DBHelper.COLUMN_DESCRIPTION);

            do {
                int id = cursor.getInt(idIndex);
                String title = cursor.getString(titleIndex);
                String category = cursor.getString(categoryIndex);
                String time = cursor.getString(timeIndex);
                String description = cursor.getString(descriptionIndex);

                list.add(new Note(id, title, category, time, description));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        fullNoteList = list;
        filterNotes(etSearch.getText().toString().trim());
    }

    private void filterNotes(String query) {
        ArrayList<Note> filteredList = new ArrayList<>();

        if (query.isEmpty()) {
            filteredList.addAll(fullNoteList);
        } else {
            for (Note note : fullNoteList) {
                if (note.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(note);
                }
            }
        }

        adapter.setNoteList(filteredList);
    }
}