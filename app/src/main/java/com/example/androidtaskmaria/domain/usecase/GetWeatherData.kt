package com.example.androidtaskmaria.domain.usecase

import com.example.androidtaskmaria.domain.repository.WeatherRepository
import com.example.androidtaskmaria.core.util.Resource
import com.example.androidtaskmaria.domain.model.WeatherInfo
import kotlinx.coroutines.flow.Flow

class GetWeatherData(private val repository: WeatherRepository) {
    suspend operator fun invoke(): Flow<Resource<WeatherInfo>> {
        return repository.getCitiesWeather()
    }
}