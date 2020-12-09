package com.example.itodolist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class NewTaskActivity extends AppCompatActivity {

    Button createTaskButton;
    TextInputLayout titleField;
    TasksRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        // Set views
        createTaskButton = (Button) findViewById(R.id.createTaskButton);
        titleField = (TextInputLayout)  findViewById(R.id.taskTitle);

        // Init repository
        repository = new TasksRepository(getApplicationContext());

        createTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    final String title =  titleField.getEditText().getText().toString();
                    repository.createTask(new Task(title, "", "", "", 6, 2, 0 ));
                    Toast.makeText(getApplicationContext(), "Tarea creada", Toast.LENGTH_SHORT).show();

                    finish();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Hubo un problema al crear la tarea", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        repository.close();
        super.onDestroy();

    }
}