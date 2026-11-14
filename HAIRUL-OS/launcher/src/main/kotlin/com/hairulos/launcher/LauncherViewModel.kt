package com.hairulos.launcher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hairulos.core.common.AiState
import com.hairulos.core.common.JarvisClock
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

const val LAUNCHER_PAGE_COUNT = 3

data class LauncherUiState(
    val time: String = "--:--",
    val date: String = "",
    val aiState: AiState = AiState.IDLE,
    val drawerOpen: Boolean = false,
    val page: Int = 0,
    val notice: String? = null,
)

class LauncherViewModel : ViewModel() {
    private val local = MutableStateFlow(LauncherUiState())
    private var noticeJob: Job? = null

    val uiState: StateFlow<LauncherUiState> =
        combine(JarvisClock.ticker(5_000L), local) { now, s ->
            s.copy(time = JarvisClock.shortTime(now), date = JarvisClock.date(now))
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), LauncherUiState())

    fun onGesture(gesture: LauncherGesture) {
        when (gesture) {
            LauncherGesture.SWIPE_UP -> local.update { it.copy(drawerOpen = true) }
            LauncherGesture.SWIPE_DOWN -> local.update { it.copy(drawerOpen = false) }
            LauncherGesture.SWIPE_LEFT ->
                local.update { it.copy(page = (it.page + 1).coerceAtMost(LAUNCHER_PAGE_COUNT - 1)) }
            LauncherGesture.SWIPE_RIGHT ->
                local.update { it.copy(page = (it.page - 1).coerceAtLeast(0)) }
        }
    }

    fun closeDrawer() = local.update { it.copy(drawerOpen = false) }

    /** Demo: tapping the core steps through the five AI states. */
    fun cycleAiState() = local.update {
        val all = AiState.entries
        it.copy(aiState = all[(it.aiState.ordinal + 1) % all.size])
    }

    fun showNotice(message: String) {
        noticeJob?.cancel()
        local.update { it.copy(notice = message) }
        noticeJob = viewModelScope.launch {
            delay(2_500)
            local.update { it.copy(notice = null) }
        }
    }
}
