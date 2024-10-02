package com.example.androidtaskmaria.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherInfo(val cnt:Int,val list: List<WeatherData>) : Parcelable {
    @Parcelize
    data class WeatherData(
        val id: Int,
        val main: Main,
        val name: String,
        val sys: Sys,
        val weather: List<Weather>,
    ) : Parcelable

    @Parcelize
    data class Main(
        val feelLike: Double,
        val grndLevel: Int,
        val humidity: Int,
        val pressure: Int,
        val seaLevel: Int,
        val temp: Double,
        val maxTemp: Double,
        val minTemp: Double
    ) : Parcelable

    @Parcelize
    data class Sys(
        val sunrise: Int,
        val sunset: Int,
    ) : Parcelable

    @Parcelize
    data class Weather(
        val main: String
    ) : Parcelable
}