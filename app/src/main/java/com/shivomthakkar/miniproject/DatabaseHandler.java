package com.shivomthakkar.miniproject;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shivom on 10/16/17.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "AttendanceLogs";
    private static final String TABLE_CONTACTS = "Attendance";

    private static final String KEY_ID = "roll_number";
    private static final String KEY_NAME = "name";
    private static final String KEY_IMEI = "imei";
    private static final String KEY_TIME = "time";
    private static final String KEY_CLASS = "class";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER, " + KEY_NAME + " TEXT, "
                + KEY_IMEI + " TEXT PRIMARY KEY, " + KEY_TIME + " TEXT, " + KEY_CLASS + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        // Create tables again
        onCreate(sqLiteDatabase);
    }

    void addStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, student.getRollNo());
        values.put(KEY_NAME, student.getName());
        values.put(KEY_IMEI, student.getImei());
        values.put(KEY_TIME, student.getTime());
        values.put(KEY_CLASS, student.getStClass());

        db.insert(TABLE_CONTACTS, null, values);
        db.close();
    }

    Student getStudent(int rollNo) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
                        KEY_NAME, KEY_IMEI, KEY_TIME, KEY_CLASS }, KEY_ID + "=?",
                new String[] { String.valueOf(rollNo) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Student student = new Student(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));

        return student;
    }

    public List<Student> getAllStudents() {
        List<Student> studentList = new ArrayList<Student>();

        String query = "SELECT * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()) {
            do {
                Student student = new Student(Integer.parseInt(cursor.getString(0)), cursor.getString(1),cursor.getString(2), cursor.getString(3), cursor.getString(4));
                studentList.add(student);
            } while(cursor.moveToNext());
        }
        return studentList;
    }

    public void deleteAll() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_CONTACTS, null, null);
    }

    public void deleteStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { String.valueOf(student.getRollNo()) });
        db.close();
    }

    public int getStudentCount() {
        String query = "SELECT * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.close();

        return cursor.getCount();
    }

    public List<Student> getListByTime(String time) {
        List<Student> studentList = new ArrayList<Student>();

        String query = "SELECT * FROM " + TABLE_CONTACTS + " WHERE time = \"" + time + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()) {
            do {
                Student student = new Student(Integer.parseInt(cursor.getString(0)), cursor.getString(1),cursor.getString(2), cursor.getString(3), cursor.getString(4));
                studentList.add(student);
            } while(cursor.moveToNext());
        }
        return studentList;
    }

    public List<Student> getListByClass(String stClass) {
        List<Student> studentList = new ArrayList<Student>();

        String query = "SELECT * FROM " + TABLE_CONTACTS + " WHERE class = \"" + stClass + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()) {
            do {
                Student student = new Student(Integer.parseInt(cursor.getString(0)), cursor.getString(1),cursor.getString(2), cursor.getString(3), cursor.getString(4));
                studentList.add(student);
            } while(cursor.moveToNext());
        }
        return studentList;
    }

}
