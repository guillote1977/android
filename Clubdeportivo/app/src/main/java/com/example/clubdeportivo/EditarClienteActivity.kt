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

class EditarClienteActivity : AppCompatActivity() {

    private lateinit var etNombreCompleto: EditText
    private lateinit var etEmail: EditText
    private lateinit var etDNI: EditText
    private lateinit var etTelefono: EditText
    private lateinit var rgMembresia: RadioGroup
    private lateinit var rbSocio: RadioButton
    private lateinit var rbNoSocio: RadioButton
    private lateinit var rgAptoFisico: RadioGroup
    private lateinit var rbSiApto: RadioButton
    private lateinit var rbNoApto: RadioButton
    private lateinit var rgEstadoCliente: RadioGroup
    private lateinit var rbActivo: RadioButton
    private lateinit var rbInactivo: RadioButton
    private lateinit var btnModificar: Button
    private lateinit var btnCancelar: Button
    private lateinit var btnGenerarCarnet: Button

    private var cliente: Cliente? = null
    private var aptoFisicoAnterior = false
    private var seCambioAptoFisico = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_cliente)

        setupBackPressedHandler()

        cliente = intent.getSerializableExtra("CLIENTE") as? Cliente
        if (cliente == null) {
            Toast.makeText(this, "Error: Cliente no encontrado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        aptoFisicoAnterior = cliente!!.aptoFisico
        inicializarVistas()
        cargarDatosCliente()
        configurarEventos()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Editar Cliente - ${cliente!!.nombre}"
    }

    private fun setupBackPressedHandler() {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                mostrarDialogoCancelar()
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
        rbSocio = findViewById(R.id.rbSocio)
        rbNoSocio = findViewById(R.id.rbNoSocio)
        rgAptoFisico = findViewById(R.id.rgAptoFisico)
        rbSiApto = findViewById(R.id.rbSiApto)
        rbNoApto = findViewById(R.id.rbNoApto)
        rgEstadoCliente = findViewById(R.id.rgEstadoCliente)
        rbActivo = findViewById(R.id.rbActivo)
        rbInactivo = findViewById(R.id.rbInactivo)
        btnModificar = findViewById(R.id.btnModificar)
        btnCancelar = findViewById(R.id.btnCancelar)
        btnGenerarCarnet = findViewById(R.id.btnGenerarCarnet)
    }

    private fun cargarDatosCliente() {
        cliente?.let { cliente ->
            etNombreCompleto.setText(cliente.nombre)
            etEmail.setText(cliente.email)
            etDNI.setText(cliente.dni)
            etTelefono.setText(cliente.telefono)

            if (cliente.tipoMembresia == "Socio") {
                rbSocio.isChecked = true
            } else {
                rbNoSocio.isChecked = true
            }

            if (cliente.aptoFisico) {
                rbSiApto.isChecked = true
                // MOSTRAR botón de carnet si ya tiene apto físico
                if (cliente.tipoMembresia == "Socio") {
                    btnGenerarCarnet.visibility = Button.VISIBLE
                }
            } else {
                rbNoApto.isChecked = true
                btnGenerarCarnet.visibility = Button.GONE
            }

            if (cliente.estado == "Activo") {
                rbActivo.isChecked = true
            } else {
                rbInactivo.isChecked = true
            }

            etDNI.isEnabled = false
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

        btnGenerarCarnet.setOnClickListener {
            generarCarnetDigital()
        }

        // Listener para cambios en apto físico
        rgAptoFisico.setOnCheckedChangeListener { _, checkedId ->
            val nuevoAptoFisico = (checkedId == R.id.rbSiApto)
            val esSocio = rbSocio.isChecked

            // Marcar que hubo cambio en apto físico
            if (nuevoAptoFisico != aptoFisicoAnterior) {
                seCambioAptoFisico = true
            }

            // Mostrar/ocultar botón de carnet según las condiciones
            if (nuevoAptoFisico && esSocio) {
                btnGenerarCarnet.visibility = Button.VISIBLE
            } else {
                btnGenerarCarnet.visibility = Button.GONE
            }
        }

        // Listener para cambios en membresía
        rgMembresia.setOnCheckedChangeListener { _, checkedId ->
            val esSocio = (checkedId == R.id.rbSocio)
            val tieneAptoFisico = rbSiApto.isChecked

            if (esSocio && tieneAptoFisico) {
                btnGenerarCarnet.visibility = Button.VISIBLE
            } else {
                btnGenerarCarnet.visibility = Button.GONE
            }
        }
    }

    private fun validarCampos(): Boolean {
        var valido = true

        if (etNombreCompleto.text.toString().trim().isEmpty()) {
            etNombreCompleto.error = "El nombre es obligatorio"
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

        val telefono = etTelefono.text.toString().trim()
        if (telefono.isEmpty()) {
            etTelefono.error = "El teléfono es obligatorio"
            valido = false
        }

        return valido
    }

    private fun modificarCliente() {
        cliente?.let { clienteOriginal ->
            val aptoFisicoNuevo = rbSiApto.isChecked
            val esSocio = rbSocio.isChecked

            val clienteActualizado = clienteOriginal.copy(
                nombre = etNombreCompleto.text.toString().trim(),
                email = etEmail.text.toString().trim(),
                telefono = etTelefono.text.toString().trim(),
                tipoMembresia = if (esSocio) "Socio" else "No Socio",
                aptoFisico = aptoFisicoNuevo,
                estado = if (rbActivo.isChecked) "Activo" else "Inactivo"
            )

            // Verificar si se cambió el apto físico de NO a SÍ
            val seHabilitoCarnet = (!aptoFisicoAnterior && aptoFisicoNuevo && esSocio)

            if (seHabilitoCarnet) {
                // Si ahora puede generar carnet, mostrar opción
                mostrarDialogoExitoConCarnet(clienteActualizado)
            } else if (seCambioAptoFisico && !aptoFisicoNuevo && esSocio) {
                // Si perdió el apto físico, informar
                mostrarDialogoExitoAptoRevocado(clienteActualizado)
            } else {
                // Cambios normales
                mostrarDialogoExitoSimple(clienteActualizado)
            }
        }
    }

    private fun mostrarDialogoExitoConCarnet(clienteActualizado: Cliente) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("✅ Actualización Exitosa")
        builder.setMessage("""
            Cliente actualizado correctamente.
            
            🎉 ¡Apto físico aprobado!
            
            Ahora puede generar el carnet digital para el cliente.
            
            ¿Desea generar el carnet ahora?
        """.trimIndent())

        builder.setPositiveButton("🎫 Generar Carnet") { dialog, which ->
            generarCarnetDigital(clienteActualizado)
        }

        builder.setNegativeButton("💾 Solo Guardar") { dialog, which ->
            finalizarEdicion(clienteActualizado)
        }

        builder.setNeutralButton("✏️ Seguir Editando") { dialog, which ->
            // Mantener la actividad abierta y mostrar el botón de carnet
            btnGenerarCarnet.visibility = Button.VISIBLE
            Toast.makeText(this, "Cambios guardados. Puede generar el carnet cuando desee.", Toast.LENGTH_SHORT).show()
        }

        builder.setCancelable(false)
        builder.show()
    }

    private fun mostrarDialogoExitoAptoRevocado(clienteActualizado: Cliente) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("✅ Actualización Exitosa")
        builder.setMessage("""
            Cliente actualizado correctamente.
            
            ⚠️  Apto físico revocado.
            
            El carnet digital ya no estará disponible.
        """.trimIndent())

        builder.setPositiveButton("Aceptar") { dialog, which ->
            finalizarEdicion(clienteActualizado)
        }

        builder.setCancelable(false)
        builder.show()
    }

    private fun mostrarDialogoExitoSimple(clienteActualizado: Cliente) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("✅ Actualización Exitosa")
        builder.setMessage("Cliente actualizado correctamente.")

        builder.setPositiveButton("Aceptar") { dialog, which ->
            finalizarEdicion(clienteActualizado)
        }

        builder.setNeutralButton("✏️ Seguir Editando") { dialog, which ->
            // Mantener la actividad abierta
            Toast.makeText(this, "Cambios guardados.", Toast.LENGTH_SHORT).show()
        }

        builder.setCancelable(false)
        builder.show()
    }

    private fun generarCarnetDigital(clienteParaCarnet: Cliente? = null) {
        try {
            val clienteCarnet = clienteParaCarnet ?: cliente
            clienteCarnet?.let {
                // Verificar requisitos
                if (!it.aptoFisico) {
                    Toast.makeText(this, "El cliente no tiene apto físico aprobado", Toast.LENGTH_SHORT).show()
                    return
                }
                if (it.tipoMembresia != "Socio") {
                    Toast.makeText(this, "Solo los socios pueden generar carnet", Toast.LENGTH_SHORT).show()
                    return
                }

                val intent = Intent(this, CarnetDigitalActivity::class.java)
                intent.putExtra("CLIENTE", it)
                startActivity(intent)

                // Si se generó desde modificación, finalizar edición
                clienteParaCarnet?.let { clienteActualizado ->
                    finalizarEdicion(clienteActualizado)
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error al generar carnet: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun finalizarEdicion(clienteActualizado: Cliente) {
        val resultIntent = Intent()
        resultIntent.putExtra("CLIENTE_ACTUALIZADO", clienteActualizado)
        setResult(RESULT_OK, resultIntent)
        finish()
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
}