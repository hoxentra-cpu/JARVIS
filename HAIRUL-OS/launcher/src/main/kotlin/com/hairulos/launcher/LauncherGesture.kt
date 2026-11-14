package com.hairulos.launcher

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlin.math.abs

/** Swipe gestures the launcher understands. Add new ones here and handle them in [LauncherViewModel]. */
enum class LauncherGesture { SWIPE_UP, SWIPE_DOWN, SWIPE_LEFT, SWIPE_RIGHT }

internal fun classifyGesture(dx: Float, dy: Float, threshold: Float): LauncherGesture? {
    if (abs(dx) < threshold && abs(dy) < threshold) return null
    return if (abs(dy) >= abs(dx)) {
        if (dy < 0f) LauncherGesture.SWIPE_UP else LauncherGesture.SWIPE_DOWN
    } else {
        if (dx < 0f) LauncherGesture.SWIPE_LEFT else LauncherGesture.SWIPE_RIGHT
    }
}

/** Reports one [LauncherGesture] when a drag ends, if it travelled far enough. Taps pass through to children. */
@Composable
fun Modifier.launcherGestures(onGesture: (LauncherGesture) -> Unit): Modifier {
    val callback = rememberUpdatedState(onGesture)
    return pointerInput(Unit) {
        val threshold = 64.dp.toPx()
        var dx = 0f
        var dy = 0f
        detectDragGestures(
            onDragStart = {
                dx = 0f
                dy = 0f
            },
            onDragEnd = { classifyGesture(dx, dy, threshold)?.let { callback.value(it) } },
            onDragCancel = {
                dx = 0f
                dy = 0f
            },
            onDrag = { change, amount ->
                change.consume()
                dx += amount.x
                dy += amount.y
            },
        )
    }
}
