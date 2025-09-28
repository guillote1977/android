package com.example.clubdeportivo

import java.io.Serializable

data class Usuario(
    val id: Int,
    val nombreUsuario: String,
    val rol: String,
    val password: String,
    val email: String = ""

) : Serializable