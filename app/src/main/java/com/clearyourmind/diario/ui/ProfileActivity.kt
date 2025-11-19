package com.clearyourmind.diario.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.clearyourmind.diario.databinding.ActivityProfileBinding

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

        binding.btnNotifications.setOnClickListener {
            Toast.makeText(this, "Configuración de Notificaciones simulada", Toast.LENGTH_SHORT).show()
        }

        // Cerrar Sesión (Logout)
        binding.btnLogout.setOnClickListener {
            Toast.makeText(this, "Cerrando sesión...", Toast.LENGTH_SHORT).show()
            
            // Volver al Login y limpiar la pila de actividades
            val intent = Intent(this, LoginActivity::class.java)
            // Estas flags borran el historial para que no puedas volver atrás
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // Botón Volver
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}