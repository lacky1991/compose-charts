package de.luckyworks.compose.charts.line.renderer.yaxis

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.luckyworks.compose.charts.piechart.utils.toLegacyInt
import kotlin.math.max
import kotlin.math.roundToInt

class InsideSmallYAxisDrawer(
    private val labelTextSize: TextUnit = 10.sp,
    private val labelTextColor: Color = Color.Black,
    private val labelValueFormatter: LabelFormatter = { value -> "%.0f".format(value) },
    private val axisLineThickness: Dp = 1.dp,
    private val axisLineWidth: Dp = 5.dp,
    private val axisLineColor: Color = Color(0x26111E38),
    private val paddingStart: Dp = 16.dp,
    private val linePathEffect: PathEffect? = null,
) : YAxisDrawer {

    private val textPaint = android.graphics.Paint().apply {
        isAntiAlias = true
        color = labelTextColor.toLegacyInt()
    }
    private val axisSmallStrokeLinePaint = Paint().apply {
        isAntiAlias = true
        color = axisLineColor.copy(alpha = 0.15f)
        pathEffect = linePathEffect
    }
    private val axisStrokeLinePaint = Paint().apply {
        isAntiAlias = true
        color = axisLineColor.copy(alpha = 0.5f)
        pathEffect = linePathEffect
    }
    private val textBounds = android.graphics.Rect()

    override fun drawAxisLine(
        drawScope: DrawScope,
        drawableArea: Rect
    ) = with(drawScope) {
        val lineThickness = axisLineThickness.toPx()

        val minLabelHeight = labelTextSize.toPx()
        val totalHeight = size.height
        val labelCount = max((totalHeight / minLabelHeight).roundToInt(), 2)

        (0..labelCount).forEach { i ->
            val y = (i * (totalHeight / labelCount))
            if (i % (labelCount / 2) == 0)
                drawContext.canvas.drawLine(
                    p1 = Offset(
                        x = drawableArea.left + paddingStart.toPx(),
                        y = y
                    ),
                    p2 = Offset(
                        x = drawableArea.left + paddingStart.toPx() + axisLineWidth.toPx() * 2,
                        y = y
                    ),
                    paint = axisStrokeLinePaint.apply {
                        strokeWidth = lineThickness
                    }
                )
            else
                drawContext.canvas.drawLine(
                    p1 = Offset(
                        x = drawableArea.left + paddingStart.toPx(),
                        y = y
                    ),
                    p2 = Offset(
                        x = drawableArea.left + paddingStart.toPx() + axisLineWidth.toPx(),
                        y = y
                    ),
                    paint = axisSmallStrokeLinePaint.apply {
                        strokeWidth = lineThickness
                    }
                )
        }
    }

    override fun drawAxisLabels(
        drawScope: DrawScope,
        drawableArea: Rect,
        minValue: Float,
        maxValue: Float
    ) = with(drawScope) {
        val labelPaint = textPaint.apply {
            textSize = labelTextSize.toPx()
        }
        val minLabelHeight = labelTextSize.toPx()
        val totalHeight = size.height - minLabelHeight
        val labelCount = max(((totalHeight) / minLabelHeight).roundToInt(), 2)

        listOf(0, labelCount).forEach { index ->
            val value = minValue + ((labelCount - index) * ((maxValue - minValue) / labelCount))
            val label = labelValueFormatter(value)
            labelPaint.getTextBounds(label, 0, label.length, textBounds)
            val y = (index * (totalHeight / labelCount)) + (textBounds.height() / 2)
            drawContext.canvas.nativeCanvas.drawText(
                label,
                paddingStart.toPx() + axisLineWidth.toPx() + 30f,
                y,
                labelPaint
            )
        }
    }
}