package de.luckyworks.compose.charts.line.renderer.xaxis

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.drawscope.DrawScope

interface XAxisDrawer {

    fun requiredHeight(drawScope: DrawScope): Float

    fun drawAxisLine(
        drawScope: DrawScope,
        drawableArea: Rect
    )

    fun drawAxisLabels(
        drawScope: DrawScope,
        drawableArea: Rect,
        labels: List<String>
    )
}
