package com.hairulos.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hairulos.core.design.JarvisBackground
import com.hairulos.core.design.JarvisColors
import com.hairulos.core.design.JarvisGlassCard
import com.hairulos.core.design.JarvisInfoRow
import com.hairulos.core.design.JarvisTheme

@Composable
fun SettingsScreen(
    state: SettingsUiState,
    onAnimations: (Boolean) -> Unit,
    onParticles: (Boolean) -> Unit,
    onRadar: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        JarvisGlassCard(Modifier.fillMaxWidth(), title = "AI CORE") {
            SwitchRow("Animations", "Rotation, pulse and waveform motion", state.animations, onAnimations)
            SwitchRow("Particles", "Floating particles around the core", state.particles, onParticles)
            SwitchRow("Radar sweep", "Rotating scan inside the core", state.radar, onRadar)
            Text(
                "Changes apply immediately and reset when the app restarts.",
                style = MaterialTheme.typography.bodySmall,
                color = JarvisColors.SecondaryText,
            )
        }
        JarvisGlassCard(Modifier.fillMaxWidth(), title = "JARVIS LAUNCHER", accent = JarvisColors.Orange) {
            Text(
                "The launcher is a separate app. It never asks to become your home screen. " +
                    "To use it, install it, then open Android Settings, Apps, Default apps, Home app " +
                    "(the path varies by manufacturer) and choose JARVIS Launcher. " +
                    "You can switch back there at any time.",
                style = MaterialTheme.typography.bodyMedium,
                color = JarvisColors.PrimaryText,
            )
        }
        JarvisGlassCard(Modifier.fillMaxWidth(), title = "ABOUT") {
            JarvisInfoRow("Application", "HAIRUL OS · JARVIS Mobile")
            JarvisInfoRow("Version", "0.1.0 (Phase 1)")
            JarvisInfoRow("Voice and AI", "Not connected")
        }
    }
}

@Composable
private fun SwitchRow(title: String, subtitle: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .toggleable(value = checked, role = Role.Switch, onValueChange = onChange),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleMedium, color = JarvisColors.PrimaryText)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = JarvisColors.SecondaryText)
        }
        Switch(
            checked = checked,
            onCheckedChange = null,
            colors = SwitchDefaults.colors(
                checkedThumbColor = JarvisColors.Background,
                checkedTrackColor = JarvisColors.Cyan,
                uncheckedThumbColor = JarvisColors.SecondaryText,
                uncheckedTrackColor = JarvisColors.Panel,
            ),
        )
    }
}

@Preview(widthDp = 360, heightDp = 700)
@Composable
private fun SettingsPreview() = JarvisTheme {
    JarvisBackground { SettingsScreen(SettingsUiState(), {}, {}, {}) }
}
