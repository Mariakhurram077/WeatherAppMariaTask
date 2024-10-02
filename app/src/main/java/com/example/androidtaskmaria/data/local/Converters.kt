package com.example.androidtaskmaria.data.local

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.androidtaskmaria.data.util.JsonParser
import com.example.androidtaskmaria.domain.model.WeatherDailyInfo
import com.example.androidtaskmaria.domain.model.WeatherInfo
import com.google.gson.reflect.TypeToken

@ProvidedTypeConverter
class WeatherDataConverters(private val jsonParser: JsonParser) {
    @TypeConverter
    fun fromWeatherInfoListJson(json: String): List<WeatherInfo.WeatherData> {
        return jsonParser.fromJson<ArrayList<WeatherInfo.WeatherData>>(json, object :
            TypeToken<ArrayList<WeatherInfo.WeatherData>>() {}.type) ?: emptyList()
    }

    @TypeConverter
    fun toWeatherInfoListJson(list: List<WeatherInfo.WeatherData>): String {
        return jsonParser.toJson(
            list,
            object : TypeToken<ArrayList<WeatherInfo.WeatherData>>() {}.type
        ) ?: "[]"
    }

    @TypeConverter
    fun fromWeatherInfoDailyListJson(json: String): List<WeatherDailyInfo.WeatherInfoData> {
        return jsonParser.fromJson<ArrayList<WeatherDailyInfo.WeatherInfoData>>(json, object :
            TypeToken<ArrayList<WeatherDailyInfo.WeatherInfoData>>() {}.type) ?: emptyList()
    }

    @TypeConverter
    fun toWeatherInfoDailyListJson(list: List<WeatherDailyInfo.WeatherInfoData>): String {
        return jsonParser.toJson(
            list,
            object : TypeToken<ArrayList<WeatherDailyInfo.WeatherInfoData>>() {}.type
        ) ?: "[]"
    }
}