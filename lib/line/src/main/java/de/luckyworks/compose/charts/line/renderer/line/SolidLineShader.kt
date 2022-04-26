package de.luckyworks.compose.charts.line.renderer.line

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope

class SolidLineShader(
    private val color: Color = Color.Blue
) : LineShader {
    override fun fillLine(drawScope: DrawScope, fillPath: Path, isDragging: Boolean) {
        drawScope.drawPath(
            path = fillPath,
            color = color
        )
    }
}
