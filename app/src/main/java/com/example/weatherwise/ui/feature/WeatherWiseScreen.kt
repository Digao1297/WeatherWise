package com.example.weatherwise.ui.feature

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ComponentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherwise.data.model.WeatherInfo
import com.example.weatherwise.ui.theme.BlueSky
import com.example.weatherwise.ui.theme.WeatherWiseTheme

@Composable
fun WeatherWiseRoute(
    viewModel: WeatherWiseViewModel = viewModel(),
    activity: ComponentActivity,
) {
    val weatherInfoState by viewModel.weatherInfoState.collectAsStateWithLifecycle()

    RequestPermission(activity = activity, onGo = {
        WeatherWiseScreen(weatherInfo = weatherInfoState.weatherInfo, padding = it)
    })

}

@Composable
fun WeatherWiseScreen(
    context: Context = LocalContext.current,
    weatherInfo: WeatherInfo?,
    padding: PaddingValues,
) {
    weatherInfo?.let {
        val iconDrawable: Int = context.resources.getIdentifier(
            "weather_${weatherInfo.conditionIcon}",
            "drawable",
            context.packageName,
        )

        Surface(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            color = if (weatherInfo.isDay) {
                BlueSky
            } else Color.DarkGray
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = weatherInfo.locationName,
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = weatherInfo.dayOfWeek,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(32.dp))

                Image(
                    painter = painterResource(id = iconDrawable),
                    contentDescription = null,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = weatherInfo.condition,
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall,
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "${weatherInfo.temperature}°",
                    color = Color.White,
                    style = MaterialTheme.typography.displayLarge,
                )
            }
        }
    }

}

@Preview
@Composable
fun WeatherWisePreview() {
    WeatherWiseTheme {
        WeatherWiseScreen(
            weatherInfo = WeatherInfo(
                locationName = "Belo Horizonte",
                conditionIcon = "01d",
                condition = "Cloudy",
                temperature = 32,
                dayOfWeek = "Saturday",
                isDay = true,
            ),
            padding = PaddingValues()
        )
    }
}
