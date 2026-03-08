package com.example.makzholvtipo_35;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnAdd, btnDelete, btnRead, btnPickDateTime;
    EditText etTitle, etCategory;
    TextView tvDeadline, tvResult;
    DBHelper dbHelper;

    String selectedDeadline = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        btnDelete = findViewById(R.id.btnDelete);
        btnRead = findViewById(R.id.btnRead);
        btnPickDateTime = findViewById(R.id.btnPickDateTime);

        etTitle = findViewById(R.id.etTitle);
        etCategory = findViewById(R.id.etCategory);

        tvDeadline = findViewById(R.id.tvDeadline);
        tvResult = findViewById(R.id.tvResult);

        btnAdd.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnRead.setOnClickListener(this);
        btnPickDateTime.setOnClickListener(this);

        dbHelper = new DBHelper(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btnPickDateTime) {
            pickDateTime();
        } else if (id == R.id.btnAdd) {
            addTask();
        } else if (id == R.id.btnRead) {
            readTasks();
        } else if (id == R.id.btnDelete) {
            deleteAllTasks();
        }
    }

    // Функция добавления задачи в базу данных
    private void addTask() {
        String title = etTitle.getText().toString().trim();
        String category = etCategory.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(this, "Введите название дела", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedDeadline.isEmpty()) {
            Toast.makeText(this, "Выберите дедлайн", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(DBHelper.COLUMN_TITLE, title);
        cv.put(DBHelper.COLUMN_DEADLINE, selectedDeadline);
        cv.put(DBHelper.COLUMN_CATEGORY, category);

        long rowId = db.insert(DBHelper.TABLE_NAME, null, cv);

        if (rowId != -1) {
            Toast.makeText(this, "Дело добавлено", Toast.LENGTH_SHORT).show();
            etTitle.setText("");
            etCategory.setText("");
            selectedDeadline = "";
            tvDeadline.setText("Дедлайн не выбран");
        } else {
            Toast.makeText(this, "Ошибка при добавлении", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    // Функция чтения всех задач из базы данных
    private void readTasks() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.query(
                DBHelper.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                DBHelper.COLUMN_ID + " DESC"
        );

        StringBuilder builder = new StringBuilder();

        if (c.moveToFirst()) {
            int idColIndex = c.getColumnIndex(DBHelper.COLUMN_ID);
            int titleColIndex = c.getColumnIndex(DBHelper.COLUMN_TITLE);
            int deadlineColIndex = c.getColumnIndex(DBHelper.COLUMN_DEADLINE);
            int categoryColIndex = c.getColumnIndex(DBHelper.COLUMN_CATEGORY);

            do {
                builder.append("ID: ").append(c.getInt(idColIndex)).append("\n");
                builder.append("Название: ").append(c.getString(titleColIndex)).append("\n");
                builder.append("Дедлайн: ").append(c.getString(deadlineColIndex)).append("\n");
                builder.append("Категория: ").append(c.getString(categoryColIndex)).append("\n");
                builder.append("--------------------------\n");
            } while (c.moveToNext());

            tvResult.setText(builder.toString());
        } else {
            tvResult.setText("Список дел пуст");
        }

        c.close();
        db.close();
    }

    // Функция удаления всех задач из базы данных
    private void deleteAllTasks() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int deletedRows = db.delete(DBHelper.TABLE_NAME, null, null);

        tvResult.setText("");
        Toast.makeText(this, "Удалено записей: " + deletedRows, Toast.LENGTH_SHORT).show();

        db.close();
    }

    // Функция выбора даты и времени
    private void pickDateTime() {
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

                                selectedDeadline = String.format(
                                        Locale.getDefault(),
                                        "%02d.%02d.%04d %02d:%02d",
                                        selectedDay,
                                        selectedMonth + 1,
                                        selectedYear,
                                        selectedHour,
                                        selectedMinute
                                );

                                tvDeadline.setText(selectedDeadline);

                            }, hour, minute, true);

                    timePickerDialog.show();

                }, year, month, day);

        datePickerDialog.show();
    }
}