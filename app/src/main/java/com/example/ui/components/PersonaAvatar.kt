package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.data.model.CallEngineState
import com.example.ui.theme.LilacAccent
import com.example.ui.theme.NeonMint
import com.example.ui.theme.VibrantPurple

@Composable
fun PersonaAvatar(
    engineState: CallEngineState,
    size: Dp = 100.dp,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "avatarPulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    val ringColor = when (engineState) {
        CallEngineState.SPEAKING -> LilacAccent
        CallEngineState.LISTENING -> NeonMint
        CallEngineState.WAITING -> CoolGrayAvatar
        CallEngineState.THINKING -> VibrantPurple
        CallEngineState.DONE -> NeonMint
    }

    Box(
        modifier = modifier
            .size(size)
            .scale(if (engineState == CallEngineState.LISTENING || engineState == CallEngineState.SPEAKING) pulseScale else 1f),
        contentAlignment = Alignment.Center
    ) {
        // Outer glowing halo ring
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            ringColor.copy(alpha = 0.35f),
                            Color.Transparent
                        )
                    )
                )
                .border(2.5.dp, ringColor, CircleShape)
        )

        // Inner avatar surface
        Box(
            modifier = Modifier
                .size(size - 18.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            VibrantPurple.copy(alpha = 0.85f),
                            Color(0xFF140D2E)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.SmartToy,
                contentDescription = "AI Persona Avatar",
                tint = if (engineState == CallEngineState.SPEAKING) LilacAccent else NeonMint,
                modifier = Modifier.size(size * 0.44f)
            )
        }
    }
}

private val CoolGrayAvatar = Color(0xFF8D99AE)
