package com.example.androidtaskmaria.presentation.detailscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidtaskmaria.core.util.Resource
import com.example.androidtaskmaria.domain.model.WeatherDailyInfo
import com.example.androidtaskmaria.domain.usecase.WeatherUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class DetailsScreenViewModel @Inject constructor(private val weatherUseCases: WeatherUseCases) :
    ViewModel() {
    private val _detailScreenEvents = MutableSharedFlow<DetailScreenEvents>()
    val detailScreenEvents = _detailScreenEvents.asSharedFlow()
    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading
    private var _weatherDataDaily = MutableStateFlow<WeatherDailyInfo?>(
        WeatherDailyInfo(
            city = WeatherDailyInfo.City(
                WeatherDailyInfo.Coord(0.0, 0.0),
                country = "",
                id = 0,
                name = "",
                population = 0,
                sunrise = 0,
                sunset = 0,
                timezone = 0
            ),
            cnt = 0,
            cod = "",
            list = emptyList(),
            message = 0
        )
    )
    val weatherDataDaily: StateFlow<WeatherDailyInfo?> get() = _weatherDataDaily

    private var _weatherData =
        MutableStateFlow<List<DailyWeatherData>>(emptyList())
    val weatherData: StateFlow<List<DailyWeatherData>> get() = _weatherData

    private val groupedWeatherData: MutableList<DailyWeatherData> = mutableListOf()
    fun getWeatherInfoDaily(cityName: String) = viewModelScope.launch {
        weatherUseCases.getWeatherDailyData(cityName).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _isLoading.postValue(true)
                }

                is Resource.Success -> {
                    _isLoading.postValue(false)
                    _weatherDataDaily.emit(result.data)
                }

                is Resource.Error -> {
                    _detailScreenEvents.emit(
                        DetailScreenEvents.ShowErrorMessage(
                            result.message ?: ""
                        )
                    )
                }
            }

        }.launchIn(this)
    }

    private fun getDayName(timestamp: Int): String {
        val date = Date(timestamp * 1000L)
        val formatter = SimpleDateFormat("EEEE", Locale.getDefault())
        return formatter.format(date)
    }

    fun groupWeatherByDay(list: List<WeatherDailyInfo.WeatherInfoData>, sunset: Int, sunrise: Int) =
        viewModelScope.launch {
            val dailyWeatherMap =
                mutableMapOf<String, MutableList<WeatherDailyInfo.WeatherInfoData>>()

            list.forEach { weatherInfo ->
                val dayName = getDayName(weatherInfo.dt)
                dailyWeatherMap.putIfAbsent(dayName, mutableListOf())
                dailyWeatherMap[dayName]?.add(weatherInfo)
            }

            dailyWeatherMap.forEach { (day, weatherDataList) ->
                val minTemp = weatherDataList.minOf { it.main.temp_min }
                val maxTemp = weatherDataList.maxOf { it.main.temp_max }
                val weatherCondition =
                    weatherDataList.first().weather[0].main
                groupedWeatherData.add(
                    DailyWeatherData(day, minTemp, maxTemp, weatherCondition, sunrise, sunset)
                )
            }
            _weatherData.emit(groupedWeatherData)
        }

    sealed class DetailScreenEvents {
        data class ShowErrorMessage(val message: String) : DetailScreenEvents()
    }
}

data class DailyWeatherData(
    val dayName: String,
    val minTemp: Double,
    val maxTemp: Double,
    val weatherCondition: String,
    val sunrise: Int,
    val sunset: Int,
)