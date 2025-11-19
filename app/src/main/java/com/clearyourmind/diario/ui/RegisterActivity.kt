package com.clearyourmind.diario.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.clearyourmind.diario.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        // Botones sociales simulados
        binding.btnGoogleRegister.setOnClickListener {
            simulateSocialRegister("Google")
        }

        binding.btnAppleRegister.setOnClickListener {
            simulateSocialRegister("Apple")
        }

        // Botón Registrarse (Formulario)
        binding.btnRegisterAction.setOnClickListener {
            validateAndRegister()
        }

        // Volver al Login
        binding.tvBackToLogin.setOnClickListener {
            finish() // Simplemente cierra esta pantalla y muestra la anterior (Login)
        }
    }

    private fun simulateSocialRegister(provider: String) {
        Toast.makeText(this, "Conectando con $provider...", Toast.LENGTH_SHORT).show()
        binding.root.postDelayed({
            loginSuccess()
        }, 1500)
    }

    private fun validateAndRegister() {
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        // VALIDACIÓN DE CORREO: Solo permitimos dominios específicos
        val validDomains = listOf("@gmail", "@outlook", "@duocuc", "@hotmail")
        // Verificamos si el email contiene alguno de los dominios permitidos (ignorando mayúsculas/minúsculas)
        val isValidDomain = validDomains.any { email.contains(it, ignoreCase = true) }

        if (!isValidDomain) {
            Toast.makeText(this, "Correo no válido. Usa: Gmail, Outlook, DuocUC o Hotmail.", Toast.LENGTH_LONG).show()
            return
        }

        if (password.length < 6) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
            return
        }

        // Simulación de éxito
        Toast.makeText(this, "Creando cuenta...", Toast.LENGTH_SHORT).show()
        binding.root.postDelayed({
            Toast.makeText(this, "¡Cuenta creada con éxito!", Toast.LENGTH_SHORT).show()
            loginSuccess()
        }, 1500)
    }

    private fun loginSuccess() {
        // Ir a la pantalla principal y borrar el historial para que no vuelva al registro
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}