package de.luckyworks.compose.charts.line.renderer.yaxis

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.luckyworks.compose.charts.piechart.utils.toLegacyInt
import kotlin.math.max
import kotlin.math.roundToInt

class InsideYAxisDrawer(
    private val labelTextSize: TextUnit = 10.sp,
    private val labelTextColor: Color = Color.Black,
    private val labelRatio: Float = 2.8f,
    private val labelValueFormatter: LabelFormatter = { value -> "%.0f".format(value) },
    private val axisLineThickness: Dp = 1.dp,
    private val axisLineColor: Color = Color(0xff8B8B8B),
    private val linePathEffect: PathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 2f),
) : YAxisDrawer {
    private val axisLinePaint = Paint().apply {
        isAntiAlias = true
        color = axisLineColor
        style = PaintingStyle.Stroke
    }
    private val textPaint = android.graphics.Paint().apply {
        isAntiAlias = true
        color = labelTextColor.toLegacyInt()
    }
    private val axisStrokeLinePaint = Paint().apply {
        isAntiAlias = true
        color = axisLineColor
        pathEffect = linePathEffect
    }
    private val textBounds = android.graphics.Rect()

    override fun drawAxisLine(
        drawScope: DrawScope,
        drawableArea: Rect
    ) = with(drawScope) {
        val lineThickness = axisLineThickness.toPx()
        val x = drawableArea.right - (lineThickness / 2f)

        drawContext.canvas.drawLine(
            p1 = Offset(
                x = x,
                y = drawableArea.top
            ),
            p2 = Offset(
                x = x,
                y = drawableArea.bottom
            ),
            paint = axisLinePaint.apply {
                strokeWidth = lineThickness
            }
        )
    }

    override fun drawAxisLabels(
        drawScope: DrawScope,
        drawableArea: Rect,
        minValue: Float,
        maxValue: Float
    ) = with(drawScope) {
        val lineThickness = axisLineThickness.toPx()
        val labelPaint = textPaint.apply {
            textSize = labelTextSize.toPx()
        }
        val minLabelHeight = (labelTextSize.toPx() * labelRatio)
        val totalHeight = drawScope.size.height
        val labelCount = max((drawScope.size.height / minLabelHeight).roundToInt(), 2)

        for (i in 1..labelCount) {
            val value = minValue + ((labelCount - i) * ((maxValue - minValue) / labelCount))

            val label = labelValueFormatter(value)
            val x = 2.dp.toPx()
            labelPaint.getTextBounds(label, 0, label.length, textBounds)

            val y = (i * (totalHeight / labelCount)) + (textBounds.height() / 2f) - 10f
            if (i != labelCount)
                drawContext.canvas.drawLine(
                    p1 = Offset(
                        x = 0f,
                        y = y
                    ),
                    p2 = Offset(
                        x = size.width,
                        y = y
                    ),
                    paint = axisStrokeLinePaint.apply {
                        strokeWidth = lineThickness
                    }
                )
            drawContext.canvas.nativeCanvas.drawText(
                label,
                x,
                y - 8.dp.toPx(),
                labelPaint
            )
        }
    }
}