package com.example.itodolist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NewTaskActivity extends AppCompatActivity {

    Button createTaskButton;
    TextInputLayout titleField;
    TextInputLayout dueDateField;
    TextInputLayout unitsField;
    TextInputLayout measureUnitsField;
    TasksRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        // Set views
        createTaskButton = (Button) findViewById(R.id.createTaskButton);
        titleField = (TextInputLayout) findViewById(R.id.taskTitle);
        dueDateField = (TextInputLayout) findViewById(R.id.taskDate);
        unitsField = (TextInputLayout) findViewById(R.id.taskUnits);
        measureUnitsField = (TextInputLayout) findViewById(R.id.taskMeasureUnits);

        // Init repository
        repository = new TasksRepository(getApplicationContext());

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
                final String units = unitsField.getEditText().getText().toString();
                final String measureUnits = measureUnitsField.getEditText().getText().toString();

                int amount;
                try {
                    amount =  Integer.parseInt(units);
                } catch (Exception e) {
                    amount = 0;
                    e.printStackTrace();
                }


                Date currentTime = Calendar.getInstance().getTime();
                String fDate = new SimpleDateFormat("yyyy-MM-dd").format(currentTime);
                try {
                    repository.createTask(new Task(title, fDate, date, measureUnits, amount, 0, 0, "", ""));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(NewTaskActivity.this, "Tarea creada", Toast.LENGTH_SHORT).show();
                NewTaskActivity.this.finish();

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

    @Override
    protected void onDestroy() {
        repository.close();
        super.onDestroy();

    }
}