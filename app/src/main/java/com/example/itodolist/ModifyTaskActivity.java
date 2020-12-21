package com.example.itodolist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ModifyTaskActivity extends AppCompatActivity {
    Button createTaskButton;
    TextInputLayout titleField;
    TextInputLayout currentUnits;
    TextInputLayout dueDateField;
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

        titleField.getEditText().setText(task.name);
        currentUnits.getEditText().setText(String.valueOf(task.totalUnits) + " " + String.valueOf(task.measureUnit));
        dueDateField.getEditText().setText(task.endDate);

        createTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final String title = titleField.getEditText().getText().toString();
                final String date = dueDateField.getEditText().getText().toString();
                final String units = currentUnits.getEditText().getText().toString();
                String[] parts = units.split(" ");

                int amount = Integer.parseInt(parts[0]);

                Date currentTime = Calendar.getInstance().getTime();
                String fDate = new SimpleDateFormat("yyyy-MM-dd").format(currentTime);
                try {
//                    repository.modifyTask(new Task(title, fDate, date, parts[1], task.totalUnits , amount, 0, task.id));
                    repository.modifyTask(new Task(title, fDate, date, parts[1], task.totalUnits , amount, 0, task.id));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(ModifyTaskActivity.this, "Tarea creada", Toast.LENGTH_SHORT).show();

                finish();

            }
        });
    }
}