package com.example.weatherwise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.weatherwise.ui.feature.WeatherWiseRoute
import com.example.weatherwise.ui.theme.WeatherWiseTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherWiseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherWiseTheme {
                WeatherWiseRoute(activity = this)
            }
        }
    }
}