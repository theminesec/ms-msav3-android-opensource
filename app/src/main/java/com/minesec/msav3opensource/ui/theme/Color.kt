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
    val processing: Color
    val headerSurface: Color
    val headerForeground: Color
}

@Immutable
data class MsaColorsLight(
    override val primary: Color = Color(0xFF4F46E5), // Indigo-600
    override val primaryForeground: Color = Color(0xFFF1F4F9),
    override val primaryGradient: Color = Color(0xFF6366F1), // Indigo-500
    override val secondary: Color = Color(0xFF1E1B4B), // Indigo-950
    override val secondaryForeground: Color = Color(0xFFEEF2FF),
    override val background: Color = Color(0xFFFFFFFF),
    override val foreground: Color = Color(0xFF0F172A),
    override val muted: Color = Color(0xFFE2E8F0),
    override val mutedForeground: Color = Color(0xFF64748B),
    override val accent: Color = Color(0xFFF1F5F9),
    override val accentForeground: Color = Color(0xFF334155),
    override val approval: Color = Color(0xFF10B981), // Emerald-500
    override val approvalForeground: Color = Color(0xFFF0FDF4),
    override val error: Color = Color(0xFFEF4444), // Red-500
    override val errorForeground: Color = Color(0xFFFEF2F2),
    override val highlightCard: Color = Color(0xFFEEF2FF), // Indigo-50
    override val input: Color = Color(0xFF94A3B8),
    override val ring: Color = Color(0xFFCBD5E1),
    override val statusYellow: Color = Color(0xFFF59E0B),
    override val processing: Color = Color(0xFF3B82F6), // Blue-500
    override val headerSurface: Color = Color(0xFF4F46E5), // Indigo-600
    override val headerForeground: Color = Color(0xFFFFFFFF)
) : MsaColors

@Immutable
data class MsaColorsDark(
    override val primary: Color = Color(0xFF818CF8), // Indigo-400
    override val primaryForeground: Color = Color(0xFFF1F4F9),
    override val primaryGradient: Color = Color(0xFFA5B4FC), // Indigo-300
    override val secondary: Color = Color(0xFF312E81), // Indigo-900
    override val secondaryForeground: Color = Color(0xFFEEF2FF),
    override val background: Color = Color(0xFF0F172A), // Slate-900
    override val foreground: Color = Color(0xFFF8FAFC),
    override val muted: Color = Color(0xFF1E293B),
    override val mutedForeground: Color = Color(0xFF94A3B8),
    override val accent: Color = Color(0xFF334155),
    override val accentForeground: Color = Color(0xFFCBD5E1),
    override val approval: Color = Color(0xFF34D399), // Emerald-400
    override val approvalForeground: Color = Color(0xFFF0FDF4),
    override val error: Color = Color(0xFFF87171), // Red-400
    override val errorForeground: Color = Color(0xFFFEF2F2),
    override val highlightCard: Color = Color(0x66312E81),
    override val input: Color = Color(0xFF94A3B8),
    override val ring: Color = Color(0xFF475569),
    override val statusYellow: Color = Color(0xFFFBBF24),
    override val processing: Color = Color(0xFF60A5FA), // Blue-400
    override val headerSurface: Color = Color(0xFF1E1B4B), // Indigo-950
    override val headerForeground: Color = Color(0xFFFFFFFF)
): MsaColors

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