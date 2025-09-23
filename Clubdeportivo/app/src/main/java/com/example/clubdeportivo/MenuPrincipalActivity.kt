package com.example.clubdeportivo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.Toast

class MenuPrincipalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_principal)

        val btnLogout = findViewById<Button>(R.id.btnLogout)
        val gridMenu = findViewById<GridView>(R.id.gridMenu)

        // Lista de opciones del menú - ACTUALIZADA con Gestión de Actividades
        val menuItems = listOf(
            "Registrar CLIENTE" to android.R.drawable.ic_menu_add,
            "Gestionar CLIENTES" to android.R.drawable.ic_menu_manage,
            "Registrar PAGO" to android.R.drawable.ic_menu_edit,
            "Gestionar ACTIVIDADES" to android.R.drawable.ic_menu_day,
            "Gestionar USUARIOS" to android.R.drawable.ic_menu_my_calendar,
            "Gestionar Profesores" to android.R.drawable.ic_menu_compass
        )

        // Adaptador
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

        // Click listener ACTUALIZADO con Gestión de Actividades funcional
        gridMenu.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> {
                    // Registrar CLIENTE
                    val intent = Intent(this@MenuPrincipalActivity, RegistroClienteActivity::class.java)
                    startActivity(intent)
                }
                1 -> {
                    // Gestionar CLIENTES
                    val intent = Intent(this@MenuPrincipalActivity, GestionClientesActivity::class.java)
                    startActivity(intent)
                }
                2 -> {
                    // Registrar PAGO
                    val intent = Intent(this@MenuPrincipalActivity, RegistroPagoActivity::class.java)
                    startActivity(intent)
                }
                3 -> {
                    // Gestionar ACTIVIDADES - AHORA FUNCIONAL
                    val intent = Intent(this@MenuPrincipalActivity, GestionActividadesActivity::class.java)
                    startActivity(intent)
                }
                4 -> {
                    // Gestionar USUARIOS
                    Toast.makeText(this, "Gestionar Usuarios - Próximamente", Toast.LENGTH_SHORT).show()
                }
                5 -> {
                    // Gestionar Profesores
                    Toast.makeText(this, "Gestionar Profesores - Próximamente", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Botón de cerrar sesión
        btnLogout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}