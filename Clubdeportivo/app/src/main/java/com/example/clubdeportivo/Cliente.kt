package com.example.clubdeportivo

import java.io.Serializable

data class Cliente(
    val id: Int,
    val nombre: String,
    val dni: String,
    val email: String,
    val telefono: String,
    val tipoMembresia: String,
    val aptoFisico: Boolean,
    val estado: String,
    val fechaRegistro: String,
    val fechaVencimiento: String
) : Serializable {
    companion object {
        fun getClientesEjemplo(): List<Cliente> {
            return listOf(
                Cliente(1, "Juan Perez", "35462587", "juan@email.com", "123456789", "Socio", true, "Activo", "01/01/2024", "01/02/2024"),
                Cliente(2, "Leticia James", "26315478", "leticia@email.com", "987654321", "No Socio", false, "Moroso", "15/01/2024", "15/01/2024"),
                Cliente(3, "Ignacio Reales", "46879547", "ignacio@email.com", "456123789", "Socio", true, "Por Vencer", "20/01/2024", "20/02/2024"),
                Cliente(4, "Raquel Ramirez", "13458765", "raquel@email.com", "789456123", "No Socio", true, "Activo", "05/01/2024", "05/02/2024"),
                Cliente(5, "Carlos Lopez", "11111111", "carlos@email.com", "111222333", "Socio", false, "Sin Apto Físico", "10/01/2024", "10/02/2024"),
                Cliente(6, "Ana Garcia", "22222222", "ana@email.com", "444555666", "No Socio", true, "Inactivo", "25/01/2024", "25/01/2024")
            )
        }
    }
}