package com.example.inventario_android.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.LinkedList;
import java.util.List;

public class CompraFragment extends Fragment {

    private FragmentCompraBinding binding;
    private static final String TAG = "POR COMPRAR";
    private FirebaseFirestore db;
    private ProductoAdapter compraAdapter;
    private ProductoAdapter favoritosAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCompraBinding.inflate(inflater, container, false);
        db = DB_Conexion.crearConexion();
        setupRecyclerViews();

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonComprar.setOnClickListener(v ->
                NavHostFragment.findNavController(CompraFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment));

        binding.btnNavegarAddCompra.setOnClickListener(v ->
                NavHostFragment.findNavController(CompraFragment.this)
                        .navigate(R.id.action_SecondFragment_to_AddProductoFragment));

        cargarDatos();
    }

    private void setupRecyclerViews() {
        compraAdapter = new ProductoAdapter();
        binding.recyclerViewProductos.setAdapter(compraAdapter);
        binding.recyclerViewProductos.setLayoutManager(new LinearLayoutManager(getContext()));

        favoritosAdapter = new ProductoAdapter();
        binding.recyclerViewFavoritosCompra.setAdapter(favoritosAdapter);
        binding.recyclerViewFavoritosCompra.setLayoutManager(new LinearLayoutManager(getContext()));

        ProductoAdapter.OnProductoListener commonListener = new ProductoAdapter.OnProductoListener() {
            @Override
            public void onEditar(Producto producto) {}

            @Override
            public void onEliminar(Producto producto) { 
                new AlertDialog.Builder(getContext())
                    .setTitle(R.string.msg_confirm_delete_title)
                    .setMessage(R.string.msg_confirm_delete)
                    .setPositiveButton(R.string.msg_product_deleted, (d, w) -> eliminarProducto(producto.getId()))
                    .setNegativeButton(R.string.btn_cancel, null)
                    .show();
            }

            @Override
            public void onCambiarEstado(Producto producto) { actualizarProducto(producto); }

            @Override
            public void onValorar(Producto producto) { mostrarDialogoValorar(producto); }

            @Override
            public void onFavorito(Producto producto) { 
                actualizarProducto(producto);
                int mensajeId = producto.isFavorito() ? R.string.added_favorites : R.string.removed_favorites;
                Snackbar.make(binding.getRoot(), mensajeId, Snackbar.LENGTH_SHORT).show();
            }
        };

        compraAdapter.setListener(commonListener);
        favoritosAdapter.setListener(commonListener);
    }

    private void cargarDatos() {
        DB_Conexion.getLista(db, new DB_Conexion.ListaCallback() {
            @Override
            public void onListaCargada(List<Producto> productos) {
                List<Producto> compra = new LinkedList<>();
                List<Producto> favoritos = new LinkedList<>();

                for (Producto prod : productos) {
                    if (prod.isPor_comprar()) {
                        if (prod.isFavorito()) favoritos.add(prod);
                        else compra.add(prod);
                    }
                }
                compraAdapter.setProductos(compra);
                favoritosAdapter.setProductos(favoritos);
            }

            @Override
            public void onError(Exception e) {
                mostrarErrorDialog(getString(R.string.msg_error_load), e.getMessage());
            }
        });
    }

    private void actualizarProducto(Producto productoAEditar){
        DB_Conexion.editarProducto(db, productoAEditar, new DB_Conexion.DocumentCallback() {
            @Override
            public void onSuccess() {
                cargarDatos();
                Toast.makeText(getContext(), R.string.msg_product_updated, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Exception e) {
                mostrarErrorDialog(getString(R.string.msg_error_update), e.getMessage());
            }
        });
    }

    private void eliminarProducto(String idDelProductoAEliminar){
        DB_Conexion.eliminarProducto(db, idDelProductoAEliminar, new DB_Conexion.DocumentCallback() {
            @Override
            public void onSuccess() {
                cargarDatos();
                Snackbar.make(binding.getRoot(), R.string.msg_product_deleted, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onError(Exception e) {
                mostrarErrorDialog(getString(R.string.msg_error_delete), e.getMessage());
            }
        });
    }

    private void mostrarErrorDialog(String titulo, String mensaje) {
        new AlertDialog.Builder(getContext())
                .setTitle(titulo)
                .setMessage(mensaje)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void mostrarDialogoValorar(Producto producto) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_valoracion, null);
        RatingBar rbPuntuacion = view.findViewById(R.id.rb_puntuacion);
        EditText etComentario = view.findViewById(R.id.et_comentario_valoracion);

        rbPuntuacion.setRating(0);
        etComentario.setText("");

        new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.title_rate_product) + ": " + producto.getNombre())
                .setView(view)
                .setPositiveButton(R.string.btn_rate, (d, w) -> {
                    producto.agregarResena(etComentario.getText().toString(), rbPuntuacion.getRating());
                    actualizarProducto(producto);
                })
                .setNegativeButton(R.string.btn_cancel, null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
