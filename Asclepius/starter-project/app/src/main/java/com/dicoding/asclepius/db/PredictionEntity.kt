package com.dicoding.asclepius.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "prediction_history")
@Parcelize
data class PredictionEntity(
  @PrimaryKey(autoGenerate = true) val id: Int = 0,
  val imageUri: String?,
  val cancerScore: Float,
  val nonCancerScore: Float,
  val timestamp: Long,
) : Parcelable
