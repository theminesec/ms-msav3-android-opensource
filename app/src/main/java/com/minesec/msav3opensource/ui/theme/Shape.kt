package com.minesec.msav3opensource.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.theminesec.multiplatform.msa_core.app.navigation.LocalWindowSize
import com.theminesec.multiplatform.msa_core.app.navigation.WindowWidthSize

@Composable
fun adaptiveShapes(): Shapes {
    val windowSize = LocalWindowSize.current

    fun Int.adaptive(): Dp = when (windowSize.width) {
        WindowWidthSize.Compact -> this.dp
        WindowWidthSize.Medium -> this.dp * 1.2f
        WindowWidthSize.Expanded -> this.dp * 1.4f
    }

    return Shapes(
        extraSmall = RoundedCornerShape(2.adaptive()),
        small = RoundedCornerShape(4.adaptive()),
        medium = RoundedCornerShape(6.adaptive()),
        large = RoundedCornerShape(8.adaptive()),
        extraLarge = RoundedCornerShape(10.adaptive()),
    )
}