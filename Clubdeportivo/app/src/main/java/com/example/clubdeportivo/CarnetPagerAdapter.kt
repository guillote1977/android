package com.example.clubdeportivo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

class CarnetPagerAdapter(
    private val activity: CarnetDigitalActivity,
    private val cliente: Cliente,
    private val carnet: Carnet
) : RecyclerView.Adapter<CarnetPagerAdapter.CarnetViewHolder>() {

    class CarnetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarnetViewHolder {
        val layoutId = when (viewType) {
            0 -> R.layout.activity_carnet_digital_frente
            1 -> R.layout.activity_carnet_digital_dorso
            else -> R.layout.activity_carnet_digital_frente
        }
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return CarnetViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarnetViewHolder, position: Int) {
        when (position) {
            0 -> configurarFrente(holder.itemView)
            1 -> configurarDorso(holder.itemView)
        }
    }

    override fun getItemCount(): Int = 2

    override fun getItemViewType(position: Int): Int = position

    private fun configurarFrente(view: View) {
        val tvNombre = view.findViewById<TextView>(R.id.tvNombreCarnet)
        val tvMembresia = view.findViewById<TextView>(R.id.tvMembresiaCarnet)
        val tvDNI = view.findViewById<TextView>(R.id.tvDNICarnet)
        val tvVencimiento = view.findViewById<TextView>(R.id.tvVencimientoCarnet)
        val tvNumero = view.findViewById<TextView>(R.id.tvNumeroCarnet)

        tvNombre.text = cliente.nombre.uppercase()
        tvMembresia.text = cliente.tipoMembresia.uppercase()
        tvDNI.text = "ID: ${cliente.dni}"
        tvVencimiento.text = "Vence: ${carnet.fechaVencimiento}"
        tvNumero.text = carnet.numeroCarnet
    }

    private fun configurarDorso(view: View) {
        val tvEmailInstitucional = view.findViewById<TextView>(R.id.tvEmailInstitucionalCarnet)
        val tvContacto = view.findViewById<TextView>(R.id.tvContactoCarnet)
        val tvDireccion = view.findViewById<TextView>(R.id.tvDireccionCarnet)
        val tvDNI = view.findViewById<TextView>(R.id.tvDNICarnet)
        val tvVencimiento = view.findViewById<TextView>(R.id.tvVencimientoCarnet)
        val tvNumero = view.findViewById<TextView>(R.id.tvNumeroCarnet)

        // Información del club (puedes personalizar estos valores)
        tvEmailInstitucional.text = "info@onyxfitness.com"
        tvContacto.text = "+54 911 4658 745"
        tvDireccion.text = "Av. Imagine 745"

        // Datos del cliente en el dorso también
        tvDNI.text = "ID: ${cliente.dni}"
        tvVencimiento.text = "Vence: ${carnet.fechaVencimiento}"
        tvNumero.text = carnet.numeroCarnet
    }
}