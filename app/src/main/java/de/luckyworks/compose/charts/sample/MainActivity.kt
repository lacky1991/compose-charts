package de.luckyworks.compose.charts.sample

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import de.luckyworks.compose.charts.sample.ui.ChartsApp

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      ChartsApp()
    }
  }
}
