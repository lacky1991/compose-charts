package de.luckyworks.compose.charts.bar

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import de.luckyworks.compose.charts.bar.renderer.label.LabelDrawer
import de.luckyworks.compose.charts.bar.renderer.xaxis.XAxisDrawer
import kotlin.math.abs

internal object BarChartUtils {
    fun axisAreas(
        drawScope: DrawScope,
        totalSize: Size,
        xAxisDrawer: XAxisDrawer,
        labelDrawer: LabelDrawer
    ): Pair<Rect, Rect> = with(drawScope) {
        // yAxis
        val yAxisTop = labelDrawer.requiredAboveBarHeight(drawScope)

        // Either 50dp or 10% of the chart width.
        val yAxisRight = minOf(50.dp.toPx(), size.width * 10f / 100f)

        // xAxis
        val xAxisRight = totalSize.width

        // Measure the size of the text and line.
        val xAxisTop = totalSize.height - xAxisDrawer.requiredHeight(drawScope)

        return Pair(
            Rect(yAxisRight, xAxisTop, xAxisRight, totalSize.height),
            Rect(0f, yAxisTop, yAxisRight, xAxisTop)
        )
    }

    fun barDrawableArea(xAxisArea: Rect): Rect {
        return Rect(
            left = xAxisArea.left,
            top = 0f,
            right = xAxisArea.right,
            bottom = xAxisArea.top
        )
    }

    fun calculateSelectedIndex(
        barChartData: BarChartData,
        barDrawableArea: Rect,
        touchEvent: Offset,
    ): Int? {
        return barChartData.bars.withIndex().minByOrNull { indexedValue ->
            val totalBars = barChartData.bars.size
            val widthOfBarArea = barDrawableArea.width / totalBars
            val offsetOfBar = widthOfBarArea * 0.2f

            val left = barDrawableArea.left + (indexedValue.index * widthOfBarArea)
            val right = left + widthOfBarArea - offsetOfBar

            abs(touchEvent.x - ((left + right) / 2))
        }?.index
    }

    fun BarChartData.forEachWithArea(
        drawScope: DrawScope,
        barDrawableArea: Rect,
        progress: Float,
        labelDrawer: LabelDrawer,
        barSpacePercent: Float,
        block: (index: Int, barArea: Rect, bar: BarChartData.Bar) -> Unit
    ) {
        val totalBars = bars.size
        val widthOfBarArea = barDrawableArea.width / totalBars
        val offsetOfBar = widthOfBarArea * barSpacePercent

        bars.forEachIndexed { index, bar ->
            val left = barDrawableArea.left + (index * widthOfBarArea)
            val height = barDrawableArea.height

            val barHeight = (height - labelDrawer.requiredAboveBarHeight(drawScope)) * progress

            val barArea = Rect(
                left = left + offsetOfBar,
                top = barDrawableArea.bottom - (bar.value / maxBarValue) * barHeight,
                right = left + widthOfBarArea - offsetOfBar,
                bottom = barDrawableArea.bottom
            )

            block(index, barArea, bar)
        }
    }
}