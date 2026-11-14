package com.hairulos.feature.assistant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hairulos.core.common.AiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ChatMessage(val fromUser: Boolean, val text: String)

data class AssistantUiState(
    val aiState: AiState = AiState.IDLE,
    val messages: List<ChatMessage> = listOf(
        ChatMessage(false, "Interface online. Voice input and AI replies are demo-only in this phase."),
    ),
)

/** Holds the AI core state. Everything here is a scripted demo: no speech recognition, no AI model. */
class AssistantViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AssistantUiState())
    val uiState: StateFlow<AssistantUiState> = _uiState.asStateFlow()

    private var demoJob: Job? = null

    fun setState(state: AiState) {
        demoJob?.cancel()
        _uiState.update { it.copy(aiState = state) }
    }

    fun toggleMic() {
        setState(if (_uiState.value.aiState == AiState.LISTENING) AiState.IDLE else AiState.LISTENING)
    }

    /** Adds the user message, then plays THINKING -> SPEAKING -> IDLE with a canned reply. */
    fun submit(text: String) {
        val input = text.trim()
        if (input.isEmpty()) return
        demoJob?.cancel()
        _uiState.update {
            it.copy(aiState = AiState.THINKING, messages = (it.messages + ChatMessage(true, input)).takeLast(50))
        }
        demoJob = viewModelScope.launch {
            delay(1_500)
            _uiState.update {
                it.copy(
                    aiState = AiState.SPEAKING,
                    messages = (it.messages + ChatMessage(false, "Demo reply. Received \"$input\". No AI model is connected yet.")).takeLast(50),
                )
            }
            delay(1_800)
            _uiState.update { it.copy(aiState = AiState.IDLE) }
        }
    }
}
