package de.luckyworks.compose.charts.sample.ui

import androidx.compose.animation.Crossfade
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import com.github.tehras.charts.theme.ChartsTheme
import de.luckyworks.compose.charts.sample.ui.line.LineChartScreen
import de.luckyworks.compose.charts.sample.ui.pie.PieChartScreen
import de.luckyworks.compose.charts.sample.ui.selector.SelectChartScreen
import de.luckyworks.compose.charts.sample.ui.bar.BarChartScreen

@Composable
fun ChartsApp() {
  ChartsTheme {
    AppContent()
  }
}

@Composable
fun AppContent() {
  Crossfade(ChartScreenStatus.currentChart) { screen ->
    Surface(color = MaterialTheme.colors.background) {
      when (screen) {
        ChartScreen.SelectChart -> SelectChartScreen()
        ChartScreen.Pie -> PieChartScreen()
        ChartScreen.Bar -> BarChartScreen()
        ChartScreen.Line -> LineChartScreen()
      }
    }
  }
}
