package de.luckyworks.compose.charts.sample.ui.pie

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.luckyworks.compose.charts.piechart.PieChart
import de.luckyworks.compose.charts.piechart.PieChartData
import de.luckyworks.compose.charts.piechart.renderer.SimpleSliceDrawer
import de.luckyworks.compose.charts.sample.theme.Margins
import de.luckyworks.compose.charts.sample.ui.ChartScreenStatus

@Composable
fun PieChartScreen() {
    Scaffold(
        topBar = {
            TopAppBar(navigationIcon = {
                IconButton(onClick = { ChartScreenStatus.navigateHome() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Go back to home")
                }
            }, title = { Text(text = "Pie Chart") })
        },
    ) { PieChartScreenContent() }
}

@Composable
private fun PieChartScreenContent() {
    val pieChartDataModel = remember { PieChartDataModel() }

    Column(
        modifier = Modifier.padding(
            horizontal = Margins.horizontal, vertical = Margins.vertical
        )
    ) {
        PieChartRow(pieChartDataModel)
        SliceThicknessRow(pieChartDataModel.sliceThickness) {
            pieChartDataModel.sliceThickness = it
        }
        AddOrRemoveSliceRow(pieChartDataModel)
    }
}

@Composable
private fun PieChartRow(pieChartDataModel: PieChartDataModel) {
    Column {

        val selectedLabel = remember { mutableStateOf<PieChartData.Slice?>(null) }

        Text(
            text = selectedLabel.value?.value?.toString() ?: "",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .padding(bottom = 16.dp)
        )
        val selectedIndex = remember { mutableStateOf(-1) }
        val touchEvent = remember { mutableStateOf<Offset?>(null) }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(vertical = Margins.vertical)
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clickable {
                    selectedLabel.value = null
                    selectedIndex.value = -1
                    touchEvent.value = null
                })
            PieChart(
                modifier = Modifier
                    .size(200.dp)
                    .align(Center),
                pieChartData = pieChartDataModel.pieChartData,
                sliceDrawer = SimpleSliceDrawer(
                    sliceThickness = pieChartDataModel.sliceThickness.dp,
                    selectedSliceThickness = pieChartDataModel.sliceThickness.dp + 5.dp
                ),
                selectedIndex = selectedIndex.value,
                onSelection = { index, slice ->
                    selectedLabel.value = slice
                    selectedIndex.value = index
                },
                touchEvent = touchEvent
            )
        }
    }
}

@Composable
private fun SliceThicknessRow(sliceThickness: Float, onValueUpdated: (Float) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Margins.verticalLarge),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Slice thickness",
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(end = Margins.horizontal)
        )
        Slider(
            value = sliceThickness,
            onValueChange = { onValueUpdated(it) },
            valueRange = 1f.rangeTo(20f)
        )
    }
}

@Composable
private fun AddOrRemoveSliceRow(pieChartDataModel: PieChartDataModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Margins.vertical),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            enabled = pieChartDataModel.slices.size > 3,
            onClick = { pieChartDataModel.removeSlice() },
            shape = CircleShape
        ) {
            Icon(Icons.Filled.Remove, contentDescription = "Remove slice from pie chart")
        }
        Row(
            modifier = Modifier.padding(horizontal = Margins.horizontal),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Slices: ")
            Text(
                text = pieChartDataModel.slices.count().toString(), style = TextStyle(
                    fontWeight = FontWeight.ExtraBold, fontSize = 18.sp
                )
            )
        }
        Button(
            enabled = pieChartDataModel.slices.size < 9,
            onClick = { pieChartDataModel.addSlice() },
            shape = CircleShape
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add slice to pie chart")
        }
    }
}

@Preview
@Composable
private fun PieChartScreenPreview() = PieChartScreen()
