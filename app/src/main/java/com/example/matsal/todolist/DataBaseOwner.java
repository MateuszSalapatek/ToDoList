package com.example.matsal.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.session.PlaybackState;

public class DataBaseOwner extends SQLiteOpenHelper {


    public DataBaseOwner(Context context){
        super (context, "Note.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( "create table ToDoList (id integer primary key autoincrement," +
                                            "note text," +
                                            "plannedDate text," +
                                            "finishedDate text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addNote(String note, String date){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("note", note);
        values.put("plannedDate", date);
        db.insertOrThrow("ToDoList",null,values);
    }

    public Cursor getAllData(){
        String[] columns ={"id","note","plannedDate","finishedDate"};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("ToDoList",columns,null,null,null,null,null);
        return cursor;
    }

    public void deleteNote(int id){
        SQLiteDatabase db = getWritableDatabase();
        String[] arguments = {""+id};
        db.delete("ToDoList","id=?",arguments);
    }
}