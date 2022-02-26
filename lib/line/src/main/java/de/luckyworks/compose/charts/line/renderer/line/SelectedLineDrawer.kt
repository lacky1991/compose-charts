package de.luckyworks.compose.charts.line.renderer.line

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.drawscope.DrawScope
import de.luckyworks.compose.charts.line.LineChartData

interface SelectedLineDrawer {
    fun drawLine(
        drawScope: DrawScope,
        drawableArea: Rect,
        point: LineChartData.Point,
        pointLocation: Offset
    )
}