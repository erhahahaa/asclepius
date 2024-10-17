package com.dicoding.asclepius.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.dicoding.asclepius.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
  private lateinit var binding: ActivityResultBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityResultBinding.inflate(layoutInflater)
    setContentView(binding.root)

    val result = intent.getStringExtra("PREDICTION_RESULT")
    binding.resultText.text = result

    val imageStr = intent.getStringExtra("IMAGE_URI")
    val imageUri = imageStr?.toUri()
    binding.resultImage.setImageURI(imageUri)
  }
}
