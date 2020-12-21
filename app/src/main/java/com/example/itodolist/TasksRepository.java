package com.example.itodolist;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;



// Usando esta clase se guardan las tareas en FIREBASE
// Para guardar en la base de datos local usar TasksDatabaseRepository de forma similar a esta
public class TasksRepository {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public TasksRepository(Context context) {}

    void getTasks(final EventListener<List<Task>> listener) {

        db.collection("tasks").whereEqualTo("userId", FirebaseAuth.getInstance().getCurrentUser().getUid())
        .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("FIREBASE", "Listen failed.", e);
                    return;
                }

                final List<Task> tasks = new ArrayList<Task>();
                for (QueryDocumentSnapshot document : value) {
                    final Map<String, Object> data = document.getData();
                    final int progressbar = 0;
                    final String name = (String) data.get("name");
                    final String begindate = (String) data.get("begindate");
                    final String enddate = (String) data.get("enddate");
                    final String measureunit = (String) data.get("measureunit");
                    final Long totalunits = (Long) data.get("totalunits");
                    final Long currentunits = (Long) data.get("currentunit");
                    final String notes = (String) data.get("notes");

                    tasks.add(new Task(name, begindate, enddate, measureunit, totalunits.intValue(), currentunits.intValue(), progressbar, document.getId(), notes));
                    Log.d("FIREBASE", document.getId() + " => " + document.getData());

                    listener.onEvent(tasks, null);
                }

            }
        });
        
    }

    void createTask(Task task) throws Exception {

        Map<String, Object> user = new HashMap<>();
        user.put("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());
        user.put("name", task.name);
        user.put("begindate", task.beginDate);
        user.put("enddate", task.endDate);
        user.put("totalunits", task.totalUnits);
        user.put("currentunit", task.currentUnits);
        user.put("measureunit", task.measureUnit);
        user.put("notes", task.notes);

// Add a new document with a generated ID
        db.collection("tasks")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("FIREBASE", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("FIREBASE", "Error adding document", e);
                    }
                });


    }

    void modifyTask(final Task task) throws Exception {
        Map<String, Object> user = new HashMap<>();
        user.put("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());
        user.put("name", task.name);
        user.put("begindate", task.beginDate);
        user.put("enddate", task.endDate);
        user.put("totalunits", task.totalUnits);
        user.put("currentunit", task.currentUnits);
        user.put("measureunit", task.measureUnit);
        user.put("notes", task.notes);

// Add a new document with a generated ID
        db.collection("tasks").document(task.id)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("FIREBASE", "DocumentSnapshot updated with ID: " + task.id);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("FIREBASE", "Error adding document", e);
                    }
                });
        return;

    }

    void deleteTask(final Task task) throws Exception {
        db.collection("tasks").document(task.id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("FIREBASE", "DocumentSnapshot updated with ID: " + task.id);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("FIREBASE", "Error adding document", e);
                    }
                });

    }


}


