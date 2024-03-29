package de.luckyworks.compose.charts.piechart

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import de.luckyworks.compose.charts.piechart.PieChartUtils.calculateAngle
import de.luckyworks.compose.charts.piechart.PieChartUtils.calculateSelectedIndex
import de.luckyworks.compose.charts.piechart.animation.simpleChartAnimation
import de.luckyworks.compose.charts.piechart.renderer.SimpleSliceDrawer
import de.luckyworks.compose.charts.piechart.renderer.SliceDrawer

@Composable
fun PieChart(
    pieChartData: PieChartData,
    modifier: Modifier = Modifier,
    animation: AnimationSpec<Float> = simpleChartAnimation(),
    sliceDrawer: SliceDrawer = SimpleSliceDrawer(),
    startAngel: Float = 0f,
    selectedIndex: Int = -1,
    onSelection: ((index: Int, PieChartData.Slice) -> Unit)? = null,
    touchEvent: MutableState<Offset?> = remember { mutableStateOf(null) },
) {
    val transitionProgress = remember(pieChartData.slices) { Animatable(initialValue = 0f) }

    // When slices value changes we want to re-animated the chart.
    LaunchedEffect(pieChartData.slices) {
        transitionProgress.animateTo(1f, animationSpec = animation)
    }

    DrawChart(
        pieChartData = pieChartData,
        modifier = modifier,
        progress = transitionProgress.value,
        sliceDrawer = sliceDrawer,
        startAngel = startAngel,
        selectedIndex = selectedIndex,
        onSelection = onSelection,
        touchEvent = touchEvent,
    )
}

@Composable
private fun DrawChart(
    pieChartData: PieChartData,
    modifier: Modifier,
    progress: Float,
    sliceDrawer: SliceDrawer,
    startAngel: Float = 0f,
    selectedIndex: Int = -1,
    onSelection: ((index: Int, PieChartData.Slice) -> Unit)? = null,
    touchEvent: MutableState<Offset?> = remember { mutableStateOf(null) },
) {
    val slices = pieChartData.slices

    Canvas(modifier = modifier
        .pointerInput(Unit) {
            detectTapGestures(onPress = { offset ->
                touchEvent.value = offset
            })
        }
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
                if (index != -1) onSelection?.invoke(index, pieChartData.slices[index])
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
                    isSelected = index == selectedIndex,
                    isDragging = selectedIndex != -1
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
