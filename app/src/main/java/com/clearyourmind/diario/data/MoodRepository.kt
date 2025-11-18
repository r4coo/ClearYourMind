package com.clearyourmind.diario.data

class MoodRepository(private val dao: MoodEntryDao) {

    val allEntries = dao.getAllEntries()

    suspend fun insert(entry: MoodEntry) {
        dao.insert(entry)
    }
}
