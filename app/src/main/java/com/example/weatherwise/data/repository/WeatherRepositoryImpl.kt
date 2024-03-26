package com.example.weatherwise.data.repository

import com.example.weatherwise.data.model.WeatherInfo
import com.example.weatherwise.data.remote.RemoteDataSource
import com.example.weatherwise.data.remote.response.toModel
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : WeatherRepository {
    override suspend fun getWeatherData(lat: Float, lng: Float): WeatherInfo {
        val response = remoteDataSource.getWeatherDataResponse(lat, lng)
        return response.toModel()
    }
}