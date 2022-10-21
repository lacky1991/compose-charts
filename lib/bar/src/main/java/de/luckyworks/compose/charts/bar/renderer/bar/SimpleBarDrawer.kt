package de.luckyworks.compose.charts.bar.renderer.bar

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import de.luckyworks.compose.charts.bar.BarChartData

class SimpleBarDrawer : BarDrawer {
    private val barPaint = Paint().apply {
        this.isAntiAlias = true
    }

    override fun drawBar(
        drawScope: DrawScope,
        canvas: Canvas,
        barArea: Rect,
        bar: BarChartData.Bar,
        isSelected: Boolean,
        isDragging: Boolean,
    ) {
        canvas.drawRect(barArea, barPaint.apply {
            color = bar.color.copy(alpha = if (isSelected) 1f else 0.4f)
        })
    }
}
