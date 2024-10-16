package com.dicoding.asclepius.helper

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.toReadableString(): String {
  val date = Date(this)
  val format = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
  return format.format(date)
}
