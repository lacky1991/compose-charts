package de.luckyworks.compose.charts.line.renderer.line

import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope

interface LineShader {
    fun fillLine(
        drawScope: DrawScope,
        fillPath: Path,
        isDragging: Boolean
    )
}
