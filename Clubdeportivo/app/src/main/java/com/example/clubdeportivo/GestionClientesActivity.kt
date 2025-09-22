package com.example.clubdeportivo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class GestionClientesActivity : AppCompatActivity() {

    private lateinit var etBuscarCliente: EditText
    private lateinit var btnBuscar: ImageButton
    private lateinit var rvClientes: RecyclerView
    private lateinit var tvResultados: TextView
    private lateinit var btnNuevoCliente: Button
    private lateinit var btnEditarCliente: Button
    private lateinit var btnVolverGestion: Button
    private lateinit var btnCompartirLista: Button

    // Botones de filtro
    private lateinit var btnTodos: Button
    private lateinit var btnMorosos: Button
    private lateinit var btnPorVencer: Button
    private lateinit var btnSinApto: Button
    private lateinit var btnActivos: Button
    private lateinit var btnInactivos: Button

    private lateinit var clienteAdapter: ClienteAdapter
    private var listaClientes = mutableListOf<Cliente>()
    private var listaClientesCompleta = mutableListOf<Cliente>()
    private var clienteSeleccionado: Cliente? = null
    private var filtroActual = "Todos"

    companion object {
        private const val REQUEST_EDITAR_CLIENTE = 1001
        private const val REQUEST_NUEVO_CLIENTE = 1002
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestion_clientes)

        inicializarVistas()
        configurarRecyclerView()
        configurarEventos()
        cargarClientesEjemplo()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Gestión de Clientes"
    }

    private fun inicializarVistas() {
        etBuscarCliente = findViewById(R.id.etBuscarCliente)
        btnBuscar = findViewById(R.id.btnBuscar)
        rvClientes = findViewById(R.id.rvClientes)
        tvResultados = findViewById(R.id.tvResultados)
        btnNuevoCliente = findViewById(R.id.btnNuevoCliente)
        btnEditarCliente = findViewById(R.id.btnEditarCliente)
        btnVolverGestion = findViewById(R.id.btnVolverGestion)
        btnCompartirLista = findViewById(R.id.btnCompartirLista)

        // Filtros
        btnTodos = findViewById(R.id.btnTodos)
        btnMorosos = findViewById(R.id.btnMorosos)
        btnPorVencer = findViewById(R.id.btnPorVencer)
        btnSinApto = findViewById(R.id.btnSinApto)
        btnActivos = findViewById(R.id.btnActivos)
        btnInactivos = findViewById(R.id.btnInactivos)
    }

    private fun configurarRecyclerView() {
        clienteAdapter = ClienteAdapter(listaClientes) { cliente ->
            clienteSeleccionado = cliente
            mostrarClienteSeleccionado(cliente)
        }

        rvClientes.apply {
            layoutManager = LinearLayoutManager(this@GestionClientesActivity)
            adapter = clienteAdapter
        }
    }

    private fun mostrarClienteSeleccionado(cliente: Cliente) {
        Toast.makeText(this, "Seleccionado: ${cliente.nombre} - DNI: ${cliente.dni}", Toast.LENGTH_SHORT).show()
    }

    private fun configurarEventos() {
        btnBuscar.setOnClickListener {
            buscarCliente()
        }

        etBuscarCliente.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH) {
                buscarCliente()
                true
            } else {
                false
            }
        }

        btnNuevoCliente.setOnClickListener {
            val intent = Intent(this, RegistroClienteActivity::class.java)
            startActivityForResult(intent, REQUEST_NUEVO_CLIENTE)
        }

        btnEditarCliente.setOnClickListener {
            clienteSeleccionado?.let { cliente ->
                val intent = Intent(this, EditarClienteActivity::class.java)
                intent.putExtra("CLIENTE", cliente)
                startActivityForResult(intent, REQUEST_EDITAR_CLIENTE)
            } ?: run {
                Toast.makeText(this, "Seleccione un cliente primero", Toast.LENGTH_SHORT).show()
            }
        }

        btnVolverGestion.setOnClickListener {
            val intent = Intent(this, MenuPrincipalActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        btnCompartirLista.setOnClickListener {
            compartirListaClientes()
        }

        // Configurar filtros
        btnTodos.setOnClickListener { aplicarFiltro("Todos") }
        btnMorosos.setOnClickListener { aplicarFiltro("Moroso") }
        btnPorVencer.setOnClickListener { aplicarFiltro("Por Vencer") }
        btnSinApto.setOnClickListener { aplicarFiltro("Sin Apto Físico") }
        btnActivos.setOnClickListener { aplicarFiltro("Activo") }
        btnInactivos.setOnClickListener { aplicarFiltro("Inactivo") }
    }

    private fun cargarClientesEjemplo() {
        listaClientesCompleta.clear()
        listaClientesCompleta.addAll(Cliente.getClientesEjemplo())

        aplicarFiltro(filtroActual)
        actualizarContador()
    }

    private fun buscarCliente() {
        val textoBusqueda = etBuscarCliente.text.toString().trim().lowercase()

        if (textoBusqueda.isEmpty()) {
            aplicarFiltro(filtroActual)
        } else {
            val resultados = listaClientesCompleta.filter { cliente ->
                cliente.dni.contains(textoBusqueda, ignoreCase = true) ||
                        cliente.nombre.lowercase().contains(textoBusqueda) ||
                        cliente.id.toString().contains(textoBusqueda) ||
                        cliente.email.lowercase().contains(textoBusqueda)
            }
            // Usar la lista actual del adapter directamente
            listaClientes.clear()
            listaClientes.addAll(resultados)
            clienteAdapter.actualizarLista(listaClientes)
        }
        actualizarContador()
    }

    private fun aplicarFiltro(filtro: String) {
        filtroActual = filtro

        resetearBotonesFiltro()

        when (filtro) {
            "Todos" -> btnTodos.setBackgroundResource(R.drawable.button_filter_active)
            "Moroso" -> btnMorosos.setBackgroundResource(R.drawable.button_filter_active)
            "Por Vencer" -> btnPorVencer.setBackgroundResource(R.drawable.button_filter_active)
            "Sin Apto Físico" -> btnSinApto.setBackgroundResource(R.drawable.button_filter_active)
            "Activo" -> btnActivos.setBackgroundResource(R.drawable.button_filter_active)
            "Inactivo" -> btnInactivos.setBackgroundResource(R.drawable.button_filter_active)
        }

        val clientesFiltrados = when (filtro) {
            "Todos" -> listaClientesCompleta
            else -> listaClientesCompleta.filter { it.estado == filtro }
        }

        listaClientes.clear()
        listaClientes.addAll(clientesFiltrados)
        clienteAdapter.actualizarLista(listaClientes)
        actualizarContador()
    }

    private fun resetearBotonesFiltro() {
        val botones = listOf(btnTodos, btnMorosos, btnPorVencer, btnSinApto, btnActivos, btnInactivos)
        botones.forEach { boton ->
            boton.setBackgroundResource(R.drawable.button_filter_inactive)
        }
    }

    private fun actualizarContador() {
        val cantidad = listaClientes.size
        tvResultados.text = "Clientes encontrados: $cantidad"
    }

    // ================== FUNCIONALIDAD COMPARTIR TEXTO ==================

    private fun compartirListaClientes() {
        try {
            val clientesFiltrados = obtenerClientesParaExportar()
            if (clientesFiltrados.isEmpty()) {
                Toast.makeText(this, "No hay clientes para compartir", Toast.LENGTH_SHORT).show()
                return
            }

            val textoCompartir = generarTextoListaClientes(clientesFiltrados)

            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, textoCompartir)
                putExtra(Intent.EXTRA_SUBJECT, "Lista de Clientes - ONYX FITNESS")
                type = "text/plain"
            }

            startActivity(Intent.createChooser(shareIntent, "Compartir lista de clientes"))
            Toast.makeText(this, "Compartiendo lista de clientes...", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            Toast.makeText(this, "Error al compartir: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun obtenerClientesParaExportar(): List<Cliente> {
        // Si la lista actual está vacía, usar la lista completa
        return if (listaClientes.isNotEmpty()) {
            listaClientes
        } else {
            // Si no hay filtro aplicado, usar lista completa
            listaClientesCompleta
        }
    }

    private fun generarTextoListaClientes(clientes: List<Cliente>): String {
        val fecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())

        val builder = StringBuilder()
        builder.append("🏋️ LISTA DE CLIENTES - ONYX FITNESS\n")
        builder.append("══════════════════════════════════════\n")
        builder.append("📅 Fecha: $fecha\n")
        builder.append("🔍 Filtro: $filtroActual\n")
        builder.append("👥 Total: ${clientes.size} clientes\n")
        builder.append("══════════════════════════════════════\n\n")

        clientes.forEachIndexed { index, cliente ->
            builder.append("${index + 1}. ${cliente.nombre}\n")
            builder.append("   🆔 DNI: ${cliente.dni}\n")
            builder.append("   📧 ${cliente.email}\n")
            builder.append("   📞 ${cliente.telefono}\n")
            builder.append("   🏷️ ${cliente.tipoMembresia}\n")
            builder.append("   📊 ${cliente.estado}\n")
            builder.append("   🏥 Apto: ${if (cliente.aptoFisico) "SÍ" else "NO"}\n")
            builder.append("   📅 Vence: ${cliente.fechaVencimiento}\n")

            if (index < clientes.size - 1) {
                builder.append("   ──────────────────────────\n")
            }
        }

        builder.append("\n══════════════════════════════════════\n")
        builder.append("💪 ONYX FITNESS - Club Deportivo\n")

        return builder.toString()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_EDITAR_CLIENTE -> {
                if (resultCode == RESULT_OK) {
                    val clienteActualizado = data?.getSerializableExtra("CLIENTE_ACTUALIZADO") as? Cliente
                    clienteActualizado?.let { cliente ->
                        actualizarClienteEnLista(cliente)
                        Toast.makeText(this, "✅ Cliente actualizado: ${cliente.nombre}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            REQUEST_NUEVO_CLIENTE -> {
                if (resultCode == RESULT_OK) {
                    cargarClientesEjemplo()
                    Toast.makeText(this, "✅ Nuevo cliente registrado", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun actualizarClienteEnLista(clienteActualizado: Cliente) {
        val index = listaClientesCompleta.indexOfFirst { it.id == clienteActualizado.id }
        if (index != -1) {
            listaClientesCompleta[index] = clienteActualizado
            aplicarFiltro(filtroActual)
        }
    }

    override fun onResume() {
        super.onResume()
        cargarClientesEjemplo()
    }

    override fun onSupportNavigateUp(): Boolean {
        val intent = Intent(this, MenuPrincipalActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
        return true
    }
}