package de.luckyworks.compose.charts.line.renderer.xaxis

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.drawscope.DrawScope

object NoXAxisDrawer : XAxisDrawer {

    override fun drawAxisLabels(drawScope: DrawScope, drawableArea: Rect, labels: List<String>) {
        // Leave empty on purpose, we do not want to draw anything.
    }

    override fun drawAxisLine(drawScope: DrawScope, drawableArea: Rect) {
        // Leave empty on purpose, we do not want to draw anything.
    }

    override fun requiredHeight(drawScope: DrawScope) = 0f

}