package com.example.androidtaskmaria.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.androidtaskmaria.domain.model.WeatherDailyInfo
import com.example.androidtaskmaria.domain.model.WeatherDailyInfo.City
import com.example.androidtaskmaria.domain.model.WeatherDailyInfo.WeatherInfoData

@Entity(tableName = "weather_info_daily")
data class WeatherDailyDataEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val cityName: String,
    val cnt: Int?,
    val cod: String?,
    val list: List<WeatherInfoData>?,
    val message: Int?
) {
    fun toWeatherDailyInfo(): WeatherDailyInfo {
        return WeatherDailyInfo(
            city = City(
                WeatherDailyInfo.Coord(0.0, 0.0),
                country = "",
                id = 0,
                name = "",
                population = 0,
                sunrise = 0,
                sunset = 0,
                timezone = 0
            ),
            cnt = cnt ?: 0,
            cod = cod.orEmpty(),
            list = list ?: emptyList(),
            message = message ?: 0
        )
    }
}