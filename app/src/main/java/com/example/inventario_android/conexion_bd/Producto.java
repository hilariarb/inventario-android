package com.example.inventario_android.conexion_bd;

import androidx.annotation.Nullable;

public class Producto {
    private String id;
    private String nombre;
    private String descripcion;
    private boolean inventario;
    private boolean por_comprar;

    public Producto() {
        // Requerido por Firebase
    }

    public Producto(String id, String nombre, String descripcion, boolean inventario, boolean por_comprar) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.inventario = inventario;
        this.por_comprar = por_comprar;
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
