package com.example.androidtaskmaria.data.remote.dto

import com.example.androidtaskmaria.data.local.entity.WeatherInfoEntity
import com.example.androidtaskmaria.data.remote.dto.WeatherInfoDTO.MainDTO
import com.example.androidtaskmaria.data.remote.dto.WeatherInfoDTO.SysDTO
import com.example.androidtaskmaria.data.remote.dto.WeatherInfoDTO.WeatherDTO
import com.example.androidtaskmaria.domain.model.WeatherDailyInfo
import com.example.androidtaskmaria.domain.model.WeatherInfo
import com.google.gson.annotations.SerializedName

data class WeatherInfoDTO(
    @SerializedName("city") val city: CityDTO?,
    @SerializedName("cnt") val cnt: Int?,
    @SerializedName("cod") val cod: String?,
    @SerializedName("list") val list: List<WeatherDataDTO>?,
    @SerializedName("message") val message: Int?
) {
    data class WeatherDataDTO(
        @SerializedName("rain") val rain: RainDTO,
        @SerializedName("clouds") val clouds: CloudsDTO?,
        @SerializedName("coord") val coord: CoordDTO?,
        @SerializedName("dt") val dt: Int?,
        @SerializedName("dt_txt") val dtTxt: String?,
        @SerializedName("id") val id: Int?,
        @SerializedName("pop") val pop: Double,
        @SerializedName("main") val main: MainDTO?,
        @SerializedName("name") val name: String?,
        @SerializedName("sys") val sys: SysDTO?,
        @SerializedName("visibility") val visibility: Int?,
        @SerializedName("weather") val weather: List<WeatherDTO>?,
        @SerializedName("wind") val wind: WindDTO?
    )

    data class CloudsDTO(
        @SerializedName("all") val all: Int?
    )

    data class CoordDTO(
        @SerializedName("lat") val latitude: Double?,
        @SerializedName("lon") val longitude: Double?
    )

    data class RainDTO(
        @SerializedName("3h") val hours: Double?
    )

    data class MainDTO(
        @SerializedName("feels_like") val feelLike: Double?,
        @SerializedName("grnd_level") val grndLevel: Int?,
        @SerializedName("humidity") val humidity: Int?,
        @SerializedName("pressure") val pressure: Int?,
        @SerializedName("sea_level") val seaLevel: Int?,
        @SerializedName("temp") val temp: Double?,
        @SerializedName("temp_kf") val tempKf: Double?,
        @SerializedName("temp_max") val maxTemp: Double?,
        @SerializedName("temp_min") val minTemp: Double?
    )

    data class SysDTO(
        @SerializedName("country") val country: String?,
        @SerializedName("sunrise") val sunrise: Int?,
        @SerializedName("sunset") val sunset: Int?,
        @SerializedName("timezone") val type: Int?,
        @SerializedName("pod") val pod: String?
    )

    data class WeatherDTO(
        @SerializedName("description") val description: String?,
        @SerializedName("icon") val icon: String?,
        @SerializedName("id") val id: Int?,
        @SerializedName("main") val main: String?
    )

    data class WindDTO(
        @SerializedName("deg") val deg: Int?,
        @SerializedName("gust") val gust: Double?,
        @SerializedName("speed") val speed: Double?
    )

    data class CityDTO(
        @SerializedName("coord") val coordinates: CoordDTO?,
        @SerializedName("country") val country: String?,
        @SerializedName("id") val id: Int?,
        @SerializedName("name") val name: String?,
        @SerializedName("population") val population: Int?,
        @SerializedName("sunrise") val sunrise: Int?,
        @SerializedName("sunset") val sunset: Int?,
        @SerializedName("timezone") val timezone: Int?
    )

}

fun WeatherInfoDTO.toWeatherInfo(): WeatherInfo {
    return WeatherInfo(cnt = cnt ?: 0, list = list?.map { it.toWeatherData() } ?: emptyList())
}

fun WeatherInfoDTO.toWeatherInfoEntity(): WeatherInfoEntity {
    return WeatherInfoEntity(cnt = cnt, list = list?.map { it.toWeatherData() } ?: emptyList())
}

fun WeatherInfoDTO.WeatherDataDTO.toWeatherData(): WeatherInfo.WeatherData {
    return WeatherInfo.WeatherData(
        id = id ?: 0,
        main = main?.toMain() ?: WeatherInfo.Main(
            feelLike = 0.0,
            grndLevel = 0,
            humidity = 0,
            pressure = 0,
            seaLevel = 0,
            temp = 0.0,
            maxTemp = 0.0,
            minTemp = 0.0
        ),
        name = name.orEmpty(),
        sys = sys?.toSys() ?: WeatherInfo.Sys(
            sunset = 0,
            sunrise = 0,
        ),
        weather = weather?.map {
            it.toWeather()
        } ?: emptyList())
}

