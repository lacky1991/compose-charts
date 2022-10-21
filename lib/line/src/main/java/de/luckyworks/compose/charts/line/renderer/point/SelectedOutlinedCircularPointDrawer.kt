package de.luckyworks.compose.charts.line.renderer.point

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class SelectedOutlinedCircularPointDrawer(
    private val radius: Dp = 4.dp,
    private val color: Color = Color.Blue,
    private val innerColor: Color = Color.Blue,
) : PointDrawer {
    override fun drawPoint(
        drawScope: DrawScope,
        center: Offset,
        isDragging: Boolean,
        isSelected: Boolean
    ) = with(drawScope) {
        if (isSelected) {
            drawCircle(
                color = innerColor,
                radius = radius.toPx(),
                center = center,
            )
            drawCircle(
                color = color,
                radius = radius.toPx(),
                style = Stroke(width = 6f),
                center = center,
            )
        }

    }
}
