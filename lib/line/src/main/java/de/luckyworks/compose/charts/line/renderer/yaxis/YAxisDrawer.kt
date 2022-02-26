package de.luckyworks.compose.charts.line.renderer.yaxis

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.drawscope.DrawScope

interface YAxisDrawer {

    fun drawAxisLine(
        drawScope: DrawScope,
        drawableArea: Rect
    )

    fun drawAxisLabels(
        drawScope: DrawScope,
        drawableArea: Rect,
        minValue: Float,
        maxValue: Float
    )
}