package com.example.androidtaskmaria.domain.repository

import com.example.androidtaskmaria.core.util.Resource
import com.example.androidtaskmaria.domain.model.WeatherDailyInfo
import com.example.androidtaskmaria.domain.model.WeatherInfo
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getCitiesWeather(): Flow<Resource<WeatherInfo>>
    suspend fun getCityWeatherDaily(cityName:String):Flow<Resource<WeatherDailyInfo>>
}