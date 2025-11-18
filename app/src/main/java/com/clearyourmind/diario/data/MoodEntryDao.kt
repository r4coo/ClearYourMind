package com.clearyourmind.diario.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodEntryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: MoodEntry)

    @Query("SELECT * FROM mood_entries ORDER BY timestamp DESC")
    fun getAllEntries(): Flow<List<MoodEntry>>
}
