package com.minesec.msav3opensource.ui.helper.items

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.minesec.msav3opensource.ui.theme.MsaTheme

import com.theminesec.multiplatform.msa_core.app.navigation.LocalWindowSize
import com.theminesec.multiplatform.msa_core.app.navigation.WindowHeightSize
import com.theminesec.multiplatform.msa_core.app.navigation.WindowSize
import com.theminesec.multiplatform.msa_core.app.navigation.WindowWidthSize

enum class MSAButtonStyle {
    FILLED,  // Solid background color
    OUTLINED, // Transparent background with border
    GRADIENT // First Color→ Start point (left/top) Second Color→ End point (right/bottom)
}

@Composable
fun MSAButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    defaultButtonStyle: MSAButtonStyle = MSAButtonStyle.GRADIENT, //default style when not specified
    gradientColors: List<Color> = listOf(MsaTheme.colors.primaryGradient, MsaTheme.colors.primary), //Default gradient color
    backgroundColor: Color = MsaTheme.colors.primary,  // Enabled filled button color ,outlined text color
    disabledBackgroundColor: Color = MsaTheme.colors.muted,  // Disabled color
    contentColor: Color = MsaTheme.colors.background, // Text color for filled,gradient
    disabledContentColor: Color = MsaTheme.colors.mutedForeground,
    borderColor: Color = MsaTheme.colors.primary,  // Border color for outlined
    borderWidth: Dp = 1.dp,
    shape: Shape = MsaTheme.shapes.large,
    contentPadding: PaddingValues = PaddingValues(vertical = MsaTheme.spacing.sm),
    style: TextStyle = MsaTheme.typography.bodyLarge
) {
    val buttonColors = when (defaultButtonStyle) {
        MSAButtonStyle.GRADIENT -> ButtonDefaults.buttonColors(
            containerColor = Color.Transparent, // Important for gradient to show
            contentColor = if (enabled) contentColor else disabledContentColor,
            disabledContainerColor = disabledBackgroundColor,
            disabledContentColor = disabledContentColor
        )
        MSAButtonStyle.FILLED -> ButtonDefaults.buttonColors(
            containerColor = if (enabled) backgroundColor else disabledBackgroundColor,
            contentColor = if (enabled) contentColor else disabledContentColor,
            disabledContainerColor = disabledBackgroundColor,
            disabledContentColor = disabledContentColor
        )
        MSAButtonStyle.OUTLINED -> ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = if (enabled) backgroundColor else disabledContentColor,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = disabledContentColor
        )
    }

    val border = when (defaultButtonStyle) {
        MSAButtonStyle.GRADIENT -> null
        MSAButtonStyle.FILLED -> null
        MSAButtonStyle.OUTLINED -> BorderStroke(borderWidth, if (enabled) borderColor else disabledContentColor)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(MsaTheme.minTouchSize.lg)
            .clip(shape)
            .then(
                if (defaultButtonStyle == MSAButtonStyle.GRADIENT && enabled) {
                    Modifier.background(
                        brush = Brush.verticalGradient(gradientColors),
                        shape = shape
                    )
                } else {
                    Modifier
                }
            )
    ) {
        Button(
            onClick = onClick,
            modifier = modifier.matchParentSize().height(MsaTheme.minTouchSize.lg),
            enabled = enabled,
            colors = buttonColors,
            border = border,
            shape = shape,
            contentPadding = contentPadding,

        ) {
            Text(
                text = text,
                style = style
            )
        }
    }
}

@Composable
fun MSAIconButton(
    iconResId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = MsaTheme.colors.accentForeground, // Icon color
    borderColor: Color = MsaTheme.colors.input, // border color
    borderWidth: Dp = 1.dp,
    cornerRadius: Dp = MsaTheme.radius.lg,
    iconSize: Dp = MsaTheme.iconSize.md
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(cornerRadius))
            .background(MsaTheme.colors.background)
            .border(
                width = borderWidth,
                color = borderColor,
                shape = RoundedCornerShape(cornerRadius)
            ),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.fillMaxSize()
