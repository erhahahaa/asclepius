package com.dicoding.asclepius.view

import android.app.Activity
import android.content.Intent
import android.graphics.ImageDecoder
import android.graphics.ImageDecoder.createSource
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.db.AppDatabase
import com.dicoding.asclepius.db.PredictionEntity
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.helper.getFileName
import com.dicoding.asclepius.helper.toReadableString
import com.yalantis.ucrop.UCrop
import java.io.File
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.tensorflow.lite.support.label.Category

class MainActivity : AppCompatActivity() {
  private lateinit var binding: ActivityMainBinding

  private var currentImageUri: Uri? = null
  private val db: AppDatabase by lazy { AppDatabase(this) }

  private val getImageFromGallery =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
      if (result.resultCode == Activity.RESULT_OK && result.data != null) {
        val sourceUri = result.data?.data
        Log.d("MainActivity", "getImageFromGallery: $sourceUri")
        sourceUri?.let { startCrop(it) }
      } else {
        showToast("Failed to pick image")
      }
    }

  private val cropImageResultLauncher =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
      if (result.resultCode == Activity.RESULT_OK) {
        val resultUri = result.data?.let { UCrop.getOutput(it) }
        if (resultUri != null) {
          currentImageUri = resultUri
          showImage()
        } else {
          showToast("Crop error: Unable to get the image")
        }
      }
    }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    supportActionBar?.title = getString(R.string.home)

    binding.galleryButton.setOnClickListener { startGallery() }
    binding.analyzeButton.setOnClickListener { analyzeImage() }
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_main, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.menu_history -> {
        startActivity(Intent(this, HistoryActivity::class.java))
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  private fun startGallery() {
    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    getImageFromGallery.launch(intent)
  }

  private fun startCrop(sourceUri: Uri) {
    val fileName = sourceUri.getFileName()
    val destinationUri = Uri.fromFile(File(cacheDir, fileName))

    UCrop.of(sourceUri, destinationUri)
      .withAspectRatio(1f, 1f)
      .withMaxResultSize(500, 500)
      .start(this, cropImageResultLauncher)
  }

  private fun showImage() {
    currentImageUri?.let { binding.previewImageView.setImageURI(it) }
      ?: showToast("No image selected")
  }

  @Suppress("DEPRECATION")
  private fun analyzeImage() {
    currentImageUri?.let { uri ->
      val resolver = contentResolver
      val bitmap =
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
          MediaStore.Images.Media.getBitmap(resolver, uri)
        } else {
          val source = createSource(resolver, uri)
          ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
            decoder.setTargetSampleSize(1)
            decoder.isMutableRequired = true
          }
        }
      val classifier = ImageClassifierHelper(context = this)
      val result = classifier.classifyImage(bitmap)
      savePredictionToDb(uri.toString(), result)
      moveToResult(result.toReadableString())
    } ?: showToast("No image selected")
  }

  @OptIn(DelicateCoroutinesApi::class)
  private fun savePredictionToDb(imageUri: String, results: List<Category>) {
    val cancerCategory = results.find { it.label == "Cancer" }
    if (cancerCategory == null) return
    val nonCancerCategory = results.find { it.label == "Non Cancer" }
    if (nonCancerCategory == null) return

    val prediction =
      PredictionEntity(
        imageUri = imageUri,
        cancerScore = cancerCategory.score,
        nonCancerScore = nonCancerCategory.score,
        timestamp = System.currentTimeMillis(),
      )

    GlobalScope.launch {
      val find = db.predictionDao().getPrediction(imageUri)
      if (find != null) {
        db.predictionDao().deletePrediction(find.id)
      }
      db.predictionDao().insert(prediction)
    }
  }

  private fun moveToResult(result: String) {
    val intent = Intent(this, ResultActivity::class.java)
    intent.putExtra("PREDICTION_RESULT", result)
    intent.putExtra("IMAGE_URI", currentImageUri.toString())
    startActivity(intent)
  }

  private fun showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
  }
}
