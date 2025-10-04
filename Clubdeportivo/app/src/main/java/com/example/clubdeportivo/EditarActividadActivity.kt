package com.example.clubdeportivo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class EditarActividadActivity : AppCompatActivity() {

    private lateinit var textViewTitulo: TextView
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

        try {
            inicializarVistas()
            cargarDatosExistente()
            configurarListeners()
        } catch (e: Exception) {
            Log.e("EditarActividad", "Error en onCreate: ${e.message}")
            Toast.makeText(this, "Error al cargar la actividad", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun inicializarVistas() {
        textViewTitulo = findViewById(R.id.textViewNewActivity)
        etNombreActividad = findViewById(R.id.etNombreActividad)
        etPrecio = findViewById(R.id.etPrecio)
        etDescripcion = findViewById(R.id.etDescripcion)
        btnCancelar = findViewById(R.id.btnCancelar)
        btnGuardarActividad = findViewById(R.id.btnGuardarActividad)

        Log.d("EditarActividad", "Vistas inicializadas correctamente")
    }

    private fun cargarDatosExistente() {
        try {
            actividadExistente = intent.getSerializableExtra("actividad") as? Actividad

            if (actividadExistente != null) {
                esEdicion = true
                textViewTitulo.text = "Editar Actividad"
                etNombreActividad.setText(actividadExistente!!.nombre)
                etPrecio.setText(actividadExistente!!.precio.toInt().toString())
                etDescripcion.setText(actividadExistente!!.descripcion)
                Log.d("EditarActividad", "Cargando actividad existente: ${actividadExistente!!.nombre}")
            } else {
                textViewTitulo.text = "Nueva Actividad"
                Log.d("EditarActividad", "Creando nueva actividad")
            }
        } catch (e: Exception) {
            Log.e("EditarActividad", "Error al cargar datos: ${e.message}")
            Toast.makeText(this, "Error al cargar los datos", Toast.LENGTH_SHORT).show()
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
            etNombreActividad.requestFocus()
            return
        }

        if (precioTexto.isEmpty()) {
            mostrarError("El precio es obligatorio")
            etPrecio.requestFocus()
            return
        }

        val precio = try {
            precioTexto.toDouble()
        } catch (e: NumberFormatException) {
            mostrarError("El precio debe ser un número válido")
            etPrecio.requestFocus()
            return
        }

        if (precio <= 0) {
            mostrarError("El precio debe ser mayor a 0")
            etPrecio.requestFocus()
            return
        }

        try {
            val resultadoIntent = Intent()
            if (esEdicion) {
                val actividadActualizada = actividadExistente!!.copy(
                    nombre = nombre,
                    precio = precio,
                    descripcion = descripcion  // ✅ Ya no hay problema de tipos
                )
                resultadoIntent.putExtra("actividad_actualizada", actividadActualizada)
                Toast.makeText(this, "Actividad actualizada correctamente", Toast.LENGTH_SHORT).show()
                Log.d("EditarActividad", "Actividad actualizada: $nombre")
            } else {
                val nuevaActividad = Actividad(
                    id = (System.currentTimeMillis() % Int.MAX_VALUE).toInt(),
                    nombre = nombre,
                    precio = precio,
                    descripcion = descripcion  // ✅ Ya no hay problema de tipos
                )
                resultadoIntent.putExtra("nueva_actividad", nuevaActividad)
                Toast.makeText(this, "Actividad creada correctamente", Toast.LENGTH_SHORT).show()
                Log.d("EditarActividad", "Nueva actividad creada: $nombre")
            }

            setResult(RESULT_OK, resultadoIntent)
            finish()
        } catch (e: Exception) {
            Log.e("EditarActividad", "Error al guardar actividad: ${e.message}")
            mostrarError("Error al guardar la actividad")
        }
    }

    private fun mostrarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}