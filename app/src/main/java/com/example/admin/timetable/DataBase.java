package com.example.admin.timetable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by admin on 2016/12/9.
 */
public class DataBase extends SQLiteOpenHelper {
    private static final String TABLE_NAME = "Course";
    public DataBase(Context context, String name, SQLiteDatabase.CursorFactory factory,
                int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE if not exists " + TABLE_NAME
                + " (name TEXT, teacher TEXT, email_ftp TEXT, week int, start int, end int, address TEXT, ddl TEXT, ddl_time TEXT, lamp int)";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {}
    public void insert(Course course) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", course.getName());
        cv.put("teacher", course.getTeacher());
        cv.put("email_ftp", course.getEmail_ftp());
        cv.put("week", course.getWeek());
        cv.put("start", course.getStart());
        cv.put("end", course.getEnd());
        cv.put("address", course.getAddress());
        cv.put("ddl", course.getDdl());
        cv.put("ddl_time", course.getDdl_time());
        cv.put("lamp", course.getLamp());
        db.insert(TABLE_NAME, null, cv);
        db.close();
        Log.v("insert", "yes");
    }
    public Course query(int week, int start) {
        SQLiteDatabase db = getReadableDatabase();
        Course course = new Course();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where week = " + week + "" + " and start = " + start + "", null);
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                course.setName(cursor.getString(cursor.getColumnIndex("name")));
                course.setTeacher(cursor.getString(cursor.getColumnIndex("teacher")));
                course.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                course.setEmail_ftp(cursor.getString(cursor.getColumnIndex("email_ftp")));
                course.setDdl(cursor.getString(cursor.getColumnIndex("ddl")));
                course.setStart(cursor.getInt(cursor.getColumnIndex("start")));
                course.setEnd(cursor.getInt(cursor.getColumnIndex("end")));
                course.setWeek(cursor.getInt(cursor.getColumnIndex("week")));
                course.setDdl_time(cursor.getString(cursor.getColumnIndex("ddl_time")));
                course.setLamp(cursor.getInt(cursor.getColumnIndex("lamp")));
            }
        }
        db.close();
        return course;
    }
    public void delete(int week, int start) {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "week = ? and start = ?";
        String[] whereArgs = {week + "", start + ""};
        db.delete(TABLE_NAME, whereClause, whereArgs);
        db.close();
    }
    public void update(Course course) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", course.getName());
        cv.put("teacher", course.getTeacher());
        cv.put("email_ftp", course.getEmail_ftp());
        cv.put("week", course.getWeek());
        cv.put("start", course.getStart());
        cv.put("end", course.getEnd());
        cv.put("address", course.getAddress());
        cv.put("ddl", course.getDdl());
        cv.put("ddl_time", course.getDdl_time());
        cv.put("lamp", course.getLamp());
        String whereClause = "week = ? and start = ?";
        String[] whereArgs = {course.getWeek() + "", course.getStart() + ""};
        db.update(TABLE_NAME, cv, whereClause, whereArgs);
        db.close();
    }
    public Boolean query_same(int week, int start, int end) {
        Boolean has = false;
        SQLiteDatabase db = getReadableDatabase();
        String[] column = {"name"};
        String select = "week = ? and ((start >= ? and start <= ?) or (end >= ? and end <= ?))";
        String[] args = {week + "", start + "", end + "", start + "", end + ""};
        Cursor cursor = db.query(TABLE_NAME, column, select, args, null, null, null);
        if (cursor.getCount() != 0) {
            has = true;
        }
        db.close();
        return has;
    }
    public Cursor query_all() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from Course", null);
        return cursor;
    }
}
