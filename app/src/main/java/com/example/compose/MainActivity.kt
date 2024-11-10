package com.example.compose

import android.os.Build.VERSION_CODES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.example.compose.ui.theme.ComposeTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.random.Random

class MainActivity : ComponentActivity() {

    @RequiresApi(VERSION_CODES.R)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        val colors = TopAppBarDefaults.topAppBarColors()
                        TopAppBar(
                            title = { Text("Particles") },
                            colors = colors.copy(containerColor = colors.containerColor.copy(alpha = 0.8f))
                        )
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.consumeWindowInsets(innerPadding)) {
                        Particles(Modifier.fillMaxSize())
                    }
                }
            }
        }
    }
}

fun createParticlesFlow(
    delay: Long,
    touchWeight: Float,
    touchRadius: Int,
    touchPositionProvider: () -> Offset?,
    sizeProvider: () -> Size
) = flow {
    while (true) {
        val touchPosition = touchPositionProvider()
        val generateInTouch = Random.nextFloat() < touchWeight
        val coordinates = if (touchPosition != null && generateInTouch) {
            // Random angle between 0 and 2π.
            val angle = Random.nextFloat() * (2 * Math.PI).toFloat()
            // Random radius with an uniform distribution on the circle.
            val positionRadius = Random.nextFloat().pow(1.2f) * touchRadius
            Pair(
                (touchPosition.x + cos(angle) * positionRadius),
                (touchPosition.y + sin(angle) * positionRadius),
            )
        } else {
            val size = sizeProvider()
            Pair(
                Random.nextFloat() * size.width,
                Random.nextFloat() * size.height,
            )
        }
        // Opacity between 0.2 and 1.
        val alpha = Random.nextFloat() * 0.8f + 0.2f
        val radius = Random.nextInt(2, 8)
        emit(Particle(coordinates.first, coordinates.second, alpha, radius))
        delay(delay)
    }
}
