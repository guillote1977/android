package com.example.clubdeportivo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProfesorAdapter(
    private var profesores: List<Profesor>,
    private val onEditarClickListener: (Profesor) -> Unit,
    private val onEliminarClickListener: (Profesor) -> Unit
) : RecyclerView.Adapter<ProfesorAdapter.ProfesorViewHolder>() {

    class ProfesorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNumero: TextView = itemView.findViewById(R.id.tvNumero)
        val tvNombreProfesor: TextView = itemView.findViewById(R.id.tvNombreProfesor)
        val tvDNI: TextView = itemView.findViewById(R.id.tvDNI)
        val tvActividades: TextView = itemView.findViewById(R.id.tvActividades)
        val ivEditar: ImageView = itemView.findViewById(R.id.ivEditar)
        val ivEliminar: ImageView = itemView.findViewById(R.id.ivEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfesorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_profesor, parent, false)
        return ProfesorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfesorViewHolder, position: Int) {
        val profesor = profesores[position]

        holder.tvNumero.text = (position + 1).toString()
        holder.tvNombreProfesor.text = profesor.nombre
        holder.tvDNI.text = profesor.dni
        holder.tvActividades.text = profesor.getActividadesFormateadas()

        holder.ivEditar.setOnClickListener {
            onEditarClickListener(profesor)
        }

        holder.ivEliminar.setOnClickListener {
            onEliminarClickListener(profesor)
        }
    }

    override fun getItemCount(): Int = profesores.size

    fun actualizarLista(nuevaLista: List<Profesor>) {
        profesores = nuevaLista
        notifyDataSetChanged()
    }
}