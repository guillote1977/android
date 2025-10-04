package com.example.clubdeportivo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import java.io.Serializable

class EditarProfesorActivity : AppCompatActivity() {

    private lateinit var tvTitulo: TextView
    private lateinit var etNombreProfesor: EditText
    private lateinit var etDNI: EditText
    private lateinit var etTelefono: EditText
    private lateinit var etEmail: EditText
    private lateinit var containerActividades: LinearLayout
    private lateinit var btnCancelar: Button
    private lateinit var btnGuardarProfesor: Button

    private var profesorExistente: Profesor? = null
    private var esEdicion = false
    private var listaActividades = mutableListOf<Actividad>()
    private var actividadesSeleccionadas = mutableListOf<Actividad>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_profesor)

        inicializarVistas()
        cargarActividadesEjemplo() // Cargar actividades disponibles
        cargarDatosExistente()
        configurarCheckboxesActividades() // Configurar los checkboxes
        configurarListeners()
    }

    private fun inicializarVistas() {
        tvTitulo = findViewById(R.id.textViewNewTeacher)
        etNombreProfesor = findViewById(R.id.etNombreProfesor)
        etDNI = findViewById(R.id.etDNI)
        etTelefono = findViewById(R.id.etTelefono)
        etEmail = findViewById(R.id.etEmail)
        containerActividades = findViewById(R.id.containerActividades)
        btnCancelar = findViewById(R.id.btnCancelar)
        btnGuardarProfesor = findViewById(R.id.btnGuardarProfesor)
    }

    private fun cargarActividadesEjemplo() {
        // Cargar las mismas actividades que en GestionProfesoresActivity
        listaActividades.clear()
        listaActividades.addAll(listOf(
            Actividad(1, "Fútbol 5", 40000.0),
            Actividad(2, "Natación", 50000.0),
            Actividad(3, "Zumba", 30000.0),
            Actividad(4, "Basquet", 20000.0)
        ))
    }

    private fun cargarDatosExistente() {
        profesorExistente = intent.getSerializableExtra("profesor") as? Profesor

        if (profesorExistente != null) {
            esEdicion = true
            tvTitulo.text = "Editar Profesor"
            etNombreProfesor.setText(profesorExistente!!.nombre)
            etDNI.setText(profesorExistente!!.dni)
            etTelefono.setText(profesorExistente!!.telefono)
            etEmail.setText(profesorExistente!!.email)
            actividadesSeleccionadas.addAll(profesorExistente!!.actividades)
        } else {
            tvTitulo.text = "Nuevo Profesor"
        }
    }

    private fun configurarCheckboxesActividades() {
        containerActividades.removeAllViews()

        if (listaActividades.isEmpty()) {
            val textView = TextView(this)
            textView.text = "No hay actividades disponibles"
            textView.setTextColor(getColor(android.R.color.white))
            containerActividades.addView(textView)
            return
        }

        // Título de la sección
        val titulo = TextView(this)
        titulo.text = "Selecciona las actividades:"
        titulo.setTextColor(getColor(android.R.color.white))
        titulo.textSize = 14f
        containerActividades.addView(titulo)

        // Crear un checkbox por cada actividad
        listaActividades.forEach { actividad ->
            val checkBox = CheckBox(this)
            checkBox.text = "${actividad.nombre} - ${actividad.getPrecioFormateado()}"
            checkBox.setTextColor(getColor(android.R.color.white))

            // Marcar como seleccionado si la actividad está en la lista del profesor
            val estaSeleccionada = actividadesSeleccionadas.any { it.id == actividad.id }
            checkBox.isChecked = estaSeleccionada

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    if (!actividadesSeleccionadas.any { it.id == actividad.id }) {
                        actividadesSeleccionadas.add(actividad)
                    }
                } else {
                    actividadesSeleccionadas.removeAll { it.id == actividad.id }
                }
            }

            // Agregar margen inferior para separación
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(0, 0, 0, 16)
            checkBox.layoutParams = layoutParams

            containerActividades.addView(checkBox)
        }
    }

    private fun actualizarContadorActividades() {
        // Buscar el TextView del contador o crearlo si no existe
        var contadorView: TextView? = null
        for (i in 0 until containerActividades.childCount) {
            val view = containerActividades.getChildAt(i)
            if (view is TextView && view.id == R.id.tvContadorActividades) {
                contadorView = view
                break
            }
        }

        if (contadorView == null) {
            contadorView = TextView(this)
            contadorView.id = R.id.tvContadorActividades
            contadorView.setTextColor(getColor(android.R.color.white))
            contadorView.textSize = 12f
            containerActividades.addView(contadorView, 0) // Agregar al inicio
        }

        contadorView.text = "Actividades seleccionadas: ${actividadesSeleccionadas.size}"
    }

    private fun configurarListeners() {
        btnCancelar.setOnClickListener {
            finish()
        }

        btnGuardarProfesor.setOnClickListener {
            guardarProfesor()
        }
    }

    private fun guardarProfesor() {
        val nombre = etNombreProfesor.text.toString().trim()
        val dni = etDNI.text.toString().trim()
        val telefono = etTelefono.text.toString().trim()
        val email = etEmail.text.toString().trim()

        if (nombre.isEmpty()) {
            mostrarError("El nombre del profesor es obligatorio")
            return
        }

        if (dni.isEmpty()) {
            mostrarError("El DNI es obligatorio")
            return
        }

        if (telefono.isEmpty()) {
            mostrarError("El teléfono es obligatorio")
            return
        }

        val resultadoIntent = Intent()
        if (esEdicion) {
            val profesorActualizado = profesorExistente!!.copy(
                nombre = nombre,
                dni = dni,
                telefono = telefono,
                email = email,
                actividades = actividadesSeleccionadas.toList()
            )
            resultadoIntent.putExtra("profesor_actualizado", profesorActualizado)
            Toast.makeText(this, "Profesor actualizado correctamente", Toast.LENGTH_SHORT).show()
        } else {
            val nuevoProfesor = Profesor(
                id = (System.currentTimeMillis() % Int.MAX_VALUE).toInt(),
                nombre = nombre,
                dni = dni,
                telefono = telefono,
                email = email,
                actividades = actividadesSeleccionadas.toList()
            )
            resultadoIntent.putExtra("nuevo_profesor", nuevoProfesor)
            Toast.makeText(this, "Profesor creado correctamente", Toast.LENGTH_SHORT).show()
        }

        setResult(RESULT_OK, resultadoIntent)
        finish()
    }

    private fun mostrarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}