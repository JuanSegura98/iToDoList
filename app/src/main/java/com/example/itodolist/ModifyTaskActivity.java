package com.example.itodolist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ModifyTaskActivity extends AppCompatActivity {
    Button createTaskButton;
    TextInputLayout titleField;
    TextInputLayout currentUnits;
    TextInputLayout totalUnits;
    TextInputLayout measureUnits;
    TextInputLayout dueDateField;
    TextInputLayout notesField;
    TasksRepository repository;

    Task task;

    public ModifyTaskActivity() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_task);

        Bundle extras = getIntent().getExtras();
        repository = new TasksRepository(this);

        if (extras != null) {
            task = extras.getParcelable("task");
            // and get whatever type user account id is
        }
        // Inflate elements
        createTaskButton = (Button) findViewById(R.id.createTaskButton);

        titleField = (TextInputLayout) findViewById(R.id.taskTitle);
        dueDateField = (TextInputLayout) findViewById(R.id.taskDate);
        currentUnits = (TextInputLayout) findViewById(R.id.numberOfUnits);
        totalUnits = (TextInputLayout) findViewById(R.id.numberOfTotalUnits);
        measureUnits = (TextInputLayout) findViewById(R.id.measureUnits);
        notesField = (TextInputLayout) findViewById(R.id.taskNotes);

        titleField.getEditText().setText(task.name);
        currentUnits.getEditText().setText(String.valueOf(task.currentUnits));
        totalUnits.getEditText().setText(String.valueOf(task.totalUnits));
        measureUnits.getEditText().setText(String.valueOf(task.measureUnit));
        dueDateField.getEditText().setText(task.endDate);
        notesField.getEditText().setText(task.notes);

        dueDateField.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        createTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final String title = titleField.getEditText().getText().toString();
                final String date = dueDateField.getEditText().getText().toString();
                final String mUnits = measureUnits.getEditText().getText().toString();
                final String notes = notesField.getEditText().getText().toString();

                int cUnits = 0;
                int tUnits = 0;

                try {
                    cUnits = Integer.parseInt(currentUnits.getEditText().getText().toString());
                    tUnits = Integer.parseInt(totalUnits.getEditText().getText().toString());
                } catch(Exception e){
                    Toast.makeText(ModifyTaskActivity.this, "Por favor, introduce las unidades", Toast.LENGTH_SHORT).show();
                }

                try {
//                    repository.modifyTask(new Task(title, fDate, date, parts[1], task.totalUnits , amount, 0, task.id));
                    repository.modifyTask(new Task(title, task.beginDate, date, mUnits, tUnits , cUnits, 0, task.id, notes));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(ModifyTaskActivity.this, "Tarea creada", Toast.LENGTH_SHORT).show();

                finish();

            }
        });
    }


    private void showDatePickerDialog() {
        DatePickerFragment newFragment =  DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener(

        ) {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String date = String.format("%04d", year)  + "-" + String.format("%02d", month + 1) + "-" +  String.format("%02d", day);
                dueDateField.getEditText().setText(date);
            }
        });
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


}