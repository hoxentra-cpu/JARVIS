package com.hairulos.core.design

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.hairulos.core.common.AiState

/** Demo buttons that switch the AI core between its five states. */
@Composable
fun JarvisStateSelector(
    current: AiState,
    onSelect: (AiState) -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = CutCornerShape(topStart = 0.dp, topEnd = 8.dp, bottomEnd = 0.dp, bottomStart = 8.dp)
    Row(
        modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        AiState.entries.forEach { s ->
            val selected = s == current
            val c = s.coreColor()
            Box(
                Modifier
                    .heightIn(min = 40.dp)
                    .clip(shape)
                    .background(if (selected) c.copy(alpha = 0.22f) else JarvisColors.Panel)
                    .border(1.dp, if (selected) c else JarvisColors.SecondaryBlue.copy(alpha = 0.5f), shape)
                    .clickable(role = Role.Button) { onSelect(s) }
                    .padding(horizontal = 14.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    s.label(),
                    style = MaterialTheme.typography.labelMedium,
                    color = if (selected) c else JarvisColors.SecondaryText,
                    maxLines = 1,
                )
            }
        }
    }
}
