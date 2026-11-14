package com.hairulos.jarvis

import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hairulos.core.design.AiCoreOptions
import com.hairulos.core.design.JarvisBackground
import com.hairulos.core.design.JarvisBottomNavigation
import com.hairulos.core.design.JarvisTopBar
import com.hairulos.core.design.label
import com.hairulos.feature.assistant.AssistantScreen
import com.hairulos.feature.assistant.AssistantViewModel
import com.hairulos.feature.dashboard.DashboardViewModel
import com.hairulos.feature.dashboard.HomeScreen
import com.hairulos.feature.dashboard.MissionControlScreen
import com.hairulos.feature.dashboard.SystemScreen
import com.hairulos.feature.settings.SettingsScreen
import com.hairulos.feature.settings.SettingsViewModel

private const val PROFILE_NAME = "HAIRUL"

@Composable
fun JarvisApp() {
    val dashboardVm: DashboardViewModel = viewModel()
    val assistantVm: AssistantViewModel = viewModel()
    val settingsVm: SettingsViewModel = viewModel()

    val dashboard by dashboardVm.uiState.collectAsStateWithLifecycle()
    val assistant by assistantVm.uiState.collectAsStateWithLifecycle()
    val settings by settingsVm.uiState.collectAsStateWithLifecycle()
    val coreOptions = AiCoreOptions(settings.animations, settings.particles, settings.radar)

    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val current = Destination.entries.firstOrNull { it.route == backStackEntry?.destination?.route }
        ?: Destination.HOME

    // Mirror AI core state changes into the activity list.
    LaunchedEffect(assistant.aiState) {
        dashboardVm.log("AI core state: ${assistant.aiState.label()}")
    }

    fun navigateTo(destination: Destination) {
        navController.navigate(destination.route) {
            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    JarvisBackground {
        Scaffold(
            modifier = Modifier.imePadding(),
            containerColor = Color.Transparent,
            topBar = {
                JarvisTopBar(
                    clock = dashboard.time,
                    date = dashboard.date,
                    profileName = PROFILE_NAME,
                    connectionLabel = dashboard.connectionLabel,
                    connected = dashboard.device.isOnline,
                    screenTitle = current.title,
                )
            },
            bottomBar = {
                JarvisBottomNavigation(
                    items = Destination.entries.map { it.navItem },
                    selectedIndex = current.ordinal,
                    onSelect = { navigateTo(Destination.entries[it]) },
                )
            },
        ) { padding ->
            NavHost(
                navController = navController,
                startDestination = Destination.HOME.route,
                modifier = Modifier.padding(padding),
            ) {
                composable(Destination.HOME.route) {
                    HomeScreen(
                        state = dashboard,
                        aiState = assistant.aiState,
                        coreOptions = coreOptions,
                        onSelectState = assistantVm::setState,
                        onMicClick = assistantVm::toggleMic,
                        onCommand = {
                            assistantVm.submit(it)
                            navigateTo(Destination.ASSISTANT)
                        },
                    )
                }
                composable(Destination.ASSISTANT.route) {
                    AssistantScreen(
                        state = assistant,
                        coreOptions = coreOptions,
                        onSelectState = assistantVm::setState,
                        onMicClick = assistantVm::toggleMic,
                        onSend = assistantVm::submit,
                    )
                }
                composable(Destination.MISSION.route) { MissionControlScreen() }
                composable(Destination.SYSTEM.route) { SystemScreen(dashboard) }
                composable(Destination.SETTINGS.route) {
                    SettingsScreen(
                        state = settings,
                        onAnimations = settingsVm::setAnimations,
                        onParticles = settingsVm::setParticles,
                        onRadar = settingsVm::setRadar,
                    )
                }
            }
        }
    }
}
