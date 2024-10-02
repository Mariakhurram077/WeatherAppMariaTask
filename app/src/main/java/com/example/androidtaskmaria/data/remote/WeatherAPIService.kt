package com.example.androidtaskmaria.data.remote

import com.example.androidtaskmaria.data.remote.dto.WeatherInfoDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPIService {
    @GET("data/2.5/group?id=1162015,1174872,1172451,1177662,1166993,1170395,1168197,1167528,1164786,1163054,1179400,1176734,1166146,1183880,1166546,1179246,1173491")
    suspend fun getAllCitiesWeather(
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): WeatherInfoDTO

    @GET("data/2.5/forecast")
    suspend fun getCityForecastDaily(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): WeatherInfoDTO
}
