package com.example.clubdeportivo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class EditarActividadActivity : AppCompatActivity() {

    private lateinit var tvTitulo: TextView
    private lateinit var etNombreActividad: EditText
    private lateinit var etPrecio: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var btnCancelar: Button
    private lateinit var btnGuardarActividad: Button

    private var actividadExistente: Actividad? = null
    private var esEdicion = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_actividad)

        inicializarVistas()
        cargarDatosExistente()
        configurarListeners()
    }

    private fun inicializarVistas() {
        tvTitulo = findViewById(R.id.tvTitulo)
        etNombreActividad = findViewById(R.id.etNombreActividad)
        etPrecio = findViewById(R.id.etPrecio)
        etDescripcion = findViewById(R.id.etDescripcion)
        btnCancelar = findViewById(R.id.btnCancelar)
        btnGuardarActividad = findViewById(R.id.btnGuardarActividad)
    }

    private fun cargarDatosExistente() {
        actividadExistente = intent.getSerializableExtra("actividad") as? Actividad

        if (actividadExistente != null) {
            esEdicion = true
            tvTitulo.text = "Editar Actividad"
            etNombreActividad.setText(actividadExistente!!.nombre)
            etPrecio.setText(actividadExistente!!.precio.toInt().toString())
            etDescripcion.setText(actividadExistente!!.descripcion)
        } else {
            tvTitulo.text = "Nueva Actividad"
        }
    }

    private fun configurarListeners() {
        btnCancelar.setOnClickListener {
            finish()
        }

        btnGuardarActividad.setOnClickListener {
            guardarActividad()
        }
    }

    private fun guardarActividad() {
        val nombre = etNombreActividad.text.toString().trim()
        val precioTexto = etPrecio.text.toString().trim()
        val descripcion = etDescripcion.text.toString().trim()

        if (nombre.isEmpty()) {
            mostrarError("El nombre de la actividad es obligatorio")
            return
        }

        if (precioTexto.isEmpty()) {
            mostrarError("El precio es obligatorio")
            return
        }

        val precio = try {
            precioTexto.toDouble()
        } catch (e: NumberFormatException) {
            mostrarError("El precio debe ser un número válido")
            return
        }

        if (precio <= 0) {
            mostrarError("El precio debe ser mayor a 0")
            return
        }

        val resultadoIntent = Intent()
        if (esEdicion) {
            val actividadActualizada = actividadExistente!!.copy(
                nombre = nombre,
                precio = precio,
                descripcion = descripcion
            )
            resultadoIntent.putExtra("actividad_actualizada", actividadActualizada)
            Toast.makeText(this, "Actividad actualizada correctamente", Toast.LENGTH_SHORT).show()
        } else {
            val nuevaActividad = Actividad(
                id = (System.currentTimeMillis() % Int.MAX_VALUE).toInt(),
                nombre = nombre,
                precio = precio,
                descripcion = descripcion
            )
            resultadoIntent.putExtra("nueva_actividad", nuevaActividad)
            Toast.makeText(this, "Actividad creada correctamente", Toast.LENGTH_SHORT).show()
        }

        setResult(RESULT_OK, resultadoIntent)
        finish()
    }

    private fun mostrarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}