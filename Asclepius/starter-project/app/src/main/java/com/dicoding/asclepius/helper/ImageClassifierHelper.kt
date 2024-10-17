package com.dicoding.asclepius.helper

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.dicoding.asclepius.ml.CancerClassification
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.label.Category

class ImageClassifierHelper(context: Context) {

  private lateinit var model: CancerClassification

  init {
    try {
      model = CancerClassification.newInstance(context)
      Log.d(TAG, "ImageClassifier initialized successfully")
    } catch (e: IllegalStateException) {
      Log.e(TAG, "Error initializing ImageClassifier: ${e.message}")
    }
  }

  fun classifyImage(image: Bitmap): List<Category> {
    val tensorImage = TensorImage.fromBitmap(image)
    val results = model.process(tensorImage)
    val probability = results.getProbabilityAsCategoryList()

    return probability
  }
}

fun Float.toPercent(): String {
  return "${(this * 100).toInt()}%"
}

fun List<Category>.toReadableString(): String {
  val result = StringBuilder()
  for (category in this) {
    result.append("${category.label}: ${category.score.toPercent()}\n")
  }
  return result.toString()
}
