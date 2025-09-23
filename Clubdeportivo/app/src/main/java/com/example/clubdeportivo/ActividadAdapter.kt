package com.example.clubdeportivo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ActividadAdapter(
    private var actividades: List<Actividad>,
    private val onEditarClickListener: (Actividad) -> Unit,
    private val onEliminarClickListener: (Actividad) -> Unit
) : RecyclerView.Adapter<ActividadAdapter.ActividadViewHolder>() {

    class ActividadViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNumero: TextView = itemView.findViewById(R.id.tvNumero)
        val tvNombreActividad: TextView = itemView.findViewById(R.id.tvNombreActividad)
        val tvPrecio: TextView = itemView.findViewById(R.id.tvPrecio)
        val ivEditar: ImageView = itemView.findViewById(R.id.ivEditar)
        val ivEliminar: ImageView = itemView.findViewById(R.id.ivEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActividadViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_actividad, parent, false)
        return ActividadViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActividadViewHolder, position: Int) {
        val actividad = actividades[position]

        holder.tvNumero.text = (position + 1).toString()
        holder.tvNombreActividad.text = actividad.nombre
        holder.tvPrecio.text = actividad.getPrecioFormateado()

        holder.ivEditar.setOnClickListener {
            onEditarClickListener(actividad)
        }

        holder.ivEliminar.setOnClickListener {
            onEliminarClickListener(actividad)
        }
    }

    override fun getItemCount(): Int = actividades.size

    fun actualizarLista(nuevaLista: List<Actividad>) {
        actividades = nuevaLista
        notifyDataSetChanged()
    }
}