package com.example.androidtaskmaria.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.androidtaskmaria.data.local.entity.WeatherDailyDataEntity
import com.example.androidtaskmaria.data.local.entity.WeatherInfoEntity

@Dao
interface WeatherDailyInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weatherDailyInfoEntity: WeatherDailyDataEntity)

    @Query("SELECT * FROM weather_info_daily WHERE cityName = :cityName")
    suspend fun getWeatherDataByCityName(cityName: String): WeatherDailyDataEntity?

    @Query("DELETE FROM weather_info_daily WHERE cityName = :cityName")
    suspend fun deleteWeatherDataForCity(cityName: String)

}