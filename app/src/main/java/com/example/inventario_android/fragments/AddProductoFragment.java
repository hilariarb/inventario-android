package com.example.inventario_android.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.example.inventario_android.R;
import com.example.inventario_android.conexion_bd.DB_Conexion;
import com.example.inventario_android.conexion_bd.Producto;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddProductoFragment extends Fragment {

    private EditText etNombre, etDescripcion;
    private CheckBox cbInventario, cbPorComprar;
    private Button btnGuardar;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_producto, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = DB_Conexion.crearConexion();

        etNombre = view.findViewById(R.id.etNombre);
        etDescripcion = view.findViewById(R.id.etDescripcion);
        cbInventario = view.findViewById(R.id.cbInventario);
        cbPorComprar = view.findViewById(R.id.cbPorComprar);
        btnGuardar = view.findViewById(R.id.btnGuardar);

        // Lógica de exclusión mutua para los CheckBox
        cbInventario.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                cbPorComprar.setChecked(false);
            }
        });

        cbPorComprar.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                cbInventario.setChecked(false);
            }
        });

        btnGuardar.setOnClickListener(v -> guardarProducto());
    }

    private void guardarProducto() {
        String nombre = etNombre.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();
        boolean inventario = cbInventario.isChecked();
        boolean porComprar = cbPorComprar.isChecked();

        if (nombre.isEmpty()) {
            Toast.makeText(getContext(), "El nombre es obligatorio", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!inventario && !porComprar) {
            Toast.makeText(getContext(), "Selecciona una opción (Inventario o Comprar)", Toast.LENGTH_SHORT).show();
            return;
        }

        Producto nuevo = new Producto(null, nombre, descripcion, inventario, porComprar);

        DB_Conexion.crearProducto(db, nuevo, new DB_Conexion.DocumentCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(getContext(), "Producto guardado", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(AddProductoFragment.this).navigateUp();
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(getContext(), "Error al guardar", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
