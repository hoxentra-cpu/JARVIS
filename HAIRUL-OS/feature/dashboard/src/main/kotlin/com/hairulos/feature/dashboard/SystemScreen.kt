package com.hairulos.feature.dashboard

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hairulos.core.design.JarvisBackground
import com.hairulos.core.design.JarvisGlassCard
import com.hairulos.core.design.JarvisInfoRow
import com.hairulos.core.design.JarvisTheme

@Composable
fun SystemScreen(state: DashboardUiState, modifier: Modifier = Modifier) {
    val device = state.device
    val config = LocalConfiguration.current
    val sizeClass = when {
        config.screenWidthDp < 360 -> "Small"
        config.screenWidthDp < 420 -> "Standard"
        else -> "Large"
    }
    val orientation = if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) "Landscape" else "Portrait"

    Column(
        modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        JarvisGlassCard(Modifier.fillMaxWidth(), title = "DEVICE") {
            JarvisInfoRow("Model", device.displayName())
            JarvisInfoRow("Android", device.androidRelease)
            JarvisInfoRow("API level", device.apiLevel.toString())
        }
        JarvisGlassCard(Modifier.fillMaxWidth(), title = "POWER") {
            JarvisInfoRow("Battery", if (device.batteryPercent < 0) "Unknown" else "${device.batteryPercent}%")
            JarvisInfoRow("Charging", if (device.isCharging) "Yes" else "No")
        }
        JarvisGlassCard(Modifier.fillMaxWidth(), title = "NETWORK") {
            JarvisInfoRow("Connection", state.connectionLabel)
            JarvisInfoRow("Online", if (device.isOnline) "Yes" else "No")
        }
        JarvisGlassCard(Modifier.fillMaxWidth(), title = "DISPLAY") {
            JarvisInfoRow("Size class", sizeClass)
            JarvisInfoRow("Window", "${config.screenWidthDp} x ${config.screenHeightDp} dp")
            JarvisInfoRow("Density", "${config.densityDpi} dpi")
            JarvisInfoRow("Orientation", orientation)
        }
    }
}

@Preview(widthDp = 360, heightDp = 700)
@Composable
private fun SystemPreview() = JarvisTheme { JarvisBackground { SystemScreen(DashboardUiState()) } }
