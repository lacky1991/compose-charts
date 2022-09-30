package de.luckyworks.compose.charts.line

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.drawscope.DrawScope
import de.luckyworks.compose.charts.line.LineChartUtils.calculateDrawableArea
import de.luckyworks.compose.charts.line.LineChartUtils.calculateFillPath
import de.luckyworks.compose.charts.line.LineChartUtils.calculateLinePath
import de.luckyworks.compose.charts.line.LineChartUtils.calculatePointLocation
import de.luckyworks.compose.charts.line.LineChartUtils.calculateSelectedIndex
import de.luckyworks.compose.charts.line.LineChartUtils.calculateXAxisDrawableArea
import de.luckyworks.compose.charts.line.LineChartUtils.calculateXAxisLabelsDrawableArea
import de.luckyworks.compose.charts.line.LineChartUtils.calculateYAxisDrawableArea
import de.luckyworks.compose.charts.line.LineChartUtils.withProgress
import de.luckyworks.compose.charts.line.renderer.line.GradientLineShader
import de.luckyworks.compose.charts.line.renderer.line.LineDrawer
import de.luckyworks.compose.charts.line.renderer.line.LineShader
import de.luckyworks.compose.charts.line.renderer.line.SolidLineDrawer
import de.luckyworks.compose.charts.line.renderer.point.FilledCircularPointDrawer
import de.luckyworks.compose.charts.line.renderer.point.PointDrawer
import de.luckyworks.compose.charts.line.renderer.xaxis.SimpleXAxisDrawer
import de.luckyworks.compose.charts.line.renderer.xaxis.XAxisDrawer
import de.luckyworks.compose.charts.line.renderer.yaxis.SimpleYAxisDrawer
import de.luckyworks.compose.charts.line.renderer.yaxis.YAxisDrawer
import de.luckyworks.compose.charts.piechart.animation.simpleChartAnimation
import de.luckyworks.compose.charts.piechart.utils.onTouch

/**
 * A line chart that animates when loaded.
 */
@Composable
fun LineChart(
    modifier: Modifier = Modifier,
    lineChartData: LineChartData,
    animation: AnimationSpec<Float> = simpleChartAnimation(),
    pointDrawer: PointDrawer = FilledCircularPointDrawer(),
    lineDrawer: LineDrawer = SolidLineDrawer(),
    lineShader: LineShader = GradientLineShader(),
    xAxisDrawer: XAxisDrawer = SimpleXAxisDrawer(),
    yAxisDrawer: YAxisDrawer = SimpleYAxisDrawer(),
    horizontalOffset: Float = 0.0f,
    onRelease: (() -> Unit)? = null,
    onSelection: ((index: Int, point: LineChartData.Point, touchEvent: Offset) -> Unit)? = null,
) {
    check(horizontalOffset in 0f..0.25f) {
        "Horizontal offset is the % offset from sides, " +
                "and should be between 0%-25%"
    }

    val transitionAnimation = remember(lineChartData.points) { Animatable(initialValue = 0f) }

    LaunchedEffect(lineChartData.points) {
        transitionAnimation.snapTo(0f)
        transitionAnimation.animateTo(1f, animationSpec = animation)
    }

    val touchEvent = remember { mutableStateOf<Offset?>(null) }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .onTouch(
                onTouch = { offset -> touchEvent.value = offset },
                onRelease = {
                    touchEvent.value = null
                    onRelease?.invoke()
                }
            )

    ) {

        val yAxisDrawableArea = calculateYAxisDrawableArea(
            xAxisLabelSize = xAxisDrawer.requiredHeight(this),
            size = size
        )
        val xAxisDrawableArea = calculateXAxisDrawableArea(
            yAxisWidth = yAxisDrawableArea.width,
            labelHeight = xAxisDrawer.requiredHeight(this),
            size = size
        )
        val xAxisLabelsDrawableArea = calculateXAxisLabelsDrawableArea(
            xAxisDrawableArea = xAxisDrawableArea,
            offset = horizontalOffset
        )
        val chartDrawableArea = calculateDrawableArea(
            xAxisDrawableArea = xAxisDrawableArea,
            yAxisDrawableArea = yAxisDrawableArea,
            size = size,
            offset = horizontalOffset
        )

        val selectedIndex = touchEvent.value?.let { touchEvent ->
            calculateSelectedIndex(
                lineChartData = lineChartData,
                chartDrawableArea = chartDrawableArea,
                touchEvent = touchEvent
            )?.also {
                if (index != -1) onSelection?.invoke(it, lineChartData.points[it], touchEvent)
            }
        }

        lineChartData.points.forEachIndexed { index, point ->
            withProgress(
                index = index,
                lineChartData = lineChartData,
                transitionProgress = transitionAnimation.value
            ) {
                val pointLocation = calculatePointLocation(
                    drawableArea = chartDrawableArea,
                    lineChartData = lineChartData,
                    point = point,
                    index = index,
                )
                pointDrawer.drawPoint(
                    drawScope = this,
                    center = pointLocation,
                    isDragging = selectedIndex != null,
                    isSelected = selectedIndex == index,
                )
                if (index == selectedIndex) {
                    lineDrawer.drawSelectedLine(
                        drawScope = this,
                        drawableArea = chartDrawableArea,
                        point = point,
                        pointLocation = pointLocation,
                    )
                }
            }
        }

        drawAxis(
            lineChartData = lineChartData,
            scope = this,
            xAxisDrawer = xAxisDrawer,
            xAxisDrawableArea = xAxisDrawableArea,
            yAxisDrawableArea = yAxisDrawableArea,
            xAxisLabelsDrawableArea = xAxisLabelsDrawableArea,
            yAxisDrawer = yAxisDrawer,
        )

        // Draw the chart line.
        lineShader.fillLine(
            drawScope = this,
            fillPath = calculateFillPath(
                drawableArea = chartDrawableArea,
                lineChartData = lineChartData,
                transitionProgress = transitionAnimation.value
            ),
            isDragging = selectedIndex != null,
        )

        lineDrawer.drawLine(
            drawScope = this,
            linePath = calculateLinePath(
                drawableArea = chartDrawableArea,
                lineChartData = lineChartData,
                transitionProgress = transitionAnimation.value
            ),
            isDragging = selectedIndex != null,
        )
    }
}

private fun drawAxis(
    lineChartData: LineChartData,
    scope: DrawScope,
    xAxisDrawer: XAxisDrawer,
    xAxisDrawableArea: Rect,
    yAxisDrawableArea: Rect,
    xAxisLabelsDrawableArea: Rect,
    yAxisDrawer: YAxisDrawer,
) {

    // Draw the X Axis line.
    xAxisDrawer.drawAxisLine(
        drawScope = scope,
        drawableArea = xAxisDrawableArea,
    )

    xAxisDrawer.drawAxisLabels(
        drawScope = scope,
        drawableArea = xAxisLabelsDrawableArea,
        labels = lineChartData.points.map { it.label }
    )

    // Draw the Y Axis line.
    yAxisDrawer.drawAxisLine(
        drawScope = scope,
        drawableArea = yAxisDrawableArea
    )

    yAxisDrawer.drawAxisLabels(
        drawScope = scope,
        drawableArea = yAxisDrawableArea,
        minValue = lineChartData.minYValue,
        maxValue = lineChartData.maxYValue
    )
}
