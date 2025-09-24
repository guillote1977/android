package com.example.clubdeportivo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GestionProfesoresActivity : AppCompatActivity() {

    private lateinit var rvProfesores: RecyclerView
    private lateinit var etBuscarProfesor: EditText
    private lateinit var btnBuscarProfesor: ImageButton
    private lateinit var tvResultados: TextView
    private lateinit var btnNuevoProfesor: Button
    private lateinit var btnExportar: Button
    private lateinit var btnVolverProfesores: Button

    private lateinit var profesorAdapter: ProfesorAdapter
    private var listaProfesores = mutableListOf<Profesor>()
    private var listaFiltrada = mutableListOf<Profesor>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            setContentView(R.layout.activity_gestion_profesores)
            inicializarVistas()
            configurarRecyclerView() // ¡PRIMERO configurar el adapter!
            cargarDatosEjemplo()     // LUEGO cargar datos
            configurarListeners()
        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
            finish()
        }
    }

    private fun inicializarVistas() {
        rvProfesores = findViewById(R.id.rvProfesores)
        etBuscarProfesor = findViewById(R.id.etBuscarProfesor)
        btnBuscarProfesor = findViewById(R.id.btnBuscarProfesor)
        tvResultados = findViewById(R.id.tvResultados)
        btnNuevoProfesor = findViewById(R.id.btnNuevoProfesor)
        btnExportar = findViewById(R.id.btnExportar)
        btnVolverProfesores = findViewById(R.id.btnVolverProfesores)
    }

    private fun configurarRecyclerView() {
        // Inicializar el adapter PRIMERO con lista vacía
        profesorAdapter = ProfesorAdapter(
            emptyList(), // Lista vacía inicialmente
            onEditarClickListener = { profesor ->
                editarProfesor(profesor)
            },
            onEliminarClickListener = { profesor ->
                eliminarProfesor(profesor)
            }
        )

        rvProfesores.apply {
            layoutManager = LinearLayoutManager(this@GestionProfesoresActivity)
            adapter = profesorAdapter
        }
    }

    private fun cargarDatosEjemplo() {
        // Crear actividades de ejemplo directamente
        val futbol = Actividad(1, "Fútbol 5", 40000.0)
        val natacion = Actividad(2, "Natación", 50000.0)
        val zumba = Actividad(3, "Zumba", 30000.0)
        val basquet = Actividad(4, "Basquet", 20000.0)

        listaProfesores.clear()

        // Profesor 1
        listaProfesores.add(Profesor(
            1,
            "Gabriel Díaz",
            "31778889",
            listOf(futbol, natacion),
            "3115687458",
            "gabriel@club.com"
        ))

        // Profesor 2
        listaProfesores.add(Profesor(
            2,
            "Diego Fernandez",
            "29000111",
            listOf(zumba),
            "3224455667",
            "diego@club.com"
        ))

        // Profesor 3
        listaProfesores.add(Profesor(
            3,
            "Juan Perez",
            "28121987",
            listOf(basquet),
            "333778899",
            "juan@club.com"
        ))

        listaFiltrada.clear()
        listaFiltrada.addAll(listaProfesores)

        // Ahora sí actualizar el adapter con los datos
        profesorAdapter.actualizarLista(listaFiltrada)
        actualizarContador()
    }

    private fun configurarListeners() {
        btnNuevoProfesor.setOnClickListener {
            nuevoProfesor()
        }

        btnExportar.setOnClickListener {
            exportarProfesores()
        }

        btnVolverProfesores.setOnClickListener {
            finish()
        }

        btnBuscarProfesor.setOnClickListener {
            buscarProfesores()
        }

        etBuscarProfesor.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH) {
                buscarProfesores()
                true
            } else {
                false
            }
        }
    }

    private fun nuevoProfesor() {
        try {
            val intent = Intent(this, EditarProfesorActivity::class.java)
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun editarProfesor(profesor: Profesor) {
        try {
            val intent = Intent(this, EditarProfesorActivity::class.java)
            intent.putExtra("profesor", profesor)
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun eliminarProfesor(profesor: Profesor) {
        try {
            listaProfesores.remove(profesor)
            buscarProfesores()
            Toast.makeText(this, "Profesor eliminado", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Error eliminando profesor: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun buscarProfesores() {
        try {
            val query = etBuscarProfesor.text.toString().trim().lowercase()

            listaFiltrada.clear()

            if (query.isEmpty()) {
                listaFiltrada.addAll(listaProfesores)
            } else {
                listaFiltrada.addAll(listaProfesores.filter {
                    it.nombre.lowercase().contains(query) || it.dni.contains(query)
                })
            }

            profesorAdapter.actualizarLista(listaFiltrada)
            actualizarContador()
        } catch (e: Exception) {
            Toast.makeText(this, "Error buscando: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun exportarProfesores() {
        Toast.makeText(this, "Exportando lista de profesores...", Toast.LENGTH_SHORT).show()
    }

    private fun actualizarContador() {
        // Solo actualizar el texto, no tocar el adapter
        tvResultados.text = "Profesores encontrados: ${listaFiltrada.size}"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_NUEVO_PROFESOR -> {
                    val nuevoProfesor = data?.getSerializableExtra("nuevo_profesor") as? Profesor
                    nuevoProfesor?.let {
                        listaProfesores.add(it)
                        buscarProfesores()
                    }
                }
                REQUEST_EDITAR_PROFESOR -> {
                    val profesorActualizado = data?.getSerializableExtra("profesor_actualizado") as? Profesor
                    profesorActualizado?.let { profesor ->
                        val index = listaProfesores.indexOfFirst { it.id == profesor.id }
                        if (index != -1) {
                            listaProfesores[index] = profesor
                            buscarProfesores()
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val REQUEST_NUEVO_PROFESOR = 1
        const val REQUEST_EDITAR_PROFESOR = 2
    }
}