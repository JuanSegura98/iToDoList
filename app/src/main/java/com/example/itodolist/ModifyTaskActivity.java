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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_task);
        // Inflate elements
        createTaskButton = (Button) findViewById(R.id.createTaskButton);
        titleField = (TextInputLayout)  findViewById(R.id.taskTitle);
        dueDateField = (TextInputLayout)  findViewById(R.id.taskDate);
        currentUnits = (TextInputLayout)  findViewById(R.id.taskUnits);
    }
}