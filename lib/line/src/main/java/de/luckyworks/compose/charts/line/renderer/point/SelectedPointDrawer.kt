package de.luckyworks.compose.charts.line.renderer.point

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import de.luckyworks.compose.charts.line.LineChartData

interface SelectedPointDrawer {
    fun drawPoint(
        drawScope: DrawScope,
        center: Offset,
        point: LineChartData.Point,
        pointLocation: Offset,
    )
}
