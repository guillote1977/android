package com.example.clubdeportivo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import java.io.Serializable
import kotlin.text.isEmpty
import kotlin.text.trim

class EditarUsuarioActivity : AppCompatActivity() {

    private lateinit var tvTitulo: TextView
    private lateinit var etNombreUsuario: EditText
    private lateinit var etRol: EditText
    private lateinit var etPassword: EditText
    private lateinit var etEmail: EditText
    private lateinit var btnCancelar: Button
    private lateinit var btnGuardarUsuario: Button

    private var usuarioExistente: Usuario? = null
    private var esEdicion = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_usuario)

        inicializarVistas()
        cargarDatosExistente()
        configurarListeners()
    }

    private fun inicializarVistas() {
        tvTitulo = findViewById(R.id.titleNewUser)
        etNombreUsuario = findViewById(R.id.etNombreUsuario)
        etRol = findViewById(R.id.etRol)
        etPassword = findViewById(R.id.etPassword)
        etEmail = findViewById(R.id.etEmail)

        btnCancelar = findViewById(R.id.btnCancelar)
        btnGuardarUsuario = findViewById(R.id.btnGuardarUsuario)
    }


    private fun cargarDatosExistente() {
        usuarioExistente = intent.getSerializableExtra("usuario") as? Usuario

        if (usuarioExistente != null) {
            esEdicion = true
            tvTitulo.text = "Editar Usuario"
            etNombreUsuario.setText(usuarioExistente!!.nombreUsuario)
            etRol.setText(usuarioExistente!!.rol)
            etPassword.setText(usuarioExistente!!.password)
            etEmail.setText(usuarioExistente!!.email)

        } else {
            tvTitulo.text = "Nuevo Usuario"
        }
    }


    private fun configurarListeners() {
        btnCancelar.setOnClickListener {
            finish()
        }

        btnGuardarUsuario.setOnClickListener {
            guardarUsuario()
        }
    }

    private fun guardarUsuario() {
        val nombre = etNombreUsuario.text.toString().trim()
        val rol = etRol.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val email = etEmail.text.toString().trim()

        if (nombre.isEmpty()) {
            mostrarError("El nombre de usuario es obligatorio")
            return
        }

        if (password.isEmpty()) {
            mostrarError("La contraseña de usuario es obligatoria")
            return
        }

        if (rol.isEmpty()) {
            mostrarError("El ROL de usuario es obligatorio")
            return
        }

        if (email.isEmpty()) {
            mostrarError("El email de usuario es obligatorio")
            return
        }

        val resultadoIntent = Intent()
        if (esEdicion) {
            val usuarioActualizado = usuarioExistente!!.copy(
                nombreUsuario = nombre,
                rol = rol,
                password = password,
                email = email,
            )

            resultadoIntent.putExtra("usuario_actualizado", usuarioActualizado)
            Toast.makeText(this, "Usuario actualizado correctamente", Toast.LENGTH_SHORT).show()
        } else {
            val nuevoUsuario = Usuario(
                id = (System.currentTimeMillis() % Int.MAX_VALUE).toInt(),
                nombreUsuario = nombre,
                rol = rol,
                password = password,
                email = email

            )
            resultadoIntent.putExtra("nuevo_usuario", nuevoUsuario)
            Toast.makeText(this, "Usuario creado correctamente", Toast.LENGTH_SHORT).show()
        }

        setResult(RESULT_OK, resultadoIntent)
        finish()
    }

    private fun mostrarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}