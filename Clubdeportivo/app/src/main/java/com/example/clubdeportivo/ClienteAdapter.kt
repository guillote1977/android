package com.example.clubdeportivo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class ClienteAdapter(
    private var clientes: List<Cliente>,
    private val onClienteClick: (Cliente) -> Unit
) : RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder>() {

    class ClienteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNumero: TextView = itemView.findViewById(R.id.tvNumero)
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        val tvDNI: TextView = itemView.findViewById(R.id.tvDNI)
        val tvEstado: TextView = itemView.findViewById(R.id.tvEstado)
        val ivSeleccionado: ImageView = itemView.findViewById(R.id.ivSeleccionado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cliente, parent, false)
        return ClienteViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClienteViewHolder, position: Int) {
        val cliente = clientes[position]

        holder.tvNumero.text = (position + 1).toString()
        holder.tvNombre.text = cliente.nombre
        holder.tvDNI.text = cliente.dni

        // Configurar estado
        when (cliente.estado) {
            "Moroso" -> {
                holder.tvEstado.text = "Moroso"
                holder.tvEstado.setTextColor(ContextCompat.getColor(holder.itemView.context, android.R.color.holo_red_dark))
                holder.tvEstado.setBackgroundColor(0x22FF0000.toInt())
                holder.tvEstado.visibility = View.VISIBLE
            }
            "Por Vencer" -> {
                holder.tvEstado.text = "Por Vencer"
                holder.tvEstado.setTextColor(ContextCompat.getColor(holder.itemView.context, android.R.color.holo_orange_dark))
                holder.tvEstado.setBackgroundColor(0x22FF9800.toInt())
                holder.tvEstado.visibility = View.VISIBLE
            }
            "Sin Apto Físico" -> {
                holder.tvEstado.text = "Sin Apto"
                holder.tvEstado.setTextColor(ContextCompat.getColor(holder.itemView.context, android.R.color.holo_purple))
                holder.tvEstado.setBackgroundColor(0x229C27B0.toInt())
                holder.tvEstado.visibility = View.VISIBLE
            }
            "Inactivo" -> {
                holder.tvEstado.text = "Inactivo"
                holder.tvEstado.setTextColor(ContextCompat.getColor(holder.itemView.context, android.R.color.darker_gray))
                holder.tvEstado.setBackgroundColor(0x22555555.toInt())
                holder.tvEstado.visibility = View.VISIBLE
            }
            else -> {
                holder.tvEstado.visibility = View.GONE
            }
        }

        holder.itemView.setOnClickListener {
            onClienteClick(cliente)
        }
    }

    override fun getItemCount(): Int = clientes.size

    fun actualizarLista(nuevaLista: List<Cliente>) {
        clientes = nuevaLista
        notifyDataSetChanged()
    }
}