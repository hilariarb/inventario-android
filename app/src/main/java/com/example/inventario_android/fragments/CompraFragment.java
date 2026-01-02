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
import com.example.inventario_android.databinding.FragmentCompraBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.LinkedList;
import java.util.List;

public class CompraFragment extends Fragment {

    private FragmentCompraBinding binding;
    private static final String TAG = "POR COMPRAR";
    private FirebaseFirestore db;
    private ProductoAdapter productoAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCompraBinding.inflate(inflater, container, false);
        db = DB_Conexion.crearConexion();
        setupRecyclerView();

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonComprar.setOnClickListener(v ->
                NavHostFragment.findNavController(CompraFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment));

        cargarCompra();
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = binding.recyclerViewProductos;
        productoAdapter = new ProductoAdapter();
        
        productoAdapter.setListener(new ProductoAdapter.OnProductoListener() {
            @Override
            public void onEditar(Producto producto) {
                // Opcional: implementar edición desde aquí también
            }

            @Override
            public void onEliminar(Producto producto) {
                eliminarProducto(producto.getId());
            }

            @Override
            public void onCambiarEstado(Producto producto) {
                actualizarProducto(producto);
            }
        });

        recyclerView.setAdapter(productoAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void cargarCompra() {
        DB_Conexion.getLista(db, new DB_Conexion.ListaCallback() {
            @Override
            public void onListaCargada(List<Producto> productos) {
                List<Producto> aux = new LinkedList<>();
                if (productoAdapter != null) {
                    for (Producto prod : productos) {
                        if (prod.isPor_comprar()) {
                            aux.add(prod);
                        }
                    }
                    productoAdapter.setProductos(aux);
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Error al cargar la lista desde la db: ", e);
                Toast.makeText(getContext(), "Error al cargar los datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actualizarProducto(Producto productoAEditar){
        DB_Conexion.editarProducto(db, productoAEditar, new DB_Conexion.DocumentCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(getContext(), "Producto movido al Inventario", Toast.LENGTH_SHORT).show();
                cargarCompra();
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(getContext(), "Error al actualizar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void eliminarProducto(String idDelProductoAEliminar){
        DB_Conexion.eliminarProducto(db, idDelProductoAEliminar, new DB_Conexion.DocumentCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(getContext(), "Producto eliminado", Toast.LENGTH_SHORT).show();
                cargarCompra();
            }

            @Override
            public void onError(Exception e) {
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
