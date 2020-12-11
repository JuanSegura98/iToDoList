package com.example.itodolist;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ProgressbarsFragment extends Fragment {

    public ProgressbarsFragment() {
        // Required empty public constructor
    }

    private FloatingActionButton fab;
    TasksRepository repository;
    RecyclerView taskListView;
    ConstraintLayout layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = new TasksRepository(getContext());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_progressbars, container, false);

        fab = (FloatingActionButton) view.findViewById(R.id.floating_action_button);
        taskListView = (RecyclerView) view.findViewById(R.id.taskList);
        layout = (ConstraintLayout) view.findViewById(R.id.taskConstraintLayout);
        List<Task> tasks = getTasks();
        TaskRowAdapter customAdapter = new TaskRowAdapter(getContext(), tasks);
        taskListView.setAdapter(customAdapter);
        taskListView.setLayoutManager(new LinearLayoutManager(getContext()));
        enableSwipeToDeleteAndUndo();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewTaskActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    List<Task> getTasks() {
        try {
            List<Task> tasks = repository.getTasks();
            if (tasks.isEmpty()) {
                Toast.makeText(getContext(), "Lista vacia", Toast.LENGTH_SHORT).show();
            }
            return tasks;
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error al leer lista de tareas", Toast.LENGTH_SHORT).show();
            return new ArrayList<Task>();
        }
    }

    public void printValues() {
        List<Task> tasks = getTasks();
        for (Task task : tasks) {
            System.out.println(task);
            Toast.makeText(getContext(), task.name + " - " + task.progressBar, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        List<Task> tasks = getTasks();
        TaskRowAdapter customAdapter = new TaskRowAdapter(getContext(), tasks);
        taskListView.setAdapter(customAdapter);
    }

    @Override
    public void onDestroy() {
        repository.close();
        super.onDestroy();
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getContext()) {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
                final TaskRowAdapter mAdapter = (TaskRowAdapter) taskListView.getAdapter();

                final int position = viewHolder.getAdapterPosition();
                final Task item = mAdapter.mData.get(position);

                mAdapter.removeItem(position);
                try {
                    repository.deleteTask(item);
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Error al eliminar tarea", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                Snackbar snackbar = Snackbar
                        .make((layout), "Task was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            repository.createTask(item);
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "Error al recuperar tarea", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        mAdapter.restoreItem(item, position);
                        taskListView.scrollToPosition(position);
                    }
                });


                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(taskListView);
    }

}

