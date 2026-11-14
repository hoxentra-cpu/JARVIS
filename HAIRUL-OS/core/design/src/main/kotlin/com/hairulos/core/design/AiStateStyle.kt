package com.hairulos.core.design

import androidx.compose.ui.graphics.Color
import com.hairulos.core.common.AiState

fun AiState.coreColor(): Color = when (this) {
    AiState.IDLE -> JarvisColors.PrimaryBlue
    AiState.LISTENING -> JarvisColors.Cyan
    AiState.THINKING -> JarvisColors.Orange
    AiState.SPEAKING -> JarvisColors.Mint
    AiState.ERROR -> JarvisColors.Alert
}

fun AiState.label(): String = name

/** Plain-language description shown under the core. */
fun AiState.description(): String = when (this) {
    AiState.IDLE -> "Standing by"
    AiState.LISTENING -> "Listening (demo)"
    AiState.THINKING -> "Processing (demo)"
    AiState.SPEAKING -> "Responding (demo)"
    AiState.ERROR -> "Fault simulated"
}

internal fun AiState.animationSpeed(): Float = when (this) {
    AiState.IDLE -> 1f
    AiState.LISTENING -> 1.6f
    AiState.THINKING -> 3.2f
    AiState.SPEAKING -> 2f
    AiState.ERROR -> 0.4f
}

internal fun AiState.pulseAmplitude(): Float = when (this) {
    AiState.IDLE -> 0.6f
    AiState.LISTENING -> 1f
    AiState.THINKING -> 0.8f
    AiState.SPEAKING -> 1.3f
    AiState.ERROR -> 0.5f
}
