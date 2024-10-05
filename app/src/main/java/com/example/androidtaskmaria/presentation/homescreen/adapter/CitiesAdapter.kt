package com.example.androidtaskmaria.presentation.homescreen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.androidtaskmaria.R
import com.example.androidtaskmaria.databinding.CitiesLayoutBinding
import com.example.androidtaskmaria.domain.model.WeatherInfo
import com.example.androidtaskmaria.presentation.isDaytime
import com.example.androidtaskmaria.presentation.toWeatherIcon

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
            val currentTime = System.currentTimeMillis() / 1000
            val isDay = currentTime.isDaytime(sunrise = item.sys.sunrise, sunset = item.sys.sunset)
            val iconResId = item.weather[0].main.toWeatherIcon(isDay = isDay)
            binding.apply {
                temperatureText.text = itemView.context.getString(
                    R.string.temperature,
                    Math.round(item.main.temp).toString()
                )
                cityName.text = item.name
                weatherCondition.text = item.weather[0].main
                weatherConditionIv.setImageResource(iconResId)
                weatherLayout.setOnClickListener {
                    onItemClick.invoke(item)
                }
            }
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