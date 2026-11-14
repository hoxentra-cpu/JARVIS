package com.hairulos.core.design

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/** Header: logo (+ current screen), live clock and date, profile chip and connection chip. */
@Composable
fun JarvisTopBar(
    clock: String,
    date: String,
    profileName: String,
    connectionLabel: String,
    connected: Boolean,
    modifier: Modifier = Modifier,
    screenTitle: String? = null,
) {
    Column(
        modifier
            .fillMaxWidth()
            .background(JarvisColors.Panel.copy(alpha = 0.9f))
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(
                    "J.A.R.V.I.S.",
                    style = MaterialTheme.typography.titleLarge,
                    color = JarvisColors.Cyan,
                    maxLines = 1,
                )
                if (screenTitle != null) {
                    Text(
                        screenTitle,
                        style = MaterialTheme.typography.labelSmall,
                        color = JarvisColors.SecondaryText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    clock,
                    style = MaterialTheme.typography.titleLarge.copy(fontFeatureSettings = "tnum"),
                    color = JarvisColors.PrimaryText,
                    maxLines = 1,
                )
                Text(
                    date,
                    style = MaterialTheme.typography.labelSmall,
                    color = JarvisColors.SecondaryText,
                    maxLines = 1,
                )
            }
        }
        Spacer(Modifier.height(6.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Chip(horizontalGap = 6) {
                Box(
                    Modifier.size(20.dp).clip(CircleShape).background(JarvisColors.SecondaryBlue),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        profileName.take(1),
                        style = MaterialTheme.typography.labelSmall,
                        color = JarvisColors.PrimaryText,
                    )
                }
                Text(
                    profileName,
                    style = MaterialTheme.typography.labelMedium,
                    color = JarvisColors.PrimaryText,
                    maxLines = 1,
                )
            }
            Spacer(Modifier.weight(1f))
            val dot = if (connected) JarvisColors.Cyan else JarvisColors.Orange
            Chip(horizontalGap = 6, border = dot.copy(alpha = 0.5f)) {
                Box(Modifier.size(8.dp).clip(CircleShape).background(dot))
                Text(
                    connectionLabel,
                    style = MaterialTheme.typography.labelMedium,
                    color = JarvisColors.PrimaryText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun Chip(
    horizontalGap: Int,
    border: androidx.compose.ui.graphics.Color = JarvisColors.SecondaryBlue.copy(alpha = 0.6f),
    content: @Composable androidx.compose.foundation.layout.RowScope.() -> Unit,
) {
    Row(
        Modifier
            .clip(CircleShape)
            .border(1.dp, border, CircleShape)
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(horizontalGap.dp),
        content = content,
    )
}
