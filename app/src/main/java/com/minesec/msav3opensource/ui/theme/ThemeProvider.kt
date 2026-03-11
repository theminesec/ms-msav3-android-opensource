package com.minesec.msav3opensource.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable

@Immutable
open class ThemeProvider {

    @ReadOnlyComposable
    open fun provideMsaColors(darkTheme: Boolean = false): MsaColors = if (darkTheme) MsaColorsDark() else MsaColorsLight()

    @ReadOnlyComposable
    fun provideMaterialColors(darkTheme: Boolean): ColorScheme {
        return if (darkTheme) {
            darkColorScheme(
                primary = provideMsaColors(true).primary,
                onPrimary = provideMsaColors(true).primaryForeground,
                secondary = provideMsaColors(true).secondary,
                onSecondary = provideMsaColors(true).secondaryForeground,
                surface = provideMsaColors(true).background,
                onSurface = provideMsaColors(true).foreground,
                background = provideMsaColors(true).background,
                onBackground = provideMsaColors(true).foreground,
                error = provideMsaColors(true).error,
                onError = provideMsaColors(true).errorForeground,
            )
        } else {
            lightColorScheme(
                primary = provideMsaColors().primary,
                onPrimary = provideMsaColors().primaryForeground,
                secondary = provideMsaColors().secondary,
                onSecondary = provideMsaColors().secondaryForeground,
                background = provideMsaColors(true).background,
                onBackground = provideMsaColors(true).foreground,
                surface = provideMsaColors().background,
                onSurface = provideMsaColors().foreground,
                error = provideMsaColors().error,
                onError = provideMsaColors().errorForeground,
            )
        }
    }

    @ReadOnlyComposable
    open fun provideSchemeColors(): SchemeColors = SchemeColors()

    @Composable
    open fun provideSpacing(): Spacing = adaptiveSpacing()

    @Composable
    open fun provideAnimationSize() = adaptiveAnimationSize()

    @Composable
    open fun provideMinTouchSize() = adaptiveMinTouchSize()

    @Composable
    open fun provideIconSize() = adaptiveIconSize()

    @Composable
    open fun provideRadius() = adaptiveRadius()

    @Composable
    open fun provideShapes() = adaptiveShapes()

    @Composable
    open fun provideTypography() = adaptiveTypography()
}