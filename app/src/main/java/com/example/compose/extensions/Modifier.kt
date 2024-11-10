package com.example.compose.extensions

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput

fun Modifier.onTouchPositionChanged(onTouchPositionChanged: (Offset?) -> Unit): Modifier {
    var touchPosition: Offset? = null
    return this
        .pointerInput(Unit) {
            detectDragGestures(
                onDrag = { _, offset ->
                    touchPosition = touchPosition?.let { Offset((it.x + offset.x), (it.y + offset.y)) }
                    onTouchPositionChanged(touchPosition)
                },
                onDragEnd = {
                    touchPosition = null
                    onTouchPositionChanged(null)
                }
            )
        }
        .pointerInput(Unit) {
            detectTapGestures(
                onPress = {
                    touchPosition = Offset(it.x, it.y)
                    onTouchPositionChanged(touchPosition)
                },
                onTap = {
                    touchPosition = null
                    onTouchPositionChanged(null)
                },
            )
        }
}
