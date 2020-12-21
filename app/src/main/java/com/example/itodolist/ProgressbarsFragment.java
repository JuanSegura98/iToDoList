package com.example.itodolist;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

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

    private DrawerLayout drawer;
    TaskRowAdapter customAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = new TasksRepository(getContext());
        getTasks();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_progressbars, container, false);

        fab = (FloatingActionButton) view.findViewById(R.id.floating_action_button);
        taskListView = (RecyclerView) view.findViewById(R.id.taskList);
        layout = (ConstraintLayout) view.findViewById(R.id.taskConstraintLayout);

        customAdapter = new TaskRowAdapter(getContext(),new ArrayList<Task>());
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


        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar_main);
        final MainActivity activity = (MainActivity) getActivity();

        activity.setSupportActionBar(toolbar);

        drawer = view.findViewById(R.id.drawer_layout);

        activity.toggle = new ActionBarDrawerToggle(activity, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(activity.toggle);

        activity.getSupportActionBar().setHomeButtonEnabled(true);
        activity.toggle.syncState();
        NavigationView navigationView = (NavigationView) view.findViewById(R.id.nav_view);

        navigationView.getMenu().findItem(R.id.nav_item_log_out).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                activity.onNavigationItemSelected(menuItem);
                return true;
            }
        });

        navigationView.setNavigationItemSelectedListener(activity);
        return view;
    }

    void getTasks() {

        repository.getTasks(new EventListener<List<Task>>() {
            @Override
            public void onEvent(@Nullable List<Task> value, @Nullable FirebaseFirestoreException error) {
                final List<Task> tasks = value;

                if (tasks.isEmpty()) {
                    Toast.makeText(getContext(), "Lista vacia", Toast.LENGTH_SHORT).show();
                } else {
                    setTaskListView(tasks);

                }
            }
        });


    }

    void setTaskListView(final List<Task> tasks) {
        try {
            customAdapter.setItems(tasks);
            customAdapter.setClickListener(new TaskRowAdapter.ItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    final Task task = tasks.get(position);
                    Intent intent = new Intent(getActivity(), ModifyTaskActivity.class);
                    intent.putExtra("task", task);
                    startActivity(intent);
                }
            });
            customAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onStart() {
        super.onStart();

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

