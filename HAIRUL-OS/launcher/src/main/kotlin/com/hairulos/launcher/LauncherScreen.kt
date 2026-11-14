package com.hairulos.launcher

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hairulos.core.common.AiState
import com.hairulos.core.design.JarvisAICore
import com.hairulos.core.design.JarvisBackground
import com.hairulos.core.design.JarvisColors
import com.hairulos.core.design.JarvisGlassCard
import com.hairulos.core.design.JarvisTheme
import com.hairulos.core.design.coreColor
import com.hairulos.core.design.label

private val dockItems = listOf("Phone", "Messages", "Browser", "Camera")
private val pageTitles = listOf("Core", "Widgets", "Missions")

@Composable
fun LauncherScreen(
    state: LauncherUiState,
    onGesture: (LauncherGesture) -> Unit,
    onCoreTap: () -> Unit,
    onDockTap: (String) -> Unit,
    onCloseDrawer: () -> Unit,
) {
    JarvisBackground {
        Box(Modifier.fillMaxSize().launcherGestures(onGesture)) {
            Column(
                Modifier.fillMaxSize().systemBarsPadding().padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    state.time,
                    fontSize = 56.sp,
                    fontWeight = FontWeight.Light,
                    letterSpacing = 3.sp,
                    color = JarvisColors.PrimaryText,
                    maxLines = 1,
                )
                Text(state.date, style = MaterialTheme.typography.labelMedium, color = JarvisColors.SecondaryText, maxLines = 1)

                BoxWithConstraints(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    val coreSize = minOf(maxWidth * 0.8f, maxHeight * 0.85f, 320.dp)
                    when (state.page) {
                        0 -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            JarvisAICore(
                                state.aiState,
                                Modifier.size(coreSize).clip(CircleShape).clickable(role = Role.Button, onClick = onCoreTap),
                            )
                            Text(
                                "${state.aiState.label()} (tap core to change, demo)",
                                style = MaterialTheme.typography.labelSmall,
                                color = state.aiState.coreColor(),
                                textAlign = TextAlign.Center,
                            )
                        }
                        else -> JarvisGlassCard(Modifier.fillMaxWidth(), title = pageTitles[state.page].uppercase()) {
                            Text(
                                "Placeholder page. Swipe left or right to move between pages.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = JarvisColors.PrimaryText,
                            )
                        }
                    }
                }

                PageDots(count = LAUNCHER_PAGE_COUNT, selected = state.page)
                Spacer(Modifier.height(8.dp))
                Text(
                    state.notice ?: "Swipe up for apps",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (state.notice != null) JarvisColors.Orange else JarvisColors.SecondaryText,
                )
                Spacer(Modifier.height(8.dp))
                Dock(onDockTap)
            }

            AnimatedVisibility(
                visible = state.drawerOpen,
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutVertically { it } + fadeOut(),
            ) {
                AppDrawerPlaceholder(onGesture = onGesture, onClose = onCloseDrawer)
            }
        }
    }
}

@Composable
private fun PageDots(count: Int, selected: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        repeat(count) { i ->
            Box(
                Modifier
                    .size(if (i == selected) 10.dp else 7.dp)
                    .clip(CircleShape)
                    .background(if (i == selected) JarvisColors.Cyan else JarvisColors.SecondaryBlue),
            )
        }
    }
}

@Composable
private fun Dock(onTap: (String) -> Unit) {
    JarvisGlassCard(Modifier.fillMaxWidth(), contentPadding = androidx.compose.foundation.layout.PaddingValues(10.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            dockItems.forEach { name ->
                Column(
                    Modifier.clickable(role = Role.Button) { onTap(name) }.padding(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box(
                        Modifier.size(48.dp).clip(CircleShape).border(1.dp, JarvisColors.Cyan.copy(alpha = 0.6f), CircleShape),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(name.take(1), color = JarvisColors.Cyan, style = MaterialTheme.typography.titleMedium)
                    }
                    Text(name, style = MaterialTheme.typography.labelSmall, color = JarvisColors.SecondaryText, maxLines = 1)
                }
            }
        }
    }
}

@Composable
private fun AppDrawerPlaceholder(onGesture: (LauncherGesture) -> Unit, onClose: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .background(JarvisColors.Background.copy(alpha = 0.97f))
            .systemBarsPadding()
            .padding(16.dp),
    ) {
        Column(Modifier.fillMaxWidth().launcherGestures(onGesture)) {
            Box(
                Modifier.align(Alignment.CenterHorizontally).size(width = 40.dp, height = 4.dp)
                    .clip(CircleShape).background(JarvisColors.SecondaryBlue),
            )
            Spacer(Modifier.height(12.dp))
            Text("APP DRAWER", style = MaterialTheme.typography.titleLarge, color = JarvisColors.Cyan)
            Text(
                "Placeholder. Installed apps will be listed here in a later phase.",
                style = MaterialTheme.typography.bodySmall,
                color = JarvisColors.SecondaryText,
            )
            Text(
                "Swipe down on this header, press Back, or tap here to close.",
                style = MaterialTheme.typography.bodySmall,
                color = JarvisColors.SecondaryText,
                modifier = Modifier.clickable(role = Role.Button, onClick = onClose).padding(vertical = 8.dp),
            )
        }
        LazyVerticalGrid(
            columns = GridCells.Adaptive(76.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items((1..16).toList()) { n ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        Modifier.size(52.dp).clip(CircleShape).background(JarvisColors.Panel)
                            .border(1.dp, JarvisColors.SecondaryBlue.copy(alpha = 0.6f), CircleShape),
                    )
                    Text("App $n", style = MaterialTheme.typography.labelSmall, color = JarvisColors.SecondaryText, maxLines = 1)
                }
            }
        }
    }
}

@Preview(widthDp = 360, heightDp = 740)
@Composable
private fun LauncherPreview() = JarvisTheme {
    LauncherScreen(LauncherUiState(time = "12:34", date = "Sat, 14 Nov 2026", aiState = AiState.IDLE), {}, {}, {}, {})
}

@Preview(widthDp = 320, heightDp = 560)
@Composable
private fun LauncherDrawerPreview() = JarvisTheme {
    LauncherScreen(LauncherUiState(time = "12:34", drawerOpen = true), {}, {}, {}, {})
}
