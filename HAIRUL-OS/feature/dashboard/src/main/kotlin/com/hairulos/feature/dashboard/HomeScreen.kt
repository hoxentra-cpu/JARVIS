package com.hairulos.feature.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hairulos.core.common.ActivityEntry
import com.hairulos.core.common.AiState
import com.hairulos.core.design.AiCoreOptions
import com.hairulos.core.design.JarvisAICore
import com.hairulos.core.design.JarvisBackground
import com.hairulos.core.design.JarvisColors
import com.hairulos.core.design.JarvisCommandInput
import com.hairulos.core.design.JarvisGlassCard
import com.hairulos.core.design.JarvisStateSelector
import com.hairulos.core.design.JarvisStatusPanel
import com.hairulos.core.design.JarvisTheme
import com.hairulos.core.design.JarvisWaveform
import com.hairulos.core.design.coreColor
import com.hairulos.core.design.description
import com.hairulos.core.design.label
import com.hairulos.core.device.DeviceStatus
import com.hairulos.core.device.NetworkKind

@Composable
fun HomeScreen(
    state: DashboardUiState,
    aiState: AiState,
    coreOptions: AiCoreOptions,
    onSelectState: (AiState) -> Unit,
    onMicClick: () -> Unit,
    onCommand: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var text by rememberSaveable { mutableStateOf("") }
    val device = state.device

    Column(modifier.fillMaxSize()) {
        BoxWithConstraints(Modifier.weight(1f).fillMaxWidth()) {
            val compact = maxWidth < 360.dp
            val coreSize = minOf(maxWidth * 0.82f, 340.dp, maxHeight * 0.6f).coerceAtLeast(180.dp)

            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                JarvisAICore(aiState, Modifier.size(coreSize), coreOptions)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("SYSTEM STATUS", style = MaterialTheme.typography.labelSmall, color = JarvisColors.SecondaryText)
                    Text(aiState.label(), style = MaterialTheme.typography.headlineMedium, color = aiState.coreColor())
                    Text(aiState.description(), style = MaterialTheme.typography.bodySmall, color = JarvisColors.SecondaryText)
                }
                JarvisWaveform(aiState, animated = coreOptions.animated)
                JarvisStateSelector(aiState, onSelectState)
                Text(
                    "Demo controls. Voice recognition and AI are not connected in this phase.",
                    style = MaterialTheme.typography.bodySmall,
                    color = JarvisColors.SecondaryText,
                )

                PairRow(
                    compact,
                    first = { m ->
                        val pct = device.batteryPercent
                        JarvisStatusPanel(
                            label = "BATTERY",
                            value = if (pct < 0) "N/A" else "$pct%",
                            modifier = m,
                            accent = if (pct in 0..20 && !device.isCharging) JarvisColors.Orange else JarvisColors.Cyan,
                            detail = if (device.isCharging) "Charging" else "On battery",
                            progress = if (pct < 0) null else pct / 100f,
                        )
                    },
                    second = { m ->
                        JarvisStatusPanel(
                            label = "NETWORK",
                            value = state.connectionLabel,
                            modifier = m,
                            accent = if (device.isOnline) JarvisColors.Cyan else JarvisColors.Orange,
                            detail = if (device.isOnline) "Connected" else "No active connection",
                        )
                    },
                )
                PairRow(
                    compact,
                    first = { m ->
                        JarvisStatusPanel(
                            label = "DEVICE",
                            value = device.displayName(),
                            modifier = m,
                            detail = "Android ${device.androidRelease} (API ${device.apiLevel})",
                        )
                    },
                    second = { m ->
                        JarvisStatusPanel(
                            label = "WEATHER",
                            value = "UNAVAILABLE",
                            modifier = m,
                            accent = JarvisColors.Orange,
                            detail = "No weather provider configured yet.",
                        )
                    },
                )

                JarvisGlassCard(Modifier.fillMaxWidth(), title = "RECENT ACTIVITIES") {
                    if (state.activities.isEmpty()) {
                        Text("No activity yet.", style = MaterialTheme.typography.bodyMedium, color = JarvisColors.SecondaryText)
                    }
                    state.activities.forEach { entry ->
                        Row(Modifier.padding(vertical = 3.dp)) {
                            Text(
                                entry.time,
                                style = MaterialTheme.typography.bodySmall.copy(fontFeatureSettings = "tnum"),
                                color = JarvisColors.SecondaryText,
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(
                                entry.message,
                                style = MaterialTheme.typography.bodySmall,
                                color = JarvisColors.PrimaryText,
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }
                }
            }
        }

        JarvisCommandInput(
            value = text,
            onValueChange = { text = it },
            onSend = {
                onCommand(text.trim())
                text = ""
            },
            onMicClick = onMicClick,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            listening = aiState == AiState.LISTENING,
            placeholder = "Ask J.A.R.V.I.S. (demo)",
        )
    }
}

/** Two tiles side by side on normal widths; stacked on very narrow screens. */
@Composable
private fun PairRow(
    compact: Boolean,
    first: @Composable (Modifier) -> Unit,
    second: @Composable (Modifier) -> Unit,
) {
    if (compact) {
        Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            first(Modifier.fillMaxWidth())
            second(Modifier.fillMaxWidth())
        }
    } else {
        Row(
            Modifier.fillMaxWidth().height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            first(Modifier.weight(1f).fillMaxHeight())
            second(Modifier.weight(1f).fillMaxHeight())
        }
    }
}

internal fun DeviceStatus.displayName(): String =
    if (model.startsWith(manufacturer, ignoreCase = true)) model else "$manufacturer $model"

@Preview(name = "Home - standard", widthDp = 360, heightDp = 700)
@Preview(name = "Home - small", widthDp = 320, heightDp = 560)
@Composable
private fun HomePreview() = JarvisTheme {
    JarvisBackground {
        HomeScreen(
            state = DashboardUiState(
                time = "12:34:56",
                date = "Sat, 14 Nov 2026",
                device = DeviceStatus(batteryPercent = 76, network = NetworkKind.WIFI),
                activities = listOf(ActivityEntry("12:30:01", "Interface initialised (Phase 1)")),
            ),
            aiState = AiState.IDLE,
            coreOptions = AiCoreOptions(),
            onSelectState = {},
            onMicClick = {},
            onCommand = {},
        )
    }
}
