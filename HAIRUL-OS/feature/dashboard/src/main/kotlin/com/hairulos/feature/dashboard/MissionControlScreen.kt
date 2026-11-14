package com.hairulos.feature.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hairulos.core.design.JarvisBackground
import com.hairulos.core.design.JarvisColors
import com.hairulos.core.design.JarvisGlassCard
import com.hairulos.core.design.JarvisStatusPanel
import com.hairulos.core.design.JarvisTheme

private enum class MissionStatus(val label: String, val color: Color) {
    ACTIVE("ACTIVE", JarvisColors.Cyan),
    PLANNED("PLANNED", JarvisColors.SecondaryText),
}

private data class Mission(val title: String, val detail: String, val status: MissionStatus, val progress: Float)

private val missions = listOf(
    Mission("Interface foundation", "Design system, dashboard, navigation and launcher module.", MissionStatus.ACTIVE, 0.9f),
    Mission("Voice input", "Speech recognition behind the microphone button.", MissionStatus.PLANNED, 0f),
    Mission("AI assistant", "Real responses in the Assistant screen.", MissionStatus.PLANNED, 0f),
    Mission("Weather", "Provider and location configuration for the weather tile.", MissionStatus.PLANNED, 0f),
    Mission("Launcher app list", "Installed apps in the launcher drawer and real favorites.", MissionStatus.PLANNED, 0f),
)

/** Static roadmap view. It shows planned work, not live telemetry. */
@Composable
fun MissionControlScreen(modifier: Modifier = Modifier) {
    Column(
        modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            JarvisStatusPanel("ACTIVE", missions.count { it.status == MissionStatus.ACTIVE }.toString(), Modifier.weight(1f))
            JarvisStatusPanel(
                "PLANNED",
                missions.count { it.status == MissionStatus.PLANNED }.toString(),
                Modifier.weight(1f),
                accent = JarvisColors.Orange,
            )
        }
        Text(
            "Roadmap only. These entries are not live system data.",
            style = MaterialTheme.typography.bodySmall,
            color = JarvisColors.SecondaryText,
        )
        missions.forEach { m ->
            JarvisGlassCard(Modifier.fillMaxWidth(), title = m.status.label, accent = m.status.color) {
                Text(m.title, style = MaterialTheme.typography.titleMedium, color = JarvisColors.PrimaryText)
                Text(m.detail, style = MaterialTheme.typography.bodySmall, color = JarvisColors.SecondaryText)
            }
        }
    }
}

@Preview(widthDp = 360, heightDp = 700)
@Composable
private fun MissionPreview() = JarvisTheme { JarvisBackground { MissionControlScreen() } }
