package com.example.clubdeportivo

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Patterns
import androidx.appcompat.app.AlertDialog

class RegistroClienteActivity : AppCompatActivity() {

    // Declarar las variables como lateinit
    private lateinit var etNombreCompleto: EditText
    private lateinit var etEmail: EditText
    private lateinit var etDNI: EditText
    private lateinit var etTelefono: EditText
    private lateinit var rgMembresia: RadioGroup
    private lateinit var rgAptoFisico: RadioGroup
    private lateinit var rbSocio: RadioButton
    private lateinit var rbSi: RadioButton
    private lateinit var rbNo: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_cliente)

        // Inicializar todas las vistas
        inicializarVistas()

        // Configurar el botón de volver
        val btnVolver = findViewById<Button>(R.id.btnVolver)
        btnVolver.setOnClickListener {
            finish() // Volver a la actividad anterior
        }

        // Configurar el botón de registrar
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)
        btnRegistrar.setOnClickListener {
            if (validarCampos()) {
                registrarCliente()
            }
        }

        // Configurar botón de retroceso en la barra de acción
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Registro de Cliente"
    }

    private fun inicializarVistas() {
        etNombreCompleto = findViewById(R.id.etNombreCompleto)
        etEmail = findViewById(R.id.etEmail)
        etDNI = findViewById(R.id.etDNI)
        etTelefono = findViewById(R.id.etTelefono)
        rgMembresia = findViewById(R.id.rgMembresia)
        rgAptoFisico = findViewById(R.id.rgAptoFisico)
        rbSocio = findViewById(R.id.rbSocio)
        rbSi = findViewById(R.id.rbSi)
        rbNo = findViewById(R.id.rbNo)
    }

    private fun validarCampos(): Boolean {
        var valido = true

        if (etNombreCompleto.text.toString().trim().isEmpty()) {
            etNombreCompleto.error = "El nombre es obligatorio"
            valido = false
        }

        if (etEmail.text.toString().trim().isEmpty()) {
            etEmail.error = "El email es obligatorio"
            valido = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.text.toString()).matches()) {
            etEmail.error = "Email no válido"
            valido = false
        }

        if (etDNI.text.toString().trim().isEmpty()) {
            etDNI.error = "El DNI es obligatorio"
            valido = false
        }

        if (etTelefono.text.toString().trim().isEmpty()) {
            etTelefono.error = "El teléfono es obligatorio"
            valido = false
        }

        return valido
    }

    private fun registrarCliente() {
        val membresia = if (rbSocio.isChecked) "Socio" else "No Socio"
        val aptoFisico = if (rbSi.isChecked) "Sí" else "No"

        // Mostrar mensaje de éxito con AlertDialog
        mostrarDialogoExito()
    }

    private fun mostrarDialogoExito() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("✅ Registro Exitoso")
        builder.setMessage("El cliente ha sido registrado correctamente.")
        builder.setPositiveButton("Aceptar") { dialog, which ->
            // Volver al menú principal
            finish()
        }
        builder.setCancelable(false)
        builder.show()
    }

    private fun limpiarCampos() {
        etNombreCompleto.text.clear()
        etEmail.text.clear()
        etDNI.text.clear()
        etTelefono.text.clear()
        rgMembresia.check(R.id.rbSocio)
        rgAptoFisico.check(R.id.rbNo)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}