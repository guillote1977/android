package com.example.clubdeportivo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.apply
import kotlin.collections.filter
import kotlin.collections.indexOfFirst
import kotlin.jvm.java
import kotlin.let
import kotlin.text.contains
import kotlin.text.isEmpty
import kotlin.text.lowercase
import kotlin.text.trim

class GestionUsuariosActivity : AppCompatActivity() {

    private lateinit var rvUsuarios: RecyclerView

    private lateinit var etBuscarUsuario: EditText
    private lateinit var btnBuscarUsuario: ImageButton
    private lateinit var tvResultados: TextView
    private lateinit var btnNuevoUsuario: Button
    private lateinit var btnExportar: Button
    private lateinit var btnVolverUsuario: Button

    private lateinit var usuarioAdapter: UsuarioAdapter

    private var listaUsuarios = mutableListOf<Usuario>()
    private var listaFiltrada = mutableListOf<Usuario>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            setContentView(R.layout.activity_gestion_usuarios)
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
        rvUsuarios = findViewById(R.id.rvUsuarios)
        etBuscarUsuario = findViewById(R.id.etBuscarUsuario)
        btnBuscarUsuario = findViewById(R.id.btnBuscarUsuario)
        tvResultados = findViewById(R.id.tvResultados)
        btnNuevoUsuario = findViewById(R.id.btnNuevoUsuario)
        btnExportar = findViewById(R.id.btnExportar)
        btnVolverUsuario = findViewById(R.id.btnVolverUsuario)
    }

    private fun configurarRecyclerView() {
        // Inicializar el adapter PRIMERO con lista vacía
        usuarioAdapter = UsuarioAdapter(
            emptyList(), // Lista vacía inicialmente
            onEditarClickListener = { usuario ->
                editarUsuario(usuario)
            },
            onEliminarClickListener = { usuario ->
                eliminarUsuario(usuario)
            }
        )

        rvUsuarios.apply {
            layoutManager = LinearLayoutManager(this@GestionUsuariosActivity)
            adapter = usuarioAdapter
        }
    }

    private fun cargarDatosEjemplo() {

        // Usuario 1
        listaUsuarios.add(Usuario(
            1,
            "Admin",
            "Administrador",
            "admin123",
            "admin@club.com"
        ))

        // Usuario 2
        listaUsuarios.add(Usuario(
            2,
            "Empleado",
            "Usuario", //rol
            "user1234", //pass
            "empleado@club.com"
        ))


        listaFiltrada.clear()
        listaFiltrada.addAll(listaUsuarios)

        // Ahora sí actualizar el adapter con los datos
        usuarioAdapter.actualizarLista(listaFiltrada)
        actualizarContador()
    }

    private fun configurarListeners() {
        btnNuevoUsuario.setOnClickListener {
            nuevoUsuario()
        }

        btnExportar.setOnClickListener {
            exportarUsuarios()
        }

        btnVolverUsuario.setOnClickListener {
            finish()
        }

        btnBuscarUsuario.setOnClickListener {
            buscarUsuario()
        }

        etBuscarUsuario.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                buscarUsuario()
                true
            } else {
                false
            }
        }
    }

    private fun nuevoUsuario() {
        try {
            val intent = Intent(this, EditarUsuarioActivity::class.java)
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun editarUsuario(usuario: Usuario) {
        try {
            val intent = Intent(this, EditarUsuarioActivity::class.java)
            intent.putExtra("usuario", usuario)
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun eliminarUsuario(usuario: Usuario) {
        try {
            listaUsuarios.remove(usuario)
            buscarUsuario()
            Toast.makeText(this, "Usuario eliminado", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Error eliminando usuario: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun buscarUsuario() {
        try {
            val query = etBuscarUsuario.text.toString().trim().lowercase()

            listaFiltrada.clear()

            if (query.isEmpty()) {
                listaFiltrada.addAll(listaUsuarios)
            } else {
                listaFiltrada.addAll(listaUsuarios.filter {
                    it.nombreUsuario.lowercase().contains(query) || it.rol.contains(query)
                })
            }

            usuarioAdapter.actualizarLista(listaFiltrada)
            actualizarContador()
        } catch (e: Exception) {
            Toast.makeText(this, "Error buscando: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun exportarUsuarios() {
        Toast.makeText(this, "Exportando lista de usuarios...", Toast.LENGTH_SHORT).show()
    }

    private fun actualizarContador() {
        // Solo actualizar el texto, no tocar el adapter
        tvResultados.text = "Usuarios encontrados: ${listaFiltrada.size}"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_NUEVO_USUARIO -> {
                    val nuevoUsuario = data?.getSerializableExtra("nuevo_usuario") as? Usuario
                    nuevoUsuario?.let {
                        listaUsuarios.add(it)
                        buscarUsuario()
                    }
                }
                REQUEST_EDITAR_USUARIO -> {
                    val usuarioActualizado = data?.getSerializableExtra("usuario_actualizado") as? Usuario
                    usuarioActualizado?.let { usuario ->
                        val index = listaUsuarios.indexOfFirst { it.id == usuario.id }
                        if (index != -1) {
                            listaUsuarios[index] = usuario
                            buscarUsuario()
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val REQUEST_NUEVO_USUARIO = 1
        const val REQUEST_EDITAR_USUARIO = 2
    }
}