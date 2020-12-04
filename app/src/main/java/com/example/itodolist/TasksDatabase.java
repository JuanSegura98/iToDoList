package com.example.itodolist;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class TasksDatabase extends SQLiteOpenHelper {
    public TasksDatabase(Context context, String nombre,
                    SQLiteDatabase.CursorFactory factory,
                    int version) {
        super(context, nombre, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL("create table progressbars(progressbar integer primary key, name text, begindate text, enddate text, measureunit text, totalunits integer, currentunits integer)");
            Log.d("Create database error", "Database created");
        }catch (SQLException e) {
            Log.d("Create database error", "Has mamado");
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int versAnte, int versNue) {
        db.execSQL("drop table if exists progressbars");
        db.execSQL("create table progressbars(progressbar integer primary key, name text, begindate text, enddate text, measureunit text, totalunits integer, currentunits integer)");
    }
}