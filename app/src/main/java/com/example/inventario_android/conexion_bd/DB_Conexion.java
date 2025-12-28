package com.example.inventario_android.conexion_bd;

import android.util.Log;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.LinkedList;
import java.util.List;

public class DB_Conexion {
    private static final String TAG = "CONEXION FIREBASE";
    private static final String COLECCION = "nombres";

    public interface ListaCallback {
        void onListaCargada(List<Producto> productos);
        void onError(Exception e);
    }

    public static FirebaseFirestore crearConexion(){
        return FirebaseFirestore.getInstance();
    }
    public static void cerrarConexion(FirebaseFirestore db){
        if(db != null){
            db.terminate().addOnCompleteListener(task -> Log.d(TAG, "Conexión cerrada"));
        }
    }

    public static void getLista(FirebaseFirestore db, final ListaCallback callback) {
        db.collection(COLECCION)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Producto> items = new LinkedList<>();
                        QuerySnapshot querySnapshot = task.getResult();

                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                Producto prod = crearProductoDesdeDocumento(document);
                                if (prod != null) {
                                    items.add(prod);
                                }
                            }
                            Log.d(TAG, items.size() + " items.");
                        } else {
                            Log.d(TAG, "La colección vacia.");
                        }
                        callback.onListaCargada(items);
                    } else {
                        Log.e(TAG, "Error al cargar la lista desde la db: ", task.getException());
                        callback.onError(task.getException());
                    }
                });
    }

    private static Producto crearProductoDesdeDocumento(QueryDocumentSnapshot docu){
        try {
            return new Producto(
                    docu.getId(),
                    docu.getString("nombre"),
                    docu.getString("descripcion"),
                    docu.getBoolean("inventario"),
                    docu.getBoolean("por_comprar")
            );
        } catch (Exception e) {
            Log.e(TAG, "Error al parsear documento " + docu.getId(), e);
            return null;
        }
    }

    public static void mostrar(List<Producto> datos){
        if (datos == null || datos.isEmpty()) {
            Log.d(TAG, "No hay datos para mostrar.");
            return;
        }
        Log.d(TAG, "--- " + datos.size() + " productos ---");
        for (Producto prod : datos) {
            Log.d(TAG, "\\__ " + prod.toString());
        }
        Log.d(TAG, "---------------------------");
    }
}