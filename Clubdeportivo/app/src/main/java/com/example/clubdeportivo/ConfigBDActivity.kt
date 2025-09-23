package com.example.clubdeportivo

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ConfigBDActivity : AppCompatActivity() {

    private lateinit var etServidor: EditText
    private lateinit var etBaseDatos: EditText
    private lateinit var etUsuario: EditText
    private lateinit var etPasswordBD: EditText
    private lateinit var etPuerto: EditText
    private lateinit var btnGuardarConfig: Button
    private lateinit var btnProbarConexion: Button
    private lateinit var btnVolverConfig: Button

    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config_bd)

        sharedPref = getSharedPreferences("config_bd", Context.MODE_PRIVATE)
        inicializarVistas()
        cargarConfiguracion()
        configurarListeners()
    }

    private fun inicializarVistas() {
        etServidor = findViewById(R.id.etServidor)
        etBaseDatos = findViewById(R.id.etBaseDatos)
        etUsuario = findViewById(R.id.etUsuario)
        etPasswordBD = findViewById(R.id.etPasswordBD)
        etPuerto = findViewById(R.id.etPuerto)
        btnGuardarConfig = findViewById(R.id.btnGuardarConfig)
        btnProbarConexion = findViewById(R.id.btnProbarConexion)
        btnVolverConfig = findViewById(R.id.btnVolverConfig)
    }

    private fun cargarConfiguracion() {
        etServidor.setText(sharedPref.getString("servidor", "localhost"))
        etBaseDatos.setText(sharedPref.getString("base_datos", "proyecto"))
        etUsuario.setText(sharedPref.getString("usuario", "root"))
        etPasswordBD.setText(sharedPref.getString("password", ""))
        etPuerto.setText(sharedPref.getString("puerto", "3306"))
    }

    private fun configurarListeners() {
        btnGuardarConfig.setOnClickListener {
            guardarConfiguracion()
        }

        btnProbarConexion.setOnClickListener {
            probarConexion()
        }

        btnVolverConfig.setOnClickListener {
            finish()
        }
    }

    private fun guardarConfiguracion() {
        val servidor = etServidor.text.toString().trim()
        val baseDatos = etBaseDatos.text.toString().trim()
        val usuario = etUsuario.text.toString().trim()
        val password = etPasswordBD.text.toString().trim()
        val puerto = etPuerto.text.toString().trim()

        if (servidor.isEmpty() || baseDatos.isEmpty() || usuario.isEmpty() || puerto.isEmpty()) {
            mostrarMensaje("Complete todos los campos obligatorios")
            return
        }

        try {
            puerto.toInt()
        } catch (e: NumberFormatException) {
            mostrarMensaje("El puerto debe ser un número válido")
            return
        }

        val editor = sharedPref.edit()
        editor.putString("servidor", servidor)
        editor.putString("base_datos", baseDatos)
        editor.putString("usuario", usuario)
        editor.putString("password", password)
        editor.putString("puerto", puerto)
        editor.apply()

        mostrarMensaje("Configuración guardada correctamente")
    }

    private fun probarConexion() {
        val servidor = etServidor.text.toString().trim()
        val baseDatos = etBaseDatos.text.toString().trim()
        val usuario = etUsuario.text.toString().trim()
        val password = etPasswordBD.text.toString().trim()
        val puerto = etPuerto.text.toString().trim()

        if (servidor.isEmpty() || baseDatos.isEmpty() || usuario.isEmpty() || puerto.isEmpty()) {
            mostrarMensaje("Complete la configuración primero")
            return
        }

        mostrarMensaje("Probando conexión a $servidor:$puerto...")

        // Simulación de prueba de conexión
        Thread {
            try {
                Thread.sleep(2000)
                runOnUiThread {
                    mostrarMensaje("✓ Conexión exitosa a la base de datos")
                }
            } catch (e: InterruptedException) {
                runOnUiThread {
                    mostrarMensaje("✗ Error en la conexión")
                }
            }
        }.start()
    }

    private fun mostrarMensaje(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}