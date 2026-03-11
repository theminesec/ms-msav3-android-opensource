package com.minesec.msav3opensource.ui.template

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.minesec.msav3opensource.R
import com.minesec.msav3opensource.ui.theme.MsaTheme
import com.theminesec.multiplatform.msa_core.common.domain.models.MSAException

@Composable
fun OnGridPagination(
    gridState: LazyListState,
    buffer: Int = 5,
    totalItems: Int,
    maxItems: Int = 20,
    onLoadMore: () -> Unit
) {
    val shouldLoadMore = remember {
        derivedStateOf {
            val layoutInfo = gridState.layoutInfo
            val total = layoutInfo.totalItemsCount
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

            // Trigger only if not exceeding max items
            lastVisibleItem >= total - buffer && totalItems < maxItems
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value) {
            onLoadMore()
        }
    }
}


@Composable
fun BouncingAuthProgressBar(modifier: Modifier = Modifier) {
    val progress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            progress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 400, easing = LinearEasing)
            )
            progress.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 400, easing = LinearEasing)
            )
        }
    }

    LinearProgressIndicator(
        progress = { progress.value },
        modifier = modifier
            .fillMaxWidth()
            .height(6.dp),
        color = MsaTheme.colors.primary,
        trackColor = Color.White.copy(alpha = 0.2f),
        strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
    )
}

@Composable
fun FeatureUnavailableDialog(
    isVisible: Boolean,
    featureName: String,
    reason: String?,
    onDismiss: () -> Unit,
    onContactSupport: (() -> Unit)? = null,
    onUpgrade: (() -> Unit)? = null
) {
    if (isVisible) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            FeatureUnavailableContent(
                featureName = featureName,
                reason = reason ?: "This feature is not available for your merchant account",
                onDismiss = onDismiss,
                onContactSupport = onContactSupport,
                onUpgrade = onUpgrade
            )
        }
    }
}


@Composable
fun ReceiptNfcDialog(
    onDismiss: () -> Unit
) {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.anim_await_card_alt)
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(text = "Receipt NFC")
        },
        text = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(16.dp)
            ) {
                LottieAnimation(
                    composition = composition,
                    progress = {
                        progress
                    },
                    modifier = Modifier.size(300.dp)
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Dismiss")
            }
        }
    )
}


@Composable
private fun FeatureUnavailableContent(
    featureName: String,
    reason: String,
    onDismiss: () -> Unit,
    onContactSupport: (() -> Unit)?,
    onUpgrade: (() -> Unit)?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(
                        MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Custom lock icon using Text
                Text(
                    text = "🔒",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Title
            Text(
                text = "Feature Unavailable",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Feature name
            Text(
                text = featureName,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Reason
            Text(
                text = reason,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.2
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = if (onContactSupport != null || onUpgrade != null) {
                    Arrangement.spacedBy(12.dp)
                } else {
                    Arrangement.Center
                }
            ) {
                // Contact Support button (if provided)
                onContactSupport?.let { contactSupport ->
                    OutlinedButton(
                        onClick = {
                            contactSupport()
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Contact Support")
                    }
                }

                // Upgrade button (if provided)
                onUpgrade?.let { upgrade ->
                    Button(
                        onClick = {
                            upgrade()
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Upgrade")
                    }
                }

                // If no action buttons, show only dismiss button
                if (onContactSupport == null && onUpgrade == null) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Got it")
                    }
                }
            }
        }
    }
}

@Composable
fun MSALoadingDialog(
    message: String = "Loading..."
) {
    Dialog(onDismissRequest = {}) {
        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(40.dp),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 5.dp
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.primary,
                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium)
                )
            }
        }
    }
}

@Composable
internal fun ShowUnAuthorizedDialog(
    dialogVisibleState: Boolean, onDismiss: () -> Unit,
) {
    MSAAlertDialog(
        dialogVisibleState,
        stringResource(R.string.error),
        stringResource(R.string.session_expired),
        okButtonText = stringResource(R.string.ok),
        onOK = {
            onDismiss()
        })
}

@Composable
internal fun ShowAlertDialog(
    exception: MSAException,
    dialogVisibleState: Boolean,
    onRetry: () -> Unit,
    onDismiss: () -> Unit,
) {
    val title = when (exception) {
        is MSAException.Local -> stringResource(R.string.invalid_inputs)

        is MSAException.NetworkError.UnprocessableEntity -> stringResource(R.string.invalid_data)

        is MSAException.IOException -> stringResource(R.string.connection_error_title)

        is MSAException.NetworkError.AccessDenied -> stringResource(R.string.access_denied_title)

        is MSAException.NetworkError.Unauthorized -> stringResource(R.string.unauthorized_title)

        else -> stringResource(R.string.unexpected_error_title)
    }

    MSAAlertDialog(
        dialogVisibleState,
        title,
        exception.errorMessage.orEmpty()
            .ifEmpty { stringResource(R.string.unknow_error_occurred) },
        okButtonText = stringResource(R.string.ok),
        dismissButtonText = stringResource(R.string.retry_button),
        onDismissClick = {
            onRetry()
        },
        onOK = {
            onDismiss()
        })
}


