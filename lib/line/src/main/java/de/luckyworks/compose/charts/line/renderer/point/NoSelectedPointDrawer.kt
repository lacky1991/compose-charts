package de.luckyworks.compose.charts.line.renderer.point

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import de.luckyworks.compose.charts.line.LineChartData

object NoSelectedPointDrawer : SelectedPointDrawer {

    override fun drawPoint(
        drawScope: DrawScope,
        center: Offset,
        point: LineChartData.Point,
        pointLocation: Offset
    ) {
        // Leave empty on purpose, we do not want to draw anything.
    }
}