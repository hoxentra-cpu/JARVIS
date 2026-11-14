package com.hairulos.jarvis

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.hairulos.core.design.JarvisNavItem

/** The five top-level screens. [title] appears in the top bar, [navItem] in the bottom bar. */
enum class Destination(val route: String, val title: String, navLabel: String, icon: ImageVector) {
    HOME("home", "Home", "HOME", Icons.Filled.Home),
    ASSISTANT("assistant", "Assistant", "ASSIST", Icons.Filled.Person),
    MISSION("mission", "Mission control", "MISSION", Icons.Filled.Menu),
    SYSTEM("system", "System", "SYSTEM", Icons.Filled.Info),
    SETTINGS("settings", "Settings", "SETTINGS", Icons.Filled.Settings);

    val navItem: JarvisNavItem = JarvisNavItem(navLabel, icon)
}
