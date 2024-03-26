package com.example.weatherwise.data.repository

import com.example.weatherwise.data.model.WeatherInfo

interface WeatherRepository {

    suspend fun getWeatherData(lat: Float, lng: Float): WeatherInfo
}