package com.example.androidtaskmaria.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherDailyInfo(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<WeatherInfoData>,
    val message: Int
) : Parcelable {
    @Parcelize
    data class WeatherInfoData(
        val clouds: Clouds,
        val dt: Int,
        val dt_txt: String,
        val main: Main,
        val pop: Double,
        val sys: Sys,
        val visibility: Int,
        val weather: List<WeatherData>,
        val wind: Wind
    ) : Parcelable

    @Parcelize
    data class City(
        val coord: Coord,
        val country: String,
        val id: Int,
        val name: String,
        val population: Int,
        val sunrise: Int,
        val sunset: Int,
        val timezone: Int
    ) : Parcelable

    @Parcelize
    data class Coord(
        val lat: Double,
        val lon: Double
    ) : Parcelable

    @Parcelize
    data class Clouds(
        val all: Int
    ) : Parcelable

    @Parcelize
    data class Main(
        val feels_like: Double,
        val grnd_level: Int,
        val humidity: Int,
        val pressure: Int,
        val sea_level: Int,
        val temp: Double,
        val temp_kf: Double,
        val temp_max: Double,
        val temp_min: Double
    ) : Parcelable

    @Parcelize
    data class Sys(
        val pod: String
    ) : Parcelable

    @Parcelize
    data class WeatherData(
        val description: String,
        val icon: String,
        val id: Int,
        val main: String
    ) : Parcelable

    @Parcelize
    data class Wind(
        val deg: Int,
        val gust: Double,
        val speed: Double
    ) : Parcelable
}