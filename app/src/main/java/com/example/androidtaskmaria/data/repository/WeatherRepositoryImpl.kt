package com.example.androidtaskmaria.data.repository

import android.os.Build
import androidx.annotation.RequiresExtension
import com.example.androidtaskmaria.BuildConfig
import com.example.androidtaskmaria.core.util.Resource
import com.example.androidtaskmaria.data.local.WeatherDailyInfoDao
import com.example.androidtaskmaria.data.local.WeatherInfoDao
import com.example.androidtaskmaria.data.local.entity.WeatherDailyDataEntity
import com.example.androidtaskmaria.data.remote.WeatherAPIService
import com.example.androidtaskmaria.data.remote.dto.toWeatherInfoData
import com.example.androidtaskmaria.data.remote.dto.toWeatherInfoEntity
import com.example.androidtaskmaria.domain.model.WeatherDailyInfo
import com.example.androidtaskmaria.domain.model.WeatherInfo
import com.example.androidtaskmaria.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherInfoDao: WeatherInfoDao,
    private val weatherDailyInfoDao: WeatherDailyInfoDao,
    private val apiService: WeatherAPIService
) : WeatherRepository {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun getCitiesWeather(): Flow<Resource<WeatherInfo>> = flow {
        emit(Resource.Loading())
        val weatherInfo = weatherInfoDao.getWeatherInfo()?.toWeatherInfo()
        emit(Resource.Loading(data = weatherInfo))
        try {
            val remoteWeatherInfo = apiService.getAllCitiesWeather(apiKey = BuildConfig.API_KEY)
            weatherInfoDao.deleteWeatherInfo()
            weatherInfoDao.insert(remoteWeatherInfo.toWeatherInfoEntity())
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    message = "OOPS! Something went wrong",
                    data = weatherInfo
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = "Couldn't reach server. Please check your internet connection",
                    data = weatherInfo
                )
            )
        }
        val updatedWeatherInfo = weatherInfoDao.getWeatherInfo()?.toWeatherInfo()
        emit(Resource.Success(data = updatedWeatherInfo))
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun getCityWeatherDaily(cityName: String): Flow<Resource<WeatherDailyInfo>> =
        flow {
            emit(Resource.Loading())
            val weatherDailyInfo = weatherDailyInfoDao.getWeatherDataByCityName(cityName = cityName)
                ?.toWeatherDailyInfo()
            emit(Resource.Loading(data = weatherDailyInfo))
            try {
                val remoteWeatherDailyInfo = apiService.getCityForecastDaily(
                    cityName = cityName,
                    apiKey = BuildConfig.API_KEY
                )
                weatherDailyInfoDao.deleteWeatherDataForCity(cityName)
                weatherDailyInfoDao.insert(
                    WeatherDailyDataEntity(
                        id = 0,
                        cityName = cityName,
                        cnt = remoteWeatherDailyInfo.cnt,
                        cod = remoteWeatherDailyInfo.cod,
                        list = remoteWeatherDailyInfo.list?.map { it.toWeatherInfoData() },
                        message = remoteWeatherDailyInfo.message
                    )
                )
            } catch (e: HttpException) {
                if (e.code() == 404) {
                    emit(
                        Resource.Error(
                            message = "Please Enter Correct City Name",
                            data = weatherDailyInfo
                        )
                    )
                } else {
                    emit(
                        Resource.Error(
                            message = "OOPS! Something went wrong",
                            data = weatherDailyInfo
                        )
                    )
                }
            } catch (e: IOException) {
                emit(
                    Resource.Error(
                        message = "Couldn't reach server. Please check your internet connection",
                        data = weatherDailyInfo
                    )
                )
            }
            val updatedWeatherInfo =
                weatherDailyInfoDao.getWeatherDataByCityName(cityName)?.toWeatherDailyInfo()
            emit(Resource.Success(data = updatedWeatherInfo))
        }
}

