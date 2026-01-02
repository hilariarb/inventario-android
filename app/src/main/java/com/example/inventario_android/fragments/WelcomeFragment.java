package com.example.inventario_android.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.example.inventario_android.R;

public class WelcomeFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Espera 2 segundos y navega al inventario
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (isAdded()) {
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_WelcomeFragment_to_FirstFragment);
            }
        }, 2000);
    }
}
