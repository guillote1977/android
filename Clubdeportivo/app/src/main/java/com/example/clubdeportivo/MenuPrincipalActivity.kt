package com.example.clubdeportivo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*

class MenuPrincipalActivity : AppCompatActivity() {

    private lateinit var gridMenu: GridView
    private lateinit var btnLogout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_principal)

        initializeViews()
        setupGridMenu()
        setupLogoutButton()
    }

    private fun initializeViews() {
        gridMenu = findViewById(R.id.gridMenu)
        btnLogout = findViewById(R.id.btnLogout)
    }

    private fun setupGridMenu() {
        val menuItems = listOf(
            "Registrar CLIENTE" to android.R.drawable.ic_menu_add,
            "Gestionar CLIENTES" to android.R.drawable.ic_menu_manage,
            "Gestionar PAGO" to android.R.drawable.ic_menu_edit,
            "Gestionar ACTIVIDADES" to android.R.drawable.ic_menu_day,
            "Gestionar USUARIOS" to android.R.drawable.ic_menu_my_calendar,
            "Gestionar Profesores" to android.R.drawable.ic_menu_compass,
            "Asistencia Profesores" to android.R.drawable.ic_menu_agenda
        )

        gridMenu.adapter = object : BaseAdapter() {
            override fun getCount(): Int = menuItems.size
            override fun getItem(position: Int): Any = menuItems[position]
            override fun getItemId(position: Int): Long = position.toLong()

            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = convertView ?: layoutInflater.inflate(R.layout.grid_item_menu, parent, false)

                val (title, iconRes) = menuItems[position]

                view.findViewById<ImageView>(R.id.iconMenu).setImageResource(iconRes)
                view.findViewById<TextView>(R.id.textMenu).text = title

                return view
            }
        }

        gridMenu.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            handleMenuItemClick(position)
        }
    }

    private fun handleMenuItemClick(position: Int) {
        when (position) {
            0 -> navigateToRegistroCliente()
            1 -> showToast("Gestionar Clientes")
            2 -> showToast("Gestionar Pagos")
            3 -> showToast("Gestionar Actividades")
            4 -> showToast("Gestionar Usuarios")
            5 -> showToast("Gestionar Profesores")
            6 -> showToast("Asistencia Profesores")
        }
    }

    private fun navigateToRegistroCliente() {
        val intent = Intent(this, RegistroClienteActivity::class.java)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setupLogoutButton() {
        btnLogout.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        // Minimizar la aplicación en lugar de cerrarla
        moveTaskToBack(true)
    }
}