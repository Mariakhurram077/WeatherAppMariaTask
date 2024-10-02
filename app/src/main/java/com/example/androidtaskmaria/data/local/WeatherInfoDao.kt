package com.example.androidtaskmaria.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.androidtaskmaria.data.local.entity.WeatherInfoEntity

@Dao
interface WeatherInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weatherInfoEntity: WeatherInfoEntity)

    @Query("SELECT * FROM weather_info")
    suspend fun getWeatherInfo(): WeatherInfoEntity?

    @Query("DELETE FROM weather_info")
    suspend fun deleteWeatherInfo()
}