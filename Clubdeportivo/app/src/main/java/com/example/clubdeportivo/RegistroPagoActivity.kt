package com.example.clubdeportivo

import android.graphics.Color
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RegistroPagoActivity : AppCompatActivity() {

    private lateinit var etCampoDNI: EditText
    private lateinit var etCampoMonto: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_registro_pago)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cont1)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        etCampoDNI = findViewById(R.id.etCampoDNI)
        etCampoMonto = findViewById(R.id.etCampoMonto)

        val btnVolver2 = findViewById<Button>(R.id.btnVolver2)
        btnVolver2.setOnClickListener {
            finish() // Volver a la actividad anterior
        }

        val btPagar= findViewById<Button>(R.id.btPagar)
        btPagar.setOnClickListener {
            if(validarCampos()){
                mostrarDialogoExito()
            }
        }
    }

    private fun validarCampos(): Boolean {
        var valido = true

        if (etCampoDNI.text.toString().trim().isEmpty()) {
            etCampoDNI.error = "Campo DNI incompleto"
            valido = false
        }

        if (etCampoMonto.text.toString().trim().isEmpty()) {
            etCampoMonto.error = "Debe ingresar el monto a pagar"
            valido = false
        }


        return valido
    }



    private fun mostrarDialogoExito() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("PAGO APROBADO")
        builder.setMessage("El pago se registró correctamente.")
        builder.setPositiveButton("Aceptar") { dialog, which ->
            // Volver al menú principal
            finish()
        }
        builder.setCancelable(false)
        builder.show()
    }
}