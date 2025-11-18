package com.clearyourmind.diario.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mood_entries")
data class MoodEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val moodScore: Int,
    val reflection: String,
    val imagePath: String? = null,
    val location: String? = null
)
