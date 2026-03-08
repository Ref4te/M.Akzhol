package com.example.makzholvtipo_35;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnAdd, btnRead, btnDelete, btnPickDateTime;
    private EditText etTitle, etCategory;
    private TextView tvSelectedDeadline;

    private RecyclerView recyclerViewTasks;
    private TaskAdapter taskAdapter;
    private ArrayList<Task> taskList;

    private DBHelper dbHelper;
    private String selectedDeadline = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initRecyclerView();

        dbHelper = new DBHelper(this);

        btnAdd.setOnClickListener(this);
        btnRead.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnPickDateTime.setOnClickListener(this);

        loadTasksFromDatabase();
    }

    private void initViews() {
        btnAdd = findViewById(R.id.btnAdd);
        btnRead = findViewById(R.id.btnRead);
        btnDelete = findViewById(R.id.btnDelete);
        btnPickDateTime = findViewById(R.id.btnPickDateTime);

        etTitle = findViewById(R.id.etTitle);
        etCategory = findViewById(R.id.etCategory);
        tvSelectedDeadline = findViewById(R.id.tvSelectedDeadline);

        recyclerViewTasks = findViewById(R.id.recyclerViewTasks);
    }

    private void initRecyclerView() {
        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(taskList);

        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTasks.setAdapter(taskAdapter);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btnPickDateTime) {
            pickDateTime();
        } else if (id == R.id.btnAdd) {
            addTask();
        } else if (id == R.id.btnRead) {
            loadTasksFromDatabase();
        } else if (id == R.id.btnDelete) {
            deleteAllTasks();
        }
    }

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

        long result = db.insert(DBHelper.TABLE_NAME, null, cv);
        db.close();

        if (result != -1) {
            Toast.makeText(this, "Дело добавлено", Toast.LENGTH_SHORT).show();

            etTitle.setText("");
            etCategory.setText("");
            selectedDeadline = "";
            tvSelectedDeadline.setText("Дедлайн не выбран");

            loadTasksFromDatabase();
        } else {
            Toast.makeText(this, "Ошибка добавления", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteAllTasks() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int deletedRows = db.delete(DBHelper.TABLE_NAME, null, null);
        db.close();

        Toast.makeText(this, "Удалено записей: " + deletedRows, Toast.LENGTH_SHORT).show();
        loadTasksFromDatabase();
    }

    private void loadTasksFromDatabase() {
        ArrayList<Task> newTaskList = new ArrayList<>();

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

        if (c.moveToFirst()) {
            int idColIndex = c.getColumnIndex(DBHelper.COLUMN_ID);
            int titleColIndex = c.getColumnIndex(DBHelper.COLUMN_TITLE);
            int deadlineColIndex = c.getColumnIndex(DBHelper.COLUMN_DEADLINE);
            int categoryColIndex = c.getColumnIndex(DBHelper.COLUMN_CATEGORY);

            do {
                int id = c.getInt(idColIndex);
                String title = c.getString(titleColIndex);
                String deadline = c.getString(deadlineColIndex);
                String category = c.getString(categoryColIndex);

                newTaskList.add(new Task(id, title, deadline, category));
            } while (c.moveToNext());
        }

        c.close();
        db.close();

        taskAdapter.setTaskList(newTaskList);
    }

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

                                tvSelectedDeadline.setText(selectedDeadline);

                            }, hour, minute, true);

                    timePickerDialog.show();

                }, year, month, day);

        datePickerDialog.show();
    }
}