package com.example.inventario_android.conexion_bd;

import androidx.annotation.Nullable;

public class Producto {
    private String id;
    private String nombre;
    private String descripcion;
    private boolean inventario;
    private boolean por_comprar;

    public Producto(String id, String nombre, String descripcion, boolean inventario, boolean por_comprar) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.inventario = inventario;
        this.por_comprar = por_comprar;
    }
//------------------------------------------------------ GETTERS
    public String getId() {
        return id;
    }
    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean isInventario() {
        return inventario;
    }

    public boolean isPor_comprar() {
        return por_comprar;
    }
//------------------------------------------------------ SETTERS
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setPor_comprar(String por_comprar) {
        this.por_comprar = Boolean.parseBoolean(por_comprar);
    }

    public void setInventario(String inventario) {
        this.por_comprar = Boolean.parseBoolean(inventario);
    }

//------------------------------------------------------ OVERRIDE
    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Producto)){
            return false;
        }
        Producto otro = (Producto) obj;
        return otro.getId().equals(this.getId());
    }

    @Override
    public String toString() {
        return getId() + " => " + getNombre() +
                " - " + getDescripcion() +
                " - " + isInventario() +
                " - " + isPor_comprar();
    }
}
