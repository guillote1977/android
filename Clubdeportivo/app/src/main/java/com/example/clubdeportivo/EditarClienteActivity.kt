package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class EditarClienteActivity : AppCompatActivity() {

    // Variables para las vistas
    private lateinit var etNombreCompleto: EditText
    private lateinit var etEmail: EditText
    private lateinit var etDNI: EditText
    private lateinit var etTelefono: EditText
    private lateinit var rgMembresia: RadioGroup
    private lateinit var rbSocio: RadioButton
    private lateinit var rgAptoFisico: RadioGroup
    private lateinit var rbSiApto: RadioButton
    private lateinit var rgEstadoCliente: RadioGroup
    private lateinit var rbActivo: RadioButton
    private lateinit var btnModificar: Button
    private lateinit var btnCancelar: Button

    private var cliente: Cliente? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_cliente)

        // Obtener el cliente pasado desde GestionClientesActivity
        cliente = intent.getSerializableExtra("CLIENTE") as? Cliente

        inicializarVistas()
        cargarDatosCliente()
        configurarEventos()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Editar Cliente"
    }

    private fun inicializarVistas() {
        etNombreCompleto = findViewById(R.id.etNombreCompleto)
        etEmail = findViewById(R.id.etEmail)
        etDNI = findViewById(R.id.etDNI)
        etTelefono = findViewById(R.id.etTelefono)
        rgMembresia = findViewById(R.id.rgMembresia)
        rbSocio = findViewById(R.id.rbSocio)
        rgAptoFisico = findViewById(R.id.rgAptoFisico)
        rbSiApto = findViewById(R.id.rbSiApto)
        rgEstadoCliente = findViewById(R.id.rgEstadoCliente)
        rbActivo = findViewById(R.id.rbActivo)
        btnModificar = findViewById(R.id.btnModificar)
        btnCancelar = findViewById(R.id.btnCancelar)
    }

    private fun cargarDatosCliente() {
        cliente?.let { cliente ->
            // Cargar datos en los campos
            etNombreCompleto.setText(cliente.nombre)
            etEmail.setText(cliente.email)
            etDNI.setText(cliente.dni)
            etTelefono.setText(cliente.telefono)

            // Configurar membresía
            if (cliente.tipoMembresia == "Socio") {
                rbSocio.isChecked = true
            } else {
                findViewById<RadioButton>(R.id.rbNoSocio).isChecked = true
            }

            // Configurar apto físico
            if (cliente.aptoFisico) {
                rbSiApto.isChecked = true
            } else {
                findViewById<RadioButton>(R.id.rbNoApto).isChecked = true
            }

            // Configurar estado
            if (cliente.estado == "Activo") {
                rbActivo.isChecked = true
            } else {
                findViewById<RadioButton>(R.id.rbInactivo).isChecked = true
            }
        } ?: run {
            // Si no hay cliente, mostrar error y cerrar
            Toast.makeText(this, "Error: Cliente no encontrado", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun configurarEventos() {
        btnModificar.setOnClickListener {
            if (validarCampos()) {
                modificarCliente()
            }
        }

        btnCancelar.setOnClickListener {
            mostrarDialogoCancelar()
        }
    }

    private fun validarCampos(): Boolean {
        var valido = true

        // Validar nombre
        if (etNombreCompleto.text.toString().trim().isEmpty()) {
            etNombreCompleto.error = "El nombre es obligatorio"
            valido = false
        }

        // Validar email
        val email = etEmail.text.toString().trim()
        if (email.isEmpty()) {
            etEmail.error = "El email es obligatorio"
            valido = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.error = "Email no válido"
            valido = false
        }

        // Validar DNI
        if (etDNI.text.toString().trim().isEmpty()) {
            etDNI.error = "El DNI es obligatorio"
            valido = false
        }

        // Validar teléfono
        if (etTelefono.text.toString().trim().isEmpty()) {
            etTelefono.error = "El teléfono es obligatorio"
            valido = false
        }

        return valido
    }

    private fun modificarCliente() {
        cliente?.let { clienteOriginal ->
            // Crear cliente actualizado
            val clienteActualizado = clienteOriginal.copy(
                nombre = etNombreCompleto.text.toString().trim(),
                email = etEmail.text.toString().trim(),
                dni = etDNI.text.toString().trim(),
                telefono = etTelefono.text.toString().trim(),
                tipoMembresia = if (rbSocio.isChecked) "Socio" else "No Socio",
                aptoFisico = rbSiApto.isChecked,
                estado = if (rbActivo.isChecked) "Activo" else "Inactivo"
            )

            // Aquí iría la lógica para actualizar en la base de datos
            // Por ahora simulamos la actualización

            mostrarDialogoExito(clienteActualizado)
        }
    }

    private fun mostrarDialogoExito(clienteActualizado: Cliente) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("✅ Modificación Exitosa")
        builder.setMessage("Los datos del cliente han sido actualizados correctamente.\n\n" +
                "Cliente: ${clienteActualizado.nombre}\n" +
                "DNI: ${clienteActualizado.dni}")

        builder.setPositiveButton("Aceptar") { dialog, which ->
            // Devolver el resultado a GestionClientesActivity
            val resultIntent = Intent()
            resultIntent.putExtra("CLIENTE_ACTUALIZADO", clienteActualizado)
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        builder.setCancelable(false)
        builder.show()
    }

    private fun mostrarDialogoCancelar() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("⚠️ Cancelar Edición")
        builder.setMessage("¿Estás seguro de que quieres cancelar la edición? Los cambios no guardados se perderán.")

        builder.setPositiveButton("Sí, Cancelar") { dialog, which ->
            finish()
        }

        builder.setNegativeButton("Seguir Editando") { dialog, which ->
            dialog.dismiss()
        }

        builder.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        mostrarDialogoCancelar()
        return true
    }

    override fun onBackPressed() {
        mostrarDialogoCancelar()
    }
}