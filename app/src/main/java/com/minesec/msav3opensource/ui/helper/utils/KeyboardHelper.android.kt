package com.minesec.msav3opensource.ui.helper.utils

import android.content.ClipData
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.Clipboard

suspend fun copyTextToClipboard(
    clipboard: Clipboard,
    text: String
) {
    val clipData = ClipData.newPlainText("label", text)
    val clipEntry = ClipEntry(clipData)
    clipboard.setClipEntry(clipEntry)
}