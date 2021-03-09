package com.mustachenko.neumorphic

import androidx.compose.animation.animatedFloat
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.gesture.tapGestureFilter
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.unit.*
import com.mustachenko.neumorphic.model.Radius
import com.mustachenko.neumorphic.model.ShadowColor
import com.mustachenko.neumorphic.model.TapType
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.mustachenko.neumorphic.model.TapState

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
    val alphaInitialValue = if (requestInnerShadow) 1f else 0f
    val alphaAnimatedValue = animatedFloat(initVal = alphaInitialValue)
    var clicked by remember { mutableStateOf(TapState.UNDEFINED) }

    return@composed tapGestureFilter {
        if (!alphaAnimatedValue.isRunning && tapType != TapType.NONE) {
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
    }.drawBehind {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val targetValue = if (clicked == TapState.DEFAULT) 0f else 1f
        if (!alphaAnimatedValue.isRunning
            && clicked != TapState.UNDEFINED
            && tapType != TapType.NONE
            && alphaAnimatedValue.value != targetValue
        ) {
            val anim = tween<Float>(durationMillis = 200, easing = LinearOutSlowInEasing)

            alphaAnimatedValue.animateTo(
                targetValue = targetValue,
                anim = anim
            ) { _, _ ->
                alphaAnimatedValue.snapTo(targetValue)
                if (tapType == TapType.CLICK) {
                    clicked = TapState.DEFAULT
                    alphaAnimatedValue.animateTo(0f, anim = anim)
                }
            }
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