package de.luckyworks.compose.charts.piechart.renderer

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.drawscope.DrawScope
import de.luckyworks.compose.charts.piechart.PieChartData.Slice

class SimpleSliceDrawer(
    private val sliceThickness: Float = 25f,
    private val selectedSliceThickness: Float = 40f,
) : SliceDrawer {
    init {
        require(sliceThickness in 10f..100f) {
            "Thickness of $sliceThickness must be between 10-100"
        }
    }

    private val sectionPaint = Paint().apply {
        isAntiAlias = true
        style = PaintingStyle.Stroke
    }

    override fun drawSlice(
        drawScope: DrawScope,
        canvas: Canvas,
        area: Size,
        startAngle: Float,
        sweepAngle: Float,
        slice: Slice,
        isSelected: Boolean,
        isDragging: Boolean
    ) {
        val sliceThickness = calculateSectorThickness(sliceThickness = sliceThickness, area = area)
        val selectedSliceThickness =
            calculateSectorThickness(sliceThickness = selectedSliceThickness, area = area)
        val drawableArea = calculateDrawableArea(sliceThickness = sliceThickness, area = area)


        canvas.drawArc(
            rect = drawableArea, paint = sectionPaint.apply {
                color = slice.color
                strokeWidth = if (isSelected) selectedSliceThickness else sliceThickness
            }, startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false
        )
    }

    private fun calculateSectorThickness(sliceThickness: Float, area: Size): Float {
        val minSize = minOf(area.width, area.height)

        return minSize * (sliceThickness / 200f)
    }

    private fun calculateDrawableArea(sliceThickness: Float, area: Size): Rect {
        val sliceThicknessOffset =
            calculateSectorThickness(sliceThickness = sliceThickness, area = area) / 2f
        val offsetHorizontally = (area.width - area.height) / 2f

        return Rect(
            left = sliceThicknessOffset + offsetHorizontally,
            top = sliceThicknessOffset,
            right = area.width - sliceThicknessOffset - offsetHorizontally,
            bottom = area.height - sliceThicknessOffset
        )
    }
}