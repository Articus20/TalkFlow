package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CoolGray
import com.example.ui.theme.NeonMint
import com.example.ui.theme.WhiteText
import kotlin.math.cos
import kotlin.math.sin

data class RadarDimension(val label: String, val value: Int) // 0-100

@Composable
fun RadarChart(
    dimensions: List<RadarDimension>,
    modifier: Modifier = Modifier
) {
    val animProgress = remember { Animatable(0f) }

    LaunchedEffect(dimensions) {
        animProgress.snapTo(0f)
        animProgress.animateTo(1f, animationSpec = tween(700))
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Canvas(
            modifier = Modifier.size(140.dp)
        ) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val maxRadius = (size.minDimension / 2f) * 0.85f
            val count = dimensions.size.coerceAtLeast(3)

            // Draw concentric background polygons (25%, 50%, 75%, 100%)
            val gridSteps = listOf(0.25f, 0.5f, 0.75f, 1f)
            for (step in gridSteps) {
                val gridPath = Path()
                for (i in 0 until count) {
                    val angle = (-Math.PI / 2.0 + i * (2 * Math.PI / count)).toFloat()
                    val r = maxRadius * step
                    val x = center.x + r * cos(angle)
                    val y = center.y + r * sin(angle)
                    if (i == 0) gridPath.moveTo(x, y) else gridPath.lineTo(x, y)
                }
                gridPath.close()
                drawPath(
                    path = gridPath,
                    color = Color(0x338D99AE),
                    style = Stroke(width = 1.dp.toPx())
                )
            }

            // Draw radial spoke lines
            for (i in 0 until count) {
                val angle = (-Math.PI / 2.0 + i * (2 * Math.PI / count)).toFloat()
                val endX = center.x + maxRadius * cos(angle)
                val endY = center.y + maxRadius * sin(angle)
                drawLine(
                    color = Color(0x228D99AE),
                    start = center,
                    end = Offset(endX, endY),
                    strokeWidth = 1.dp.toPx()
                )
            }

            // Draw data polygon
            val dataPath = Path()
            val pointCoords = mutableListOf<Offset>()

            for (i in 0 until count) {
                val dimValue = (dimensions.getOrNull(i)?.value ?: 50) / 100f
                val effectiveRadius = maxRadius * dimValue * animProgress.value
                val angle = (-Math.PI / 2.0 + i * (2 * Math.PI / count)).toFloat()
                val x = center.x + effectiveRadius * cos(angle)
                val y = center.y + effectiveRadius * sin(angle)
                pointCoords.add(Offset(x, y))

                if (i == 0) dataPath.moveTo(x, y) else dataPath.lineTo(x, y)
            }
            dataPath.close()

            // Fill polygon with Neon Mint glow
            drawPath(
                path = dataPath,
                color = NeonMint.copy(alpha = 0.28f)
            )

            // Outline polygon
            drawPath(
                path = dataPath,
                color = NeonMint,
                style = Stroke(width = 2.5.dp.toPx())
            )

            // Points
            for (point in pointCoords) {
                drawCircle(
                    color = NeonMint,
                    radius = 3.5.dp.toPx(),
                    center = point
                )
            }
        }

        // Dimension Chips row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            dimensions.forEach { dim ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = dim.label,
                        color = CoolGray,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "${dim.value}",
                        color = WhiteText,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
