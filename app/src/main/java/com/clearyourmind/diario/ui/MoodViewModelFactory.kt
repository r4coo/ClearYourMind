package com.clearyourmind.diario.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.clearyourmind.diario.data.MoodEntryDao

class MoodViewModelFactory(private val dao: MoodEntryDao) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MoodViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MoodViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
