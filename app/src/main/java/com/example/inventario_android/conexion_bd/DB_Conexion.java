package com.example.inventario_android.conexion_bd;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DB_Conexion {
    private static final String TAG = "Conexion Firebase";
    private static final String COLECCION = "nombres";

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

        db.collection(COLECCION)
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
                        return;
                    }
                });
        return datos;
    }

    public static void crearDocumento(FirebaseFirestore db, Producto producto){
        Map<String, Object> mapa = new HashMap<>();
        String id = "";
        mapa.put("nombre", producto.getNombre());
        mapa.put("descripcion", producto.getDescripcion());
        mapa.put("inventario", producto.isInventario());
        mapa.put("por_comprar", producto.isPor_comprar());

        /* ---- INSERTAR CON ID AUTOGENERADO
        db.collection(COLECCION)
                .add(mapa) // .add() crea un documento con un ID automático
                .addOnSuccessListener(documentReference -> Log.d(TAG, "Documento creado con ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w(TAG, "Error al crear documento", e));
        */

        // ----- INSERTAR CON ID PERSONALIZADO
        db.collection(COLECCION)
                .document(producto.getId())
                .set(mapa)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Documento creado: " + producto.getId());
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error al crear documento", e));
    }

    public static void actualizar(FirebaseFirestore db, Producto producto){
        DocumentReference docu = db.collection(COLECCION).document(producto.getId());
        if(producto.getId() == null || producto.getId().isEmpty()){
            Log.e(TAG, "No se ha obtenido correctamente el producto de referencia. ID vacio o nulo");
        }else{
            Map<String, Object> updates = new HashMap<>();
            updates.put("nombre", producto.getNombre());
            updates.put("descripcion", producto.getDescripcion());
            updates.put("inventario", producto.isInventario());
            updates.put("por_comprar", producto.isPor_comprar());docu.update(updates)
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Documento ID=" + producto.getId()
                            + " actualizado."))
                    .addOnFailureListener(e -> Log.w(TAG, "Error al actualizar documento", e));
        }
    }

    public static void eliminar(FirebaseFirestore db, Producto producto){
        if (producto.getId() == null || producto.getId().isEmpty()) {
            Log.e(TAG, "No se ha obtenido correctamente el producto de referencia. ID vacio o nulo");
            return;
        }

        db.collection(COLECCION).document(producto.getId()).delete()
                .addOnSuccessListener(aVoid ->
                        Log.d(TAG, "Documento " + producto.getId() + " eliminado."))
                .addOnFailureListener(e -> Log.w(TAG, "Error al eliminar documento", e));
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
        //Log.d(TAG, "Producto: " + producto.toString());
        return producto;
    }

    public static void mostrar(List<Producto> datos){
        for (Producto prod : datos) {
            Log.d(TAG, "\\_" + prod.toString());
        }
    }
}
