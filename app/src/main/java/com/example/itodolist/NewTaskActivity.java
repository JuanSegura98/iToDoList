package com.example.itodolist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    TasksRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        // Set views
        createTaskButton = (Button) findViewById(R.id.createTaskButton);
        titleField = (TextInputLayout)  findViewById(R.id.taskTitle);
        dueDateField = (TextInputLayout)  findViewById(R.id.taskDate);
        unitsField = (TextInputLayout)  findViewById(R.id.taskUnits);

        // Init repository
        repository = new TasksRepository(getApplicationContext());

        createTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    final String title =  titleField.getEditText().getText().toString();
                    final String date =  dueDateField.getEditText().getText().toString();
                    final String units =  unitsField.getEditText().getText().toString();
                    String[] parts = units.split(" ");

                    int amount = Integer.parseInt(parts[0]);

                    Date currentTime = Calendar.getInstance().getTime();
                    String fDate = new SimpleDateFormat("YYYY-MM-DD").format(currentTime);

                    repository.createTask(new Task(title, fDate, date, parts[1], amount, 0, 0 ));
                    Toast.makeText(NewTaskActivity.this, "Tarea creada", Toast.LENGTH_SHORT).show();

                    finish();
                } catch (Exception e) {
                    Toast.makeText(NewTaskActivity.this, "Hubo un problema al crear la tarea", Toast.LENGTH_SHORT).show();
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