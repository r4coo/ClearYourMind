package com.clearyourmind.diario.ui

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.clearyourmind.diario.data.api.SpringRetrofitClient
import com.clearyourmind.diario.data.api.Suggestion
import com.clearyourmind.diario.databinding.ActivityProfileBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        // Simulaciones de configuración
        binding.btnAccount.setOnClickListener {
            Toast.makeText(this, "Editar Perfil: Próximamente", Toast.LENGTH_SHORT).show()
        }

        binding.btnSecurity.setOnClickListener {
            Toast.makeText(this, "Ajustes de Seguridad simulados", Toast.LENGTH_SHORT).show()
        }

        // --- INTEGRACIÓN CON MICROSERVICIO SPRING BOOT ---
        binding.btnNotifications.setOnClickListener {
            showSuggestionDialog()
        }

        // Cerrar Sesión (Logout)
        binding.btnLogout.setOnClickListener {
            Toast.makeText(this, "Cerrando sesión...", Toast.LENGTH_SHORT).show()
            
            // Volver al Login y limpiar la pila de actividades
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // Botón Volver
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun showSuggestionDialog() {
        val input = EditText(this)
        input.hint = "Escribe tu sugerencia aquí..."

        AlertDialog.Builder(this)
            .setTitle("Enviar Sugerencia")
            .setMessage("Ayúdanos a mejorar la app. Tu mensaje se enviará a nuestro servidor.")
            .setView(input)
            .setPositiveButton("Enviar") { dialog, _ ->
                val text = input.text.toString()
                if (text.isNotBlank()) {
                    sendSuggestionToMicroservice(text)
                } else {
                    Toast.makeText(this, "El mensaje no puede estar vacío", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ -> dialog.cancel() }
            .show()
    }

    private fun sendSuggestionToMicroservice(content: String) {
        Toast.makeText(this, "Enviando...", Toast.LENGTH_SHORT).show()

        val suggestion = Suggestion(content = content, author = "Usuario Android")

        SpringRetrofitClient.instance.sendSuggestion(suggestion).enqueue(object : Callback<Suggestion> {
            override fun onResponse(call: Call<Suggestion>, response: Response<Suggestion>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ProfileActivity, "¡Enviado con éxito al servidor!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@ProfileActivity, "Error del servidor: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Suggestion>, t: Throwable) {
                Toast.makeText(this@ProfileActivity, "Fallo de conexión. ¿Está corriendo el servidor en tu PC?", Toast.LENGTH_LONG).show()
            }
        })
    }
}