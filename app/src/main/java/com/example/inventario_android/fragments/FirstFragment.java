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

        prueba_GET(); // <--------------------------------------------------------
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


}