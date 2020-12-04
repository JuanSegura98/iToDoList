package com.example.itodolist;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class Progressbars extends Fragment {

    public Progressbars() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //insertValues();

        String value = printValues();
        return inflater.inflate(R.layout.fragment_progressbars, container, false);
    }

    public void insertValues() {
        long rowInserted;
        TasksDatabase admin = new TasksDatabase(getContext(), "administracion", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();
        ContentValues registro = new ContentValues();
        registro.put("progressbar", 1);
        registro.put("name", "Materiales");
        registro.put("begindate", "2020-12-04");
        registro.put("enddate", "2020-12-25");
        registro.put("measureunit", "Temas");
        registro.put("totalunits", 20);
        registro.put("currentunits", 10);
        rowInserted = db.insert("progressbars", null, registro);
        if(rowInserted == -1)
            Toast.makeText(getContext(), "Problema al almacenar los datos", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getContext(), "Datos almacenados", Toast.LENGTH_SHORT).show();
        db.close();
    }
    public String printValues(){
        int progressbar;
        String thisname = "No name";
        String initdate;
        String enddate;
        String Temas;
        int totalunits;
        int currentunits;
        TasksDatabase admin = new TasksDatabase(getContext(), "administracion", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();
        Cursor fila = db.rawQuery("select progressbar, name, begindate, enddate, measureunit, totalunits, currentunits from progressbars where progressbar = 1", null);
        if (fila.moveToFirst()) {
            try {
                progressbar = fila.getInt(0);
                Log.d("ThisValues", Integer.toString(progressbar));
                Toast.makeText(getContext(), Integer.toString(progressbar), Toast.LENGTH_SHORT).show();
                thisname =  fila.getString(1);
                Toast.makeText(getContext(), thisname, Toast.LENGTH_SHORT).show();
                initdate = fila.getString(2);
                Toast.makeText(getContext(), initdate, Toast.LENGTH_SHORT).show();
                enddate = fila.getString(3);
                Toast.makeText(getContext(), enddate, Toast.LENGTH_SHORT).show();
                Temas = fila.getString(4);
                Toast.makeText(getContext(), Temas, Toast.LENGTH_SHORT).show();
                totalunits= fila.getInt(5);
                Toast.makeText(getContext(), totalunits, Toast.LENGTH_SHORT).show();
                currentunits= fila.getInt(6);
                Toast.makeText(getContext(), currentunits, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.d("ThisValues", "Has mamado");
            }
        } else Toast.makeText(getContext(), "No existe el cursor", Toast.LENGTH_SHORT).show();
        db.close();
        return thisname;
    }
}
