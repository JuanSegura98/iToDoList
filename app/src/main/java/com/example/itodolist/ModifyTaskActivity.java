package com.example.itodolist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

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
        currentUnits.getEditText().setText(String.valueOf(task.currentUnits));
        dueDateField.getEditText().setText(task.endDate);
    }
}