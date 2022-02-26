package de.luckyworks.compose.charts.piechart.utils

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput

fun Modifier.onTouch(
    onTouch: (PointerInputScope.(offset: Offset) -> Unit),
    onRelease: () -> Unit,
) = pointerInput(Unit) {
    detectTapGestures(onPress = { offset ->
        onTouch(this@pointerInput, offset)
        if (tryAwaitRelease()) onRelease()
    })
}
    .pointerInput(Unit) {
        detectDragGestures(
            onDragCancel = { onRelease() },
            onDragEnd = { onRelease() },
            onDrag = { change, _ ->
                onTouch(
                    this,
                    change.position
                )
            })
    }