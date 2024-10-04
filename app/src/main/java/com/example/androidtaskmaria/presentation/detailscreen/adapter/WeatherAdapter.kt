package com.example.androidtaskmaria.presentation.detailscreen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.androidtaskmaria.R
import com.example.androidtaskmaria.databinding.WeatherForecastLayoutBinding
import com.example.androidtaskmaria.presentation.detailscreen.DailyWeatherData

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
            val currentTime = System.currentTimeMillis() / 1000 // Current time in seconds
            val isDay = isDaytime(currentTime, item.sunrise, item.sunset)
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
                weatherCondIv.setImageResource(getWeatherIcon(item.weatherCondition, isDay))
            }
        }

        private fun getWeatherIcon(condition: String, isDay: Boolean): Int {
            return when {
                condition.contains(
                    "clear",
                    true
                ) -> if (isDay) R.drawable.clear_day else R.drawable.ic_clear_night

                condition.contains(
                    "clouds",
                    true
                ) -> if (isDay) R.drawable.ic_cloudy else R.drawable.ic_cloudy_night

                condition.contains(
                    "rain",
                    true
                ) -> R.drawable.ic_rain

                condition.contains("thunderstorm", true)

                -> R.drawable.ic_rain

                condition.contains(
                    "smoke",
                    true
                ) -> if (isDay) R.drawable.ic_smoke_haze else R.drawable.ic_smoke_haze_night

                condition.contains(
                    "haze",
                    true
                ) -> if (isDay) R.drawable.ic_smoke_haze else R.drawable.ic_smoke_haze_night

                condition.contains(
                    "mist",
                    true
                ) -> if (isDay) R.drawable.ic_smoke_haze else R.drawable.ic_smoke_haze_night

                else -> R.drawable.ic_cloudy
            }
        }

        private fun isDaytime(currentTime: Long, sunrise: Int, sunset: Int): Boolean {
            return currentTime in (sunrise..sunset)
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