package com.example.makzholvtipo_35;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

public class ProviderMainActivity extends AppCompatActivity {

    private TextView tvStudents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvStudents = new TextView(this);
        setContentView(tvStudents);

        showStudents();
    }

    private void showStudents() {
        Cursor cursor = getContentResolver().query(
                StudentContract.CONTENT_URI,
                null,
                null,
                null,
                StudentContract.StudentEntry._ID + " ASC"
        );

        StringBuilder builder = new StringBuilder();

        if (cursor != null) {
            int idIndex = cursor.getColumnIndex(StudentContract.StudentEntry._ID);
            int nameIndex = cursor.getColumnIndex(StudentContract.StudentEntry.COLUMN_NAME);
            int groupIndex = cursor.getColumnIndex(StudentContract.StudentEntry.COLUMN_GROUP);
            int ageIndex = cursor.getColumnIndex(StudentContract.StudentEntry.COLUMN_AGE);

            while (cursor.moveToNext()) {
                builder.append("ID: ").append(cursor.getInt(idIndex)).append("\n");
                builder.append("Имя: ").append(cursor.getString(nameIndex)).append("\n");
                builder.append("Группа: ").append(cursor.getString(groupIndex)).append("\n");
                builder.append("Возраст: ").append(cursor.getInt(ageIndex)).append("\n");
                builder.append("----------------------\n");
            }

            cursor.close();
        } else {
            builder.append("Нет данных");
        }

        tvStudents.setText(builder.toString());
    }
}