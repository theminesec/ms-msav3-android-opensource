package com.minesec.msav3opensource.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class MsaColorsLight(
    // Primary - Blue theme
    override val primary: Color = Color(0xFF2563EB),
    override val primaryForeground: Color = Color(0xFFF8FAFC),
    override val primaryGradient: Color = Color(0xFF3B82F6),

    // Secondary - Purple
    override val secondary: Color = Color(0xFF7C3AED),
    override val secondaryForeground: Color = Color(0xFFF5F3FF),

    // Background & Foreground
    override val background: Color = Color(0xFFFFFFFF),
    override val foreground: Color = Color(0xFF0F172A),

    // Muted
    override val muted: Color = Color(0xFFE2E8F0),
    override val mutedForeground: Color = Color(0xFF64748B),

    // Accent
    override val accent: Color = Color(0xFFF1F5F9),
    override val accentForeground: Color = Color(0xFF475569),

    // Status
    override val approval: Color = Color(0xFF10B981),
    override val approvalForeground: Color = Color(0xFFECFDF5),
    override val error: Color = Color(0xFFDC2626),
    override val errorForeground: Color = Color(0xFFFEF2F2),
    override val statusYellow: Color = Color(0xFFF59E0B),

    // Other
    override val highlightCard: Color = Color(0xFFEFF6FF),
    override val input: Color = Color(0xFF94A3B8),
    override val ring: Color = Color(0xFFCBD5E1),
) : MsaColors

@Immutable
data class MsaColorsDark(
    // Primary - Blue theme
    override val primary: Color = Color(0xFF3B82F6),
    override val primaryForeground: Color = Color(0xFFF8FAFC),
    override val primaryGradient: Color = Color(0xFF60A5FA),

    // Secondary - Purple
    override val secondary: Color = Color(0xFF8B5CF6),
    override val secondaryForeground: Color = Color(0xFFF5F3FF),

    // Background & Foreground
    override val background: Color = Color(0xFF0F172A),
    override val foreground: Color = Color(0xFFF8FAFC),

    // Muted
    override val muted: Color = Color(0xFF1E293B),
    override val mutedForeground: Color = Color(0xFF94A3B8),

    // Accent
    override val accent: Color = Color(0xFF334155),
    override val accentForeground: Color = Color(0xFFE2E8F0),

    // Status
    override val approval: Color = Color(0xFF34D399),
    override val approvalForeground: Color = Color(0xFFECFDF5),
    override val error: Color = Color(0xFFF87171),
    override val errorForeground: Color = Color(0xFFFEF2F2),
    override val statusYellow: Color = Color(0xFFFBBF24),

    // Other
    override val highlightCard: Color = Color(0x661E3A5F),
    override val input: Color = Color(0xFF64748B),
    override val ring: Color = Color(0xFF475569),
) : MsaColors