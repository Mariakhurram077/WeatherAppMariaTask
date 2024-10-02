package com.example.androidtaskmaria.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.androidtaskmaria.data.local.entity.WeatherDailyDataEntity
import com.example.androidtaskmaria.data.local.entity.WeatherInfoEntity

@Database(version = 1, entities = [WeatherInfoEntity::class, WeatherDailyDataEntity::class])
@TypeConverters(WeatherDataConverters::class)
abstract class WeatherInfoDatabase : RoomDatabase() {
    abstract val weatherInfoDao: WeatherInfoDao
    abstract val weatherDailyInfoDao: WeatherDailyInfoDao
}