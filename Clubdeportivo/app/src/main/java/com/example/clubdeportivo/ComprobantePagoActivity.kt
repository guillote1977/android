package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class ComprobantePagoActivity : AppCompatActivity() {

    private lateinit var txtNumeroComprobante: TextView
    private lateinit var txtCliente: TextView
    private lateinit var txtDNI: TextView
    private lateinit var txtFecha: TextView
    private lateinit var txtMonto: TextView
    private lateinit var txtFormaPago: TextView
    private lateinit var txtConcepto: TextView
    private lateinit var btnEnviarCliente: Button
    private lateinit var btnCompartir: Button
    private lateinit var btnVolverComprobante: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comprobante_pago)

        inicializarVistas()
        cargarDatosDelPago()
        configurarEventos()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Comprobante de Pago"
    }

    private fun inicializarVistas() {
        txtNumeroComprobante = findViewById(R.id.txtNumeroComprobante)
        txtCliente = findViewById(R.id.txtCliente)
        txtDNI = findViewById(R.id.txtDNI)
        txtFecha = findViewById(R.id.txtFecha)
        txtMonto = findViewById(R.id.txtMonto)
        txtFormaPago = findViewById(R.id.txtFormaPago)
        txtConcepto = findViewById(R.id.txtConcepto)
        btnEnviarCliente = findViewById(R.id.btnEnviarCliente)
        btnCompartir = findViewById(R.id.btnCompartir)
        btnVolverComprobante = findViewById(R.id.btnVolverComprobante)
    }

    private fun cargarDatosDelPago() {
        // Obtener datos del intent
        val cliente = intent.getStringExtra("cliente") ?: "Juan Pérez"
        val dni = intent.getStringExtra("dni") ?: "45454544"
        val monto = intent.getStringExtra("monto") ?: "40.000"
        val montoOriginal = intent.getStringExtra("montoOriginal") ?: monto
        val descuento = intent.getStringExtra("descuento") ?: "0%"
        val formaPago = intent.getStringExtra("formaPago") ?: "Efectivo"
        val concepto = intent.getStringExtra("concepto") ?: "Cuota Mensual"
        val tipoMembresia = intent.getStringExtra("tipoMembresia") ?: "Socio"

        // Generar número de comprobante aleatorio
        val numeroComprobante = String.format("%03d", (1..999).random())

        // Obtener fecha actual
        val fechaActual = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())

        // Establecer los datos en las vistas
        txtNumeroComprobante.text = numeroComprobante
        txtCliente.text = cliente
        txtDNI.text = dni
        txtFecha.text = fechaActual

        // Mostrar información del descuento si aplica
        if (descuento != "0%") {
            txtMonto.text = "$$monto (Descuento $descuento aplicado)"
        } else {
            txtMonto.text = "$$monto"
        }

        txtFormaPago.text = formaPago
        txtConcepto.text = "$concepto - $tipoMembresia"
    }

    private fun configurarEventos() {
        btnEnviarCliente.setOnClickListener {
            enviarAlCliente()
        }

        btnCompartir.setOnClickListener {
            compartirComprobante()
        }

        btnVolverComprobante.setOnClickListener {
            finish()
        }
    }

    private fun enviarAlCliente() {
        val cliente = txtCliente.text.toString()
        val monto = txtMonto.text.toString()

        Toast.makeText(this,
            "Comprobante enviado a $cliente\nMonto: $monto",
            Toast.LENGTH_LONG).show()
    }

    private fun compartirComprobante() {
        val comprobanteText = """
            🧾 COMPROBANTE DE PAGO - ONYX FITNESS 🏋️
            
            Nro: ${txtNumeroComprobante.text}
            Cliente: ${txtCliente.text}
            DNI: ${txtDNI.text}
            Fecha: ${txtFecha.text}
            Monto: ${txtMonto.text}
            Forma de Pago: ${txtFormaPago.text}
            Concepto: ${txtConcepto.text}
            
            ✅ Pago registrado exitosamente
            💪 Gracias por preferirnos
        """.trimIndent()

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, comprobanteText)
            type = "text/plain"
        }

        startActivity(Intent.createChooser(shareIntent, "Compartir comprobante"))
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}