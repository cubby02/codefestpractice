package com.example.codefestpractice;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {


    SQLiteDatabase db;
    private static final String DB_NAME = "Userdata";
    private static final String TBL_USER = "tblUser";
    private static final String COL_NAME = "colName";
    private static final String COL_PHONE = "colPhone";
    private static final String COL_DOB = "colDob";


    public DBHelper(Context context) {
        super(context, DB_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TBL_USER + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, "+
                COL_NAME + " TEXT, " +
                COL_PHONE + " TEXT, " +
                COL_DOB + " TEXT)";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TBL_USER);
        onCreate(sqLiteDatabase);
    }

    public boolean addDetails(String name, String number, String dob){
        db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, name);
        cv.put(COL_PHONE, number);
        cv.put(COL_DOB, dob);

        long insert =db.insert(TBL_USER, null, cv);

        return insert != -1;
    }

    public boolean update(String name, String number, String dob){
        db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, name);
        cv.put(COL_PHONE, number);
        cv.put(COL_DOB, dob);

        Cursor cursor = db.rawQuery("SELECT * FROM "+ TBL_USER + " where " + COL_PHONE + "=?", new String[]{number});
        if(cursor.getCount() > 0){
            long update = db.update(TBL_USER, cv, COL_PHONE + "=?", new String[]{number});

            return  update != -1;
        } else {
            return false;
        }
    }

    public boolean delete(String name){
        db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TBL_USER + " where " + COL_NAME + "=?", new String[]{name});
        if(cursor.getCount() > 0){
            long delete = db.delete(TBL_USER, COL_NAME + "=?", new String[]{name});

            return delete != -1;
        } else {
            return false;
        }

    }

    //manual retrieval
    public Cursor getData(){
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TBL_USER, null);
        return cursor;
    }

    //bulk retrieval
    @SuppressLint("Range")
    public List<Details> getAllData(){
        db = this.getReadableDatabase();
        List<Details> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TBL_USER, null);

        if(cursor.moveToFirst()){
            do {
                Details details = new Details();

                details.setId(cursor.getInt(cursor.getColumnIndex("ID")));
                details.setName(cursor.getString(cursor.getColumnIndex(COL_NAME)));
                details.setPhone(cursor.getString(cursor.getColumnIndex(COL_PHONE)));
                details.setDob(cursor.getString(cursor.getColumnIndex(COL_DOB)));

                list.add(details);
            } while (cursor.moveToNext());
        }



        return list;
    }
}
