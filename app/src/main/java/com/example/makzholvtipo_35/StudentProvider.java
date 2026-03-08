package com.example.makzholvtipo_35;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class StudentProvider extends ContentProvider {

    private StudentDBHelper dbHelper;

    private static final int STUDENTS = 1;
    private static final int STUDENT_ID = 2;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(StudentContract.AUTHORITY,
                StudentContract.StudentEntry.TABLE_NAME, STUDENTS);

        uriMatcher.addURI(StudentContract.AUTHORITY,
                StudentContract.StudentEntry.TABLE_NAME + "/#", STUDENT_ID);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new StudentDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,
                        @Nullable String[] projection,
                        @Nullable String selection,
                        @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor;

        switch (uriMatcher.match(uri)) {
            case STUDENTS:
                cursor = db.query(
                        StudentContract.StudentEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case STUDENT_ID:
                String id = String.valueOf(ContentUris.parseId(uri));
                cursor = db.query(
                        StudentContract.StudentEntry.TABLE_NAME,
                        projection,
                        StudentContract.StudentEntry._ID + "=?",
                        new String[]{id},
                        null,
                        null,
                        sortOrder
                );
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        if (getContext() != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case STUDENTS:
                return "vnd.android.cursor.dir/vnd." + StudentContract.AUTHORITY + ".students";
            case STUDENT_ID:
                return "vnd.android.cursor.item/vnd." + StudentContract.AUTHORITY + ".students";
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        if (uriMatcher.match(uri) != STUDENTS) {
            throw new IllegalArgumentException("Invalid URI for insert: " + uri);
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id = db.insert(StudentContract.StudentEntry.TABLE_NAME, null, values);

        if (id == -1) {
            throw new RuntimeException("Failed to insert row into " + uri);
        }

        Uri insertedUri = ContentUris.withAppendedId(StudentContract.CONTENT_URI, id);

        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return insertedUri;
    }

    @Override
    public int delete(@NonNull Uri uri,
                      @Nullable String selection,
                      @Nullable String[] selectionArgs) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int deletedRows;

        switch (uriMatcher.match(uri)) {
            case STUDENTS:
                deletedRows = db.delete(
                        StudentContract.StudentEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;

            case STUDENT_ID:
                String id = String.valueOf(ContentUris.parseId(uri));
                deletedRows = db.delete(
                        StudentContract.StudentEntry.TABLE_NAME,
                        StudentContract.StudentEntry._ID + "=?",
                        new String[]{id}
                );
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        if (deletedRows > 0 && getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return deletedRows;
    }

    @Override
    public int update(@NonNull Uri uri,
                      @Nullable ContentValues values,
                      @Nullable String selection,
                      @Nullable String[] selectionArgs) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int updatedRows;

        switch (uriMatcher.match(uri)) {
            case STUDENTS:
                updatedRows = db.update(
                        StudentContract.StudentEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs
                );
                break;

            case STUDENT_ID:
                String id = String.valueOf(ContentUris.parseId(uri));
                updatedRows = db.update(
                        StudentContract.StudentEntry.TABLE_NAME,
                        values,
                        StudentContract.StudentEntry._ID + "=?",
                        new String[]{id}
                );
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        if (updatedRows > 0 && getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return updatedRows;
    }
}