package com.example.androidtaskmaria.domain.usecase

import com.example.androidtaskmaria.core.util.Resource
import com.example.androidtaskmaria.domain.model.WeatherDailyInfo
import com.example.androidtaskmaria.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow

class GetWeatherDailyData(private val repository: WeatherRepository) {
    suspend operator fun invoke(cityName: String): Flow<Resource<WeatherDailyInfo>> {
        return repository.getCityWeatherDaily(cityName)
    }
}