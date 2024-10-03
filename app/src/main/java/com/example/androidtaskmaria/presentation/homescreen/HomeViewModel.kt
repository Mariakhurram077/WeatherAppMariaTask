package com.example.androidtaskmaria.presentation.homescreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidtaskmaria.core.util.Resource
import com.example.androidtaskmaria.domain.model.WeatherDailyInfo
import com.example.androidtaskmaria.domain.model.WeatherInfo
import com.example.androidtaskmaria.domain.usecase.WeatherUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val weatherUseCases: WeatherUseCases) :
    ViewModel() {
    private val _homeScreenEvents = MutableSharedFlow<HomeScreenEvents>()
    val homeScreenEvents = _homeScreenEvents.asSharedFlow()
    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading
    private var _weatherData = MutableStateFlow<WeatherInfo?>(WeatherInfo(cnt = 0, emptyList()))
    val weatherData: StateFlow<WeatherInfo?> get() = _weatherData
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

    init {
        getWeatherData()
        //    getWeatherInfoDaily("Lahore")

    }

    private fun getWeatherData() = viewModelScope.launch {
        weatherUseCases.getWeatherData.invoke().onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _isLoading.postValue(true)
                }

                is Resource.Success -> {
                    _isLoading.postValue(false)
                    _weatherData.emit(result.data)
                }

                is Resource.Error -> {
                    _isLoading.postValue(false)
                    _homeScreenEvents.emit(HomeScreenEvents.ShowErrorMessage(result.message ?: ""))
                }
            }

        }.launchIn(this)
    }

    private fun getWeatherInfoDaily(cityName: String) = viewModelScope.launch {
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
                    _homeScreenEvents.emit(HomeScreenEvents.ShowErrorMessage(result.message ?: ""))

                }
            }

        }.launchIn(this)
    }

    sealed class HomeScreenEvents {
        data class ShowErrorMessage(val message: String) : HomeScreenEvents()
    }
}