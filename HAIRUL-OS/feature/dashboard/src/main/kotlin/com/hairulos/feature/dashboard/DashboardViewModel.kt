package com.hairulos.feature.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.hairulos.core.common.ActivityEntry
import com.hairulos.core.common.JarvisClock
import com.hairulos.core.device.DeviceStatus
import com.hairulos.core.device.DeviceStatusMonitor
import com.hairulos.core.device.NetworkKind
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

data class DashboardUiState(
    val time: String = "--:--:--",
    val date: String = "",
    val device: DeviceStatus = DeviceStatus(),
    val activities: List<ActivityEntry> = emptyList(),
) {
    val connectionLabel: String
        get() = when (device.network) {
            NetworkKind.NONE -> "OFFLINE"
            NetworkKind.WIFI -> "Wi-Fi"
            NetworkKind.CELLULAR -> "Mobile data"
            NetworkKind.ETHERNET -> "Ethernet"
            NetworkKind.OTHER -> "Online"
        }
}

class DashboardViewModel(application: Application) : AndroidViewModel(application) {
    private val monitor = DeviceStatusMonitor(application)

    private val activities = MutableStateFlow(
        System.currentTimeMillis().let { now ->
            listOf(
                ActivityEntry(JarvisClock.time(now), "Voice recognition not integrated yet"),
                ActivityEntry(JarvisClock.time(now), "Interface initialised (Phase 1)"),
            )
        },
    )

    val uiState: StateFlow<DashboardUiState> =
        combine(JarvisClock.ticker(), monitor.observe(), activities) { now, device, log ->
            DashboardUiState(
                time = JarvisClock.time(now),
                date = JarvisClock.date(now),
                device = device,
                activities = log,
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DashboardUiState())

    /** Adds a line at the top of the recent-activity list (keeps the latest six). */
    fun log(message: String) {
        val entry = ActivityEntry(JarvisClock.time(System.currentTimeMillis()), message)
        activities.update { (listOf(entry) + it).take(6) }
    }
}