//            .width(56.dp)
//            .height(40.dp)
                // button size
                .fillMaxHeight()
                .clip(MsaTheme.shapes.large)
                .background(MsaTheme.colors.background)

                .border(
                    width = borderWidth,
                    color = borderColor,
                    shape = RoundedCornerShape(cornerRadius)
                ),
            content = {
                Image(
                    painter = painterResource(iconResId),
                    contentDescription = null,
                    modifier = Modifier.size(iconSize),
                    colorFilter = ColorFilter.tint(tint)
                )
            }
        )
    }
}
//@Preview()
//@Composable
//fun PreviewIconButton() {
//    Row(
//        horizontalArrangement = Arrangement.spacedBy(MsaTheme.spacing.sm, Alignment.CenterHorizontally),
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(MsaTheme.minTouchSize.sm),
//    ) {
//        MSAIconButton(
//            iconResId = R.drawable.email,
//            onClick = {},
//            modifier = Modifier.weight(1f)
//        )
//        MSAIconButton(
//            iconResId = R.drawable.share,
//            onClick = {},
//            tint = MsaTheme.colors.approval,
//            borderColor = MsaTheme.colors.approval,
//            modifier = Modifier.weight(1f)
//        )
//        MSAIconButton(
//            iconResId = R.drawable.receipt,
//            onClick = {},
//            modifier = Modifier.weight(1f)
//            )
//    }
//}


/*
* sequence does matter in the gradient colors list
* First Color→ Start point (left/top)
* Second Color→ End point (right/bottom)
* */

@Preview()
@Composable
fun PreviewButtonShowcase() {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val widthSize = when {
            maxWidth < 600.dp -> WindowWidthSize.Compact
            maxWidth < 840.dp -> WindowWidthSize.Medium
            else -> WindowWidthSize.Expanded
        }
        val heightSize = when {
            maxHeight < 480.dp -> WindowHeightSize.Compact
            maxHeight < 900.dp -> WindowHeightSize.Medium
            else -> WindowHeightSize.Expanded
        }
        val windowSize = WindowSize(widthSize, heightSize)
        CompositionLocalProvider(LocalWindowSize provides windowSize) {
            Column(
                modifier = Modifier.padding(MsaTheme.spacing.md)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(MsaTheme.spacing.md)
            ) {
                // Default is gradient button
                MSAButton(
                    text = "Default",
                    onClick = {}
                )

                // Disabled gradient button
                MSAButton(
                    text = "Disabled Gradient(Default)",
                    onClick = {},
                    enabled = false
                )

                // Custom gradient colors
                MSAButton(
                    text = "e.g.Blue Gradient",
                    onClick = {},
                    gradientColors = listOf(Color(0xFF2196F3), Color(0xFF1260CC))
                )

                // Filled style
                MSAButton(
                    text = "Filled Button + change color",
                    onClick = {},
                    defaultButtonStyle = MSAButtonStyle.FILLED,
                    backgroundColor = Color.Red,
                )

                // Disabled filled
                MSAButton(
                    text = "Disabled Filled",
                    onClick = {},
                    defaultButtonStyle = MSAButtonStyle.FILLED,
                    enabled = false
                )

                // Outlined style
                MSAButton(
                    text = "Outlined Button",
                    onClick = {},
                    defaultButtonStyle = MSAButtonStyle.OUTLINED
                )

                // Disabled outlined
                MSAButton(
                    text = "Disabled Outlined",
                    onClick = {},
                    defaultButtonStyle = MSAButtonStyle.OUTLINED,
                    enabled = false
                )

                MSAButton(
                    text = "change Outlined color",
                    onClick = {},
                    defaultButtonStyle = MSAButtonStyle.OUTLINED,
                    backgroundColor = MsaTheme.colors.error,
                    borderColor = MsaTheme.colors.error,
                )
                Spacer(Modifier.height(40.dp))

                // Custom shaped button
                MSAButton(
                    text = "Rounded Button",
                    onClick = {},
                    shape = RoundedCornerShape(24.dp)
                )

                // Different size
                MSAButton(
                    text = "Large Button",
                    onClick = {},
                    style = MsaTheme.typography.titleLarge,
                    modifier = Modifier.height(100.dp)
                )


            }
        }
    }
}






