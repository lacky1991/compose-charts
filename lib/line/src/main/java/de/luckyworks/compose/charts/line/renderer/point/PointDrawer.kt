package de.luckyworks.compose.charts.line.renderer.point

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope

interface PointDrawer {
    fun drawPoint(
        drawScope: DrawScope,
        center: Offset,
        isDragging: Boolean,
        isSelected: Boolean
    )
}
