package com.example.androidtaskmaria.presentation.detailscreen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.androidtaskmaria.R
import com.example.androidtaskmaria.databinding.WeatherForecastLayoutBinding
import com.example.androidtaskmaria.presentation.detailscreen.DailyWeatherData
import com.example.androidtaskmaria.presentation.toWeatherIcon

class WeatherAdapter :
    ListAdapter<DailyWeatherData, WeatherAdapter.WeatherInfoViewHolder>(DiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherInfoViewHolder {
        val binding =
            WeatherForecastLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherInfoViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class WeatherInfoViewHolder(private val binding: WeatherForecastLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DailyWeatherData) {
            binding.apply {
                minTempText.text = itemView.context.getString(
                    R.string.temperature, Math.round(item.minTemp).toString()
                )
                maxTempText.text = itemView.context.getString(
                    R.string.temperature,
                    Math.round(item.maxTemp).toString()
                )
                weatherConditionText.text = item.weatherCondition
                dayText.text = item.dayName
                weatherCondIv.setImageResource(item.weatherCondition.toWeatherIcon(isDay = true))
            }
        }
    }

    class DiffCallBack : DiffUtil.ItemCallback<DailyWeatherData>() {
        override fun areItemsTheSame(
            oldItem: DailyWeatherData, newItem: DailyWeatherData
        ): Boolean {
            return oldItem.dayName == newItem.dayName
        }

        override fun areContentsTheSame(
            oldItem: DailyWeatherData, newItem: DailyWeatherData
        ): Boolean {
            return oldItem == newItem
        }
    }
}