package com.example.weatherwise.data

import com.example.weatherwise.BuildConfig
import com.example.weatherwise.data.remote.RemoteDataSource
import com.example.weatherwise.data.remote.response.WeatherDataResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject

class KtorRemoteDataSource @Inject constructor(
    private val httpClient: HttpClient,
) : RemoteDataSource {

    companion object {
        private const val BASE_URL = BuildConfig.BASE_URL
    }

    override suspend fun getWeatherDataResponse(lat: Float, lng: Float): WeatherDataResponse =
        httpClient
            .get("$BASE_URL/weather?lat=$lat&lon=$lng&appid=${BuildConfig.API_KEY}&units=metric")
            .body()
}