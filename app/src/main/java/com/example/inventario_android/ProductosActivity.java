package com.example.inventario_android;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.inventario_android.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Locale;

public class ProductosActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private static final String PREFS_NAME = "prefs";
    private static final String KEY_LANG = "lang";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Cargar el idioma guardado antes de que se cree la vista
        loadLocale();
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // El FAB está oculto para usar el menú superior
        binding.fab.setVisibility(android.view.View.GONE);
    }

    private void toggleLanguage() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String currentLang = prefs.getString(KEY_LANG, "es");
        String nextLang = currentLang.equals("es") ? "en" : "es";

        // Cambiar el idioma
        setLocale(nextLang);
        
        // Mostrar aviso al usuario
        String msg = nextLang.equals("es") ? "Idioma: Español" : "Language: English";
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

        // Reiniciar la actividad para que todos los textos se actualicen
        finish();
        startActivity(getIntent());
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());

        // Guardar la elección para la próxima vez que se abra la app
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(KEY_LANG, lang);
        editor.apply();
    }

    private void loadLocale() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String language = prefs.getString(KEY_LANG, "es");
        setLocale(language);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflar el menú con la opción de cambiar idioma
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Manejar el clic en "Cambiar idioma"
        if (item.getItemId() == R.id.action_language) {
            toggleLanguage();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}