package com.example.asteroides;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class AdaptadorPuntuaciones extends RecyclerView.Adapter<AdaptadorPuntuaciones.ViewHolder> {
    protected List<PuntuacionEntity> listaPuntuaciones; //Lista con la que se va a llenar el recyclerview

    /* Constructor*/
    public AdaptadorPuntuaciones(List<PuntuacionEntity> listaPuntuaciones) {
        this.listaPuntuaciones = listaPuntuaciones;
    }

    /*Creamos el view holder que contiene  el diseño individual de elementos que vamos a modificar*/
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imagenAsteroide;
        public TextView puntuacion;
        public TextView fecha;
        public TextView nombre;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imagenAsteroide = (ImageView) itemView.findViewById(R.id.imgPhoto);
            puntuacion = (TextView) itemView.findViewById(R.id.tvPuntuacion);
            fecha = (TextView) itemView.findViewById(R.id.tvFecha);
            nombre = (TextView) itemView.findViewById(R.id.tvNombre);
        }
    }

    /*Creamos una nueva vista sin personalizar del item_list*/
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    /*Personalizamos el contenido del item_list de acuerdo a la información de la lista puntuaciones*/
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Guardamos un elemento de la clase Puntuacion en turno para personalizar con mayor facilidad los elementos restantes
        PuntuacionEntity puntaje = listaPuntuaciones.get(position);
        //Personalizamos todos los disños con lo que contiene la lista de acuerdo a la posición
        holder.puntuacion.setText(puntaje.puntaje);
        holder.fecha.setText((CharSequence) puntaje.fecha);
        holder.nombre.setText(puntaje.nombre);
        holder.imagenAsteroide.setImageResource(puntaje.imagen);
    }

    /*Obtenemos el tamaño de la lista de puntuaciones*/
    @Override
    public int getItemCount() {
        return listaPuntuaciones.size();
    }
}
