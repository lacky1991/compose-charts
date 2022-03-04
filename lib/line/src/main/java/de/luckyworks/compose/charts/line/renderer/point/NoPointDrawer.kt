package de.luckyworks.compose.charts.line.renderer.point

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope

object NoPointDrawer : PointDrawer {
    override fun drawPoint(
        drawScope: DrawScope,
        center: Offset,
        isDragging: Boolean,
        isSelected: Boolean
    ) {
        // Leave empty on purpose, we do not want to draw anything.
    }
}