package com.example.androidtaskmaria.presentation.homescreen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.androidtaskmaria.R
import com.example.androidtaskmaria.databinding.CitiesLayoutBinding
import com.example.androidtaskmaria.domain.model.WeatherInfo

class CitiesAdapter(
    private val onItemClick: (item: WeatherInfo.WeatherData) -> Unit,
) :
    ListAdapter<WeatherInfo.WeatherData, CitiesAdapter.CitiesViewHolder>(DiffCallBack()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CitiesAdapter.CitiesViewHolder {
        val binding =
            CitiesLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CitiesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CitiesAdapter.CitiesViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class CitiesViewHolder(private val binding: CitiesLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: WeatherInfo.WeatherData) {
            val currentTime = System.currentTimeMillis() / 1000 // Current time in seconds
            val isDay = isDaytime(currentTime, item.sys.sunrise, item.sys.sunset)
            val iconResId = getWeatherIcon(item.weather[0].main, isDay)
            binding.apply {
                val temperature = Math.round(item.main.temp)
                temperatureText.text = itemView.context.getString(R.string.temperature, temperature.toString())
                cityName.text = item.name
                weatherCondition.text = item.weather[0].main
                weatherConditionIv.setImageResource(iconResId)
                weatherLayout.setOnClickListener {
                    onItemClick.invoke(item)
                }
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

    class DiffCallBack : DiffUtil.ItemCallback<WeatherInfo.WeatherData>() {
        override fun areItemsTheSame(
            oldItem: WeatherInfo.WeatherData,
            newItem: WeatherInfo.WeatherData
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: WeatherInfo.WeatherData,
            newItem: WeatherInfo.WeatherData
        ): Boolean {
            return oldItem == newItem
        }

    }
}