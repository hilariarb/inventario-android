package com.example.inventario_android.conexion_bd;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.LinkedList;
import java.util.List;

public class DB_Conexion {
    private static final String TAG = "Conexion Firebase";

    public static FirebaseFirestore crearConexion(){
        return FirebaseFirestore.getInstance();
    }
    public static void cerrarConexion(FirebaseFirestore db){
        if(db==null){
            Log.d(TAG,"No se ha creado la conexión.");
        }else{
            db.terminate();
            Log.d(TAG,"Conexión cerrada.");
        }
    }

    public static List<Producto> obtener(FirebaseFirestore db) {
        LinkedList<Producto> datos = new LinkedList<>();

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
                            Producto prod = crearProducto(document);
                            if(prod!=null){
                                datos.add(prod);
                            }
                        }

                    } else {
                        Log.w(TAG, "ERROR al obtener documentos.", task.getException());
                    }
                });
        return datos;
    }

    private static Producto crearProducto(QueryDocumentSnapshot docu){
        Producto producto = null;
        Log.d(TAG, "Documento: " + docu.getId() + ": " + docu.getData());
        if(!docu.getData().isEmpty()){
            producto = new Producto( docu.getId(),
                    docu.getData().get("nombre").toString(),
                    docu.getData().get("descripcion").toString(),
                    Boolean.parseBoolean(docu.getData().get("inventario").toString()),
                    Boolean.parseBoolean(docu.getData().get("por_comprar").toString()));

        }
        Log.d(TAG, "Producto: " + producto.toString());
        return producto;
    }

    public static void mostrar(List<Producto> datos){
        for (Producto prod : datos) {
            Log.d(TAG, "\\_" + prod.toString());
        }
    }
}