fun MainDTO.toMain(): WeatherInfo.Main {
    return WeatherInfo.Main(
        feelLike = feelLike ?: 0.0,
        grndLevel = grndLevel ?: 0,
        humidity = humidity ?: 0,
        pressure = pressure ?: 0,
        seaLevel = seaLevel ?: 0,
        temp = temp ?: 0.0,
        maxTemp = maxTemp ?: 0.0,
        minTemp = minTemp ?: 0.0
    )
}

fun SysDTO.toSys(): WeatherInfo.Sys {
    return WeatherInfo.Sys(
        sunrise = sunrise ?: 0,
        sunset = sunset ?: 0,
    )
}

fun WeatherDTO.toWeather(): WeatherInfo.Weather {
    return WeatherInfo.Weather(
        main = main.orEmpty()
    )
}

fun WeatherInfoDTO.toWeatherDailyInfo(): WeatherDailyInfo {
    return WeatherDailyInfo(
        city = city?.toWeatherInfoCity() ?: WeatherDailyInfo.City(
            coord = WeatherDailyInfo.Coord(lat = 0.0, lon = 0.0),
            country = "",
            id = 0,
            name = "",
            population = 0,
            sunset = 0,
            sunrise = 0,
            timezone = 0
        ),
        cnt = cnt ?: 0,
        cod = cod ?: "",
        list = list?.map { it.toWeatherInfoData() } ?: emptyList(),
        message = message ?: 0)
}

fun WeatherInfoDTO.WeatherDataDTO.toWeatherInfoData(): WeatherDailyInfo.WeatherInfoData {
    return WeatherDailyInfo.WeatherInfoData(
        clouds = clouds?.toWeatherInfoClouds() ?: WeatherDailyInfo.Clouds(all = 0),
        dt = dt ?: 0,
        dt_txt = dtTxt ?: "",
        main = main?.toWeatherInfoMain() ?: WeatherDailyInfo.Main(
            feels_like = 0.0,
            grnd_level = 0,
            humidity = 0,
            pressure = 0,
            sea_level = 0,
            temp = 0.0,
            temp_kf = 0.0,
            temp_max = 0.0,
            temp_min = 0.0
        ),
        pop = pop,
        sys = sys?.toWeatherInfoSys() ?: WeatherDailyInfo.Sys(""),
        visibility = visibility ?: 0,
        weather = weather?.map { it.toWeatherInfoWeather() } ?: emptyList(),
        wind = wind?.toWeatherInfoWind() ?: WeatherDailyInfo.Wind(
            deg = 0,
            gust = 0.0,
            speed = 0.0
        )
    )
}

fun WeatherInfoDTO.MainDTO.toWeatherInfoMain(): WeatherDailyInfo.Main {
    return WeatherDailyInfo.Main(
        feels_like = feelLike ?: 0.0,
        grnd_level = grndLevel ?: 0,
        humidity = humidity ?: 0,
        pressure = pressure ?: 0,
        sea_level = seaLevel ?: 0,
        temp = temp ?: 0.0,
        temp_kf = tempKf ?: 0.0,
        temp_max = maxTemp ?: 0.0,
        temp_min = minTemp ?: 0.0
    )
}

fun WeatherInfoDTO.SysDTO.toWeatherInfoSys(): WeatherDailyInfo.Sys {
    return WeatherDailyInfo.Sys(pod = pod.orEmpty())
}

fun WeatherInfoDTO.CoordDTO.toWeatherInfoCoord(): WeatherDailyInfo.Coord {
    return WeatherDailyInfo.Coord(lat = latitude ?: 0.0, lon = longitude ?: 0.0)
}

fun WeatherInfoDTO.CloudsDTO.toWeatherInfoClouds(): WeatherDailyInfo.Clouds {
    return WeatherDailyInfo.Clouds(all = all ?: 0)
}

fun WeatherInfoDTO.WindDTO.toWeatherInfoWind(): WeatherDailyInfo.Wind {
    return WeatherDailyInfo.Wind(deg = deg ?: 0, gust = gust ?: 0.0, speed = speed ?: 0.0)
}

fun WeatherInfoDTO.CityDTO.toWeatherInfoCity(): WeatherDailyInfo.City {
    return WeatherDailyInfo.City(
        coord = coordinates?.toWeatherInfoCoord() ?: WeatherDailyInfo.Coord(lat = 0.0, lon = 0.0),
        country = country.orEmpty(),
        id = id ?: 0,
        name = name.orEmpty(),
        population = population ?: 0,
        sunrise = sunrise ?: 0,
        sunset = sunset ?: 0, timezone = timezone ?: 0
    )
}

fun WeatherInfoDTO.WeatherDTO.toWeatherInfoWeather(): WeatherDailyInfo.WeatherData {
    return WeatherDailyInfo.WeatherData(
        description = description.orEmpty(),
        icon = icon.orEmpty(),
        id = id ?: 0,
        main = main.orEmpty()
    )
}
