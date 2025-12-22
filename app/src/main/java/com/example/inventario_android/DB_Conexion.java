package com.example.inventario_android;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class DB_Conexion {
    private static final String TAG = "Conexion Firebase";
    private FirebaseFirestore db;

    public static FirebaseFirestore crearConexion(){
        return FirebaseFirestore.getInstance();
    }

    public static void obtener(FirebaseFirestore db) {
        db.collection("nombres")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()) {
                            Log.d(TAG, "Colección vacía.");
                            return;
                        }

                        Log.d(TAG, "[ " + task.getResult().size() + " documentos obtenidos ]");
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, "\\_" + document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.w(TAG, "ERROR al obtener documentos.", task.getException());
                    }
                });
    }
}
