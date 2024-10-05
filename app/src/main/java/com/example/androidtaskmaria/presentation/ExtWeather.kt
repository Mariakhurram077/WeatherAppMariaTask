package com.example.androidtaskmaria.presentation

import com.example.androidtaskmaria.R

fun String.toWeatherIcon(isDay: Boolean): Int {
    return when {
        contains("clear", true) -> if (isDay) R.drawable.clear_day else R.drawable.ic_clear_night
        contains("clouds", true) -> if (isDay) R.drawable.ic_cloudy else R.drawable.ic_cloudy_night
        contains("rain", true) -> R.drawable.ic_rain
        contains("thunderstorm", true) -> R.drawable.ic_rain
        contains(
            "smoke",
            true
        ) -> if (isDay) R.drawable.ic_smoke_haze else R.drawable.ic_smoke_haze_night

        contains(
            "haze",
            true
        ) -> if (isDay) R.drawable.ic_smoke_haze else R.drawable.ic_smoke_haze_night

        contains(
            "mist",
            true
        ) -> if (isDay) R.drawable.ic_smoke_haze else R.drawable.ic_smoke_haze_night

        else -> R.drawable.ic_cloudy
    }
}

fun Long.isDaytime(sunrise: Int, sunset: Int): Boolean {
    return this in sunrise..sunset
}

