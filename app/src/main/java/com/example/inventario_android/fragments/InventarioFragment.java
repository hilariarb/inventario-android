package com.example.inventario_android.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
import android.app.AlertDialog;
import android.widget.EditText;


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

        binding.btnNavegarAdd.setOnClickListener(v ->
                NavHostFragment.findNavController(InventarioFragment.this)
                        .navigate(R.id.action_FirstFragment_to_AddProductoFragment));

        // Mantengo el FAB por si quieres usarlo también, pero oculto según el layout anterior
        binding.fabAddProducto.setOnClickListener(v ->
                NavHostFragment.findNavController(InventarioFragment.this)
                        .navigate(R.id.action_FirstFragment_to_AddProductoFragment));

        cargarInventario();
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = binding.recyclerViewProductos;
        productoAdapter = new ProductoAdapter();

        productoAdapter.setListener(new ProductoAdapter.OnProductoListener() {
            @Override
            public void onEditar(Producto producto) {
                mostrarDialogoEditar(producto);
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

    private void cargarInventario() {
        DB_Conexion.getLista(db, new DB_Conexion.ListaCallback() {
            @Override
            public void onListaCargada(List<Producto> productos) {
                List<Producto> aux = new LinkedList<>();
                if (productoAdapter != null) {
                    for (Producto prod : productos) {
                        if (prod.isInventario()) {
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
                Toast.makeText(getContext(), "Producto actualizado", Toast.LENGTH_SHORT).show();
                cargarInventario();
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
                cargarInventario();
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(getContext(), "Error al eliminar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDialogoEditar(Producto producto) {
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.dialog_producto, null);

        EditText etNombre = view.findViewById(R.id.etNombre);
        EditText etDescripcion = view.findViewById(R.id.etDescripcion);
        CheckBox cbInventario = view.findViewById(R.id.cbInventario);
        CheckBox cbPorComprar = view.findViewById(R.id.cbPorComprar);

        etNombre.setText(producto.getNombre());
        etDescripcion.setText(producto.getDescripcion());
        cbInventario.setChecked(producto.isInventario());
        cbPorComprar.setChecked(producto.isPor_comprar());

        new AlertDialog.Builder(getContext())
                .setTitle("Editar producto")
                .setView(view)
                .setPositiveButton("Guardar", (d, w) -> {
                    producto.setNombre(etNombre.getText().toString());
                    producto.setDescripcion(etDescripcion.getText().toString());
                    producto.setInventario(cbInventario.isChecked());
                    producto.setPor_comprar(cbPorComprar.isChecked());
                    actualizarProducto(producto);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
