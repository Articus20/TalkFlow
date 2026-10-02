package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.example.data.model.CallEngineState
import com.example.ui.theme.NeonMint
import com.example.ui.theme.VibrantPurple
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun VoiceWaveVisualizer(
    engineState: CallEngineState,
    rmsLevel: Float, // 0f to 1f
    modifier: Modifier = Modifier
) {
    val barCount = 13
    val barHeights = remember { mutableStateListOf<Float>().apply { repeat(barCount) { add(0.15f) } } }

    LaunchedEffect(engineState, rmsLevel) {
        var step = 0
        while (true) {
            step++
            for (i in 0 until barCount) {
                val envelope = 0.35f + 0.65f * cos((i - 6).toFloat() / 6f * (Math.PI.toFloat() / 2f))
                val targetHeight = when (engineState) {
                    CallEngineState.SPEAKING -> {
                        val wave = (sin(step * 0.35f + i * 0.8f) * 0.45f + 0.55f).coerceIn(0.15f, 1f)
                        envelope * wave
                    }
                    CallEngineState.LISTENING -> {
                        val micBoost = (rmsLevel * 1.5f).coerceIn(0f, 0.85f)
                        val noise = (sin(step * 0.5f + i * 0.4f) * 0.1f)
                        (envelope * (0.15f + micBoost + noise)).coerceIn(0.1f, 1f)
                    }
                    CallEngineState.WAITING -> {
                        val pulse = (sin(step * 0.15f + i * 0.25f) * 0.15f + 0.25f)
                        envelope * pulse
                    }
                    CallEngineState.THINKING -> {
                        val ripple = (sin(step * 0.4f + i * 0.5f) * 0.12f + 0.2f)
                        envelope * ripple
                    }
                    CallEngineState.DONE -> 0.1f
                }
                barHeights[i] = targetHeight.coerceIn(0.08f, 1f)
            }
            delay(50L)
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(84.dp),
        horizontalArrangement = Arrangement.spacedBy(7.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0 until barCount) {
            val animatedHeightFraction by animateFloatAsState(
                targetValue = barHeights[i],
                animationSpec = spring(dampingRatio = 0.75f, stiffness = 400f),
                label = "barHeight$i"
            )

            val isMint = i % 2 == 0
            val brush = if (isMint) {
                Brush.verticalGradient(listOf(NeonMint, NeonMint.copy(alpha = 0.7f)))
            } else {
                Brush.verticalGradient(listOf(VibrantPurple, VibrantPurple.copy(alpha = 0.8f)))
            }

            val currentHeight = (84.dp * animatedHeightFraction).coerceAtLeast(8.dp)

            Box(
                modifier = Modifier
                    .width(6.dp)
                    .height(currentHeight)
                    .clip(RoundedCornerShape(3.dp))
                    .background(brush)
            )
        }
    }
}
