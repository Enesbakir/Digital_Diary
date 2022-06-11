package com.example.digital_diary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "diaryApp.DB";
    private static final int DATABASE_VERSION = 1;
    private static final String ID_COL = "id";
    private static final String TABLE_NAME = "entries";
    private static final String TITLE_COL = "title";
    private static final String ENTRY_COL ="entry";
    private static final String PASSWORD_COL = "password";
    private static final String LOCATION_COL = "location";
    private static final String DATE_COL = "date";
    private static final String MEDIA_COL= "mediaPath";
    private static final String LONGTITUDE_COL = "longtitude";
    private static final String LATIDUDE_COL = "latidude";

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TITLE_COL + " TEXT,"
                + ENTRY_COL + " TEXT,"
                + LOCATION_COL + " TEXT,"
                + DATE_COL + " TEXT,"
                + MEDIA_COL + " TEXT,"
                + LONGTITUDE_COL+ " REAL,"
                + LATIDUDE_COL + " REAL,"
                + PASSWORD_COL + " TEXT)";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void addEntry(Entry entry) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TITLE_COL,entry.getTitle());
        values.put(DATE_COL,entry.getDate());
        values.put(ENTRY_COL,entry.getEnrtyText());
        values.put(LOCATION_COL,entry.getLocation());
        values.put(MEDIA_COL,entry.getMediaPath());
        values.put(LONGTITUDE_COL,entry.getLongtidude());
        values.put(LATIDUDE_COL,entry.getLatidude());
        db.insert(TABLE_NAME,null,values);
        db.close();
    }

    public void updateEntry(Entry entry){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TITLE_COL,entry.getTitle());
        values.put(DATE_COL,entry.getDate());
        values.put(ENTRY_COL,entry.getEnrtyText());
        values.put(LOCATION_COL,entry.getLocation());
        values.put(MEDIA_COL,entry.getMediaPath());
        db.update(TABLE_NAME,values,""+DATE_COL+"=?",new String[]{entry.getDate()});
        db.close();
    }

    public boolean deleteEntry(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        int result =db.delete(TABLE_NAME,"id=?",new String[]{id});
        db.close();
        if(result>0){
            return true;
        }
        return false;
    }

    public void addPassword(String id,String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PASSWORD_COL,password);
        db.update(TABLE_NAME,values,""+ID_COL+"=?",new String[]{id});
        db.close();
    }


    public boolean deletePassword(String id,String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String[] selectingArgs={id};
        Log.i("dfaf", "deletePassword: "+id);
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " +ID_COL+"= ?",selectingArgs );
        if (cursor.moveToFirst()) {
            if(cursor.getString(8).equals(password)){
                values.putNull(PASSWORD_COL);
                db.update(TABLE_NAME,values,""+ID_COL+"=?",new String[]{id});
                db.close();
                return true;
            }
            db.close();
        }
        return false;
    }

    public ArrayList<Entry> getAllEntries(){
        ArrayList<Entry> entries = new ArrayList<Entry>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                entries.add(new Entry(cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(8),
                        cursor.getString(0),
                        cursor.getString(5)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return entries;
    }
    public ArrayList<Entry> getAllEntriesWitHLocation(){
        ArrayList<Entry> entries = new ArrayList<Entry>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                entries.add(new Entry(cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(8),
                        cursor.getString(0),
                        cursor.getString(5),
                        cursor.getDouble(7),
                        cursor.getDouble(6)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return entries;
    }
}
