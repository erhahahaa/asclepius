package com.dicoding.asclepius.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PredictionDao {
  @Insert suspend fun insert(prediction: PredictionEntity)

  @Query("SELECT * FROM prediction_history ORDER BY timestamp DESC")
  suspend fun getAllPredictions(): List<PredictionEntity>

  @Query("SELECT * FROM prediction_history WHERE imageUri LIKE :name ORDER BY timestamp DESC")
  suspend fun getPrediction(name: String): PredictionEntity?

  @Query("DELETE FROM prediction_history") suspend fun deleteAllPredictions()

  @Query("DELETE FROM prediction_history WHERE id = :id") suspend fun deletePrediction(id: Int)
}
