package com.example.inventario_android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.inventario_android.conexion_bd.Producto;
import java.util.ArrayList;
import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {

    private List<Producto> productos = new ArrayList<>();

    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTextView;
        TextView descripcionTextView;
        CheckBox inventarioCheckBox;
        CheckBox porComprarCheckBox;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.tv_nombre_producto);
            descripcionTextView = itemView.findViewById(R.id.tv_descripcion_producto);
            inventarioCheckBox = itemView.findViewById(R.id.cb_en_inventario);
            porComprarCheckBox = itemView.findViewById(R.id.cb_por_comprar);
        }
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.producto, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto productoActual = productos.get(position);

        holder.nombreTextView.setText(productoActual.getNombre());
        holder.descripcionTextView.setText(productoActual.getDescripcion());
        holder.inventarioCheckBox.setChecked(productoActual.isInventario());
        holder.porComprarCheckBox.setChecked(productoActual.isPor_comprar());
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public void setProductos(List<Producto> nuevosProductos) {
        this.productos = nuevosProductos;
        notifyDataSetChanged();
    }
}