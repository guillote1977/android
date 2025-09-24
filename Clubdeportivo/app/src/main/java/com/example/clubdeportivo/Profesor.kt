package com.example.clubdeportivo

import java.io.Serializable

data class Profesor(
    val id: Int,
    val nombre: String,
    val dni: String,
    val actividades: List<Actividad> = emptyList(),
    val telefono: String = "",
    val email: String = ""
) : Serializable {
    fun getActividadesFormateadas(): String {
        return if (actividades.isEmpty()) {
            "Sin actividades asignadas"
        } else {
            actividades.joinToString(", ") { it.nombre }
        }
    }
}