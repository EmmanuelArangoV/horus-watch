package com.horus.wear.presentation.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.unit.dp

@Composable
fun HeartIllustration(modifier: Modifier = Modifier, color: Color) {
    Canvas(modifier = modifier.size(24.dp)) {
        val width = size.width
        val height = size.height
        val path = Path().apply {
            moveTo(width / 2f, height / 5f)
            cubicTo(width * 5 / 8f, 0f, width, height / 5f, width, height / 2.5f)
            cubicTo(width, height * 2 / 3f, width / 2f, height * 5 / 6f, width / 2f, height)
            cubicTo(width / 2f, height * 5 / 6f, 0f, height * 2 / 3f, 0f, height / 2.5f)
            cubicTo(0f, height / 5f, width * 3 / 8f, 0f, width / 2f, height / 5f)
            close()
        }
        drawPath(path = path, color = color, style = Fill)
    }
}

@Composable
fun StarIllustration(modifier: Modifier = Modifier, color: Color) {
    Canvas(modifier = modifier.size(24.dp)) {
        val path = Path().apply {
            val cx = size.width / 2
            val cy = size.height / 2
            val outerRadius = size.width / 2
            val innerRadius = outerRadius / 2.5f
            val spikes = 5
            var angle = -Math.PI / 2
            moveTo(cx + outerRadius * Math.cos(angle).toFloat(), cy + outerRadius * Math.sin(angle).toFloat())
            for (i in 0 until spikes * 2) {
                val r = if (i % 2 == 0) outerRadius else innerRadius
                lineTo(cx + r * Math.cos(angle).toFloat(), cy + r * Math.sin(angle).toFloat())
                angle += Math.PI / spikes
            }
            close()
        }
        drawPath(path = path, color = color, style = Fill)
    }
}

@Composable
fun PillIllustration(modifier: Modifier = Modifier, color: Color) {
    Canvas(modifier = modifier.size(24.dp)) {
        val w = size.width
        val h = size.height
        drawRoundRect(
            color = color,
            size = androidx.compose.ui.geometry.Size(w, h / 2f),
            topLeft = androidx.compose.ui.geometry.Offset(0f, h / 4f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(h / 4f, h / 4f)
        )
    }
}

@Composable
fun ShieldIllustration(modifier: Modifier = Modifier, color: Color) {
    Canvas(modifier = modifier.size(24.dp)) {
        val w = size.width
        val h = size.height
        val path = Path().apply {
            moveTo(w / 2f, 0f)
            lineTo(w, h / 5f)
            lineTo(w, h / 2f)
            cubicTo(w, h * 0.8f, w / 2f, h, w / 2f, h)
            cubicTo(w / 2f, h, 0f, h * 0.8f, 0f, h / 2f)
            lineTo(0f, h / 5f)
            close()
        }
        drawPath(path = path, color = color, style = Fill)
    }
}

@Composable
fun PhoneIllustration(modifier: Modifier = Modifier, color: Color) {
    Canvas(modifier = modifier.size(24.dp)) {
        val w = size.width
        val h = size.height
        drawRoundRect(
            color = color,
            size = androidx.compose.ui.geometry.Size(w * 0.6f, h * 0.9f),
            topLeft = androidx.compose.ui.geometry.Offset(w * 0.2f, h * 0.05f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.1f, w * 0.1f)
        )
    }
}

