package de.luckyworks.compose.charts.line.renderer.line

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.drawscope.DrawScope
import de.luckyworks.compose.charts.line.LineChartData

object NoSelectedLineDrawer : SelectedLineDrawer {

    override fun drawLine(
        drawScope: DrawScope,
        drawableArea: Rect,
        point: LineChartData.Point,
        pointLocation: Offset
    ) {
        // Leave empty on purpose, we do not want to draw anything.
    }
}