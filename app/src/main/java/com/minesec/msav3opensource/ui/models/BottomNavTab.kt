package com.minesec.msav3opensource.ui.models

import com.minesec.msav3opensource.R
enum class BottomNavTab(
    val titleResId: Int, val iconResId: Int, val iconResIdSurface: Int,
) {
    Payment(R.string.payment_tab, R.drawable.payment_icon, R.drawable.payment_icon_surface),
    Help(R.string.help_tab, R.drawable.help, R.drawable.help_surface),
    Settings(R.string.settings_title, R.drawable.settings, R.drawable.settings_surface);
}