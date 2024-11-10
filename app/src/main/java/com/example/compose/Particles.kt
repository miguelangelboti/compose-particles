package com.example.compose

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import com.example.compose.extensions.onTouchPositionChanged
import kotlin.random.Random

@Composable
fun Particles(modifier: Modifier = Modifier) {

    val particles = remember { mutableStateListOf<Particle>() }
    var canvasSize by remember { mutableStateOf(Size.Zero) }
    var touchPosition by remember { mutableStateOf<Offset?>(null) }
    val colorAnimatable = remember { Animatable(Color.Red) }

    LaunchedEffect(Unit) {
        while (true) {
            colorAnimatable.animateTo(
                targetValue = Color(
                    Random.nextInt(220, 256),
                    Random.nextInt(50, 52),
                    Random.nextInt(50, 256)
                ),
                animationSpec = tween(
                    durationMillis = 3000,
                    easing = LinearEasing
                )
            )
        }
    }

    LaunchedEffect(Unit) {
        createParticlesFlow(
            delay = Constants.DELAY,
            touchWeight = Constants.TOUCH_WEIGHT,
            touchRadius = Constants.TOUCH_RADIUS,
            touchPositionProvider = { touchPosition },
            sizeProvider = { canvasSize }
        ).collect { particle ->
            if (particles.size >= Constants.PARTICLES) {
                particles.removeAt(0)
            }
            particles.add(particle)
        }
    }

    Canvas(
        modifier = modifier
            .onSizeChanged { canvasSize = Size(it.width.toFloat(), it.height.toFloat()) }
            .onTouchPositionChanged { touchPosition = it },
    ) {
        particles.forEach { particle ->
            drawCircle(
                color = colorAnimatable.value.copy(alpha = particle.alpha),
                radius = particle.radius.toFloat(),
                center = Offset(particle.x, particle.y)
            )
        }
    }
}

data class Particle(
    val x: Float,
    val y: Float,
    val alpha: Float,
    val radius: Int,
)
