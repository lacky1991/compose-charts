package de.luckyworks.compose.charts.line.renderer.line

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.luckyworks.compose.charts.line.LineChartData

class SolidLineDrawer(
    private val thickness: Dp = 3.dp,
    private val color: Color = Color.Cyan,
    private val drawXLine: Boolean = true,
    private val drawYLine: Boolean = true,
    private val pathEffect: PathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 10f), 2f),
) : LineDrawer {
    private val paint = Paint().apply {
        this.color = this@SolidLineDrawer.color
        this.style = PaintingStyle.Stroke
        this.isAntiAlias = true
    }

    override fun drawLine(
        drawScope: DrawScope,
        linePath: Path,
        isDragging: Boolean
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

    override fun drawSelectedLine(
        drawScope: DrawScope,
        drawableArea: Rect,
        point: LineChartData.Point,
        pointLocation: Offset
    ) {
        with(drawScope) {
            if (drawXLine) {
                drawLine(
                    color = color,
                    pathEffect = pathEffect,
                    start = Offset(drawableArea.left, pointLocation.y),
                    end = Offset(drawableArea.right, pointLocation.y),
                    strokeWidth = thickness.toPx(),
                )
            }
            if (drawYLine) {
                drawLine(
                    color = color,
                    pathEffect = pathEffect,
                    start = Offset(pointLocation.x, 0f),
                    end = Offset(pointLocation.x, drawableArea.height),
                    strokeWidth = thickness.toPx(),
                )
            }
        }
    }
}
