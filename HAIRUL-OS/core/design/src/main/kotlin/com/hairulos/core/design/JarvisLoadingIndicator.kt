package com.hairulos.core.design

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun JarvisLoadingIndicator(
    modifier: Modifier = Modifier,
    indicatorSize: Dp = 32.dp,
    color: Color = JarvisColors.Cyan,
) {
    val transition = rememberInfiniteTransition(label = "loading")
    val angle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(1200, easing = LinearEasing)),
        label = "loadingAngle",
    )
    Canvas(modifier.size(indicatorSize)) {
        val stroke = Stroke(width = size.minDimension * 0.09f, cap = StrokeCap.Round)
        val pad = stroke.width / 2f
        val arcSize = Size(size.width - 2 * pad, size.height - 2 * pad)
        rotate(angle) {
            drawArc(color, 0f, 90f, false, Offset(pad, pad), arcSize, style = stroke)
            drawArc(color.copy(alpha = 0.4f), 180f, 60f, false, Offset(pad, pad), arcSize, style = stroke)
        }
    }
}
