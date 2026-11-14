package com.hairulos.feature.settings

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class SettingsUiState(
    val animations: Boolean = true,
    val particles: Boolean = true,
    val radar: Boolean = true,
)

/** Settings live in memory only in Phase 1; they reset when the app process ends. */
class SettingsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    fun setAnimations(value: Boolean) = _uiState.update { it.copy(animations = value) }
    fun setParticles(value: Boolean) = _uiState.update { it.copy(particles = value) }
    fun setRadar(value: Boolean) = _uiState.update { it.copy(radar = value) }
}
