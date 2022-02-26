package de.luckyworks.compose.charts.line.renderer.line

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import de.luckyworks.compose.charts.line.LineChartData

data class PathEffectSelectedLineDrawer(
    val color: Color = Color.Cyan,
    val pathEffect: PathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 10f), 2f),
    val strokeWidth: Float = Stroke.HairlineWidth,
    val drawXLine: Boolean = true,
    val drawYLine: Boolean = true,
) : SelectedLineDrawer {

    override fun drawLine(
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
                    strokeWidth = strokeWidth,
                )
            }
            if (drawYLine) {
                drawLine(
                    color = color,
                    pathEffect = pathEffect,
                    start = Offset(pointLocation.x, 0f),
                    end = Offset(pointLocation.x, drawableArea.height),
                    strokeWidth = strokeWidth,
                )
            }
        }
    }
}