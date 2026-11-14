package com.hairulos.core.design

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

internal val JarvisCardShape = CutCornerShape(topStart = 0.dp, topEnd = 14.dp, bottomEnd = 0.dp, bottomStart = 14.dp)

/** Translucent panel with a gradient outline and two cut corners. */
@Composable
fun JarvisGlassCard(
    modifier: Modifier = Modifier,
    title: String? = null,
    accent: Color = JarvisColors.Cyan,
    contentPadding: PaddingValues = PaddingValues(14.dp),
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier
            .clip(JarvisCardShape)
            .background(
                Brush.verticalGradient(
                    listOf(JarvisColors.Panel.copy(alpha = 0.92f), JarvisColors.Panel.copy(alpha = 0.62f)),
                ),
            )
            .border(
                1.dp,
                Brush.linearGradient(listOf(accent.copy(alpha = 0.75f), JarvisColors.SecondaryBlue.copy(alpha = 0.25f))),
                JarvisCardShape,
            )
            .padding(contentPadding),
    ) {
        if (title != null) {
            Text(title, style = MaterialTheme.typography.labelSmall, color = accent)
            Spacer(Modifier.height(8.dp))
        }
        content()
    }
}
