package com.example.inventario_android.conexion_bd;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventario_android.R;

import java.util.ArrayList;
import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {

    private List<Producto> productos = new ArrayList<>();
    private OnProductoListener listener;

    public interface OnProductoListener {
        void onEditar(Producto producto);
        void onEliminar(Producto producto);
        void onCambiarEstado(Producto producto);
        void onValorar(Producto producto);
        void onFavorito(Producto producto);
    }

    public void setListener(OnProductoListener listener) {
        this.listener = listener;
    }

    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTextView;
        TextView descripcionTextView;
        TextView comentarioTextView;
        CheckBox inventarioCheckBox;
        CheckBox porComprarCheckBox;
        RatingBar puntuacionVisual;
        ImageButton borrarButton;
        ImageButton valorarButton;
        ImageButton favoritoButton;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.tv_nombre_producto);
            descripcionTextView = itemView.findViewById(R.id.tv_descripcion_producto);
            comentarioTextView = itemView.findViewById(R.id.tv_comentario_producto);
            inventarioCheckBox = itemView.findViewById(R.id.cb_en_inventario);
            porComprarCheckBox = itemView.findViewById(R.id.cb_por_comprar);
            puntuacionVisual = itemView.findViewById(R.id.rb_puntuacion_visual);
            borrarButton = itemView.findViewById(R.id.btn_borrar);
            valorarButton = itemView.findViewById(R.id.btn_valorar);
            favoritoButton = itemView.findViewById(R.id.btn_favorito);
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
        
        if (productoActual.getResenas() != null && !productoActual.getResenas().isEmpty()) {
            Producto.Resena ultima = productoActual.getResenas().get(0);
            holder.puntuacionVisual.setRating(ultima.getPuntuacion());
            holder.comentarioTextView.setText(ultima.getTexto());
            holder.comentarioTextView.setVisibility(View.VISIBLE);
            holder.puntuacionVisual.setVisibility(View.VISIBLE);
        } else {
            holder.puntuacionVisual.setRating(0);
            holder.comentarioTextView.setText("");
            holder.comentarioTextView.setVisibility(View.GONE);
            holder.puntuacionVisual.setVisibility(View.GONE);
        }

        // Cambio de icono a corazón
        if (productoActual.isFavorito()) {
            holder.favoritoButton.setImageResource(R.drawable.ic_heart_filled);
        } else {
            holder.favoritoButton.setImageResource(R.drawable.ic_heart_empty);
        }
        
        holder.inventarioCheckBox.setOnCheckedChangeListener(null);
        holder.porComprarCheckBox.setOnCheckedChangeListener(null);
        
        holder.inventarioCheckBox.setChecked(productoActual.isInventario());
        holder.porComprarCheckBox.setChecked(productoActual.isPor_comprar());

        holder.porComprarCheckBox.setOnClickListener(v -> {
            if (holder.porComprarCheckBox.isChecked()) {
                productoActual.setPor_comprar(true);
                productoActual.setInventario(false);
                if (listener != null) listener.onCambiarEstado(productoActual);
            }
        });

        holder.inventarioCheckBox.setOnClickListener(v -> {
            if (holder.inventarioCheckBox.isChecked()) {
                productoActual.setInventario(true);
                productoActual.setPor_comprar(false);
                if (listener != null) listener.onCambiarEstado(productoActual);
            }
        });

        holder.borrarButton.setOnClickListener(v -> {
            if (listener != null) listener.onEliminar(productoActual);
        });

        holder.valorarButton.setOnClickListener(v -> {
            if (listener != null) listener.onValorar(productoActual);
        });

        holder.favoritoButton.setOnClickListener(v -> {
            if (listener != null) {
                productoActual.setFavorito(!productoActual.isFavorito());
                listener.onFavorito(productoActual);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null) listener.onEditar(productoActual);
            return true;
        });
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
