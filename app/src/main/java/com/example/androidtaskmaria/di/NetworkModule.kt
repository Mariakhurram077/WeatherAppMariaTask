package com.example.androidtaskmaria.di

import android.app.Application
import androidx.room.Room
import com.example.androidtaskmaria.BuildConfig
import com.example.androidtaskmaria.data.local.WeatherDataConverters
import com.example.androidtaskmaria.data.local.WeatherInfoDatabase
import com.example.androidtaskmaria.domain.usecase.GetWeatherData
import com.example.androidtaskmaria.data.remote.WeatherAPIService
import com.example.androidtaskmaria.data.repository.WeatherRepositoryImpl
import com.example.androidtaskmaria.data.util.GsonParser
import com.example.androidtaskmaria.domain.repository.WeatherRepository
import com.example.androidtaskmaria.domain.usecase.GetWeatherDailyData
import com.example.androidtaskmaria.domain.usecase.WeatherUseCases
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun providesBaseUrl(): String {
        return BuildConfig.BASE_URL
    }

    @Provides
    @Singleton
    fun providesRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): WeatherAPIService {
        return retrofit.create(WeatherAPIService::class.java)
    }

    @Provides
    @Singleton
    fun providesUseCase(repository: WeatherRepository): WeatherUseCases {
        return WeatherUseCases(GetWeatherData(repository), GetWeatherDailyData(repository))
    }

    @Provides
    @Singleton
    fun providesRepository(
        db: WeatherInfoDatabase, apiService: WeatherAPIService
    ): WeatherRepository {
        return WeatherRepositoryImpl(db.weatherInfoDao, db.weatherDailyInfoDao, apiService)
    }

    @Provides
    @Singleton
    fun providesDatabase(context: Application): WeatherInfoDatabase {
        return Room.databaseBuilder(context, WeatherInfoDatabase::class.java, "WeatherDatabase")
            .addTypeConverter(WeatherDataConverters(GsonParser(Gson()))).build()
    }
}
