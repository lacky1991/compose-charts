package de.luckyworks.compose.charts.line.renderer.line

import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class SolidLineDrawer(
    private val thickness: Dp = 3.dp,
    private val color: Color = Color.Cyan
) : LineDrawer {
    private val paint = Paint().apply {
        this.color = this@SolidLineDrawer.color
        this.style = PaintingStyle.Stroke
        this.isAntiAlias = true
    }

    override fun drawLine(
        drawScope: DrawScope,
        linePath: Path
    ) {
        with(drawScope) {
            drawContext.canvas.drawPath(
                path = linePath,
                paint = paint.apply {
                    strokeWidth = thickness.toPx()
                    pathEffect = PathEffect.cornerPathEffect(10.dp.toPx())
                }
            )
        }
    }
}