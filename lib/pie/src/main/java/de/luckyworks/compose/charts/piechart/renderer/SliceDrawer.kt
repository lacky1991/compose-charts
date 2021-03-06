package de.luckyworks.compose.charts.piechart.renderer

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.drawscope.DrawScope
import de.luckyworks.compose.charts.piechart.PieChartData.Slice

interface SliceDrawer {
  fun drawSlice(
      drawScope: DrawScope,
      canvas: Canvas,
      area: Size,
      startAngle: Float,
      sweepAngle: Float,
      slice: Slice,
      isSelected: Boolean,
      isDragging: Boolean
  )
}
