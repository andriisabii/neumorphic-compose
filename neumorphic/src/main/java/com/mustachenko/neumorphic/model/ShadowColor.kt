package com.mustachenko.neumorphic.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class ShadowColor(
    val startShadowColor: Color = Color.White,
    val endShadowColor: Color = Color.Black
)

data class Radius(
    val topLeft: Dp = 0.dp,
    val bottomLeft: Dp = 0.dp,
    val topRight: Dp = 0.dp,
    val bottomRight: Dp = 0.dp,
)