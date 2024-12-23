package com.example.classmate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="AttendEase.db";
    private static final int DATABASE_VERSION=1;

    private static final String TABLE_SUBJECTS="subjects";
    private static final String COLUMN_ID="id";
    private static final String COLUMN_NAME="name";
    private static final String COLUMN_PRESENT="present";
    private static final String COLUMN_ABSENT="absent";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SUBJECTS_TABLE="CREATE TABLE " + TABLE_SUBJECTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT NOT NULL,"
                + COLUMN_PRESENT + " INTEGER DEFAULT 0,"
                + COLUMN_ABSENT + " INTEGER DEFAULT 0"
                + ")";
        db.execSQL(CREATE_SUBJECTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBJECTS);
        onCreate(db);
    }

    public long addSubject(Subject subject) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, subject.getName());
        values.put(COLUMN_PRESENT, subject.getTotalPresent());
        values.put(COLUMN_ABSENT, subject.getTotalAbsent());
        long id = db.insert(TABLE_SUBJECTS, null, values);
        db.close();
        return id;
    }


    public List<Subject> getAllSubjects(){
        List<Subject> subjects=new ArrayList<>();
        String selectQuery="SELECT * FROM " + TABLE_SUBJECTS;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                String name=cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                int present=cursor.getInt(cursor.getColumnIndex(COLUMN_PRESENT));
                int absent=cursor.getInt(cursor.getColumnIndex(COLUMN_ABSENT));
                subjects.add(new Subject(name, present, absent));
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return subjects;
    }

    public void updateSubject(Subject subject){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(COLUMN_PRESENT,subject.getTotalPresent());
        values.put(COLUMN_ABSENT,subject.getTotalAbsent());
        db.update(TABLE_SUBJECTS,values,COLUMN_NAME + " = ?",
                new String[]{subject.getName()});
        db.close();
    }

    public void deleteSubject(String subjectName){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_SUBJECTS,COLUMN_NAME + " = ?",
                new String[]{subjectName});
        db.close();
    }
}

