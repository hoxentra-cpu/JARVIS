package com.hairulos.core.design

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.hairulos.core.common.AiState
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/** Toggles used by the Settings screen. */
data class AiCoreOptions(
    val animated: Boolean = true,
    val particles: Boolean = true,
    val radar: Boolean = true,
)

private val TWO_PI = (2 * PI).toFloat()

private class Particle(
    val angle: Float,
    val radiusFraction: Float,
    val speed: Float,
    val dotSize: Float,
    val twinkle: Float,
)

/**
 * The AI reactor: three concentric rotating rings, radar sweep, pulsing centre and particles,
 * all drawn on one Canvas. Colour, rotation speed and pulse depend on [state].
 */
@Composable
fun JarvisAICore(
    state: AiState,
    modifier: Modifier = Modifier,
    options: AiCoreOptions = AiCoreOptions(),
) {
    val color by animateColorAsState(state.coreColor(), tween(600), label = "coreColor")
    val phase = rememberPhase(if (options.animated) state.animationSpeed() else 0f)
    val particles = remember {
        val rnd = Random(7)
        List(36) {
            Particle(
                angle = rnd.nextFloat() * TWO_PI,
                radiusFraction = rnd.nextFloat(),
                speed = 0.4f + rnd.nextFloat(),
                dotSize = 1f + rnd.nextFloat() * 1.6f,
                twinkle = 1f + rnd.nextFloat() * 2f,
            )
        }
    }
    val description = "AI core, ${state.label()}"

    Canvas(modifier.aspectRatio(1f).semantics { contentDescription = description }) {
        val t = phase.floatValue
        val c = center
        val r = size.minDimension / 2f
        val col = color
        val thin = 1.5.dp.toPx()

        // Halo
        drawCircle(
            brush = Brush.radialGradient(listOf(col.copy(alpha = 0.28f), Color.Transparent), center = c, radius = r),
            radius = r,
            center = c,
        )

        // Ring 1: outer dashed ring, slow clockwise
        rotate(t * 18f, c) {
            drawCircle(
                color = col.copy(alpha = 0.55f),
                radius = r * 0.97f,
                center = c,
                style = Stroke(width = thin, pathEffect = PathEffect.dashPathEffect(floatArrayOf(r * 0.07f, r * 0.045f))),
            )
        }

        // Ring 2: three heavy arc segments, counter-clockwise
        val r2 = r * 0.80f
        drawCircle(col.copy(alpha = 0.18f), r2, c, style = Stroke(thin))
        rotate(-t * 32f, c) {
            repeat(3) { i ->
                drawArc(
                    color = col,
                    startAngle = i * 120f,
                    sweepAngle = 78f,
                    useCenter = false,
                    topLeft = Offset(c.x - r2, c.y - r2),
                    size = Size(r2 * 2f, r2 * 2f),
                    style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round),
                )
            }
        }

        // Ring 3: tick ring, clockwise
        val r3 = r * 0.60f
        rotate(t * 48f, c) {
            drawCircle(col.copy(alpha = 0.35f), r3, c, style = Stroke(thin))
            for (i in 0 until 48) {
                val a = i * (TWO_PI / 48f)
                val len = if (i % 4 == 0) r * 0.07f else r * 0.035f
                val inner = r3 - len
                drawLine(
                    color = col.copy(alpha = 0.8f),
                    start = Offset(c.x + cos(a) * inner, c.y + sin(a) * inner),
                    end = Offset(c.x + cos(a) * r3, c.y + sin(a) * r3),
                    strokeWidth = thin,
                )
            }
        }

        // Radar: range ring, crosshair and rotating sweep
        if (options.radar) {
            drawCircle(col.copy(alpha = 0.12f), r3 * 0.5f, c, style = Stroke(thin))
            drawLine(col.copy(alpha = 0.12f), Offset(c.x - r3, c.y), Offset(c.x + r3, c.y), strokeWidth = thin)
            drawLine(col.copy(alpha = 0.12f), Offset(c.x, c.y - r3), Offset(c.x, c.y + r3), strokeWidth = thin)
            rotate(t * 95f, c) {
                drawCircle(
                    brush = Brush.sweepGradient(listOf(Color.Transparent, col.copy(alpha = 0.5f)), center = c),
                    radius = r3 * 0.96f,
                    center = c,
                )
            }
        }

        // Pulsing centre
        val pulse = 0.5f + 0.5f * sin(t * 3f)
        val flicker = if (state == AiState.ERROR && sin(t * 14f) < 0f) 0.4f else 1f
        val coreR = r * (0.15f + 0.05f * state.pulseAmplitude() * pulse)
        drawCircle(
            brush = Brush.radialGradient(
                listOf(Color.White.copy(alpha = 0.95f * flicker), col.copy(alpha = 0.85f * flicker), Color.Transparent),
                center = c,
                radius = coreR * 2.2f,
            ),
            radius = coreR * 2.2f,
            center = c,
        )

        // Particles
        if (options.particles) {
            particles.forEach { p ->
                val a = p.angle + t * p.speed * 0.12f
                val rad = r * (0.22f + 0.7f * p.radiusFraction)
                val alpha = 0.25f + 0.6f * abs(sin(t * p.twinkle + p.angle))
                drawCircle(
                    color = col.copy(alpha = alpha),
                    radius = p.dotSize.dp.toPx(),
                    center = Offset(c.x + cos(a) * rad, c.y + sin(a) * rad),
                )
            }
        }
    }
}
