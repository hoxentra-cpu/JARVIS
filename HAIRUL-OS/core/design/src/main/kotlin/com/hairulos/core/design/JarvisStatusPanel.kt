package com.hairulos.core.design

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/** Compact metric tile: label, headline value, optional detail line and progress bar (0..1). */
@Composable
fun JarvisStatusPanel(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    accent: Color = JarvisColors.Cyan,
    detail: String? = null,
    progress: Float? = null,
) {
    JarvisGlassCard(modifier = modifier, title = label, accent = accent) {
        Text(
            value,
            style = MaterialTheme.typography.titleLarge,
            color = JarvisColors.PrimaryText,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        if (detail != null) {
            Spacer(Modifier.height(2.dp))
            Text(
                detail,
                style = MaterialTheme.typography.bodySmall,
                color = JarvisColors.SecondaryText,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
        }
        if (progress != null) {
            Spacer(Modifier.height(8.dp))
            Box(Modifier.fillMaxWidth().height(4.dp).background(JarvisColors.SecondaryBlue.copy(alpha = 0.35f))) {
                Box(Modifier.fillMaxWidth(progress.coerceIn(0f, 1f)).fillMaxHeight().background(accent))
            }
        }
    }
}
