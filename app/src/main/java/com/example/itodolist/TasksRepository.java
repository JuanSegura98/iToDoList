package com.example.itodolist;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TasksRepository {

    final TasksDatabase admin;


    public TasksRepository(Context context) {

        admin = new TasksDatabase(context, "administracion", null, 1);
    }

    List<Task> getTasks() {
        List<Task> tasks = new ArrayList<Task>();
        SQLiteDatabase db = admin.getWritableDatabase();
        Cursor result = db.rawQuery("select progressbar, name, begindate, enddate, measureunit, totalunits, currentunits from progressbars", null);
        if (result.moveToFirst()) {
            do {
                final int progressbar = result.getInt(0);
                final String name = result.getString(1);
                final String begindate = result.getString(2);
                final String enddate = result.getString(3);
                final String measureunit = result.getString(4);
                final int totalunits = result.getInt(5);
                final int currentunits = result.getInt(6);
                tasks.add(new Task(name, begindate, enddate, measureunit, totalunits, currentunits, progressbar));
            } while (result.moveToNext());
        }
        db.close();
        return tasks;
    }

    void createTask(Task task) {
        SQLiteDatabase db = admin.getWritableDatabase();
        final String values = String.format("(\"%s\", \"%s\", \"%s\", \"%s\", %s, %s);", task.name, task.beginDate, task.endDate, task.measureUnit, String.valueOf(task.totalUnits), String.valueOf(task.currentUnits));
        Cursor result = db.rawQuery("insert into progressbars (name, begindate, enddate, measureunit, totalunits, currentunits) \n values " + values, null);
    }

    void close() {
        admin.close();
    }
}