@Composable
private fun MSAAlertDialog(
    dialogVisibility: Boolean,
    title: String,
    content: String,
    okButtonText: String,
    onOK: () -> Unit,
    dismissButtonText: String? = null, // Optional second button
    onDismissClick: (() -> Unit)? = null,
    onDismiss: () -> Unit = {},
    image: Int? = null
) {
    if (!dialogVisibility) return

    Dialog(onDismissRequest = { onDismiss() }) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(12.dp))
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                image?.let {
                    Image(
                        painter = painterResource(it),
                        contentDescription = null,
                        modifier = Modifier.size(100.dp)
                    )
                }

                Spacer(
                    modifier = Modifier.size(15.dp)
                )
                Text(
                    title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = MsaTheme.colors.primary,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )

                Spacer(
                    modifier = Modifier.size(7.dp)
                )
                Text(
                    content,
                    style = MaterialTheme.typography.labelMedium,
                    color = MsaTheme.colors.error,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )

                Spacer(
                    modifier = Modifier.size(5.dp)
                )
                Row(
                    horizontalArrangement = if (dismissButtonText != null) Arrangement.SpaceAround else Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = okButtonText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MsaTheme.colors.secondary,
                        modifier = Modifier.clickable { onOK() }
                    )

                    dismissButtonText?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MsaTheme.colors.primary,
                            modifier = Modifier.clickable { onDismissClick?.invoke() }
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.size(15.dp)
                )
            }
        }
    }
}

@Composable
fun AnimatedCircularProgressWithLogo(
    logoRes: Int = R.drawable.minesec_logo_dark, // Using Int for drawable resource
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 8.dp,
    size: Dp = 200.dp,
    logoSize: Dp = 60.dp, // Made logo larger
    animationDuration: Int = 2000 // Duration in milliseconds
) {
    // Infinite animation for progress
    val infiniteTransition = rememberInfiniteTransition(label = "progress_animation")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = animationDuration,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "progress"
    )

    // Rotation animation for logo border
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = animationDuration + 500, // Slightly different speed
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    // Center the entire dialog on screen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)), // Semi-transparent background
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = modifier.size(size),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val canvasSize = this.size
                val strokeWidthPx = strokeWidth.toPx()
                val radius = (canvasSize.minDimension - strokeWidthPx) / 2

                // Background circle (light green)
                drawCircle(
                    color = Color(0xFFE8F5E8), // Light green background
                    radius = radius,
                    style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
                )

                // Animated progress arc with gradient
                val sweepAngle = 360f * progress
                val startAngle = -90f // Start from top

                val gradient = Brush.sweepGradient(
                    colors = listOf(
                        Color(0xFF4CAF50), // Green
                        Color(0xFF66BB6A), // Lighter green
                        Color(0xFF4CAF50)  // Back to green
                    ),
                    center = Offset(canvasSize.width / 2, canvasSize.height / 2)
                )

                drawArc(
                    brush = gradient,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round),
                    topLeft = Offset(strokeWidthPx / 2, strokeWidthPx / 2),
                    size = Size(
                        canvasSize.width - strokeWidthPx,
                        canvasSize.height - strokeWidthPx
                    )
                )
            }

            // Animated logo container with rotating border
            Box(
                modifier = Modifier
                    .size(120.dp) // Slightly larger for border effect
                    .rotate(rotation) // Rotating animation
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF4CAF50),
                                Color(0xFF2E7D32),
                                Color(0xFF4CAF50)
                            ),
                            radius = 60.dp.value
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Inner logo container (stationary)
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .rotate(-rotation) // Counter-rotate to keep logo upright
                        .background(
                            color = Color(0xFF4CAF50),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(logoRes),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .size(logoSize)
                            .rotate(-rotation * 0.3f), // Subtle logo rotation
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                }
            }

            // Additional animated border rings
            Canvas(modifier = Modifier.size(130.dp)) {
                val center = Offset(this.size.width / 2, this.size.height / 2)
                val borderRadius = 65.dp.toPx()

                // Animated dashed border
                drawCircle(
                    color = Color(0xFF66BB6A).copy(alpha = 0.6f),
                    radius = borderRadius,
                    center = center,
                    style = Stroke(
                        width = 2.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(
                            floatArrayOf(
                                10.dp.toPx() * (1 + progress * 0.5f),
                                5.dp.toPx()
                            ),
                            phase = rotation * 2
                        )
                    )
                )
            }
        }
    }

}

// Usage example
@Preview
@Composable
fun ProgressPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
        contentAlignment = Alignment.Center
    ) {
        AnimatedCircularProgressWithLogo(
            size = 200.dp
        )
    }
}

