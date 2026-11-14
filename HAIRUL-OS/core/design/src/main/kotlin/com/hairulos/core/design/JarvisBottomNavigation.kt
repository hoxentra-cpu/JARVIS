package com.hairulos.core.design

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class JarvisNavItem(val label: String, val icon: ImageVector)

@Composable
fun JarvisBottomNavigation(
    items: List<JarvisNavItem>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .fillMaxWidth()
            .background(JarvisColors.Panel.copy(alpha = 0.97f))
            .navigationBarsPadding(),
    ) {
        Box(Modifier.fillMaxWidth().height(1.dp).background(JarvisColors.SecondaryBlue.copy(alpha = 0.6f)))
        Row(Modifier.fillMaxWidth()) {
            items.forEachIndexed { index, item ->
                val selected = index == selectedIndex
                val tint by animateColorAsState(
                    if (selected) JarvisColors.Cyan else JarvisColors.SecondaryText,
                    label = "navTint",
                )
                Column(
                    Modifier
                        .weight(1f)
                        .heightIn(min = 56.dp)
                        .clickable(role = Role.Tab) { onSelect(index) }
                        .padding(vertical = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box(
                        Modifier
                            .width(24.dp)
                            .height(2.dp)
                            .background(if (selected) JarvisColors.Cyan else Color.Transparent),
                    )
                    Spacer(Modifier.height(4.dp))
                    Icon(item.icon, contentDescription = null, tint = tint, modifier = Modifier.size(22.dp))
                    Text(
                        item.label,
                        color = tint,
                        fontSize = 10.sp,
                        letterSpacing = 0.4.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}
