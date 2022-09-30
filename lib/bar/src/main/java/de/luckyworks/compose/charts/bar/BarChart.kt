package de.luckyworks.compose.charts.bar

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import de.luckyworks.compose.charts.bar.BarChartUtils.axisAreas
import de.luckyworks.compose.charts.bar.BarChartUtils.barDrawableArea
import de.luckyworks.compose.charts.bar.BarChartUtils.calculateSelectedIndex
import de.luckyworks.compose.charts.bar.BarChartUtils.forEachWithArea
import de.luckyworks.compose.charts.bar.renderer.bar.BarDrawer
import de.luckyworks.compose.charts.bar.renderer.bar.SimpleBarDrawer
import de.luckyworks.compose.charts.bar.renderer.label.LabelDrawer
import de.luckyworks.compose.charts.bar.renderer.label.SimpleValueDrawer
import de.luckyworks.compose.charts.bar.renderer.xaxis.SimpleXAxisDrawer
import de.luckyworks.compose.charts.bar.renderer.xaxis.XAxisDrawer
import de.luckyworks.compose.charts.bar.renderer.yaxis.SimpleYAxisDrawer
import de.luckyworks.compose.charts.bar.renderer.yaxis.YAxisDrawer
import de.luckyworks.compose.charts.piechart.animation.simpleChartAnimation
import de.luckyworks.compose.charts.piechart.utils.onTouch

@Composable
fun BarChart(
    barChartData: BarChartData,
    modifier: Modifier = Modifier,
    animation: AnimationSpec<Float> = simpleChartAnimation(),
    barDrawer: BarDrawer = SimpleBarDrawer(),
    xAxisDrawer: XAxisDrawer = SimpleXAxisDrawer(),
    yAxisDrawer: YAxisDrawer = SimpleYAxisDrawer(),
    labelDrawer: LabelDrawer = SimpleValueDrawer(),
    barSpacePercent: Float = 0.2f,
    onRelease: (() -> Unit)? = null,
    onSelection: ((index: Int, bar: BarChartData.Bar, touchEvent: Offset) -> Unit)? = null,
) {
    val transitionAnimation = remember(barChartData.bars) { Animatable(initialValue = 0f) }

    LaunchedEffect(barChartData.bars) {
        transitionAnimation.animateTo(1f, animationSpec = animation)
    }

    val touchEvent = remember { mutableStateOf<Offset?>(null) }

    val progress = transitionAnimation.value

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .onTouch(onTouch = { offset -> touchEvent.value = offset }, onRelease = {
                touchEvent.value = null
                onRelease?.invoke()
            })
            .drawBehind {
                drawIntoCanvas { canvas ->
                    val (xAxisArea, yAxisArea) = axisAreas(
                        drawScope = this,
                        totalSize = size,
                        xAxisDrawer = xAxisDrawer,
                        labelDrawer = labelDrawer
                    )
                    val barDrawableArea = barDrawableArea(xAxisArea)

                    val selectedIndex = touchEvent.value?.let { touchEvent ->
                        calculateSelectedIndex(
                            barChartData = barChartData,
                            barDrawableArea = barDrawableArea,
                            touchEvent = touchEvent
                        )?.also {
                            if (index != -1) onSelection?.invoke(
                                it,
                                barChartData.bars[it],
                                touchEvent
                            )
                        }
                    }
                    // Draw yAxis line.
                    yAxisDrawer.drawAxisLine(
                        drawScope = this,
                        canvas = canvas,
                        drawableArea = yAxisArea,
                        isDragging = selectedIndex != null,
                    )

                    // Draw xAxis line.
                    xAxisDrawer.drawAxisLine(
                        drawScope = this,
                        canvas = canvas,
                        drawableArea = xAxisArea,
                        isDragging = selectedIndex != null,
                    )
                    // Draw each bar.
                    barChartData.forEachWithArea(
                        drawScope = this,
                        barDrawableArea = barDrawableArea,
                        progress = progress,
                        labelDrawer = labelDrawer,
                        barSpacePercent = barSpacePercent,
                    ) { index, barArea, bar ->
                        barDrawer.drawBar(
                            drawScope = this,
                            canvas = canvas,
                            barArea = barArea,
                            bar = bar,
                            isSelected = index == selectedIndex,
                            isDragging = selectedIndex != null,
                        )
                    }
                }
            }
    ) {
        /**
         *  Typically we could draw everything here, but because of the lack of canvas.drawText
         *  APIs we have to use Android's `nativeCanvas` which seems to be drawn behind
         *  Compose's canvas.
         */
        drawIntoCanvas { canvas ->
            val (xAxisArea, yAxisArea) = axisAreas(
                drawScope = this,
                totalSize = size,
                xAxisDrawer = xAxisDrawer,
                labelDrawer = labelDrawer
            )
            val barDrawableArea = barDrawableArea(xAxisArea)
            val selectedIndex = touchEvent.value?.let { touchEvent ->
                calculateSelectedIndex(
                    barChartData = barChartData,
                    barDrawableArea = barDrawableArea,
                    touchEvent = touchEvent
                )?.also {
                    onSelection?.invoke(it, barChartData.bars[it], touchEvent)
                }
            }
            barChartData.forEachWithArea(
                drawScope = this,
                barDrawableArea = barDrawableArea,
                progress = progress,
                labelDrawer = labelDrawer,
                barSpacePercent = barSpacePercent,
            ) { index, barArea, bar ->
                labelDrawer.drawLabel(
                    drawScope = this,
                    canvas = canvas,
                    label = bar.label,
                    barArea = barArea,
                    xAxisArea = xAxisArea,
                    isSelected = index == selectedIndex,
                    isDragging = selectedIndex != null,
                )
            }

            yAxisDrawer.drawAxisLabels(
                drawScope = this,
                canvas = canvas,
                minValue = barChartData.minYValue,
                maxValue = barChartData.maxYValue,
                drawableArea = yAxisArea,
                isDragging = selectedIndex != null,
            )
        }
    }
}
