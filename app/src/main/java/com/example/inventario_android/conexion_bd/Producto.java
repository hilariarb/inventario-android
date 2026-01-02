package com.example.inventario_android.conexion_bd;

import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class Producto {
    private String id;
    private String nombre;
    private String descripcion;
    private boolean inventario;
    private boolean por_comprar;
    private boolean favorito;
    private float puntuacion;
    private String comentario;
    private List<Resena> resenas;

    public static class Resena {
        private String texto;
        private float puntuacion;
        private long fecha;

        public Resena() {}

        public Resena(String texto, float puntuacion, long fecha) {
            this.texto = texto;
            this.puntuacion = puntuacion;
            this.fecha = fecha;
        }

        public String getTexto() { return texto; }
        public void setTexto(String texto) { this.texto = texto; }
        public float getPuntuacion() { return puntuacion; }
        public void setPuntuacion(float puntuacion) { this.puntuacion = puntuacion; }
        public long getFecha() { return fecha; }
        public void setFecha(long fecha) { this.fecha = fecha; }
    }

    public Producto() {
        this.resenas = new ArrayList<>();
    }

    public Producto(String id, String nombre, String descripcion, boolean inventario, boolean por_comprar) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.inventario = inventario;
        this.por_comprar = por_comprar;
        this.favorito = false;
        this.puntuacion = 0;
        this.comentario = "";
        this.resenas = new ArrayList<>();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public boolean isInventario() { return inventario; }
    public void setInventario(boolean inventario) { this.inventario = inventario; }
    public boolean isPor_comprar() { return por_comprar; }
    public void setPor_comprar(boolean por_comprar) { this.por_comprar = por_comprar; }
    public boolean isFavorito() { return favorito; }
    public void setFavorito(boolean favorito) { this.favorito = favorito; }
    public float getPuntuacion() { return puntuacion; }
    public void setPuntuacion(float puntuacion) { this.puntuacion = puntuacion; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    public List<Resena> getResenas() { 
        if (resenas == null) resenas = new ArrayList<>();
        return resenas; 
    }
    public void setResenas(List<Resena> resenas) { this.resenas = resenas; }

    public void agregarResena(String texto, float puntuacion) {
        if (this.resenas == null) this.resenas = new ArrayList<>();
        this.resenas.add(0, new Resena(texto, puntuacion, System.currentTimeMillis()));
        this.puntuacion = puntuacion;
        this.comentario = texto;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Producto)) return false;
        Producto otro = (Producto) obj;
        if (this.id == null || otro.id == null) return false;
        return this.id.equals(otro.id);
    }

    @Override
    public String toString() {
        return nombre + " (" + (inventario ? "En stock" : "Sin stock") + ")";
    }
}
