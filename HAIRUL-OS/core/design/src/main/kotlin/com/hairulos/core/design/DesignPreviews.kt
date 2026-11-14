package com.hairulos.core.design

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hairulos.core.common.AiState

@Preview(name = "AI core", widthDp = 360, heightDp = 360)
@Composable
private fun AiCorePreview() = JarvisTheme {
    JarvisBackground { JarvisAICore(AiState.IDLE, Modifier.padding(16.dp)) }
}

@Preview(name = "AI core - thinking", widthDp = 360, heightDp = 360)
@Composable
private fun AiCoreThinkingPreview() = JarvisTheme {
    JarvisBackground { JarvisAICore(AiState.THINKING, Modifier.padding(16.dp)) }
}

@Preview(name = "Components", widthDp = 360, heightDp = 720)
@Composable
private fun ComponentsPreview() = JarvisTheme {
    JarvisBackground {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            JarvisTopBar("12:34:56", "Sat, 14 Nov 2026", "HAIRUL", "ONLINE · Wi-Fi", true, screenTitle = "Home")
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                JarvisStatusPanel("BATTERY", "82%", Modifier.weight(1f), detail = "Charging", progress = 0.82f)
                JarvisStatusPanel("WEATHER", "UNAVAILABLE", Modifier.weight(1f), accent = JarvisColors.Orange)
            }
            JarvisGlassCard(Modifier.fillMaxWidth(), title = "GLASS CARD") { Text("Body text", color = JarvisColors.PrimaryText) }
            JarvisWaveform(AiState.SPEAKING)
            JarvisStateSelector(AiState.LISTENING, {})
            JarvisLoadingIndicator(indicatorSize = 40.dp)
            JarvisCommandInput("", {}, {}, {})
            JarvisBottomNavigation(
                listOf(JarvisNavItem("HOME", Icons.Filled.Home), JarvisNavItem("SETTINGS", Icons.Filled.Settings)),
                0, {},
            )
        }
    }
}
