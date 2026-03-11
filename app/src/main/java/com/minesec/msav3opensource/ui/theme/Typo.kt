package com.minesec.msav3opensource.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.minesec.msav3opensource.R

private const val BaseFontFeature = "pnum" // Example: proportional numbers

fun Double.emToSp(): androidx.compose.ui.unit.TextUnit = (this * 16).sp

fun adaptiveTypography(): Typography {
    val inter = FontFamily(
        Font(R.font.inter_regular, FontWeight.Normal),
        Font(R.font.inter_medium, FontWeight.Medium),
        Font(R.font.inter_semibold, FontWeight.SemiBold),
        Font(R.font.inter_bold, FontWeight.Bold),
        Font(R.font.inter_extrabold, FontWeight.ExtraBold),
    )

    return Typography(
        labelSmall = TextStyle(
            fontFamily = inter,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.sp,
            fontFeatureSettings = BaseFontFeature,
        ),
        labelMedium = TextStyle(
            fontFamily = inter,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.sp,
            fontFeatureSettings = BaseFontFeature,
        ),
        labelLarge = TextStyle(
            fontFamily = inter,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.sp,
            fontFeatureSettings = BaseFontFeature,
        ),
        bodySmall = TextStyle(
            fontFamily = inter,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.sp,
            fontFeatureSettings = BaseFontFeature,
        ),
        bodyMedium = TextStyle(
            fontFamily = inter,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.sp,
            fontFeatureSettings = BaseFontFeature,
        ),
        bodyLarge = TextStyle(
            fontFamily = inter,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.sp,
            fontFeatureSettings = BaseFontFeature,
        ),
        titleSmall = TextStyle(
            fontFamily = inter,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.sp,
            fontFeatureSettings = BaseFontFeature,
        ),
        titleMedium = TextStyle(
            fontFamily = inter,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.sp,
            fontFeatureSettings = BaseFontFeature,
        ),
        titleLarge = TextStyle(
            fontFamily = inter,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.sp,
            fontFeatureSettings = BaseFontFeature,
        ),
        headlineSmall = TextStyle(
            fontFamily = inter,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            lineHeight = 36.sp,
            letterSpacing = 0.sp,
            fontFeatureSettings = BaseFontFeature,
        ),
    )
}
