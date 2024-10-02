package com.example.androidtaskmaria.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.androidtaskmaria.domain.model.WeatherInfo

@Entity(tableName = "weather_info")
data class WeatherInfoEntity(
    val cnt: Int?,
    val list: List<WeatherInfo.WeatherData>?,
    @PrimaryKey val id: Int? = null
) {
    fun toWeatherInfo(): WeatherInfo {
        return WeatherInfo(cnt = cnt ?: 0, list = list ?: emptyList())
    }
}
