package com.titan.core.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun LiveGraph(
    data: List<Float>,
    maxValue: Float,
    lineColor: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxWidth().height(60.dp)) {
        val width = size.width
        val height = size.height
        if (data.isEmpty() || maxValue <= 0f) return@Canvas

        val path = Path()
        val stepX = width / (data.size - 1)

        data.forEachIndexed { index, value ->
            val x = index * stepX
            val y = height - (value / maxValue) * height
            if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }

        drawPath(path, color = lineColor, style = Stroke(width = 2.dp.toPx()))
    }
}
