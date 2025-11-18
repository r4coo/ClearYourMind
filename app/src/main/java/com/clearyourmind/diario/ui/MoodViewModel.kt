package com.clearyourmind.diario.ui // Paquete actualizado

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.clearyourmind.diario.data.MoodEntryDao
import com.clearyourmind.diario.data.MoodEntry
import kotlinx.coroutines.launch

/**
 * ViewModel para gestionar el estado de la aplicación y la lógica de negocio (IE 2.2.1).
 */
class MoodViewModel(private val dao: MoodEntryDao) : ViewModel() {

    // LiveData que contiene todas las entradas del diario, observada por la Activity. (IE 2.3.1)
    val allEntries = dao.getAllEntries().asLiveData()

    // LiveData para manejar mensajes de la interfaz (ej: errores de validación, confirmaciones) (IE 2.1.2)
    private val _uiMessage = MutableLiveData<String>()
    val uiMessage: LiveData<String> = _uiMessage

    // LiveData para almacenar temporalmente la ruta de la imagen tomada. (IE 2.4.1)
    private val _currentImagePath = MutableLiveData<String?>()
    val currentImagePath: LiveData<String?> = _currentImagePath

    // LiveData para almacenar la ubicación actual. (IE 2.4.1)
    private val _currentLocation = MutableLiveData<String?>()
    val currentLocation: LiveData<String?> = _currentLocation

    /**
     * Almacena la ruta de la imagen obtenida de la cámara.
     */
    fun setImagePath(path: String?) {
        _currentImagePath.value = path
        if (path != null) {
            _uiMessage.postValue("Foto capturada y lista para guardar.")
        }
    }

    /**
     * Almacena la ubicación obtenida.
     */
    fun setLocation(lat: Double?, lon: Double?) {
        if (lat != null && lon != null) {
            val locationString = "Lat: ${String.format("%.4f", lat)}, Lon: ${String.format("%.4f", lon)}"
            _currentLocation.value = locationString
            _uiMessage.postValue("Ubicación registrada: $locationString")
        } else {
            _currentLocation.value = null
        }
    }

    /**
     * Función que gestiona la lógica de guardado de forma centralizada (IE 2.2.1).
     */
    fun saveEntry(moodScore: Int, reflectionText: String) = viewModelScope.launch {
        // Validación desde la lógica desacoplada (IE 2.2.1)
        if (reflectionText.trim().isBlank() || reflectionText.length < 10) {
            _uiMessage.postValue("Error: Por favor, escribe una reflexión de al menos 10 caracteres.")
            return@launch
        }

        // Crear la entidad del modelo de datos con los recursos nativos
        val newEntry = MoodEntry(
            moodScore = moodScore,
            reflection = reflectionText.trim(),
            imagePath = _currentImagePath.value,
            location = _currentLocation.value
        )

        // Insertar en la base de datos
        dao.insert(newEntry)
        _uiMessage.postValue("Entrada guardada con éxito.")

        // Limpiar estados temporales después de guardar
        clearTemporaryData()

        // Lógica de ayuda y sugerencia (Ayudante a la vida cotidiana)
        val suggestion = when (moodScore) {
            in 0..4 -> "Sugerencia: Parece que fue un día difícil. Intenta escribir 3 cosas por las que estás agradecido mañana."
            in 8..10 -> "Sugerencia: ¡Excelente día! Anota un objetivo pequeño para continuar con esta energía positiva."
            else -> "Sugerencia: Un buen día. Continúa con tus hábitos positivos."
        }
        _uiMessage.postValue(suggestion)
        Log.i("MoodViewModel", suggestion)
    }

    fun clearTemporaryData() {
        _currentImagePath.value = null
        _currentLocation.value = null
    }
}
