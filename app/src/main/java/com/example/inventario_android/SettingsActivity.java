package com.example.inventario_android;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // --- Referencias a las vistas del layout ---
        Button btnEspanol = findViewById(R.id.button_espanol);
        Button btnIngles = findViewById(R.id.button_ingles);
        Button btnVolver = findViewById(R.id.button_volver);
        TextView tvTitulo = findViewById(R.id.tv_settings_title);
        TextView tvBloqueTexto = findViewById(R.id.tv_info_text);

        // --- Configuración de los listeners ---

        // Botón para cambiar a Español
        btnEspanol.setOnClickListener(v -> {
            setLocale("es"); // "es" es el código para Español
        });

        // Botón para cambiar a Inglés
        btnIngles.setOnClickListener(v -> {
            setLocale("en"); // "en" es el código para Inglés
        });

        // Botón para volver a la actividad principal
        btnVolver.setOnClickListener(v -> {
            // Cierra la actividad actual y vuelve a la anterior en la pila (MainActivity)
            finish();
        });
    }

    /**
     * Cambia el idioma de la aplicación y recrea la actividad para aplicar los cambios.
     * @param langCode El código del idioma (ej. "es" para español, "en" para inglés).
     */
    private void setLocale(String langCode) {
        // Crea un objeto Locale con el nuevo idioma
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);

        // Obtiene los recursos de la app y actualiza su configuración
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());

        // Es necesario reiniciar la actividad para que los cambios de idioma surtan efecto
        // en toda la interfaz de usuario.
        recreate();
    }
}