package de.luckyworks.compose.charts.sample.ui.line

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.tehras.charts.theme.Margins.horizontal
import com.github.tehras.charts.theme.Margins.vertical
import com.github.tehras.charts.theme.Margins.verticalLarge
import de.luckyworks.compose.charts.line.LineChart
import de.luckyworks.compose.charts.line.LineChartData
import de.luckyworks.compose.charts.line.renderer.line.SolidLineDrawer
import de.luckyworks.compose.charts.line.renderer.line.SolidLineShader
import de.luckyworks.compose.charts.line.renderer.xaxis.NoLabelXAxisDrawer
import de.luckyworks.compose.charts.line.renderer.yaxis.InsideSmallYAxisDrawer
import de.luckyworks.compose.charts.sample.ui.ChartScreenStatus
import de.luckyworks.compose.charts.sample.ui.line.LineChartDataModel.PointDrawerType

@Composable
fun LineChartScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { ChartScreenStatus.navigateHome() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Go back to home")
                    }
                },
                title = { Text(text = "Line Chart") }
            )
        },
    ) { LineChartScreenContent() }
}

@Composable
fun LineChartScreenContent() {
    val lineChartDataModel = remember { LineChartDataModel() }

    Column(
        modifier = Modifier.padding(
            horizontal = horizontal,
            vertical = vertical
        )
    ) {
        val selectedLabel = remember { mutableStateOf<LineChartData.Point?>(null) }

        Text(
            text = selectedLabel.value?.label ?: "",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .padding(bottom = 16.dp)
        )

        LineChartRow(lineChartDataModel, selectedLabel)
        HorizontalOffsetSelector(lineChartDataModel)
        OffsetProgress(lineChartDataModel)
    }
}

@Composable
fun HorizontalOffsetSelector(lineChartDataModel: LineChartDataModel) {
    val selectedType = lineChartDataModel.pointDrawerType

    Row(
        modifier = Modifier.padding(
            horizontal = horizontal,
            vertical = verticalLarge
        ),
        verticalAlignment = CenterVertically
    ) {
        Text("Point Drawer")

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontal, vertical = vertical)
                .align(CenterVertically)
        ) {
            PointDrawerType.values().forEach { type ->
                OutlinedButton(
                    border = ButtonDefaults.outlinedBorder.takeIf { selectedType == type },
                    onClick = { lineChartDataModel.pointDrawerType = type }
                ) {
                    Text(type.name)
                }
            }
        }
    }
}

@Composable
fun OffsetProgress(lineChartDataModel: LineChartDataModel) {
    Row(
        modifier = Modifier.padding(horizontal = horizontal),
        verticalAlignment = CenterVertically
    ) {
        Text("Offset")

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .align(CenterVertically)
        ) {
            Slider(
                value = lineChartDataModel.horizontalOffset,
                onValueChange = { lineChartDataModel.horizontalOffset = it },
                valueRange = 0f.rangeTo(0.25f)
            )
        }
    }
}

@Composable
fun LineChartRow(
    lineChartDataModel: LineChartDataModel,
    selectedLabel: MutableState<LineChartData.Point?>
) {
    Box(
        modifier = Modifier
            .height(250.dp)
            .fillMaxWidth()
    ) {
        LineChart(
            lineChartData = lineChartDataModel.lineChartData,
            pointDrawer = lineChartDataModel.pointDrawer,
            lineShader = SolidLineShader(color = Color(0x20EEEEEE)),
            lineDrawer = SolidLineDrawer(color = MaterialTheme.colors.primary),
            xAxisDrawer = NoLabelXAxisDrawer(axisLineColor = Color(0xFF4E4D4D)),
            yAxisDrawer = InsideSmallYAxisDrawer(
                labelTextColor = Color(0xFF4E4D4D),
                axisLineColor = Color(0xFF4E4D4D),
            ),
            horizontalOffset = lineChartDataModel.horizontalOffset,
            onRelease = { selectedLabel.value = null }
        ) { index, point, offset ->
            selectedLabel.value = point
        }
    }
}

@Preview
@Composable
fun LineChartPreview() = LineChartScreen()