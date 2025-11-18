package com.clearyourmind.diario.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [MoodEntry::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun moodEntryDao(): MoodEntryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Migración de versión 1 a 2 (agrega imagePath y location)
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE mood_entries ADD COLUMN imagePath TEXT")
                db.execSQL("ALTER TABLE mood_entries ADD COLUMN location TEXT")
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mood_journal_db"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
