package com.example.clubdeportivo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class CarnetDorsoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carnet_digital_dorso)

        val cliente = intent.getSerializableExtra("CLIENTE") as? Cliente
        val carnet = intent.getSerializableExtra("CARNET") as? Carnet

        if (cliente != null && carnet != null) {
            configurarDorso(cliente, carnet)
        } else {
            Toast.makeText(this, "Error: Datos del carnet no encontrados", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        configurarEventos()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Dorso Carnet - ${cliente.nombre}"
    }

    private fun configurarDorso(cliente: Cliente, carnet: Carnet) {
        val tvDNI = findViewById<TextView>(R.id.tvDNICarnet)
        val tvVencimiento = findViewById<TextView>(R.id.tvVencimientoCarnet)
        val tvNumero = findViewById<TextView>(R.id.tvNumeroCarnet)

        tvDNI.text = "ID: ${cliente.dni}"
        tvVencimiento.text = "Vence: ${carnet.fechaVencimiento}"
        tvNumero.text = carnet.numeroCarnet
    }

    private fun configurarEventos() {
        val btnVolver = findViewById<Button>(R.id.btnVolverCarnet)
        btnVolver.setOnClickListener {
            finish()
        }

        val btnEnviarEmail = findViewById<Button>(R.id.btnEnviarEmail)
        btnEnviarEmail.setOnClickListener {
            // Reutilizar la lógica de envío de email
            enviarCarnetPorEmail()
        }

        val btnCompartir = findViewById<Button>(R.id.btnCompartirCarnet)
        btnCompartir.setOnClickListener {
            // Reutilizar la lógica de compartir
            compartirCarnet()
        }

        // Agregar botón para volver al frente - VERSIÓN CORREGIDA
        val container = findViewById<LinearLayout>(R.id.containerAcciones)
        if (container != null) {
            val btnVerFrente = Button(this).apply {
                text = "📄 Ver Frente del Carnet"
                setBackgroundResource(R.drawable.button_primary)
                setTextColor(0xFFFFFFFF.toInt())
                setOnClickListener {
                    finish() // Volver a la actividad anterior (frente)
                }

                // Configurar layout params
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    bottomMargin = 8 // 8dp
                }
            }

            // Agregar al inicio del contenedor
            container.addView(btnVerFrente, 0)
        }
    }

    private fun enviarCarnetPorEmail() {
        val cliente = intent.getSerializableExtra("CLIENTE") as? Cliente
        val carnet = intent.getSerializableExtra("CARNET") as? Carnet

        if (cliente != null && carnet != null) {
            try {
                val emailIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(cliente.email))
                    putExtra(Intent.EXTRA_SUBJECT, "🎫 Tu Carnet Digital - ONYX FITNESS (Dorso)")
                    putExtra(Intent.EXTRA_TEXT, generarTextoEmail(cliente, carnet))
                }

                startActivity(Intent.createChooser(emailIntent, "Enviar dorso del carnet por email"))

            } catch (e: Exception) {
                Toast.makeText(this, "Error al enviar email: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun compartirCarnet() {
        val cliente = intent.getSerializableExtra("CLIENTE") as? Cliente
        val carnet = intent.getSerializableExtra("CARNET") as? Carnet

        if (cliente != null && carnet != null) {
            try {
                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, generarTextoCompartir(cliente, carnet))
                    type = "text/plain"
                }

                startActivity(Intent.createChooser(shareIntent, "Compartir dorso del carnet"))

            } catch (e: Exception) {
                Toast.makeText(this, "Error al compartir: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun generarTextoEmail(cliente: Cliente, carnet: Carnet): String {
        return """
            Hola ${cliente.nombre},
            
            🎫 Adjuntamos el DORSO de tu carnet digital de ONYX FITNESS
            
            INFORMACIÓN DEL CLUB:
            • Email: info@onyxfitness.com
            • Teléfono: +54 911 4658 745
            • Dirección: Av. Imagine 745
            
            INFORMACIÓN DE TU CARNET:
            • Nombre: ${cliente.nombre}
            • DNI: ${cliente.dni}
            • N° de Carnet: ${carnet.numeroCarnet}
            • Vence: ${carnet.fechaVencimiento}
            
            💪 Gracias por ser parte de ONYX FITNESS!
            
            Saludos cordiales,
            El equipo de ONYX FITNESS
        """.trimIndent()
    }

    private fun generarTextoCompartir(cliente: Cliente, carnet: Carnet): String {
        return """
            🎫 DORSO CARNET ONYX FITNESS
            
            👤 ${cliente.nombre}
            🆔 DNI: ${cliente.dni}
            🔢 N°: ${carnet.numeroCarnet}
            📅 Vence: ${carnet.fechaVencimiento}
            
            📧 info@onyxfitness.com
            📞 +54 911 4658 745
            🏠 Av. Imagine 745
            
            💪 ONYX FITNESS - Club Deportivo
        """.trimIndent()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}