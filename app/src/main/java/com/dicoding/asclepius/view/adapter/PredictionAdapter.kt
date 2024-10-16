package com.dicoding.asclepius.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ItemHistoryBinding
import com.dicoding.asclepius.db.PredictionEntity
import com.dicoding.asclepius.helper.getFileName
import com.dicoding.asclepius.helper.toPercent
import com.dicoding.asclepius.helper.toReadableString

class PredictionAdapter(private val predictions: List<PredictionEntity>) :
  RecyclerView.Adapter<PredictionAdapter.PredictionViewHolder>() {

  inner class PredictionViewHolder(private val binding: ItemHistoryBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(prediction: PredictionEntity) {
      binding.apply {
        val context = root.context
        val imageUri = prediction.imageUri?.toUri()
        tvImageName.text = context.getString(R.string.image_name, imageUri?.getFileName())
        ivHistoryImage.setImageURI(imageUri)
        tvHistoryCancer.text =
          context.getString(R.string.cancer_score, prediction.cancerScore.toPercent())
        tvHistoryNonCancer.text =
          context.getString(R.string.non_cancer_score, prediction.nonCancerScore.toPercent())
        val readableDate = prediction.timestamp.toReadableString()
        tvHistoryDate.text = context.getString(R.string.date, readableDate)
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PredictionViewHolder {
    val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return PredictionViewHolder(binding)
  }

  override fun onBindViewHolder(holder: PredictionViewHolder, position: Int) {
    holder.bind(predictions[position])
  }

  override fun getItemCount(): Int = predictions.size
}
