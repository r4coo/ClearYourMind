package com.clearyourmind.diario.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.clearyourmind.diario.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        // 1. INICIAR SESIÓN CON CORREO Y CONTRASEÑA
        binding.btnLoginAction.setOnClickListener {
            validateAndLogin()
        }

        // 2. BOTONES SOCIALES
        binding.btnGoogleLogin.setOnClickListener {
            simulateLogin("Google")
        }

        binding.btnAppleLogin.setOnClickListener {
            simulateLogin("Apple")
        }

        // 3. MODO INVITADO
        binding.btnGuestLogin.setOnClickListener {
            goToMainApp()
        }

        // 4. IR A REGISTRO
        binding.tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validateAndLogin() {
        val email = binding.etLoginEmail.text.toString().trim()
        val password = binding.etLoginPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa tu correo y contraseña.", Toast.LENGTH_SHORT).show()
            return
        }

        // Simulación de validación básica
        if (password.length < 6) {
            Toast.makeText(this, "Contraseña incorrecta (demasiado corta).", Toast.LENGTH_SHORT).show()
            return
        }

        // Simular proceso de inicio de sesión exitoso
        Toast.makeText(this, "Verificando credenciales...", Toast.LENGTH_SHORT).show()
        binding.root.postDelayed({
            Toast.makeText(this, "¡Bienvenido de nuevo!", Toast.LENGTH_SHORT).show()
            goToMainApp()
        }, 1500)
    }

    private fun simulateLogin(provider: String) {
        Toast.makeText(this, "Iniciando sesión con $provider...", Toast.LENGTH_SHORT).show()
        
        // Simulamos un pequeño retraso para que parezca real
        binding.root.postDelayed({
            Toast.makeText(this, "¡Bienvenido, Alexis!", Toast.LENGTH_SHORT).show()
            goToMainApp()
        }, 1500)
    }

    private fun goToMainApp() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Cerramos el Login para que no se pueda volver atrás con el botón físico
    }
}