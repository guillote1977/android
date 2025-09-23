package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class RegistroClienteActivity : AppCompatActivity() {

    private lateinit var etNombreCompleto: EditText
    private lateinit var etEmail: EditText
    private lateinit var etDNI: EditText
    private lateinit var etTelefono: EditText
    private lateinit var rgMembresia: RadioGroup
    private lateinit var rgAptoFisico: RadioGroup
    private lateinit var rbSocio: RadioButton
    private lateinit var rbNoSocio: RadioButton
    private lateinit var rbSi: RadioButton
    private lateinit var rbNo: RadioButton
    private lateinit var btnRegistrar: Button
    private lateinit var btnVolver: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_cliente)

        // Configurar el manejo del botón back
        setupBackPressedHandler()

        // Inicializar todas las vistas
        inicializarVistas()

        // Configurar el botón de volver
        btnVolver.setOnClickListener {
            mostrarConfirmacionSalir()
        }

        // Configurar el botón de registrar
        btnRegistrar.setOnClickListener {
            if (validarCampos()) {
                registrarCliente()
            }
        }

        // Configurar botón de retroceso en la barra de acción
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Registro de Cliente"
    }

    private fun setupBackPressedHandler() {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                mostrarConfirmacionSalir()
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private fun inicializarVistas() {
        etNombreCompleto = findViewById(R.id.etNombreCompleto)
        etEmail = findViewById(R.id.etEmail)
        etDNI = findViewById(R.id.etDNI)
        etTelefono = findViewById(R.id.etTelefono)
        rgMembresia = findViewById(R.id.rgMembresia)
        rgAptoFisico = findViewById(R.id.rgAptoFisico)
        rbSocio = findViewById(R.id.rbSocio)
        rbNoSocio = findViewById(R.id.rbNoSocio)
        rbSi = findViewById(R.id.rbSi)
        rbNo = findViewById(R.id.rbNo)
        btnRegistrar = findViewById(R.id.btnRegistrar)
        btnVolver = findViewById(R.id.btnVolver)
    }

    private fun validarCampos(): Boolean {
        var valido = true

        if (etNombreCompleto.text.toString().trim().isEmpty()) {
            etNombreCompleto.error = "El nombre es obligatorio"
            valido = false
        } else if (etNombreCompleto.text.toString().trim().length < 3) {
            etNombreCompleto.error = "El nombre debe tener al menos 3 caracteres"
            valido = false
        }

        val email = etEmail.text.toString().trim()
        if (email.isEmpty()) {
            etEmail.error = "El email es obligatorio"
            valido = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.error = "Email no válido"
            valido = false
        }

        val dni = etDNI.text.toString().trim()
        if (dni.isEmpty()) {
            etDNI.error = "El DNI es obligatorio"
            valido = false
        } else if (dni.length < 7 || dni.length > 8) {
            etDNI.error = "DNI debe tener 7 u 8 dígitos"
            valido = false
        } else if (!dni.matches(Regex("\\d+"))) {
            etDNI.error = "DNI debe contener solo números"
            valido = false
        }

        val telefono = etTelefono.text.toString().trim()
        if (telefono.isEmpty()) {
            etTelefono.error = "El teléfono es obligatorio"
            valido = false
        } else if (telefono.length < 8) {
            etTelefono.error = "Teléfono debe tener al menos 8 dígitos"
            valido = false
        } else if (!telefono.matches(Regex("\\d+"))) {
            etTelefono.error = "Teléfono debe contener solo números"
            valido = false
        }

        return valido
    }

    private fun registrarCliente() {
        val nombre = etNombreCompleto.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val dni = etDNI.text.toString().trim()
        val telefono = etTelefono.text.toString().trim()
        val membresia = if (rbSocio.isChecked) "Socio" else "No Socio"
        val aptoFisico = rbSi.isChecked

        val cliente = Cliente(
            id = generarIdUnico(),
            nombre = nombre,
            dni = dni,
            email = email,
            telefono = telefono,
            tipoMembresia = membresia,
            aptoFisico = aptoFisico,
            estado = "Activo",
            fechaRegistro = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()),
            fechaVencimiento = calcularFechaVencimiento()
        )

        if (aptoFisico && rbSocio.isChecked) {
            mostrarDialogoExitoConCarnet(cliente)
        } else if (rbSocio.isChecked && !aptoFisico) {
            mostrarDialogoExitoSinApto(cliente)
        } else {
            mostrarDialogoExitoSimple(cliente)
        }
    }

    private fun generarIdUnico(): Int {
        return (1000..9999).random()
    }

    private fun calcularFechaVencimiento(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, 1)
        return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)
    }

    private fun mostrarDialogoExitoConCarnet(cliente: Cliente) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("✅ Registro Exitoso")
        builder.setMessage("El cliente ha sido registrado correctamente.\n\n¿Desea generar el carnet digital ahora?")

        builder.setPositiveButton("🎫 Generar Carnet") { dialog, which ->
            generarCarnetDigital(cliente)
        }

        builder.setNegativeButton("📋 Solo Guardar") { dialog, which ->
            finalizarConExito(cliente)
        }

        builder.setCancelable(false)
        builder.show()
    }

    private fun mostrarDialogoExitoSinApto(cliente: Cliente) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("✅ Registro Exitoso")
        builder.setMessage("El cliente ha sido registrado. Podrá generar el carnet cuando apruebe el apto físico.")

        builder.setPositiveButton("Aceptar") { dialog, which ->
            finalizarConExito(cliente)
        }

        builder.setCancelable(false)
        builder.show()
    }

    private fun mostrarDialogoExitoSimple(cliente: Cliente) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("✅ Registro Exitoso")
        builder.setMessage("El cliente ha sido registrado correctamente.")

        builder.setPositiveButton("Aceptar") { dialog, which ->
            finalizarConExito(cliente)
        }

        builder.setCancelable(false)
        builder.show()
    }

    private fun generarCarnetDigital(cliente: Cliente) {
        try {
            val intent = Intent(this, CarnetDigitalActivity::class.java)
            intent.putExtra("CLIENTE", cliente)
            startActivity(intent)
            finalizarConExito(cliente)
        } catch (e: Exception) {
            Toast.makeText(this, "Error al generar carnet: ${e.message}", Toast.LENGTH_SHORT).show()
            finalizarConExito(cliente)
        }
    }

    private fun finalizarConExito(cliente: Cliente) {
        val resultIntent = Intent()
        resultIntent.putExtra("CLIENTE_REGISTRADO", cliente)
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    private fun mostrarConfirmacionSalir() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("⚠️ Salir del registro")
        builder.setMessage("¿Estás seguro de que quieres salir? Los datos no guardados se perderán.")

        builder.setPositiveButton("Sí, Salir") { dialog, which ->
            // Permitir que la actividad se cierre
            finish()
        }

        builder.setNegativeButton("Seguir Registrando") { dialog, which ->
            dialog.dismiss()
        }

        builder.setCancelable(false)
        builder.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        mostrarConfirmacionSalir()
        return true
    }
}