package de.luckyworks.compose.charts.line

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

private val testLineChartData = (1 until 10).map {
    LineChartData.Point(
        value = when (it) {
            1 -> 60.0f
            2 -> 90.0f
            3 -> 40.0f
            4 -> -40.0f
            else -> 0.0f
        },
        label = "$it",
    )
}

@Preview
@Composable
private fun PreviewLineChart() {
    LineChart(
        modifier = Modifier.height(100.dp),
        lineChartData = LineChartData(testLineChartData)
    )
}