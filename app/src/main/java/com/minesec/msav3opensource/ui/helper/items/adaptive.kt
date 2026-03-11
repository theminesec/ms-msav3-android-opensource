import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.theminesec.multiplatform.msa_core.app.navigation.LocalWindowSize
import com.theminesec.multiplatform.msa_core.app.navigation.WindowWidthSize
import com.theminesec.multiplatform.msa_core.app.navigation.WindowHeightSize


//--------------------------width----------------------------------

@Composable
fun TextUnit.adaptive(): TextUnit {
    val windowSize = LocalWindowSize.current
    return when (windowSize.width) {
        WindowWidthSize.Compact -> this
        WindowWidthSize.Medium -> this * 1.2f
        WindowWidthSize.Expanded -> this * 1.4f
    }
}

//-------------------------------height-----------------------
@Composable
fun adaptiveSpacingHeight(baseSpacing: Dp): Dp {
    val windowSize = LocalWindowSize.current
    return when (windowSize.height) {
        WindowHeightSize.Compact -> baseSpacing
        WindowHeightSize.Medium -> baseSpacing
        WindowHeightSize.Expanded -> baseSpacing * 1.2f
    }
}

@Composable
fun adaptiveFontSizeHeight(baseFontSize: TextUnit): TextUnit {
    val windowSize = LocalWindowSize.current
    return when (windowSize.height) {
        WindowHeightSize.Compact -> baseFontSize
        WindowHeightSize.Medium -> baseFontSize
        WindowHeightSize.Expanded -> baseFontSize * 1.2f
    }
}

@Composable
fun adaptiveIconSizeHeight(baseIconSize: Dp): Dp {
    val windowSize = LocalWindowSize.current
    return when (windowSize.height) {
        WindowHeightSize.Compact -> baseIconSize
        WindowHeightSize.Medium -> baseIconSize
        WindowHeightSize.Expanded -> baseIconSize * 1.2f
    }
}

//----------
@Composable
fun adaptiveAmountHeight(baseSpacing: Dp): Dp {
    val windowSize = LocalWindowSize.current
    return when (windowSize.width) {
        WindowWidthSize.Compact -> baseSpacing * 0.9f
        WindowWidthSize.Medium -> baseSpacing * 1.4f
        WindowWidthSize.Expanded -> baseSpacing * 2f
    }
}

@Composable
fun rememberAdaptiveTranDetailFontSize(displayAmount: String): TextUnit {
    val windowSize = LocalWindowSize.current
    return remember(displayAmount.length, windowSize.width) {
        when (windowSize.width) {
            WindowWidthSize.Compact -> {
                when {
                    displayAmount.length <= 6 -> 40.sp
                    displayAmount.length <= 9 -> 28.sp
                    else -> 24.sp
                }
            }
            WindowWidthSize.Medium -> {
                when {
                    displayAmount.length <= 6 -> 64.sp
                    displayAmount.length <= 9 -> 52.sp
                    else -> 40.sp
                }
            }
            WindowWidthSize.Expanded -> {
                when {
                    displayAmount.length <= 6 -> 80.sp
                    displayAmount.length <= 9 -> 68.sp
                    else -> 56.sp
                }
            }
        }
    }
}

@Composable
fun rememberAwaitCardScreenAmountFontSize(displayAmount: String): TextUnit {
    val windowSize = LocalWindowSize.current
    return remember(displayAmount.length, windowSize.width) {
        when (windowSize.width) {
            WindowWidthSize.Compact -> {
                when {
                    displayAmount.length <= 6 -> 48.sp
                    displayAmount.length <= 9 -> 36.sp
                    else -> 24.sp
                }
            }
            WindowWidthSize.Medium -> {
                when {
                    displayAmount.length <= 6 -> 64.sp
                    displayAmount.length <= 9 -> 52.sp
                    else -> 40.sp
                }
            }
            WindowWidthSize.Expanded -> {
                when {
                    displayAmount.length <= 6 -> 80.sp
                    displayAmount.length <= 9 -> 68.sp
                    else -> 56.sp
                }
            }
        }
    }
}
@Composable
fun rememberPOSScreenAmountFontSize(displayAmount: String): TextUnit {
    val windowSize = LocalWindowSize.current
    return remember(displayAmount.length, windowSize.width) {
        when (windowSize.width) {
            WindowWidthSize.Compact -> {
                when {
                    displayAmount.length <= 6 -> 48.sp
                    displayAmount.length <= 9 -> 36.sp
                    else -> 24.sp
                }
            }
            WindowWidthSize.Medium -> {
                when {
                    displayAmount.length <= 6 -> 52.sp
                    displayAmount.length <= 9 -> 46.sp
                    else -> 40.sp
                }
            }
            WindowWidthSize.Expanded -> {
                when {
                    displayAmount.length <= 6 -> 56.sp
                    displayAmount.length <= 9 -> 50.sp
                    else -> 44.sp
                }
            }
        }
    }
}