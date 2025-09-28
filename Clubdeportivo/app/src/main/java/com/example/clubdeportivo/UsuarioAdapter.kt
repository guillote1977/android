package com.example.clubdeportivo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UsuarioAdapter(
    private var usuarios: List<Usuario>,
    private val onEditarClickListener: (Usuario) -> Unit,
    private val onEliminarClickListener: (Usuario) -> Unit
) : RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder>() {

    class UsuarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNumero: TextView = itemView.findViewById(R.id.tvNumero)
        val tvNombreUsuario: TextView = itemView.findViewById(R.id.tvNombreUsuario)
        val tvRol: TextView = itemView.findViewById(R.id.tvRol)
        val tvEmail: TextView = itemView.findViewById(R.id.tvEmail)
        val ivEditar: ImageView = itemView.findViewById(R.id.ivEditar)
        val ivEliminar: ImageView = itemView.findViewById(R.id.ivEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_usuario, parent, false)
        return UsuarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val usuario = usuarios[position]

        holder.tvNumero.text = (position + 1).toString()
        holder.tvNombreUsuario.text = usuario.nombreUsuario
        holder.tvRol.text = usuario.rol
        holder.tvEmail.text = usuario.email

        holder.ivEditar.setOnClickListener {
            onEditarClickListener(usuario)
        }

        holder.ivEliminar.setOnClickListener {
            onEliminarClickListener(usuario)
        }
    }

    override fun getItemCount(): Int = usuarios.size

    fun actualizarLista(nuevaLista: List<Usuario>) {
        usuarios = nuevaLista
        notifyDataSetChanged()
    }
}