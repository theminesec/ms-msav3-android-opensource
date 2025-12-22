package com.minesec.msav3opensource.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class MsaColorsLight(
    // Primary - Green theme
    override val primary: Color = Color(0xFF01B856),
    override val primaryForeground: Color = Color(0xFFF1F4F9),
    override val primaryGradient: Color = Color(0xFF2BD659),

    // Secondary
    override val secondary: Color = Color(0xFF0A307A),
    override val secondaryForeground: Color = Color(0xFFEDF2FD),

    // Background & Foreground
    override val background: Color = Color(0xFFFFFFFF),
    override val foreground: Color = Color(0xFF101A2D),

    // Muted
    override val muted: Color = Color(0xFFE1E7F4),
    override val mutedForeground: Color = Color(0xFF67748E),

    // Accent
    override val accent: Color = Color(0xFFF3F5FA),
    override val accentForeground: Color = Color(0xFF3E4B65),

    // Status
    override val approval: Color = Color(0xFF22D375),
    override val approvalForeground: Color = Color(0xFFF0FDF4),
    override val error: Color = Color(0xFFF62851),
    override val errorForeground: Color = Color(0xFFFEF2F2),
    override val statusYellow: Color = Color(0xFFF5BC15),

    // Other
    override val highlightCard: Color = Color(0xFFE6F2FF),
    override val input: Color = Color(0xFF8699C1),
    override val ring: Color = Color(0xFFC9D2E3),
) : MsaColors

@Immutable
data class MsaColorsDark(
    // Primary - Green theme
    override val primary: Color = Color(0xFF01B856),
    override val primaryForeground: Color = Color(0xFFF1F4F9),
    override val primaryGradient: Color = Color(0xFF2BD659),

    // Secondary
    override val secondary: Color = Color(0xFF0A307A),
    override val secondaryForeground: Color = Color(0xFFEDF2FD),

    // Background & Foreground
    override val background: Color = Color(0xFF101A2D),
    override val foreground: Color = Color(0xFFFFFFFF),

    // Muted
    override val muted: Color = Color(0xFF1E293B),
    override val mutedForeground: Color = Color(0xFF64748B),

    // Accent
    override val accent: Color = Color(0xFF334155),
    override val accentForeground: Color = Color(0xFFCBD5E1),

    // Status
    override val approval: Color = Color(0xFF21AB46),
    override val approvalForeground: Color = Color(0xFFF0FDF4),
    override val error: Color = Color(0xFFEF4444),
    override val errorForeground: Color = Color(0xFFFEF2F2),
    override val statusYellow: Color = Color(0xFFF5BC15),

    // Other
    override val highlightCard: Color = Color(0x662B3E64),
    override val input: Color = Color(0xFF8699C1),
    override val ring: Color = Color(0xFFC9D2E3),
) : MsaColors