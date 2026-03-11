package com.minesec.msav3opensource.ui.helper.utils

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import org.koin.java.KoinJavaComponent.getKoin
import java.io.File
import java.io.FileOutputStream


fun shareImage(bytes: ByteArray, filename: String = "receipt.png") {
    val appContext: Context =
        getKoin().get() // 👈 this retrieves the androidContext registered in Koin

    val file = File(appContext.cacheDir, filename)
    FileOutputStream(file).use { it.write(bytes) }

    val uri = FileProvider.getUriForFile(
        appContext,
        "${appContext.packageName}.provider",
        file
    )

    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "image/png"
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    appContext.startActivity(
        Intent.createChooser(shareIntent, "Share Image").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    )
}
