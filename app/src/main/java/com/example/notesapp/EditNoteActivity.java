package com.example.notesapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

public class EditNoteActivity extends AppCompatActivity {

    private EditText etTitle, etCategory, etTime, etDescription;
    private Button btnSave, btnDelete;

    private DBHelper dbHelper;

    private int noteId = -1;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        etTitle = findViewById(R.id.etTitle);
        etCategory = findViewById(R.id.etCategory);
        etTime = findViewById(R.id.etTime);
        etDescription = findViewById(R.id.etDescription);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);

        dbHelper = new DBHelper(this);

        noteId = getIntent().getIntExtra("note_id", -1);
        isEditMode = noteId != -1;

        if (isEditMode) {
            loadNoteData(noteId);
            btnDelete.setEnabled(true);
        } else {
            btnDelete.setEnabled(false);
        }

        etTime.setOnClickListener(v -> showDateTimePicker());

        btnSave.setOnClickListener(v -> saveNote());
        btnDelete.setOnClickListener(v -> deleteNote());
    }

    private void loadNoteData(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                DBHelper.TABLE_NOTES,
                null,
                DBHelper.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            etTitle.setText(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TITLE)));
            etCategory.setText(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_CATEGORY)));
            etTime.setText(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TIME)));
            etDescription.setText(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DESCRIPTION)));
        }

        cursor.close();
        db.close();
    }

    private void showDateTimePicker() {
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                            (timeView, selectedHour, selectedMinute) -> {
                                String selectedDateTime = String.format(
                                        Locale.getDefault(),
                                        "%02d.%02d.%04d %02d:%02d",
                                        selectedDay,
                                        selectedMonth + 1,
                                        selectedYear,
                                        selectedHour,
                                        selectedMinute
                                );
                                etTime.setText(selectedDateTime);
                            },
                            hour,
                            minute,
                            true
                    );
                    timePickerDialog.show();
                },
                year,
                month,
                day
        );

        datePickerDialog.show();
    }

    private void saveNote() {
        String title = etTitle.getText().toString().trim();
        String category = etCategory.getText().toString().trim();
        String time = etTime.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(this, "Введите title", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DBHelper.COLUMN_TITLE, title);
        values.put(DBHelper.COLUMN_CATEGORY, category);
        values.put(DBHelper.COLUMN_TIME, time);
        values.put(DBHelper.COLUMN_DESCRIPTION, description);

        long result;

        if (isEditMode) {
            result = db.update(
                    DBHelper.TABLE_NOTES,
                    values,
                    DBHelper.COLUMN_ID + "=?",
                    new String[]{String.valueOf(noteId)}
            );
        } else {
            result = db.insert(DBHelper.TABLE_NOTES, null, values);
        }

        db.close();

        if (result != -1) {
            Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Ошибка сохранения", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteNote() {
        if (!isEditMode) {
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int deleted = db.delete(
                DBHelper.TABLE_NOTES,
                DBHelper.COLUMN_ID + "=?",
                new String[]{String.valueOf(noteId)}
        );

        db.close();

        if (deleted > 0) {
            Toast.makeText(this, "Удалено", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Ошибка удаления", Toast.LENGTH_SHORT).show();
        }
    }
}