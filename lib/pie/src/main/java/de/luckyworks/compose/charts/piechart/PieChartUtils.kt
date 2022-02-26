package de.luckyworks.compose.charts.piechart

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import kotlin.math.atan2

internal object PieChartUtils {
    fun calculateAngle(
        sliceLength: Float,
        totalLength: Float,
        progress: Float
    ): Float {
        return 360.0f * (sliceLength * progress) / totalLength
    }

    fun calculateSelectedIndex(
        touchEvent: Offset,
        pieChartData: PieChartData,
        size: Size,
        progress: Float,
        startAngel: Float,
    ): Int {
        var angelSum = 0f
        return pieChartData.slices.withIndex().indexOfFirst { indexedValue ->
            val angel = calculateAngle(
                sliceLength = indexedValue.value.value,
                totalLength = pieChartData.totalSize,
                progress = progress
            )
            angelSum += angel
            val angelTouchEvent = calculateAngel(touchEvent, size)

            (angelTouchEvent - startAngel + 360.0) % 360.0 < angelSum
        }
    }

    private fun calculateAngel(
        point: Offset,
        size: Size
    ): Double {
        val centerX = size.width / 2
        val centerY = size.height / 2
        return (Math.toDegrees(
            atan2(
                point.y - centerY,
                point.x - centerX
            ).toDouble()
        ) + 360.0) % 360.0
    }
}