package com.example.makzholvtipo_35;

import android.net.Uri;
import android.provider.BaseColumns;

public final class StudentContract {

    private StudentContract() {
    }

    public static final String AUTHORITY = "com.example.studentproviderapp.provider";
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + StudentEntry.TABLE_NAME);

    public static final class StudentEntry implements BaseColumns {
        public static final String TABLE_NAME = "students";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_GROUP = "student_group";
        public static final String COLUMN_AGE = "age";
    }
}