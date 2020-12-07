package com.example.itodolist;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;

public class Progressbars extends Fragment {

    public Progressbars() {
        // Required empty public constructor
    }

    private FloatingActionButton fab;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //insertValues();
        int progressbar = -1;
        String name = "Error";
        String begindate = "0000-00-00";
        String enddate = "0000-00-00";
        String measureunit = "None";
        int totalunits = -1;
        int currentunits = -1;

        View view = inflater.inflate(R.layout.fragment_progressbars, container, false);

        fab = (FloatingActionButton) view.findViewById(R.id.floating_action_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NavHostFragment.findNavController(Progressbars.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);

            }
        });

        int numberOfEntries = 5;
        TextView[] Entry = new TextView[numberOfEntries];
        ImageView[] Image = new ImageView[numberOfEntries];
        for(int i = 0; i < numberOfEntries; i++){
            Entry[i] = (TextView) view.findViewById(getResources().getIdentifier("entry" + (i+1), "id", "com.example.itodolist"));
            Image[i] = (ImageView) view.findViewById(getResources().getIdentifier("completedbar" + (i+1), "id", "com.example.itodolist"));
        }

        TasksDatabase admin = new TasksDatabase(getContext(), "administracion", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();
        int iteration = 0;
        Cursor fila = db.rawQuery("select progressbar, name, begindate, enddate, measureunit, totalunits, currentunits from progressbars", null);
        if (fila.moveToFirst()) {
            do {
                try {
                    progressbar = fila.getInt(0);
                    name = fila.getString(1);
                    begindate = fila.getString(2);
                    enddate = fila.getString(3);
                    measureunit = fila.getString(4);
                    totalunits = fila.getInt(5);
                    currentunits = fila.getInt(6);

                    int percentageCompleted = Math.round((float)currentunits*100/totalunits);

                    Entry[iteration].setText(name);
                    Image[iteration].getLayoutParams().width = convertDpToPx(getContext(), percentageCompleted);
                    iteration++;
                    } catch (Exception e) {
                     Toast.makeText(getContext(), "Error parsing the row", Toast.LENGTH_SHORT).show();
                }
            } while(fila.moveToNext());
        } else
            Toast.makeText(getContext(), "Lista vacia", Toast.LENGTH_SHORT).show();
        db.close();


        return view;
    }

    public int convertDpToPx(Context context, float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
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
