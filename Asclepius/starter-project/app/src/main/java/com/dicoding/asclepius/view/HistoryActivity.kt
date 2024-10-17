package com.dicoding.asclepius.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityHistoryBinding
import com.dicoding.asclepius.db.AppDatabase
import com.dicoding.asclepius.view.adapter.PredictionAdapter
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {
  private lateinit var binding: ActivityHistoryBinding
  private val db: AppDatabase by lazy { AppDatabase(this) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityHistoryBinding.inflate(layoutInflater)
    setContentView(binding.root)

    supportActionBar?.title = getString(R.string.history)

    lifecycleScope.launch {
      val predictions = db.predictionDao().getAllPredictions()

      val adapter = PredictionAdapter(predictions)

      binding.recyclerViewHistory.apply {
        layoutManager = LinearLayoutManager(this@HistoryActivity)
        this.adapter = adapter
      }
    }
  }
}
