package com.mustachenko.neumorphic

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mustachenko.neumorphic.model.Radius
import com.mustachenko.neumorphic.model.ShadowColor
import com.mustachenko.neumorphic.model.TapState
import com.mustachenko.neumorphic.model.TapType

fun Modifier.pressable(
    backgroundColor: Color = Color.Unspecified,
    shadowColor: ShadowColor = ShadowColor(),
    radius: Radius = Radius(),
    border: Dp = 0.dp,
    innerShadowSize: Dp = 0.dp,
    tapType: TapType = TapType.NONE,
    requestInnerShadow: Boolean = false,
    elevation: Dp = 0.dp
): Modifier = composed {

    val hasInnerShadow = tapType != TapType.NONE || requestInnerShadow
    var clicked by remember { mutableStateOf(if (hasInnerShadow && tapType == TapType.SELECT) TapState.SELECTED else TapState.UNDEFINED) }
    val alphaInitialValue = if (requestInnerShadow) 1f else 0f
    var alpha by remember { mutableStateOf(alphaInitialValue) }
    val anim = tween<Float>(durationMillis = 200, easing = LinearOutSlowInEasing)
    val alphaAnimatedValue = animateFloatAsState(alpha, animationSpec = anim) {
        if (tapType == TapType.CLICK) {
            clicked = TapState.DEFAULT
            alpha = 0f
        }
    }

    pointerInput("tap") {
        detectTapGestures {
            if ((alphaAnimatedValue.value == 0f || alphaAnimatedValue.value == 1f) && tapType != TapType.NONE) {
                val tapState = clicked
                clicked =
                    if (tapState == TapState.SELECTED || tapState == TapState.PRESSED) {
                        TapState.DEFAULT
                    } else if ((tapState == TapState.DEFAULT || tapState == TapState.UNDEFINED) && tapType == TapType.SELECT) {
                        TapState.SELECTED
                    } else {
                        TapState.PRESSED
                    }
            }
        }
    }.drawBehind {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val targetValue = if (clicked == TapState.DEFAULT) 0f else 1f
        val isRunning = (alphaAnimatedValue.value != 0f && alphaAnimatedValue.value != 1f)
        if (!isRunning
            && clicked != TapState.UNDEFINED
            && tapType != TapType.NONE
            && alphaAnimatedValue.value != targetValue
        ) {
            alpha = targetValue
        }

        drawOuterShadow(
            canvasWidth,
            canvasHeight,
            elevation.toPx(),
            backgroundColor,
            radius,
            shadowColor.startShadowColor,
            shadowColor.endShadowColor
        )
        if (hasInnerShadow)
            inset(border.toPx(), border.toPx(), border.toPx(), border.toPx()) {
                drawInnerShadow(
                    size.width,
                    size.height,
                    radius,
                    innerShadowSize.toPx(),
                    shadowColor.endShadowColor,
                    shadowColor.startShadowColor,
                    alphaAnimatedValue.value
                )
            }
    }
}