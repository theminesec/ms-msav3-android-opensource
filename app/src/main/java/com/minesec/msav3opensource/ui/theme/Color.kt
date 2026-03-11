package com.minesec.msav3opensource.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

interface MsaColors {
    val primary: Color
    val primaryForeground: Color
    val primaryGradient: Color
    val secondary: Color
    val secondaryForeground: Color
    val background: Color
    val foreground: Color
    val muted: Color
    val mutedForeground: Color
    val accent: Color
    val accentForeground: Color
    val approval: Color
    val approvalForeground: Color
    val error: Color
    val errorForeground: Color
    val highlightCard: Color
    val input: Color
    val ring: Color
    val statusYellow: Color
}

@Immutable
data class SchemeColors(
    val colorVisa: Color = Color(0xff1434CA),
    val colorMastercard: Color = Color(0xffF79E1B),
    val colorJcb: Color = Color(0xff40A737),
    val colorUnionpay: Color = Color(0xffE0002B),
    val colorAmex: Color = Color(0xff006FCF),
    val colorAlipay: Color = Color(0xff1677FF),
    val colorWechat: Color = Color(0xff09BB07),
    val colorFps: Color = Color(0xff56BBE9),
    val colorPayme: Color = Color(0xffDB0011),
    val colorOctopus: Color = Color(0xffF5911E),
    val colorCash: Color = Color(0xff00A590),
)

val LocalMsaColors = staticCompositionLocalOf<MsaColors> { MsaColorsLight() }
val LocalSchemeColors = staticCompositionLocalOf { SchemeColors() }