package de.luckyworks.compose.charts.sample.ui.line

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import de.luckyworks.compose.charts.line.LineChartData
import de.luckyworks.compose.charts.line.LineChartData.Point
import de.luckyworks.compose.charts.line.renderer.point.FilledCircularPointDrawer
import de.luckyworks.compose.charts.line.renderer.point.HollowCircularPointDrawer
import de.luckyworks.compose.charts.line.renderer.point.NoPointDrawer
import de.luckyworks.compose.charts.line.renderer.point.PointDrawer
import de.luckyworks.compose.charts.sample.ui.line.LineChartDataModel.PointDrawerType.Filled
import de.luckyworks.compose.charts.sample.ui.line.LineChartDataModel.PointDrawerType.Hollow
import de.luckyworks.compose.charts.sample.ui.line.LineChartDataModel.PointDrawerType.None

class LineChartDataModel {
    var lineChartData by mutableStateOf(
        LineChartData(
            points = listOf(
                Point(randomYValue(), "Label1"),
                Point(randomYValue(), "Label2"),
                Point(randomYValue(), "Label3"),
                Point(randomYValue(), "Label4"),
                Point(randomYValue(), "Label5"),
                Point(randomYValue(), "Label6"),
                Point(randomYValue(), "Label7")
            )
        )
    )
    var horizontalOffset by mutableStateOf(5f)
    var pointDrawerType by mutableStateOf(Filled)
    val pointDrawer: PointDrawer
        get() {
            return when (pointDrawerType) {
                None -> NoPointDrawer
                Filled -> FilledCircularPointDrawer()
                Hollow -> HollowCircularPointDrawer()
            }
        }

    private fun randomYValue(): Float = (100f * Math.random()).toFloat() + 45f

    enum class PointDrawerType {
        None,
        Filled,
        Hollow
    }
}