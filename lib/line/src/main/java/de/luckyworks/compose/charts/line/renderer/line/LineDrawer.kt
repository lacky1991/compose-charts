package de.luckyworks.compose.charts.line.renderer.line

import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope

interface LineDrawer {
    fun drawLine(
        drawScope: DrawScope,
        linePath: Path
    )
}