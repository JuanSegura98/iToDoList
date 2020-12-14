package com.example.itodolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class TasksRepository {

    private final TasksDatabase admin;


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

    void createTask(Task task) throws Exception {
        long rowInserted;
        final SQLiteDatabase db = admin.getWritableDatabase();
        final ContentValues registro = new ContentValues();
        registro.put("name", task.name);
        registro.put("begindate", task.beginDate);
        registro.put("enddate", task.endDate);
        registro.put("measureunit", task.measureUnit);
        registro.put("totalunits", task.totalUnits);
        registro.put("currentunits", task.currentUnits);
        rowInserted = db.insert("progressbars", null, registro);
        if (rowInserted == -1)
            throw new Exception("Error al almacenar los datos");

        db.close();
    }

    void deleteTask(Task task) throws Exception {
        long rowDeleted;
        final SQLiteDatabase db = admin.getWritableDatabase();

        rowDeleted = db.delete("progressbars", "progressbar" + "=" + task.progressBar, null);
        if (rowDeleted == -1)
            throw new Exception("Error al eliminar tarea");
        db.close();
    }

    void close() {
        admin.close();
    }
}


