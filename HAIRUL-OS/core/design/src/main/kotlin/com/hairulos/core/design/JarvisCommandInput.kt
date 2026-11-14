package com.hairulos.core.design

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Command bar: microphone button, text field and send button. The mic is a demo control in Phase 1. */
@Composable
fun JarvisCommandInput(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    onMicClick: () -> Unit,
    modifier: Modifier = Modifier,
    listening: Boolean = false,
    placeholder: String = "Type a command",
) {
    val shape = CutCornerShape(topStart = 0.dp, topEnd = 16.dp, bottomEnd = 0.dp, bottomStart = 16.dp)
    val canSend = value.isNotBlank()
    Row(
        modifier
            .fillMaxWidth()
            .clip(shape)
            .background(JarvisColors.Panel.copy(alpha = 0.95f))
            .border(
                1.dp,
                Brush.horizontalGradient(listOf(JarvisColors.Cyan.copy(alpha = 0.8f), JarvisColors.SecondaryBlue.copy(alpha = 0.4f))),
                shape,
            )
            .padding(horizontal = 6.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val micColor = if (listening) JarvisColors.Cyan else JarvisColors.SecondaryText
        Box(
            Modifier
                .size(48.dp)
                .clip(CircleShape)
                .border(BorderStroke(1.dp, micColor), CircleShape)
                .background(if (listening) JarvisColors.Cyan.copy(alpha = 0.18f) else Color.Transparent)
                .semantics { contentDescription = if (listening) "Stop microphone demo" else "Start microphone demo" }
                .clickable(role = Role.Button, onClick = onMicClick),
            contentAlignment = Alignment.Center,
        ) {
            JarvisMicIcon(tint = micColor)
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f).padding(horizontal = 10.dp),
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = JarvisColors.PrimaryText),
            cursorBrush = SolidColor(JarvisColors.Cyan),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(onSend = { if (canSend) onSend() }),
            decorationBox = { inner ->
                Box(contentAlignment = Alignment.CenterStart) {
                    if (value.isEmpty()) {
                        Text(
                            placeholder,
                            style = MaterialTheme.typography.bodyMedium,
                            color = JarvisColors.SecondaryText,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    inner()
                }
            },
        )
        IconButton(onClick = onSend, enabled = canSend) {
            Icon(
                Icons.AutoMirrored.Filled.Send,
                contentDescription = "Send command",
                tint = if (canSend) JarvisColors.Cyan else JarvisColors.SecondaryText,
            )
        }
    }
}

/** Hand-drawn microphone glyph (the core icon set has no mic, and we avoid the large extended set). */
@Composable
fun JarvisMicIcon(tint: Color, modifier: Modifier = Modifier, iconSize: Dp = 22.dp) {
    Canvas(modifier.size(iconSize)) {
        val w = size.width
        val h = size.height
        val sw = w * 0.09f
        drawRoundRect(
            tint,
            topLeft = Offset(w * 0.36f, h * 0.08f),
            size = Size(w * 0.28f, h * 0.46f),
            cornerRadius = CornerRadius(w * 0.14f),
        )
        drawArc(
            tint, 0f, 180f, false,
            topLeft = Offset(w * 0.24f, h * 0.28f),
            size = Size(w * 0.52f, h * 0.4f),
            style = Stroke(sw, cap = StrokeCap.Round),
        )
        drawLine(tint, Offset(w * 0.5f, h * 0.68f), Offset(w * 0.5f, h * 0.88f), strokeWidth = sw, cap = StrokeCap.Round)
        drawLine(tint, Offset(w * 0.36f, h * 0.9f), Offset(w * 0.64f, h * 0.9f), strokeWidth = sw, cap = StrokeCap.Round)
    }
}
