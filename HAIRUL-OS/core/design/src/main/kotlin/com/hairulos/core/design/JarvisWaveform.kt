package com.hairulos.core.design

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import com.hairulos.core.common.AiState
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sin

/** Equaliser-style bars. Purely visual in Phase 1: it does not read microphone audio. */
@Composable
fun JarvisWaveform(
    state: AiState,
    modifier: Modifier = Modifier,
    bars: Int = 32,
    animated: Boolean = true,
) {
    val color by animateColorAsState(state.coreColor(), label = "waveColor")
    val phase = rememberPhase(if (animated) 1f else 0f)

    Canvas(modifier.fillMaxWidth().height(48.dp)) {
        val t = phase.floatValue
        val gap = 3.dp.toPx()
        val barWidth = (size.width - gap * (bars - 1)) / bars
        val minBar = 2.dp.toPx()
        for (i in 0 until bars) {
            val a = 0.5f + 0.5f * sin(t * 4f + i * 0.55f)
            val b = 0.5f + 0.5f * sin(t * 6.8f + i * 1.3f)
            val amp = when (state) {
                AiState.IDLE -> 0.08f
                AiState.LISTENING -> 0.3f + 0.5f * b
                AiState.THINKING -> max(0f, 1f - abs((t * 8f) % bars - i) / 5f) * 0.9f + 0.06f
                AiState.SPEAKING -> 0.2f + 0.75f * a * b
                AiState.ERROR -> 0.06f
            }
            val h = (size.height * amp).coerceAtLeast(minBar)
            drawRoundRect(
                color = color.copy(alpha = 0.9f),
                topLeft = Offset(i * (barWidth + gap), (size.height - h) / 2f),
                size = Size(barWidth, h),
                cornerRadius = CornerRadius(barWidth / 2f),
            )
        }
    }
}
