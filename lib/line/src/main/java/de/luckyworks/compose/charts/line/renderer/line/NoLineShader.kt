package de.luckyworks.compose.charts.line.renderer.line

import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope

object NoLineShader : LineShader {
    override fun fillLine(drawScope: DrawScope, fillPath: Path, isDragging: Boolean) {
        // Do nothing
    }
}
