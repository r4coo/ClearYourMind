package com.clearyourmind.diario.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.clearyourmind.diario.databinding.ActivityRoutineBinding

class RoutineActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRoutineBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoutineBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Cargar datos en los campos y en la tabla
        loadRoutineData()

        // Configurar botones
        binding.btnSaveRoutine.setOnClickListener {
            saveRoutine()
        }

        binding.btnEditRoutine.setOnClickListener {
            showEditMode()
        }

        // Nuevo listener para el botón de volver
        binding.btnBackMenu.setOnClickListener {
            finish() // Cierra esta pantalla y vuelve a la anterior
        }
    }

    private fun saveRoutine() {
        val sharedPref = getSharedPreferences("WeeklyRoutine", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        // Guardar lo que escribió el usuario
        editor.putString("monday", binding.etMonday.text.toString())
        editor.putString("tuesday", binding.etTuesday.text.toString())
        editor.putString("wednesday", binding.etWednesday.text.toString())
        editor.putString("thursday", binding.etThursday.text.toString())
        editor.putString("friday", binding.etFriday.text.toString())
        editor.apply()

        Toast.makeText(this, "¡Rutina actualizada!", Toast.LENGTH_SHORT).show()

        // Actualizar la tabla visualmente y cambiar de modo
        updateTableFromInputs()
        showViewMode()
    }

    private fun loadRoutineData() {
        val sharedPref = getSharedPreferences("WeeklyRoutine", Context.MODE_PRIVATE)

        val mon = sharedPref.getString("monday", "") ?: ""
        val tue = sharedPref.getString("tuesday", "") ?: ""
        val wed = sharedPref.getString("wednesday", "") ?: ""
        val thu = sharedPref.getString("thursday", "") ?: ""
        val fri = sharedPref.getString("friday", "") ?: ""

        // Llenar los inputs (para editar)
        binding.etMonday.setText(mon)
        binding.etTuesday.setText(tue)
        binding.etWednesday.setText(wed)
        binding.etThursday.setText(thu)
        binding.etFriday.setText(fri)

        // Llenar la tabla (para ver)
        binding.tvMondayResult.text = mon.ifEmpty { "Sin actividad" }
        binding.tvTuesdayResult.text = tue.ifEmpty { "Sin actividad" }
        binding.tvWednesdayResult.text = wed.ifEmpty { "Sin actividad" }
        binding.tvThursdayResult.text = thu.ifEmpty { "Sin actividad" }
        binding.tvFridayResult.text = fri.ifEmpty { "Sin actividad" }

        // Decidir qué vista mostrar al inicio
        if (mon.isEmpty() && tue.isEmpty() && wed.isEmpty() && thu.isEmpty() && fri.isEmpty()) {
            showEditMode() // Si no hay nada guardado, mostrar modo edición
        } else {
            showViewMode() // Si hay datos, mostrar la tabla
        }
    }

    private fun updateTableFromInputs() {
        binding.tvMondayResult.text = binding.etMonday.text.toString().ifEmpty { "Sin actividad" }
        binding.tvTuesdayResult.text = binding.etTuesday.text.toString().ifEmpty { "Sin actividad" }
        binding.tvWednesdayResult.text = binding.etWednesday.text.toString().ifEmpty { "Sin actividad" }
        binding.tvThursdayResult.text = binding.etThursday.text.toString().ifEmpty { "Sin actividad" }
        binding.tvFridayResult.text = binding.etFriday.text.toString().ifEmpty { "Sin actividad" }
    }

    private fun showEditMode() {
        binding.editContainer.visibility = View.VISIBLE
        binding.tableContainer.visibility = View.GONE
    }

    private fun showViewMode() {
        binding.editContainer.visibility = View.GONE
        binding.tableContainer.visibility = View.VISIBLE
    }
}