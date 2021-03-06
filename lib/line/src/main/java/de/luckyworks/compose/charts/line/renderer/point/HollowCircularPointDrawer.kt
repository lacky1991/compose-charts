package de.luckyworks.compose.charts.line.renderer.point

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class HollowCircularPointDrawer(
    private val diameter: Dp = 8.dp,
    private val lineThickness: Dp = 2.dp,
    private val color: Color = Color.Blue
) : PointDrawer {

    private val paint = Paint().apply {
        color = this@HollowCircularPointDrawer.color
        style = PaintingStyle.Stroke
        isAntiAlias = true
    }

    override fun drawPoint(
        drawScope: DrawScope,
        center: Offset,
        isDragging: Boolean,
        isSelected: Boolean
    ) {
        with(drawScope as Density) {
            drawScope.drawContext.canvas.drawCircle(
                center = center,
                radius = diameter.toPx() / 2f,
                paint = paint.apply {
                    strokeWidth = lineThickness.toPx()
                }
            )
        }
    }
}
