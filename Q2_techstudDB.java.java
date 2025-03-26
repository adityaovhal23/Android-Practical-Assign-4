DatabaseHelper.java
package com.example.studentteacherapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SchoolDB";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_STUDENT = "Student";
    private static final String TABLE_TEACHER = "Teacher";
    private static final String TABLE_STUDENT_TEACHER = "Student_Teacher";

    // Student table columns
    private static final String COLUMN_SNO = "sno";
    private static final String COLUMN_SNAME = "s_name";
    private static final String COLUMN_SCLASS = "s_class";
    private static final String COLUMN_SADDR = "s_addr";

    // Teacher table columns
    private static final String COLUMN_TNO = "tno";
    private static final String COLUMN_TNAME = "t_name";
    private static final String COLUMN_QUALIFICATION = "qualification";
    private static final String COLUMN_EXPERIENCE = "experience";

    // Student-Teacher table columns
    private static final String COLUMN_SUBJECT = "subject";

    // Create Student Table
    private static final String CREATE_STUDENT_TABLE = "CREATE TABLE " + TABLE_STUDENT + " (" +
            COLUMN_SNO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_SNAME + " TEXT, " +
            COLUMN_SCLASS + " TEXT, " +
            COLUMN_SADDR + " TEXT);";

    // Create Teacher Table
    private static final String CREATE_TEACHER_TABLE = "CREATE TABLE " + TABLE_TEACHER + " (" +
            COLUMN_TNO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TNAME + " TEXT, " +
            COLUMN_QUALIFICATION + " TEXT, " +
            COLUMN_EXPERIENCE + " TEXT);";

    // Create Student-Teacher Relationship Table
    private static final String CREATE_STUDENT_TEACHER_TABLE = "CREATE TABLE " + TABLE_STUDENT_TEACHER + " (" +
            COLUMN_SNO + " INTEGER, " +
            COLUMN_TNO + " INTEGER, " +
            COLUMN_SUBJECT + " TEXT, " +
            "FOREIGN KEY (" + COLUMN_SNO + ") REFERENCES " + TABLE_STUDENT + "(" + COLUMN_SNO + "), " +
            "FOREIGN KEY (" + COLUMN_TNO + ") REFERENCES " + TABLE_TEACHER + "(" + COLUMN_TNO + "));";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STUDENT_TABLE);
        db.execSQL(CREATE_TEACHER_TABLE);
        db.execSQL(CREATE_STUDENT_TEACHER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT_TEACHER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEACHER);
        onCreate(db);
    }

    // Insert sample data for testing
    public void insertSampleData() {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues teacherValues = new ContentValues();
        teacherValues.put(COLUMN_TNAME, "John Doe");
        teacherValues.put(COLUMN_QUALIFICATION, "M.Sc");
        teacherValues.put(COLUMN_EXPERIENCE, "5 Years");
        long tno = db.insert(TABLE_TEACHER, null, teacherValues);

        ContentValues studentValues = new ContentValues();
        studentValues.put(COLUMN_SNAME, "Alice Smith");
        studentValues.put(COLUMN_SCLASS, "10th");
        studentValues.put(COLUMN_SADDR, "New York");
        long sno = db.insert(TABLE_STUDENT, null, studentValues);

        ContentValues mappingValues = new ContentValues();
        mappingValues.put(COLUMN_SNO, sno);
        mappingValues.put(COLUMN_TNO, tno);
        mappingValues.put(COLUMN_SUBJECT, "Mathematics");
        db.insert(TABLE_STUDENT_TEACHER, null, mappingValues);
    }

    public Cursor getStudentsByTeacher(String teacherName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT s.s_name, st.subject FROM " + TABLE_STUDENT + " s " +
                "JOIN " + TABLE_STUDENT_TEACHER + " st ON s.sno = st.sno " +
                "JOIN " + TABLE_TEACHER + " t ON t.tno = st.tno " +
                "WHERE t.t_name = ?";
        return db.rawQuery(query, new String[]{teacherName});
    }
}
