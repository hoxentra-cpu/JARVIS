package com.hairulos.core.design

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/** Dark holographic backdrop: faint grid plus a blue glow near the top. */
@Composable
fun JarvisBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(modifier.fillMaxSize().background(JarvisColors.Background)) {
        Canvas(Modifier.fillMaxSize()) {
            val step = 36.dp.toPx()
            val lineColor = JarvisColors.SecondaryBlue.copy(alpha = 0.14f)
            var x = 0f
            while (x < size.width) {
                drawLine(lineColor, Offset(x, 0f), Offset(x, size.height), strokeWidth = 1f)
                x += step
            }
            var y = 0f
            while (y < size.height) {
                drawLine(lineColor, Offset(0f, y), Offset(size.width, y), strokeWidth = 1f)
                y += step
            }
            drawRect(
                Brush.radialGradient(
                    colors = listOf(JarvisColors.PrimaryBlue.copy(alpha = 0.14f), Color.Transparent),
                    center = Offset(size.width / 2f, size.height * 0.28f),
                    radius = size.maxDimension * 0.7f,
                ),
            )
        }
        content()
    }
}
