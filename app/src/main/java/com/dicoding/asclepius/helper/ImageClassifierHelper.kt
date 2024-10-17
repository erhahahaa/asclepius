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

  fun classifyImage(image: Bitmap, onError: (Exception) -> Unit): List<Category>? {
    return try {
      val tensorImage = TensorImage.fromBitmap(image)
      val results = model.process(tensorImage)
      val probability = results.getProbabilityAsCategoryList()

      probability
    } catch (e: Exception) {
      onError(e)
      null
    }
  }
}

fun Float.toPercent(): String {
  return "${(this * 100).toInt()}%"
}

fun List<Category>.determineCancer(): String {
  //  val result = StringBuilder()
  //  for (category in this) {
  //    result.append("${category.label}: ${category.score.toPercent()}\n")
  //  }
  //  return result.toString()

  val data = mutableMapOf("Cancer" to 0f, "Non-Cancer" to 0f)
  for (category in this) {
    if (category.label == "Cancer") {
      data["Cancer"] = category.score
    } else {
      data["Non-Cancer"] = category.score
    }
  }

  return if (data["Cancer"]!! > data["Non-Cancer"]!!) {
    "Cancer: ${data["Cancer"]!!.toPercent()}"
  } else {
    "Non-Cancer ${data["Non-Cancer"]!!.toPercent()}"
  }
}
