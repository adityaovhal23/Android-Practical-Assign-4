package com.example.empdeptapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "CompanyDB";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_EMP = "Emp";
    private static final String TABLE_DEPT = "Dept";

    // Employee Columns
    private static final String EMP_NO = "emp_no";
    private static final String EMP_NAME = "emp_name";
    private static final String ADDRESS = "address";
    private static final String PHONE = "phone";
    private static final String SALARY = "salary";
    private static final String DEPT_NO_FK = "dept_no";

    // Department Columns
    private static final String DEPT_NO = "dept_no";
    private static final String DEPT_NAME = "dept_name";
    private static final String LOCATION = "location";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Department Table
        String createDeptTable = "CREATE TABLE " + TABLE_DEPT + " (" +
                DEPT_NO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DEPT_NAME + " TEXT UNIQUE, " +
                LOCATION + " TEXT)";
        db.execSQL(createDeptTable);

        // Create Employee Table
        String createEmpTable = "CREATE TABLE " + TABLE_EMP + " (" +
                EMP_NO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                EMP_NAME + " TEXT, " +
                ADDRESS + " TEXT, " +
                PHONE + " TEXT, " +
                SALARY + " REAL, " +
                DEPT_NO_FK + " INTEGER, " +
                "FOREIGN KEY (" + DEPT_NO_FK + ") REFERENCES " + TABLE_DEPT + "(" + DEPT_NO + "))";
        db.execSQL(createEmpTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEPT);
        onCreate(db);
    }

    // Insert Department
    public boolean insertDepartment(String deptName, String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DEPT_NAME, deptName);
        values.put(LOCATION, location);

        long result = db.insert(TABLE_DEPT, null, values);
        return result != -1;
    }

    // Insert Employee
    public boolean insertEmployee(String name, String address, String phone, double salary, int deptNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EMP_NAME, name);
        values.put(ADDRESS, address);
        values.put(PHONE, phone);
        values.put(SALARY, salary);
        values.put(DEPT_NO_FK, deptNo);

        long result = db.insert(TABLE_EMP, null, values);
        return result != -1;
    }

    // Delete Employees by Department Name
    public int deleteEmployeesByDept(String deptName) {
        SQLiteDatabase db = this.getWritableDatabase();
        int deptId = getDeptIdByName(deptName);
        if (deptId == -1) {
            return 0;
        }
        return db.delete(TABLE_EMP, DEPT_NO_FK + "=?", new String[]{String.valueOf(deptId)});
    }

    // Get Department ID by Name
    public int getDeptIdByName(String deptName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + DEPT_NO + " FROM " + TABLE_DEPT + " WHERE " + DEPT_NAME + "=?", new String[]{deptName});
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            cursor.close();
            return id;
        }
        cursor.close();
        return -1;
    }
}
