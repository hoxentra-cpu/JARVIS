package com.hairulos.core.design

import androidx.compose.runtime.Composable
import androidx.compose.runtime.FloatState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState

/**
 * A phase value (seconds * speed) that advances every frame. Read `.floatValue` only inside a
 * draw block so that only drawing, not composition, is invalidated each frame.
 */
@Composable
internal fun rememberPhase(speed: Float): FloatState {
    val phase = remember { mutableFloatStateOf(0f) }
    val currentSpeed = rememberUpdatedState(speed)
    LaunchedEffect(Unit) {
        var last = 0L
        while (true) {
            androidx.compose.runtime.withFrameNanos { now ->
                if (last != 0L) phase.floatValue += (now - last) / 1_000_000_000f * currentSpeed.value
                last = now
            }
        }
    }
    return phase
}
