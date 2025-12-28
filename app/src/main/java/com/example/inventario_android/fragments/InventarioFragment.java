package com.example.inventario_android.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventario_android.conexion_bd.ProductoAdapter;
import com.example.inventario_android.R;
import com.example.inventario_android.conexion_bd.DB_Conexion;
import com.example.inventario_android.conexion_bd.Producto;
import com.example.inventario_android.databinding.FragmentInventarioBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.LinkedList;
import java.util.List;

public class InventarioFragment extends Fragment {

    private FragmentInventarioBinding binding;
    private static final String TAG = "INVENTARIO";
    private FirebaseFirestore db;
    private ProductoAdapter productoAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInventarioBinding.inflate(inflater, container, false);
        db = DB_Conexion.crearConexion();
        setupRecyclerView();

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonInventario.setOnClickListener(v ->
                NavHostFragment.findNavController(InventarioFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment));

        // PRUEBAS
        //cargarTodos();
        //Producto nuevo_A = new Producto("03lTU7TVyXX8sf6UohoC", "nombre_A", "descrip_A", true, true);
        //crearProducto(nuevo_A);
        //actualizarProducto(nuevo_A);
        //eliminarProducto(nuevo_A.getId());


        cargarInventario();
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = binding.recyclerViewProductos;
        productoAdapter = new ProductoAdapter();
        recyclerView.setAdapter(productoAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void cargarTodos() {
        DB_Conexion.getLista(db, new DB_Conexion.ListaCallback() {
            @Override
            public void onListaCargada(List<Producto> productos) {
                Log.d(TAG, "Recibidos " + productos.size() + " items");
                if (productoAdapter != null) {
                    productoAdapter.setProductos(productos);
                }
                DB_Conexion.mostrar(productos);
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Error al cargar la lista desde la db: ", e);
                Toast.makeText(getContext(), "Error al cargar los datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarInventario() {
        List<Producto> aux = new LinkedList<>();
        DB_Conexion.getLista(db, new DB_Conexion.ListaCallback() {
            @Override
            public void onListaCargada(List<Producto> productos) {
                if (productoAdapter != null) {
                    for (Producto prod : productos) {
                        if (prod.isInventario()) {
                            aux.add(prod);
                        }
                    }
                    productoAdapter.setProductos(aux);
                }
                DB_Conexion.mostrar(aux);
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Error al cargar la lista desde la db: ", e);
                Toast.makeText(getContext(), "Error al cargar los datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void crearProducto(Producto nuevoProducto){
        DB_Conexion.crearProducto(db, nuevoProducto, new DB_Conexion.DocumentCallback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Producto creado");
                Toast.makeText(getContext(), "Producto creado", Toast.LENGTH_SHORT).show();

                cargarInventario();
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Error al crear el producto", e);
                Toast.makeText(getContext(), "Error al crear el producto", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actualizarProducto(Producto productoAEditar){
        DB_Conexion.editarProducto(db, productoAEditar, new DB_Conexion.DocumentCallback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Producto actualizado");
                Toast.makeText(getContext(), "Producto actualizado", Toast.LENGTH_SHORT).show();

                cargarInventario();
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Error al actualizar", e);
                Toast.makeText(getContext(), "Error al actualizar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void eliminarProducto(String idDelProductoAEliminar){
        DB_Conexion.eliminarProducto(db, idDelProductoAEliminar, new DB_Conexion.DocumentCallback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Producto eliminado");
                Toast.makeText(getContext(), "Producto eliminado", Toast.LENGTH_SHORT).show();

                cargarInventario();
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Error al eliminar", e);
                Toast.makeText(getContext(), "Error al eliminar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}