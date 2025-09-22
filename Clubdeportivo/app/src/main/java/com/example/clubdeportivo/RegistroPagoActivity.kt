package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class RegistroPagoActivity : AppCompatActivity() {

    // Declarar todas las variables como lateinit a nivel de clase
    private lateinit var etCampoDNI: EditText
    private lateinit var icoBuscar: ImageButton
    private lateinit var txtDatosCliente: TextView
    private lateinit var rgTipoMembresia: RadioGroup
    private lateinit var rbSocio2: RadioButton
    private lateinit var rbNoSocio2: RadioButton
    private lateinit var etCampoMonto: EditText
    private lateinit var rgFormaPago: RadioGroup
    private lateinit var rbEfectivo: RadioButton
    private lateinit var rbTarjeta: RadioButton
    private lateinit var btPagar: Button
    private lateinit var btComprobante: Button
    private lateinit var btnVolver2: Button
    private lateinit var txtDescuento: TextView
    private lateinit var txtMontoFinal: TextView

    private var descuentoAplicado = 0.0
    private var montoOriginal = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_pago)

        try {
            inicializarVistas()
            configurarEventos()

            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "Registro de Pago"
        } catch (e: Exception) {
            Toast.makeText(this, "Error al cargar: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun inicializarVistas() {
        // Inicializar todas las vistas
        etCampoDNI = findViewById(R.id.etCampoDNI)
        icoBuscar = findViewById(R.id.icoBuscar)
        txtDatosCliente = findViewById(R.id.txtDatosCliente)
        rgTipoMembresia = findViewById(R.id.rgTipoMembresia)
        rbSocio2 = findViewById(R.id.rbSocio2)
        rbNoSocio2 = findViewById(R.id.rbNoSocio2)
        etCampoMonto = findViewById(R.id.etCampoMonto)
        rgFormaPago = findViewById(R.id.rgFormaPago)
        rbEfectivo = findViewById(R.id.rbEfectivo)
        rbTarjeta = findViewById(R.id.rbTarjeta)
        btPagar = findViewById(R.id.btPagar)
        btComprobante = findViewById(R.id.btComprobante)
        btnVolver2 = findViewById(R.id.btnVolver2)
        txtDescuento = findViewById(R.id.txtDescuento)
        txtMontoFinal = findViewById(R.id.txtMontoFinal)

        // Configurar estado inicial
        txtDatosCliente.visibility = TextView.GONE
        btComprobante.visibility = Button.GONE
        txtDescuento.visibility = TextView.GONE
        txtMontoFinal.visibility = TextView.GONE

        // Establecer valores por defecto
        rbSocio2.isChecked = true
        rbEfectivo.isChecked = true
    }

    private fun configurarEventos() {
        icoBuscar.setOnClickListener {
            buscarClientePorDNI()
        }

        btPagar.setOnClickListener {
            registrarPago()
        }

        btComprobante.setOnClickListener {
            verComprobante()
        }

        btnVolver2.setOnClickListener {
            finish()
        }

        // Listener para cambios en forma de pago
        rgFormaPago.setOnCheckedChangeListener { _, checkedId ->
            calcularDescuento()
        }

        // Listener para cambios en el monto
        etCampoMonto.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                calcularDescuento()
            }
        })

        // Listener para cambiar dinámicamente según tipo de membresía
        rgTipoMembresia.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbSocio2 -> actualizarMontoPorMembresia("Socio")
                R.id.rbNoSocio2 -> actualizarMontoPorMembresia("No Socio")
            }
        }
    }

    private fun calcularDescuento() {
        try {
            val montoTexto = etCampoMonto.text.toString().trim()
            if (montoTexto.isEmpty()) {
                ocultarDescuento()
                return
            }

            montoOriginal = montoTexto.toDouble()

            // Aplicar 5% de descuento si es efectivo
            if (rbEfectivo.isChecked) {
                descuentoAplicado = montoOriginal * 0.05
                val montoFinal = montoOriginal - descuentoAplicado

                mostrarDescuento(montoOriginal, descuentoAplicado, montoFinal)
            } else {
                // No hay descuento para tarjeta
                descuentoAplicado = 0.0
                ocultarDescuento()
            }

        } catch (e: Exception) {
            ocultarDescuento()
        }
    }

    private fun mostrarDescuento(montoOriginal: Double, descuento: Double, montoFinal: Double) {
        val formatoMoneda = "%,.0f"

        txtDescuento.text = "💰 Descuento del 5%: -$${String.format(formatoMoneda, descuento)}"
        txtMontoFinal.text = "💵 Total a pagar: $${String.format(formatoMoneda, montoFinal)}"

        txtDescuento.visibility = TextView.VISIBLE
        txtMontoFinal.visibility = TextView.VISIBLE
    }

    private fun ocultarDescuento() {
        txtDescuento.visibility = TextView.GONE
        txtMontoFinal.visibility = TextView.GONE
    }

    private fun buscarClientePorDNI() {
        try {
            val dni = etCampoDNI.text.toString().trim()

            if (dni.isEmpty()) {
                Toast.makeText(this, "Ingrese un DNI", Toast.LENGTH_SHORT).show()
                return
            }

            // Simulación de búsqueda de cliente en base de datos futura
            when (dni) {
                "12345678" -> {
                    mostrarClienteEncontrado("Juan Pérez", "12345678")
                    actualizarMontoPorMembresia(if (rbSocio2.isChecked) "Socio" else "No Socio")
                }
                "87654321" -> {
                    mostrarClienteEncontrado("María González", "87654321")
                    actualizarMontoPorMembresia(if (rbSocio2.isChecked) "Socio" else "No Socio")
                }
                "11111111" -> {
                    mostrarClienteEncontrado("Carlos López", "11111111")
                    actualizarMontoPorMembresia(if (rbSocio2.isChecked) "Socio" else "No Socio")
                }
                else -> {
                    mostrarClienteNoEncontrado()
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error en búsqueda: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun mostrarClienteEncontrado(nombre: String, dni: String) {
        txtDatosCliente.text = nombre
        txtDatosCliente.visibility = TextView.VISIBLE
        Toast.makeText(this, "Cliente encontrado: $nombre", Toast.LENGTH_SHORT).show()
    }

    private fun mostrarClienteNoEncontrado() {
        txtDatosCliente.text = "Cliente no encontrado"
        txtDatosCliente.visibility = TextView.VISIBLE
        etCampoMonto.text.clear()
        btComprobante.visibility = Button.GONE
        ocultarDescuento()
        Toast.makeText(this, "Cliente no registrado", Toast.LENGTH_SHORT).show()
    }

    private fun actualizarMontoPorMembresia(tipoMembresia: String) {
        if (txtDatosCliente.visibility == TextView.VISIBLE && txtDatosCliente.text != "Cliente no encontrado") {
            val monto = when (tipoMembresia) {
                "Socio" -> "40000"
                "No Socio" -> "50000"
                else -> "0"
            }
            etCampoMonto.setText(monto)
            calcularDescuento()
        }
    }

    private fun registrarPago() {
        try {
            if (!validarCampos()) return

            val dni = etCampoDNI.text.toString().trim()
            val cliente = txtDatosCliente.text.toString()
            val montoOriginal = etCampoMonto.text.toString()
            val montoFinal = if (rbEfectivo.isChecked) {
                (montoOriginal.toDouble() * 0.95).toString()
            } else {
                montoOriginal
            }
            val tipoMembresia = if (rbSocio2.isChecked) "Socio" else "No Socio"
            val formaPago = if (rbEfectivo.isChecked) "Efectivo" else "Tarjeta"

            // Simular registro exitoso
            val mensaje = if (rbEfectivo.isChecked) {
                """
                ✅ Pago registrado exitosamente
                
                💰 Descuento del 5% aplicado
                💵 Monto original: $${montoOriginal}
                💵 Monto final: $${montoFinal}
                💳 Forma de pago: $formaPago
                👤 Cliente: $cliente
                🆔 DNI: $dni
                🏷️ Membresía: $tipoMembresia
                """.trimIndent()
            } else {
                """
                ✅ Pago registrado exitosamente
                
                💵 Monto: $${montoFinal}
                💳 Forma de pago: $formaPago
                👤 Cliente: $cliente
                🆔 DNI: $dni
                🏷️ Membresía: $tipoMembresia
                """.trimIndent()
            }

            Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()

            // Mostrar botón de comprobante
            btComprobante.visibility = Button.VISIBLE

        } catch (e: Exception) {
            Toast.makeText(this, "Error al registrar pago: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun validarCampos(): Boolean {
        // Validar DNI
        if (etCampoDNI.text.toString().trim().isEmpty()) {
            etCampoDNI.error = "Ingrese el DNI del cliente"
            etCampoDNI.requestFocus()
            return false
        }

        // Validar que se haya buscado un cliente válido
        if (txtDatosCliente.visibility != TextView.VISIBLE) {
            Toast.makeText(this, "Primero busque un cliente", Toast.LENGTH_SHORT).show()
            etCampoDNI.requestFocus()
            return false
        }

        if (txtDatosCliente.text == "Cliente no encontrado") {
            Toast.makeText(this, "Busque un cliente válido", Toast.LENGTH_SHORT).show()
            etCampoDNI.requestFocus()
            return false
        }

        // Validar monto
        if (etCampoMonto.text.toString().trim().isEmpty()) {
            etCampoMonto.error = "Ingrese el monto"
            etCampoMonto.requestFocus()
            return false
        }

        val monto = etCampoMonto.text.toString().toDoubleOrNull()
        if (monto == null || monto <= 0) {
            etCampoMonto.error = "Monto inválido"
            etCampoMonto.requestFocus()
            return false
        }

        return true
    }

    private fun verComprobante() {
        try {
            // Validar nuevamente antes de mostrar comprobante
            if (txtDatosCliente.visibility != TextView.VISIBLE ||
                txtDatosCliente.text == "Cliente no encontrado") {
                Toast.makeText(this, "Primero registre un pago válido", Toast.LENGTH_SHORT).show()
                return
            }

            val montoOriginal = etCampoMonto.text.toString()
            val montoFinal = if (rbEfectivo.isChecked) {
                (montoOriginal.toDouble() * 0.95).toString()
            } else {
                montoOriginal
            }

            val intent = Intent(this, ComprobantePagoActivity::class.java).apply {
                putExtra("cliente", txtDatosCliente.text.toString())
                putExtra("dni", etCampoDNI.text.toString())
                putExtra("monto", montoFinal)
                putExtra("montoOriginal", montoOriginal)
                putExtra("descuento", if (rbEfectivo.isChecked) "5%" else "0%")
                putExtra("formaPago", if (rbEfectivo.isChecked) "Efectivo" else "Tarjeta")
                putExtra("tipoMembresia", if (rbSocio2.isChecked) "Socio" else "No Socio")
                putExtra("concepto", "Cuota Mensual - ${if (rbSocio2.isChecked) "Socio" else "No Socio"}")
            }

            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Error al ver comprobante: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onResume() {
        super.onResume()
        // Mantener los datos del cliente pero resetear botón de comprobante
        btComprobante.visibility = Button.GONE
    }
}