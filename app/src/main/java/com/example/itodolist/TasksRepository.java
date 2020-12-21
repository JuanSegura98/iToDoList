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

public class TasksRepository {

    private final TasksDatabase admin;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public TasksRepository(Context context) {

        admin = new TasksDatabase(context, "administracion", null, 1);
    }

   /* List<Task> getTasks() {


        List<Task> tasks = new ArrayList<Task>();
        SQLiteDatabase db = admin.getWritableDatabase();
        Cursor result = db.rawQuery("select progressbar, name, begindate, enddate, measureunit, totalunits, currentunits from progressbars", null);
        if (result.moveToFirst()) {
            do {
                final int progressbar = result.getInt(0);
                final String name = result.getString(1);
                final String begindate = result.getString(2);
                final String enddate = result.getString(3);
                final String measureunit = result.getString(4);
                final int totalunits = result.getInt(5);
                final int currentunits = result.getInt(6);
                tasks.add(new Task(name, begindate, enddate, measureunit, totalunits, currentunits, progressbar, null));
            } while (result.moveToNext());
        }
        db.close();
        return tasks;
    }
*/

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

                    tasks.add(new Task(name, begindate, enddate, measureunit, totalunits.intValue(), currentunits.intValue(), progressbar, document.getId(), ""));
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
        return;
      /*  long rowInserted;
        final SQLiteDatabase db = admin.getWritableDatabase();
        final ContentValues registro = new ContentValues();
        registro.put("name", task.name);
        registro.put("begindate", task.beginDate);
        registro.put("enddate", task.endDate);
        registro.put("measureunit", task.measureUnit);
        registro.put("totalunits", task.totalUnits);
        registro.put("currentunits", task.currentUnits);
        rowInserted = db.insert("progressbars", null, registro);
        if (rowInserted == -1)
            throw new Exception("Error al almacenar los datos");

        db.close();*/
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
      /*  long rowInserted;
        final SQLiteDatabase db = admin.getWritableDatabase();
        final ContentValues registro = new ContentValues();
        registro.put("name", task.name);
        registro.put("enddate", task.endDate);
        registro.put("currentunits", task.currentUnits);
        rowInserted = db.update("progressbars", registro, "progressbar" + "=" + task.progressBar, null);
        if (rowInserted == -1)
            throw new Exception("Error al almacenar los datos");

        db.close();*/
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
        /*long rowDeleted;
        final SQLiteDatabase db = admin.getWritableDatabase();

        rowDeleted = db.delete("progressbars", "progressbar" + "=" + task.progressBar, null);
        if (rowDeleted == -1)
            throw new Exception("Error al eliminar tarea");
        db.close();*/
    }

    void close() {
        admin.close();
    }
}


