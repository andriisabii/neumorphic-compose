package com.mustachenko.neumorphic

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.Brush.Companion.horizontalGradient
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import com.mustachenko.neumorphic.model.Radius


fun DrawScope.drawOuterShadow(
    width: Float,
    height: Float,
    elevation: Float,
    containerBackgroundColor: Color,
    shadowRadius: Radius,
    topShadowColor: Color,
    bottomShadowColor: Color
) =
    drawIntoCanvas {
        val bounds = Rect(0f, 0f, width, height)

        val roundRect = RoundRect(
            bounds,
            CornerRadius(shadowRadius.topLeft.toPx(), shadowRadius.topLeft.toPx()),
            CornerRadius(shadowRadius.topRight.toPx(), shadowRadius.topRight.toPx()),
            CornerRadius(shadowRadius.bottomRight.toPx(), shadowRadius.bottomRight.toPx()),
            CornerRadius(shadowRadius.bottomLeft.toPx(), shadowRadius.bottomLeft.toPx())
        )
        val path = Path()
        path.addRoundRect(
            roundRect
        )
        val topPaint = Paint().apply {
            isAntiAlias = true
            color = containerBackgroundColor

        }
        topPaint.asFrameworkPaint().apply {
            setShadowLayer(elevation, -elevation, -elevation, topShadowColor.toArgb())
        }

        val bottomPaint = Paint().apply {
            isAntiAlias = true
            color = containerBackgroundColor
        }
        bottomPaint.asFrameworkPaint().apply {
            setShadowLayer(elevation, elevation, elevation, bottomShadowColor.toArgb())
        }
        it.drawPath(path, topPaint)
        it.drawPath(path, bottomPaint)
    }

fun DrawScope.drawInnerShadow(
    width: Float,
    height: Float,
    shadowRadius: Radius,
    innerShadowSize: Float,
    topShadowColor: Color,
    bottomShadowColor: Color,
    alpha: Float
) = drawIntoCanvas {
    val bounds = Rect(0f, 0f, width, height)

    val roundRect = RoundRect(
        bounds,
        CornerRadius(shadowRadius.topLeft.toPx(), shadowRadius.topLeft.toPx()),
        CornerRadius(shadowRadius.topRight.toPx(), shadowRadius.topRight.toPx()),
        CornerRadius(shadowRadius.bottomRight.toPx(), shadowRadius.bottomRight.toPx()),
        CornerRadius(shadowRadius.bottomLeft.toPx(), shadowRadius.bottomLeft.toPx())
    )

    val clip = Path()
    clip.addRoundRect(roundRect)
    it.clipPath(clip, ClipOp.Intersect)
    val top =
        verticalGradient(
            listOf(topShadowColor, Color.Transparent),
            0f,
            innerShadowSize,
            TileMode.Clamp
        )

    val left = horizontalGradient(
        listOf(topShadowColor, Color.Transparent),
        0f,
        innerShadowSize,
        TileMode.Clamp
    )

    val bottom = verticalGradient(
        listOf(bottomShadowColor, Color.Transparent),
        height,
        height - innerShadowSize,
        TileMode.Clamp
    )
    val right = horizontalGradient(
        listOf(bottomShadowColor, Color.Transparent),
        width,
        width - innerShadowSize,
        TileMode.Clamp
    )

    drawRect(top, size = Size(bounds.width, bounds.height), alpha = alpha)
    drawRect(left, size = Size(bounds.width, bounds.height), alpha = alpha)
    drawRect(bottom, size = Size(bounds.width, bounds.height), alpha = alpha)
    drawRect(right, size = Size(bounds.width, bounds.height), alpha = alpha)
}
