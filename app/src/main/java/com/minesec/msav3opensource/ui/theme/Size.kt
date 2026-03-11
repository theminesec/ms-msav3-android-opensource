package com.minesec.msav3opensource.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.theminesec.multiplatform.msa_core.app.navigation.LocalWindowSize
import com.theminesec.multiplatform.msa_core.app.navigation.WindowWidthSize


interface Spacing {
    val xs3: Dp
    val xs2: Dp
    val xs: Dp
    val sm: Dp
    val md: Dp
    val lg: Dp
    val xl: Dp
    val xl2: Dp
    val xl3: Dp
    val xl4: Dp
    val xl5: Dp
}

interface AnimationSize {
    val xs: Dp
    val sm: Dp
    val md: Dp
    val lg: Dp
}

interface MinTouchSize {
    val sm: Dp
    val md: Dp
    val lg: Dp
    val xl: Dp
    val xl2: Dp
    val xl3: Dp
}

interface IconSize {
    val xs2: Dp
    val xs: Dp
    val sm: Dp
    val md: Dp
    val lg: Dp
    val xl: Dp
    val xl2: Dp
    val xl3: Dp
}

interface Radius {
    val xs: Dp
    val sm: Dp
    val md: Dp
    val lg: Dp
    val xl: Dp
    val xl2: Dp
}

@Composable
private fun Int.adaptive(): Dp {
    val windowSize = LocalWindowSize.current
    return when (windowSize.width) {
        WindowWidthSize.Compact -> this.dp
        WindowWidthSize.Medium -> this.dp * 1.2f
        WindowWidthSize.Expanded -> this.dp * 1.4f
    }
}

@Composable
fun adaptiveSpacing(): Spacing {
    return object : Spacing {
        override val xs3 = 2.adaptive()
        override val xs2 = 4.adaptive()
        override val xs = 8.adaptive()
        override val sm = 12.adaptive()
        override val md = 16.adaptive()
        override val lg = 24.adaptive()
        override val xl = 32.adaptive()
        override val xl2 = 48.adaptive()
        override val xl3 = 64.adaptive()
        override val xl4 = 80.adaptive()
        override val xl5 = 96.adaptive()
    }
}

@Composable
fun adaptiveAnimationSize(): AnimationSize {
    return object : AnimationSize {
        override val xs = 72.adaptive()
        override val sm = 160.adaptive()
        override val md = 220.adaptive()
        override val lg = 280.adaptive()
    }
}

@Composable
fun adaptiveMinTouchSize(): MinTouchSize {
    return object : MinTouchSize {
        override val sm = 40.adaptive()
        override val md = 56.adaptive()
        override val lg = 60.adaptive()
        override val xl = 72.adaptive()
        override val xl2 = 88.adaptive()
        override val xl3 = 100.adaptive()
    }
}

@Composable
fun adaptiveIconSize(): IconSize {
    return object : IconSize {
        override val xs2 = 12.adaptive()
        override val xs = 16.adaptive()
        override val sm = 20.adaptive()
        override val md = 24.adaptive()
        override val lg = 28.adaptive()
        override val xl = 36.adaptive()
        override val xl2 = 48.adaptive()
        override val xl3 = 64.adaptive()
    }
}

@Composable
fun adaptiveRadius(): Radius {
    return object : Radius {
        override val xs = 2.adaptive()
        override val sm = 4.adaptive()
        override val md = 6.adaptive()
        override val lg = 8.adaptive()
        override val xl = 12.adaptive()
        override val xl2 = 16.adaptive()
    }
}