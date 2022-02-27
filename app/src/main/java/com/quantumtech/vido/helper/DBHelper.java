package com.quantumtech.vido.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Vido.db";
    public static final String TABLE_NAME = "favorite";
    public static final String COLUMN_ID = "id";
    public static final String MOVIE_ID = "movie_id";
    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists " + TABLE_NAME + "(" + COLUMN_ID + " integer  primary key autoincrement, " +
                MOVIE_ID + " integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insert( int movie_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(""+MOVIE_ID+"", movie_id);
        db.insert(""+TABLE_NAME+"", null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME+" where id = " + id + "", null);
        return res;
    }

    public String getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
//        String[] columns =
        Cursor res = db.rawQuery("select * from "+TABLE_NAME+"", null);
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()){
            int index0 = res.getColumnIndex(COLUMN_ID);
            int index1 = res.getColumnIndex(MOVIE_ID);

            int cid = res.getInt(index0);
            int movie_id = res.getInt(index1);

            buffer.append(cid + " " + movie_id);
        }
        return buffer.toString();
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    public void update(int id, int movie_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
//        contentValues.put(""+DRUG_COLUMN_ID+"", drug_id);
        contentValues.put(""+MOVIE_ID+"", movie_id);
        db.update(""+TABLE_NAME+"", contentValues, " id = " + id , null);
    }

    public Integer delete(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(""+TABLE_NAME+"", " id = ? ", new String[]{Integer.toString(id)});
    }

    public ArrayList<String> getAllDrug() {
        ArrayList<String> array_list = new ArrayList<String>();
        hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME+"", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex(MOVIE_ID)));
            res.moveToNext();
        }
        return array_list;
    }
}