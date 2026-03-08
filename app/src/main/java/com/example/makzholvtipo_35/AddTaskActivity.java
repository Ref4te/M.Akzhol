package com.example.makzholvtipo_35;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

public class AddTaskActivity extends AppCompatActivity {

    private EditText etTitle, etCategory;
    private TextView tvSelectedDeadline;
    private Button btnPickDateTime, btnSaveTask;

    private DBHelper dbHelper;
    private String selectedDeadline = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        etTitle = findViewById(R.id.etTitle);
        etCategory = findViewById(R.id.etCategory);
        tvSelectedDeadline = findViewById(R.id.tvSelectedDeadline);
        btnPickDateTime = findViewById(R.id.btnPickDateTime);
        btnSaveTask = findViewById(R.id.btnSaveTask);

        dbHelper = new DBHelper(this);

        btnPickDateTime.setOnClickListener(v -> pickDateTime());
        btnSaveTask.setOnClickListener(v -> saveTask());
    }

    private void saveTask() {
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
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Ошибка при сохранении", Toast.LENGTH_SHORT).show();
        }
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