package com.example.clubdeportivo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GestionActividadesActivity : AppCompatActivity() {

    private lateinit var rvActividades: RecyclerView
    private lateinit var etBuscarActividad: EditText
    private lateinit var btnBuscarActividad: ImageButton
    private lateinit var tvResultados: TextView
    private lateinit var btnNuevaActividad: Button
    private lateinit var btnExportar: Button
    private lateinit var btnVolverActividades: Button

    private lateinit var actividadAdapter: ActividadAdapter
    private var listaActividades = mutableListOf<Actividad>()
    private var listaFiltrada = mutableListOf<Actividad>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestion_actividades)

        inicializarVistas()
        configurarRecyclerView()
        cargarDatosEjemplo()
        configurarListeners()
    }

    private fun inicializarVistas() {
        rvActividades = findViewById(R.id.rvActividades)
        etBuscarActividad = findViewById(R.id.etBuscarActividad)
        btnBuscarActividad = findViewById(R.id.btnBuscarActividad)
        tvResultados = findViewById(R.id.tvResultados)
        btnNuevaActividad = findViewById(R.id.btnNuevaActividad)
        btnExportar = findViewById(R.id.btnExportar)
        btnVolverActividades = findViewById(R.id.btnVolverActividades)
    }

    private fun configurarRecyclerView() {
        actividadAdapter = ActividadAdapter(
            listaFiltrada,
            onEditarClickListener = { actividad ->
                editarActividad(actividad)
            },
            onEliminarClickListener = { actividad ->
                eliminarActividad(actividad)
            }
        )

        rvActividades.apply {
            layoutManager = LinearLayoutManager(this@GestionActividadesActivity)
            adapter = actividadAdapter
        }
    }

    private fun cargarDatosEjemplo() {
        listaActividades.clear()
        listaActividades.addAll(listOf(
            Actividad(1, "Fútbol 5", 40000.0, "Fútbol 5 en cancha sintética"),
            Actividad(2, "Natación", 50000.0, "Clases de natación en piscina olímpica"),
            Actividad(3, "Zumba", 30000.0, "Clases de zumba fitness"),
            Actividad(4, "Basquet", 20000.0, "Básquetbol en cancha profesional")
        ))

        listaFiltrada.clear()
        listaFiltrada.addAll(listaActividades)
        actividadAdapter.actualizarLista(listaFiltrada)
        actualizarContador()
    }

    private fun configurarListeners() {
        btnNuevaActividad.setOnClickListener {
            nuevaActividad()
        }

        btnExportar.setOnClickListener {
            exportarActividades()
        }

        btnVolverActividades.setOnClickListener {
            finish()
        }

        btnBuscarActividad.setOnClickListener {
            buscarActividades()
        }

        etBuscarActividad.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH) {
                buscarActividades()
                true
            } else {
                false
            }
        }
    }

    private fun nuevaActividad() {
        val intent = Intent(this, EditarActividadActivity::class.java)
        startActivityForResult(intent, REQUEST_NUEVA_ACTIVIDAD)
    }

    private fun editarActividad(actividad: Actividad) {
        val intent = Intent(this, EditarActividadActivity::class.java)
        intent.putExtra("actividad", actividad) // Serializable funciona ahora
        startActivityForResult(intent, REQUEST_EDITAR_ACTIVIDAD)
    }

    private fun eliminarActividad(actividad: Actividad) {
        listaActividades.remove(actividad)
        buscarActividades()
        Toast.makeText(this, "Actividad eliminada", Toast.LENGTH_SHORT).show()
    }

    private fun buscarActividades() {
        val query = etBuscarActividad.text.toString().trim().lowercase()

        listaFiltrada.clear()

        if (query.isEmpty()) {
            listaFiltrada.addAll(listaActividades)
        } else {
            listaFiltrada.addAll(listaActividades.filter {
                it.nombre.lowercase().contains(query)
            })
        }

        actividadAdapter.actualizarLista(listaFiltrada)
        actualizarContador()
    }

    private fun exportarActividades() {
        Toast.makeText(this, "Exportando actividades...", Toast.LENGTH_SHORT).show()
    }

    private fun actualizarContador() {
        tvResultados.text = "Actividades encontradas: ${listaFiltrada.size}"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_NUEVA_ACTIVIDAD -> {
                    val nuevaActividad = data?.getSerializableExtra("nueva_actividad") as? Actividad
                    nuevaActividad?.let {
                        listaActividades.add(it)
                        buscarActividades()
                    }
                }
                REQUEST_EDITAR_ACTIVIDAD -> {
                    val actividadActualizada = data?.getSerializableExtra("actividad_actualizada") as? Actividad
                    actividadActualizada?.let { actividad ->
                        val index = listaActividades.indexOfFirst { it.id == actividad.id }
                        if (index != -1) {
                            listaActividades[index] = actividad
                            buscarActividades()
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val REQUEST_NUEVA_ACTIVIDAD = 1
        const val REQUEST_EDITAR_ACTIVIDAD = 2
    }
}