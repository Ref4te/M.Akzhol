package com.example.makzholvtipo_35;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewStudents;
    private Button btnLoadStudents;

    private StudentAdapter adapter;
    private ArrayList<Student> studentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewStudents = findViewById(R.id.recyclerViewStudents);
        btnLoadStudents = findViewById(R.id.btnLoadStudents);

        studentList = new ArrayList<>();
        adapter = new StudentAdapter(studentList);

        recyclerViewStudents.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewStudents.setAdapter(adapter);

        btnLoadStudents.setOnClickListener(v -> loadStudents());

        loadStudents();
    }

    private void loadStudents() {
        ArrayList<Student> newList = new ArrayList<>();

        Cursor cursor = null;

        try {
            cursor = getContentResolver().query(
                    StudentContract.StudentEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    StudentContract.StudentEntry._ID + " ASC"
            );

            if (cursor == null) {
                Toast.makeText(this, "Не удалось получить данные", Toast.LENGTH_SHORT).show();
                return;
            }

            int idIndex = cursor.getColumnIndex(StudentContract.StudentEntry._ID);
            int nameIndex = cursor.getColumnIndex(StudentContract.StudentEntry.COLUMN_NAME);
            int groupIndex = cursor.getColumnIndex(StudentContract.StudentEntry.COLUMN_GROUP);
            int ageIndex = cursor.getColumnIndex(StudentContract.StudentEntry.COLUMN_AGE);

            while (cursor.moveToNext()) {
                int id = cursor.getInt(idIndex);
                String name = cursor.getString(nameIndex);
                String group = cursor.getString(groupIndex);
                int age = cursor.getInt(ageIndex);

                newList.add(new Student(id, name, group, age));
            }

            adapter.setStudentList(newList);

            if (newList.isEmpty()) {
                Toast.makeText(this, "Список студентов пуст", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Ошибка доступа: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}