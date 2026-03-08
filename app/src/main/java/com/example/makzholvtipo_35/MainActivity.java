package com.example.makzholvtipo_35;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewTasks;
    private Button btnOpenAddTask;

    private DBHelper dbHelper;
    private TaskAdapter taskAdapter;
    private ArrayList<Task> taskList;

    private final ActivityResultLauncher<Intent> addTaskLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> loadTasksFromDatabase()
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewTasks = findViewById(R.id.recyclerViewTasks);
        btnOpenAddTask = findViewById(R.id.btnOpenAddTask);

        dbHelper = new DBHelper(this);

        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(taskList);

        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTasks.setAdapter(taskAdapter);

        btnOpenAddTask.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
            addTaskLauncher.launch(intent);
        });

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

    @Override
    protected void onResume() {
        super.onResume();
        loadTasksFromDatabase();
    }
}