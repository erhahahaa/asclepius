package com.dicoding.asclepius.helper

import android.net.Uri

fun Uri.getFileName(): String {
  var fileName: String? = null
  val path = this.path
  val cut = path?.lastIndexOf('/')
  if (cut != null && cut != -1) {
    fileName = path.substring(cut + 1)
  }

  val hasFormat = fileName?.contains(".") ?: false
  return if (hasFormat) {
    fileName ?: "unknown.jpg"
  } else {
    "${fileName ?: "unknown"}.jpg"
  }
}
