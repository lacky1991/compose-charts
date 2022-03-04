package de.luckyworks.compose.charts.piechart

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.tooling.preview.Preview
import de.luckyworks.compose.charts.piechart.PieChartUtils.calculateAngle
import de.luckyworks.compose.charts.piechart.PieChartUtils.calculateSelectedIndex
import de.luckyworks.compose.charts.piechart.animation.simpleChartAnimation
import de.luckyworks.compose.charts.piechart.renderer.SimpleSliceDrawer
import de.luckyworks.compose.charts.piechart.renderer.SliceDrawer
import de.luckyworks.compose.charts.piechart.utils.onTouch

@Composable
fun PieChart(
    pieChartData: PieChartData,
    modifier: Modifier = Modifier,
    animation: AnimationSpec<Float> = simpleChartAnimation(),
    sliceDrawer: SliceDrawer = SimpleSliceDrawer(),
    startAngel: Float = 0f,
    keepSelection: Boolean = false,
    startSelection: Int = -1,
    onRelease: (() -> Unit)? = null,
    onSelection: ((index: Int, PieChartData.Slice) -> Unit)? = null,
) {
    val transitionProgress = remember(pieChartData.slices) { Animatable(initialValue = 0f) }

    // When slices value changes we want to re-animated the chart.
    LaunchedEffect(pieChartData.slices) {
        transitionProgress.animateTo(1f, animationSpec = animation)
    }

    DrawChart(
        pieChartData = pieChartData,
        modifier = modifier.fillMaxSize(),
        progress = transitionProgress.value,
        sliceDrawer = sliceDrawer,
        startAngel = startAngel,
        keepSelection = keepSelection,
        startSelection = startSelection,
        onRelease = onRelease,
        onSelection = onSelection,
    )
}

@Composable
private fun DrawChart(
    pieChartData: PieChartData,
    modifier: Modifier,
    progress: Float,
    sliceDrawer: SliceDrawer,
    startAngel: Float = 0f,
    keepSelection: Boolean = false,
    startSelection: Int = -1,
    onRelease: (() -> Unit)? = null,
    onSelection: ((index: Int, PieChartData.Slice) -> Unit)? = null
) {
    val slices = pieChartData.slices

    val touchEvent = remember { mutableStateOf<Offset?>(null) }
    val selectedIndex = remember { mutableStateOf(startSelection) }

    Canvas(modifier = modifier
        .onTouch(
            onTouch = { offset -> touchEvent.value = offset },
            onRelease = {
                if (keepSelection) touchEvent.value = null
                onRelease?.invoke()
            }
        )
    ) {
        drawIntoCanvas {
            var startArc = startAngel
            touchEvent.value?.let { touchEvent ->
                calculateSelectedIndex(
                    pieChartData = pieChartData,
                    touchEvent = touchEvent,
                    startAngel = startAngel,
                    size = size,
                    progress = progress,
                )
            }?.also { index ->
                onSelection?.invoke(index, pieChartData.slices[index])
                selectedIndex.value = index
            }

            slices.forEachIndexed { index, slice ->
                val arc = calculateAngle(
                    sliceLength = slice.value,
                    totalLength = pieChartData.totalSize,
                    progress = progress
                )

                sliceDrawer.drawSlice(
                    drawScope = this,
                    canvas = drawContext.canvas,
                    area = size,
                    startAngle = startArc,
                    sweepAngle = arc,
                    slice = slice,
                    isSelected = index == selectedIndex.value,
                    isDragging = selectedIndex.value != -1
                )
                startArc += arc
            }
        }
    }
}

@Preview
@Composable
fun PieChartPreview() = PieChart(
    pieChartData = PieChartData(
        slices = listOf(
            PieChartData.Slice(25f, Color.Red),
            PieChartData.Slice(42f, Color.Blue),
            PieChartData.Slice(23f, Color.Green)
        )
    )
)