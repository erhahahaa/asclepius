package com.dicoding.asclepius.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PredictionEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
  abstract fun predictionDao(): PredictionDao

  companion object {
    @Volatile private var instance: AppDatabase? = null

    operator fun invoke(context: Context): AppDatabase =
      instance ?: synchronized(this) { instance ?: buildDatabase(context).also { instance = it } }

    private fun buildDatabase(context: Context) =
      Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "asclepius.db")
        .build()
  }
}
