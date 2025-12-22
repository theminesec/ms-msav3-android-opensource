package com.minesec.msav3opensource.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    themeProvider: ThemeProvider = ThemeProvider(),
    content: @Composable () -> Unit
) {
    val materialColors = themeProvider.provideMaterialColors(darkTheme)

    CompositionLocalProvider(
        LocalMsaColors provides themeProvider.provideMsaColors(darkTheme),
        LocalSchemeColors provides themeProvider.provideSchemeColors()
    ) {
        MaterialTheme(
            colorScheme = materialColors,
            typography = themeProvider.provideTypography(),
            shapes = themeProvider.provideShapes(),
            content = content
        )
    }
}

object MsaTheme {
    val colors: MsaColors
        @Composable
        @ReadOnlyComposable
        get() = LocalMsaColors.current

    val schemeColors: SchemeColors
        @Composable
        @ReadOnlyComposable
        get() = LocalSchemeColors.current

    val spacing: Spacing
        @Composable
        get() = adaptiveSpacing()

    val minTouchSize: MinTouchSize
        @Composable
        get() = adaptiveMinTouchSize()

    val animationSize: AnimationSize
        @Composable
        get() = adaptiveAnimationSize()

    val iconSize: IconSize
        @Composable
        get() = adaptiveIconSize()

    val radius: Radius
        @Composable
        get() = adaptiveRadius()

    val typography: Typography
        @Composable
        get() = adaptiveTypography()

    val shapes: Shapes
        @Composable
        get() = adaptiveShapes()
}
