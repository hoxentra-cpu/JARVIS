package com.hairulos.feature.assistant

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hairulos.core.common.AiState
import com.hairulos.core.design.AiCoreOptions
import com.hairulos.core.design.JarvisAICore
import com.hairulos.core.design.JarvisBackground
import com.hairulos.core.design.JarvisColors
import com.hairulos.core.design.JarvisCommandInput
import com.hairulos.core.design.JarvisGlassCard
import com.hairulos.core.design.JarvisStateSelector
import com.hairulos.core.design.JarvisTheme
import com.hairulos.core.design.JarvisWaveform
import com.hairulos.core.design.coreColor
import com.hairulos.core.design.description
import com.hairulos.core.design.label

@Composable
fun AssistantScreen(
    state: AssistantUiState,
    coreOptions: AiCoreOptions,
    onSelectState: (AiState) -> Unit,
    onMicClick: () -> Unit,
    onSend: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var text by rememberSaveable { mutableStateOf("") }
    val listState = rememberLazyListState()

    // Item 0 is the header; messages occupy 1..size.
    LaunchedEffect(state.messages.size) { listState.animateScrollToItem(state.messages.size) }

    Column(modifier.fillMaxSize()) {
        BoxWithConstraints(Modifier.weight(1f).fillMaxWidth()) {
            val coreSize = minOf(maxWidth * 0.6f, 240.dp, maxHeight * 0.4f).coerceAtLeast(120.dp)
            LazyColumn(
                Modifier.fillMaxSize(),
                state = listState,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                item {
                    Column(
                        Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        JarvisAICore(state.aiState, Modifier.size(coreSize), coreOptions)
                        Text(
                            "${state.aiState.label()}  ${state.aiState.description()}",
                            style = MaterialTheme.typography.titleMedium,
                            color = state.aiState.coreColor(),
                        )
                        JarvisWaveform(state.aiState, animated = coreOptions.animated)
                        JarvisStateSelector(state.aiState, onSelectState)
                    }
                }
                items(state.messages) { message ->
                    Box(
                        Modifier.fillMaxWidth(),
                        contentAlignment = if (message.fromUser) Alignment.CenterEnd else Alignment.CenterStart,
                    ) {
                        JarvisGlassCard(
                            Modifier.widthIn(max = 320.dp),
                            title = if (message.fromUser) "HAIRUL" else "J.A.R.V.I.S.",
                            accent = if (message.fromUser) JarvisColors.Orange else JarvisColors.Cyan,
                        ) {
                            Text(message.text, style = MaterialTheme.typography.bodyMedium, color = JarvisColors.PrimaryText)
                        }
                    }
                }
            }
        }
        JarvisCommandInput(
            value = text,
            onValueChange = { text = it },
            onSend = {
                onSend(text)
                text = ""
            },
            onMicClick = onMicClick,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            listening = state.aiState == AiState.LISTENING,
            placeholder = "Type a command (demo)",
        )
    }
}

@Preview(widthDp = 360, heightDp = 700)
@Composable
private fun AssistantPreview() = JarvisTheme {
    JarvisBackground {
        AssistantScreen(AssistantUiState(), AiCoreOptions(), {}, {}, {})
    }
}
