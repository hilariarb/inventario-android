package com.example.inventario_android.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.inventario_android.R;
import com.example.inventario_android.conexion_bd.DB_Conexion;
import com.example.inventario_android.conexion_bd.Producto;
import com.example.inventario_android.databinding.FragmentFirstBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class FirstFragment extends Fragment {
    // Prueba 2
    private FragmentFirstBinding binding;
    private static final String TAG = "FirstFragment";
    private FirebaseFirestore db;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        db = DB_Conexion.crearConexion();
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFirst.setOnClickListener(v ->
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment)
        );

        // ----------------   METODOS DE PRUEBA DE BACKEND, BORRAR ---------------
        prueba_GET();
        prueba_INSERT();
        prueba_UPDATE();
        prueba_DELETE();
        // -----------------------------------------------------------------------
    }

    @Override
    public void onDestroyView() {
        DB_Conexion.cerrarConexion(db);
        super.onDestroyView();
        binding = null;
    }
//----------------------------------------------------------------------------------
    //              Métodos de prueba back-end

    public void prueba_GET() {
        Log.d(TAG, "PRUEBA GET");
        // GET lista
        DB_Conexion.mostrar(
                DB_Conexion.obtener(db));
        Log.d(TAG, "--------------");
    }

    public void prueba_INSERT(){
        Log.d(TAG, "PRUEBA INSERT");
        Producto nuevo = new Producto("prueba_id",
                "prueba_nombre",
                "prueba_descr",
                false, false);

        DB_Conexion.crearDocumento(db, nuevo);
        prueba_GET();
        Log.d(TAG, "--------------");
    }

    public void prueba_UPDATE(){
        Log.d(TAG, "PRUEBA UPDATE");
        Producto nuevo = new Producto("prueba_id",
                "prueba_actualizada",
                "prueba descr actualizada",
                true, true);

        DB_Conexion.actualizar(db, nuevo);
        prueba_GET();
        Log.d(TAG, "--------------");
    }

    public void prueba_DELETE(){
        Log.d(TAG, "PRUEBA DELETE");
        Producto nuevo = new Producto("prueba_id",
                "prueba",
                "prueba_descr",
                false, true);

        DB_Conexion.eliminar(db, nuevo);
        prueba_GET();
        Log.d(TAG, "--------------");
    }

}