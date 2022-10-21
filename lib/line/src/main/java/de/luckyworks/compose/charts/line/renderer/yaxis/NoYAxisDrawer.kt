package de.luckyworks.compose.charts.line.renderer.yaxis

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.drawscope.DrawScope

object NoYAxisDrawer : YAxisDrawer {

    override fun drawAxisLabels(
        drawScope: DrawScope,
        drawableArea: Rect,
        minValue: Float,
        maxValue: Float
    ) {
        // Leave empty on purpose, we do not want to draw anything.
    }

    override fun drawAxisLine(drawScope: DrawScope, drawableArea: Rect) {
        // Leave empty on purpose, we do not want to draw anything.
    }
}
