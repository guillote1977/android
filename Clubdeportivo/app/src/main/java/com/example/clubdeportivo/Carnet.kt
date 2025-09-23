package com.example.clubdeportivo

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

data class Carnet(
    val id: Int,
    val clienteId: Int,
    val clienteNombre: String,
    val clienteDNI: String,
    val tipoMembresia: String,
    val fechaEmision: String,
    val fechaVencimiento: String,
    val numeroCarnet: String,
    val aptoFisicoAprobado: Boolean,
    val qrCode: String = generarCodigoQR() // Simulación
) : Serializable {

    companion object {
        private fun generarCodigoQR(): String {
            return "ONYX${Random().nextInt(9999)}"
        }

        fun generarNumeroCarnet(clienteId: Int): String {
            val fecha = SimpleDateFormat("yyMMdd", Locale.getDefault()).format(Date())
            return "ONX-$fecha-${String.format("%04d", clienteId)}"
        }

        fun generarParaCliente(cliente: Cliente): Carnet {
            val fechaEmision = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.YEAR, 1)
            val fechaVencimiento = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)

            return Carnet(
                id = Random().nextInt(1000),
                clienteId = cliente.id,
                clienteNombre = cliente.nombre,
                clienteDNI = cliente.dni,
                tipoMembresia = cliente.tipoMembresia,
                fechaEmision = fechaEmision,
                fechaVencimiento = fechaVencimiento,
                numeroCarnet = generarNumeroCarnet(cliente.id),
                aptoFisicoAprobado = cliente.aptoFisico
            )
        }
    }
}