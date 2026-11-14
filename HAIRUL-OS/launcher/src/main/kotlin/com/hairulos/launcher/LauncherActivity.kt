package com.hairulos.launcher

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hairulos.core.design.JarvisTheme

class LauncherActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
        )
        setContent {
            JarvisTheme {
                val vm: LauncherViewModel = viewModel()
                val state by vm.uiState.collectAsStateWithLifecycle()
                // On a home screen, Back should only close the drawer; it must never exit.
                BackHandler(enabled = true) { vm.closeDrawer() }
                LauncherScreen(
                    state = state,
                    onGesture = vm::onGesture,
                    onCoreTap = vm::cycleAiState,
                    onDockTap = { vm.showNotice("$it is a placeholder in Phase 1") },
                    onCloseDrawer = vm::closeDrawer,
                )
            }
        }
    }
}
