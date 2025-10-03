package com.example.clubdeportivo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class CarnetDigitalActivity : AppCompatActivity() {

    private lateinit var tvNombreCarnet: TextView
    private lateinit var tvDNICarnet: TextView
    private lateinit var tvNumeroCarnet: TextView
    private lateinit var tvMembresiaCarnet: TextView
    private lateinit var tvVencimientoCarnet: TextView
    private lateinit var tvInfoCarnet: TextView
    private lateinit var btnEnviarEmail: Button
    private lateinit var btnCompartirCarnet: Button
    private lateinit var btnVolverCarnet: Button
    private lateinit var btnVerDorso: Button

    private lateinit var carnet: Carnet
    private lateinit var cliente: Cliente

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carnet_digital_frente)

        // Obtener datos del intent
        cliente = intent.getSerializableExtra("CLIENTE") as? Cliente ?: run {
            Toast.makeText(this, "Error: Cliente no encontrado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Generar carnet para el cliente
        carnet = Carnet.generarParaCliente(cliente)

        inicializarVistas()
        configurarCarnet()
        configurarEventos()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Carnet Digital - ${cliente.nombre}"
    }

    private fun inicializarVistas() {
        tvNombreCarnet = findViewById(R.id.tvNombreCarnet)
        tvDNICarnet = findViewById(R.id.tvDNICarnet)
        tvNumeroCarnet = findViewById(R.id.tvNumeroCarnet)
        tvMembresiaCarnet = findViewById(R.id.tvMembresiaCarnet)
        tvVencimientoCarnet = findViewById(R.id.tvVencimientoCarnet)
        tvInfoCarnet = findViewById(R.id.tvInfoCarnet)
        btnEnviarEmail = findViewById(R.id.btnEnviarEmail)
        btnCompartirCarnet = findViewById(R.id.btnCompartirCarnet)
        btnVolverCarnet = findViewById(R.id.btnVolverCarnet)
        // Buscar el contenedor de botones para agregar el botón de dorso
        val containerAcciones = findViewById<LinearLayout>(R.id.containerAcciones)

        // Crear botón para ver dorso
        btnVerDorso = Button(this).apply {
            text = "📄 Ver Dorso del Carnet"
            setBackgroundResource(R.drawable.button_bg_deg)
            setTextColor(0xFF000000.toInt())

            // Layout params simplificado
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 16 // 16 pixels (suficiente para separación)
            }

            setOnClickListener {
                mostrarDorso()
            }
        }

        // Agregar botón al inicio del contenedor
        containerAcciones?.addView(btnVerDorso, 0)
    }

    private fun Int.dpToPx(): Int = (this * resources.displayMetrics.density).toInt()

    private fun configurarCarnet() {
        tvNombreCarnet.text = cliente.nombre.uppercase()
        tvDNICarnet.text = "ID: ${cliente.dni}"
        tvNumeroCarnet.text = carnet.numeroCarnet
        tvMembresiaCarnet.text = cliente.tipoMembresia.uppercase()
        tvVencimientoCarnet.text = "Vence: ${carnet.fechaVencimiento}"

        // Configurar estado del apto físico
        if (cliente.aptoFisico) {
            tvInfoCarnet.text = "Carnet activo. Acceso permitido a todas las instalaciones."
        } else {
            tvInfoCarnet.text = "Carnet provisional. Regularice el apto físico para acceso completo."
        }
    }

    private fun configurarEventos() {
        btnEnviarEmail.setOnClickListener {
            enviarCarnetPorEmail()
        }

        btnCompartirCarnet.setOnClickListener {
            compartirCarnet()
        }

        btnVolverCarnet.setOnClickListener {
            finish()
        }

        btnVerDorso.setOnClickListener {
            mostrarDorso()
        }
    }

    private fun mostrarDorso() {
        val intent = Intent(this, CarnetDorsoActivity::class.java)
        intent.putExtra("CLIENTE", cliente)
        intent.putExtra("CARNET", carnet)
        startActivity(intent)
    }

    private fun enviarCarnetPorEmail() {
        try {
            val emailIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_EMAIL, arrayOf(cliente.email))
                putExtra(Intent.EXTRA_SUBJECT, "🎫 Tu Carnet Digital - ONYX FITNESS")
                putExtra(Intent.EXTRA_TEXT, generarTextoEmail())
            }

            startActivity(Intent.createChooser(emailIntent, "Enviar carnet por email"))

        } catch (e: Exception) {
            Toast.makeText(this, "Error al enviar email: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun compartirCarnet() {
        try {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, generarTextoCompartir())
                type = "text/plain"
            }

            startActivity(Intent.createChooser(shareIntent, "Compartir carnet"))

        } catch (e: Exception) {
            Toast.makeText(this, "Error al compartir: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun generarTextoEmail(): String {
        return """
            Hola ${cliente.nombre},
            
            🎫 Adjuntamos tu carnet digital de ONYX FITNESS
            
            INFORMACIÓN DEL CARNET:
            • Nombre: ${cliente.nombre}
            • DNI: ${cliente.dni}
            • N° de Carnet: ${carnet.numeroCarnet}
            • Membresía: ${cliente.tipoMembresia}
            • Fecha emisión: ${carnet.fechaEmision}
            • Fecha vencimiento: ${carnet.fechaVencimiento}
            • Estado apto físico: ${if (cliente.aptoFisico) "VIGENTE" else "PENDIENTE"}
            
            Este carnet es necesario para acceder a nuestras instalaciones.
            Preséntalo en recepción al ingresar.
            
            💪 Gracias por ser parte de ONYX FITNESS!
            
            Saludos cordiales,
            El equipo de ONYX FITNESS
        """.trimIndent()
    }

    private fun generarTextoCompartir(): String {
        return """
            🎫 CARNET ONYX FITNESS
            
            👤 ${cliente.nombre}
            🆔 DNI: ${cliente.dni}
            🔢 N°: ${carnet.numeroCarnet}
            🏷️ ${cliente.tipoMembresia}
            📅 Vence: ${carnet.fechaVencimiento}
            ✅ Apto: ${if (cliente.aptoFisico) "SÍ" else "PENDIENTE"}
            
            💪 ONYX FITNESS - Club Deportivo
        """.trimIndent()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}