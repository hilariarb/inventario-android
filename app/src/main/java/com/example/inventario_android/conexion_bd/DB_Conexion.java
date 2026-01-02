package com.example.inventario_android.conexion_bd;

import android.util.Log;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DB_Conexion {
    private static final String TAG = "CONEXION FIREBASE";
    private static final String COLECCION = "nombres";

    public interface ListaCallback {
        void onListaCargada(List<Producto> productos);
        void onError(Exception e);
    }

    public interface DocumentCallback {
        void onSuccess();
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

    // ------------------------------------------- CRUD --------------------------------------------------------

    public static void crearProducto(FirebaseFirestore db, Producto producto, final DocumentCallback callback) {
        Map<String, Object> data = new HashMap<>();
        data.put("nombre", producto.getNombre());
        data.put("descripcion", producto.getDescripcion());
        data.put("inventario", producto.isInventario());
        data.put("por_comprar", producto.isPor_comprar());
        data.put("puntuacion", producto.getPuntuacion());
        data.put("comentario", producto.getComentario());
        data.put("resenas", producto.getResenas());

        db.collection(COLECCION)
                .add(data)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Producto creado con ID: " + documentReference.getId());
                    callback.onSuccess();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error al crear producto", e);
                    callback.onError(e);
                });
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

    public static void editarProducto(FirebaseFirestore db, Producto producto, final DocumentCallback callback) {
        if (producto.getId() == null || producto.getId().isEmpty()) {
            Log.e(TAG, "No se puede editar un producto sin ID.");
            callback.onError(new IllegalArgumentException("El ID del producto no puede ser nulo o vacío."));
            return;
        }

        DocumentReference docRef = db.collection(COLECCION).document(producto.getId());

        Map<String, Object> updates = new HashMap<>();
        updates.put("nombre", producto.getNombre());
        updates.put("descripcion", producto.getDescripcion());
        updates.put("inventario", producto.isInventario());
        updates.put("por_comprar", producto.isPor_comprar());
        updates.put("puntuacion", producto.getPuntuacion());
        updates.put("comentario", producto.getComentario());
        updates.put("resenas", producto.getResenas());

        docRef.update(updates)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Producto " + producto.getId() + " actualizado correctamente.");
                    callback.onSuccess();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error al actualizar producto " + producto.getId(), e);
                    callback.onError(e);
                });
    }

    public static void eliminarProducto(FirebaseFirestore db, String productoId, final DocumentCallback callback) {
        if (productoId == null || productoId.isEmpty()) {
            Log.e(TAG, "No se puede eliminar un producto sin ID.");
            callback.onError(new IllegalArgumentException("El ID del producto no puede ser nulo o vacío."));
            return;
        }

        db.collection(COLECCION).document(productoId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Producto " + productoId + " eliminado correctamente.");
                    callback.onSuccess();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error al eliminar producto " + productoId, e);
                    callback.onError(e);
                });
    }

    // ------------------------------------------- METODOS AUXILIARES -------------------------------------------

    private static Producto crearProductoDesdeDocumento(QueryDocumentSnapshot docu){
        try {
            Producto p = new Producto(
                    docu.getId(),
                    docu.getString("nombre"),
                    docu.getString("descripcion"),
                    docu.getBoolean("inventario") != null ? docu.getBoolean("inventario") : false,
                    docu.getBoolean("por_comprar") != null ? docu.getBoolean("por_comprar") : false
            );

            if (docu.get("puntuacion") != null) {
                p.setPuntuacion(docu.getDouble("puntuacion").floatValue());
            }
            if (docu.getString("comentario") != null) {
                p.setComentario(docu.getString("comentario"));
            }

            // Cargar historial de reseñas
            List<Map<String, Object>> resMaps = (List<Map<String, Object>>) docu.get("resenas");
            if (resMaps != null) {
                List<Producto.Resena> resenas = new ArrayList<>();
                for (Map<String, Object> map : resMaps) {
                    String texto = (String) map.get("texto");
                    float punt = 0;
                    if (map.get("puntuacion") != null) {
                        punt = ((Number) map.get("puntuacion")).floatValue();
                    }
                    long fecha = 0;
                    if (map.get("fecha") != null) {
                        fecha = ((Number) map.get("fecha")).longValue();
                    }
                    resenas.add(new Producto.Resena(texto, punt, fecha));
                }
                p.setResenas(resenas);
            }

            return p;
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
