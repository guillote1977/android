package com.example.clubdeportivo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnConexionBD: Button

    private val usuarioValido = "admin"
    private val passwordValido = "admin123"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        inicializarVistas()
        configurarListeners()
    }

    private fun inicializarVistas() {
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnConexionBD = findViewById(R.id.btnConexionBD)
    }

    private fun configurarListeners() {
        btnConexionBD.setOnClickListener {
            navegarAConfiguracionBD()
        }

        btnLogin.setOnClickListener {
            realizarLogin()
        }
    }

    private fun navegarAConfiguracionBD() {
        val intent = Intent(this, ConfigBDActivity::class.java)
        startActivity(intent)
    }

    private fun realizarLogin() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            mostrarMensaje("Por favor, complete todos los campos")
            return
        }

        if (validarCredenciales(email, password)) {
            loginExitoso()
        } else {
            mostrarMensaje("Usuario o contraseña incorrectos")
        }
    }

    private fun validarCredenciales(email: String, password: String): Boolean {
        return email == usuarioValido && password == passwordValido
    }

    private fun loginExitoso() {
        val intent = Intent(this, MenuPrincipalActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun mostrarMensaje(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}