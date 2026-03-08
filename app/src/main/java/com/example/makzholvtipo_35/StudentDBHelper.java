package com.example.makzholvtipo_35;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StudentDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "students.db";
    private static final int DATABASE_VERSION = 1;

    public StudentDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + StudentContract.StudentEntry.TABLE_NAME + " ("
                + StudentContract.StudentEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + StudentContract.StudentEntry.COLUMN_NAME + " TEXT NOT NULL, "
                + StudentContract.StudentEntry.COLUMN_GROUP + " TEXT NOT NULL, "
                + StudentContract.StudentEntry.COLUMN_AGE + " INTEGER NOT NULL"
                + ");");

        // Тестовые данные
        db.execSQL("INSERT INTO " + StudentContract.StudentEntry.TABLE_NAME +
                " (" + StudentContract.StudentEntry.COLUMN_NAME + ", "
                + StudentContract.StudentEntry.COLUMN_GROUP + ", "
                + StudentContract.StudentEntry.COLUMN_AGE + ") VALUES " +
                "('Манат Акжол', 'VTPO-35', 20), " +
                "('Ералиев Ельнур', 'VTPO-35', 20), " +
                "('Карабаев Бакдаулет', 'VTPO-35', 19);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + StudentContract.StudentEntry.TABLE_NAME);
        onCreate(db);
    }
}