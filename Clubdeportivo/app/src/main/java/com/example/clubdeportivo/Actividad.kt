package com.example.clubdeportivo

import java.io.Serializable

data class Actividad(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val descripcion: String = ""
) : Serializable {
    fun getPrecioFormateado(): String {
        return "$${precio.toInt()}"
    }
}