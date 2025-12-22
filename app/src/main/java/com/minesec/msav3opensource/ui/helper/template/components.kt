package com.minesec.msav3opensource.ui.helper.template

import KottieAnimation
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.minesec.msav3opensource.R
import com.minesec.msav3opensource.ui.theme.MsaTheme
import com.minesec.msav3opensource.ui.screens.HistoryTransactionItem
import com.theminesec.multiplatform.msa_core.common.domain.models.MSAException
import com.theminesec.multiplatform.msa_core.feature.common.data.models.TransactionType
import com.theminesec.multiplatform.msa_core.feature.common.domain.models.enums.PaymentMethod
import com.theminesec.multiplatform.msa_core.feature.common.domain.models.enums.TransactionStatus
import kotlinx.coroutines.delay
import kottieComposition.KottieCompositionSpec
import kottieComposition.animateKottieCompositionAsState
import kottieComposition.rememberKottieComposition
import utils.KottieConstants


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
fun AuthProgressBar(
    progress: Int, // progress from ViewModel (0-100)
    modifier: Modifier = Modifier,
    onCompleted: (() -> Unit)? = null // optional callback when it reaches 100%
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress / 100f,
        animationSpec = tween(
            durationMillis = 300,
            easing = FastOutSlowInEasing
        ),
        label = "progress_animation"
    )

    LaunchedEffect(progress) {
        if (progress >= 100) {
            onCompleted?.invoke()
        }
    }

    LinearProgressIndicator(
        progress = { animatedProgress },
        modifier = modifier
            .fillMaxWidth()
            .height(6.dp),
        color = MsaTheme.colors.primary,
        trackColor = Color.White.copy(alpha = 0.2f),
        strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
    )
}


@Composable
fun PasscodeDialog(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var passcode by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var passwordVisible by remember { mutableStateOf(false) }

    val minLength = 4
    val maxLength = 6
    val titleText = stringResource(R.string.enter_merchant_passcode)
    val dismissButtonText = stringResource(R.string.cancel_button)
    val confirmButtonText = stringResource(R.string.confirm_button)

    val actualTrailingIconResource: Int =
        if (passwordVisible) R.drawable.show_password
        else R.drawable.hide_password

    val trailingIconComposable: @Composable (() -> Unit) = {
        Image(
            painter = painterResource(actualTrailingIconResource),
            contentDescription = "Toggle password visibility",
            modifier = Modifier
                .size(MsaTheme.iconSize.sm)
                .clickable { passwordVisible = !passwordVisible },
            colorFilter = ColorFilter.tint(MsaTheme.colors.mutedForeground)
        )
    }

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .background(MsaTheme.colors.background, MsaTheme.shapes.medium)
                .padding(vertical = MsaTheme.spacing.lg, horizontal = MsaTheme.spacing.md)
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MsaTheme.spacing.md)
        ) {
            Text(
                text = titleText,
                style = MsaTheme.typography.titleMedium,
                color = MsaTheme.colors.foreground,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(MsaTheme.spacing.xs))

            OutlinedTextField(
                value = passcode,
                onValueChange = { input ->
                    val digits = input.filter { it.isDigit() }
                    if (digits.length <= maxLength) {
                        passcode = digits
                    }
                },
                placeholder = { Text("Enter 4–6 digit passcode") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.NumberPassword
                ),
                visualTransformation = if (passwordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                textStyle = MsaTheme.typography.bodySmall,
                colors = TextFieldDefaults.colors(
                    unfocusedTextColor = MsaTheme.colors.foreground,
                    focusedTextColor = MsaTheme.colors.foreground,
                    unfocusedIndicatorColor = MsaTheme.colors.input,
                    unfocusedContainerColor = MsaTheme.colors.background,
                    focusedIndicatorColor = MsaTheme.colors.primary,
                    focusedContainerColor = MsaTheme.colors.approvalForeground,
                ),
                shape = MsaTheme.shapes.medium,
                trailingIcon = trailingIconComposable
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(MsaTheme.spacing.xs))

                error?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                } ?: Text(
                    text = "${passcode.length} / $maxLength",
                    style = MsaTheme.typography.labelSmall,
                    color = MsaTheme.colors.mutedForeground,
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MsaTheme.spacing.md)
            ) {
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .weight(1f)
                        .height(MsaTheme.minTouchSize.sm),
                    shape = MsaTheme.shapes.medium,
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = MsaTheme.colors.primary,
                    ),
                    border = BorderStroke(1.dp, MsaTheme.colors.primary)
                ) {
                    Text(text = dismissButtonText, style = MsaTheme.typography.bodyLarge)
                }

                Button(
                    onClick = {
                        when {
                            passcode.isBlank() -> {
                                // Local UI validation can still show a quick message
                                // but don’t overwrite backend error
                            }

                            passcode.length < minLength -> { /* ... */
                            }

                            passcode.length > maxLength -> { /* ... */
                            }

                            else -> onConfirm(passcode)
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(MsaTheme.minTouchSize.sm),
                    shape = MsaTheme.shapes.medium,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(MsaTheme.minTouchSize.sm)
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        MsaTheme.colors.primaryGradient,
                                        MsaTheme.colors.primary
                                    )
                                ),
                                shape = MsaTheme.shapes.medium
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = confirmButtonText,
                            style = MsaTheme.typography.bodyLarge,
                            color = MsaTheme.colors.primaryForeground
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BiometricDialog(
    onConfirm: () -> Unit, onDismiss: () -> Unit
) {
    val titleText = stringResource(R.string.enable_biometric_title)
    val messageText = stringResource(R.string.enable_biometric_message)
    val confirmButtonText = stringResource(R.string.enable)
    val dismissButtonText = stringResource(R.string.not_now)

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .background(MsaTheme.colors.background, MsaTheme.shapes.medium)
                .padding(
                    vertical = MsaTheme.spacing.lg,
                    horizontal = MsaTheme.spacing.md
                )
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MsaTheme.spacing.md)
        ) {
            Text(
                text = titleText,
                style = MsaTheme.typography.titleMedium,
                color = MsaTheme.colors.foreground,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.Start)
            )

            Text(
                text = messageText,
                style = MsaTheme.typography.bodySmall,
                color = MsaTheme.colors.mutedForeground,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(MsaTheme.spacing.sm))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MsaTheme.spacing.md)
            ) {
                Button(
                    onClick = {
                        onDismiss()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(MsaTheme.minTouchSize.sm),
                    shape = MsaTheme.shapes.medium,
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = MsaTheme.colors.primary,
                    ),
                    border = BorderStroke(1.dp, MsaTheme.colors.primary)
                ) {
                    Text(
                        text = dismissButtonText,
                        style = MsaTheme.typography.bodyLarge
                    )
                }

                // Confirm / Enable
                Button(
                    onClick = {
                        onConfirm()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(MsaTheme.minTouchSize.sm),
                    shape = MsaTheme.shapes.medium,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(MsaTheme.minTouchSize.sm)
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        MsaTheme.colors.primaryGradient,
                                        MsaTheme.colors.primary
                                    )
                                ),
                                shape = MsaTheme.shapes.medium
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = confirmButtonText,
                            style = MsaTheme.typography.bodyLarge,
                            color = MsaTheme.colors.primaryForeground
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OriginalTransactionIdDialog(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var transactionId by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Original Transaction ID",
                color = MsaTheme.colors.primary,
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = transactionId,
                    onValueChange = {
                        if (it.length <= 20) {
                            transactionId = it.trim()
                            error = null
                        }
                    },
                    placeholder = { Text("Enter 20-char Transaction ID") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Ascii,
                        capitalization = KeyboardCapitalization.None
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    isError = error != null
                )

                Spacer(modifier = Modifier.height(6.dp))

                if (error != null) {
                    Text(
                        text = error!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                } else {
                    val length = transactionId.length
                    Text(
                        text = "$length / 20",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (length == 20)
                            MsaTheme.colors.primary
                        else
                            MaterialTheme.colorScheme.outline
                    )
                }
            }
        },
        confirmButton = {
            MSAConfirmCancelButtons(
                confirmText = "Confirm",
                cancelText = "Cancel",
                onCancelClick = onDismiss,
                onConfirmClick = {
                    when {
                        transactionId.isBlank() -> {
                            error = "Transaction ID cannot be empty"
                        }

                        transactionId.length != 20 -> {
                            error = "Transaction ID must be exactly 20 characters"
                        }

                        else -> {
                            onConfirm(transactionId)
                            onDismiss()
                        }
                    }
                }
            )
        }
    )
}

@Composable
fun TransactionAuthCardVoidDialog(
    onConfirm: (passcode: String, transactionId: String) -> Unit,
    onDismiss: () -> Unit
) {
    var passcode by remember { mutableStateOf("") }
    var transactionId by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Authentication Required",
                color = MsaTheme.colors.primary,
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = passcode,
                    onValueChange = {
                        if (it.length <= 6) {
                            passcode = it.filter { ch -> ch.isDigit() }
                            error = null
                        }
                    },
                    placeholder = { Text("Enter 6-digit passcode") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.NumberPassword
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "${passcode.length}/6 digits",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = transactionId,
                    onValueChange = {
                        if (it.length <= 20) {
                            transactionId = it.trim()
                            error = null
                        }
                    },
                    placeholder = { Text("Enter 20-char Transaction ID") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Ascii,
                        capitalization = KeyboardCapitalization.None
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "${transactionId.length}/20 characters",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )

                error?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            MSAConfirmCancelButtons(
                confirmText = "Confirm",
                cancelText = "Cancel",
                onCancelClick = onDismiss,
                onConfirmClick = {
                    when {
                        passcode.isBlank() -> error = "Passcode cannot be empty"
                        passcode.length != 6 -> error = "Passcode must be exactly 6 digits"
                        transactionId.isBlank() -> error = "Transaction ID cannot be empty"
                        transactionId.length != 20 -> error =
                            "Transaction ID must be exactly 20 characters"

                        else -> {
                            onConfirm(passcode, transactionId)
                            onDismiss()
                        }
                    }
                }
            )
        }
    )
}


@Composable
fun MSAConfirmCancelButtons(
    confirmText: String,
    onConfirmClick: () -> Unit,
    cancelText: String = "Cancel",
    onCancelClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MsaTheme.spacing.sm),
        horizontalArrangement = Arrangement.spacedBy(MsaTheme.spacing.md)
    ) {
        Button(
            onClick = onCancelClick,
            modifier = Modifier
                .weight(1f)
                .height(MsaTheme.minTouchSize.sm),
            shape = MsaTheme.shapes.medium,
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Transparent,
                contentColor = MsaTheme.colors.primary
            ),
            border = BorderStroke(1.dp, MsaTheme.colors.primary)
        ) {
            Text(
                text = cancelText,
                style = MsaTheme.typography.bodyLarge
            )
        }

        Button(
            onClick = onConfirmClick,
            modifier = Modifier
                .weight(1f)
                .height(MsaTheme.minTouchSize.sm),
            shape = MsaTheme.shapes.medium,
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent, contentColor = MsaTheme.colors.primaryForeground
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MsaTheme.colors.primaryGradient, MsaTheme.colors.primary
                            )
                        ), shape = MsaTheme.shapes.medium
                    ), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = confirmText, style = MsaTheme.typography.bodyLarge
                )
            }
        }
    }
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
    val context = LocalContext.current
    var animation by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        animation = context.assets
            .open("anim_await_card_alt.json")
            .bufferedReader()
            .use { it.readText() }
    }

    val composition = rememberKottieComposition(
        spec = KottieCompositionSpec.JsonString(animation)
    )

    val animationState by animateKottieCompositionAsState(
        composition = composition,
        iterations = KottieConstants.IterateForever
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Receipt NFC",
                style = MaterialTheme.typography.titleMedium,
                color = MsaTheme.colors.primary
            )
        },
        text = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                KottieAnimation(
                    composition = composition,
                    progress = { animationState.progress },
                    modifier = Modifier.size(200.dp)
                )
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MsaTheme.spacing.sm),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .weight(1f)
                        .height(MsaTheme.minTouchSize.sm),
                    shape = MsaTheme.shapes.medium,
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = MsaTheme.colors.primary
                    ),
                    border = BorderStroke(1.dp, MsaTheme.colors.primary)
                ) {
                    Text(
                        text = "Dismiss",
                        style = MsaTheme.typography.bodyLarge
                    )
                }
            }
        },
        modifier = Modifier.background(Color.White, RoundedCornerShape(12.dp))
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
fun SettlementSuccessDialog(
    showDialog: Boolean,
    message: String,
    onDismissRequest: () -> Unit // Function to close the dialog
) {
    var internalVisible by remember(showDialog) { mutableStateOf(showDialog) }
    if (!internalVisible) return

    // Auto-dismiss after 1 second
    LaunchedEffect(showDialog) {
        if (showDialog) {
            delay(1000L)
            internalVisible = false
            onDismissRequest()
        }
    }

    Dialog(onDismissRequest = onDismissRequest) {
        // Use a Card for a styled, elevated look
        Card(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(8.dp),
        ) {
            Row(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.success_icon),
                    contentDescription = "Approved",
                    modifier = Modifier
                        .size(70.dp)
                        .padding(end = 12.dp)
                )
                Text(
                    text = message,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
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
        //    dismissButtonText = stringResource(R.string.retry_button),
        onDismissClick = {
            onRetry()
        }, onOK = {
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
    dismissButtonText: String? = null,
    onDismissClick: (() -> Unit)? = null,
    onDismiss: () -> Unit = {},
    image: Int? = null
) {
    if (!dialogVisibility) return

    val isTwoButtons = dismissButtonText != null

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
                    .padding(10.dp)
            ) {
                image?.let {
                    Image(
                        painter = painterResource(it),
                        contentDescription = null,
                        modifier = Modifier.size(100.dp)
                    )
                }

                Spacer(modifier = Modifier.size(15.dp))

                Text(
                    title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = MsaTheme.colors.primary,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.size(7.dp))

                Text(
                    content,
                    style = MaterialTheme.typography.labelMedium,
                    color = MsaTheme.colors.foreground,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.size(20.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MsaTheme.spacing.sm),
                    horizontalArrangement = if (isTwoButtons)
                        Arrangement.spacedBy(MsaTheme.spacing.md)
                    else Arrangement.End
                ) {
                    // Dismiss Button
                    dismissButtonText?.let {
                        Button(
                            onClick = { onDismissClick?.invoke() },
                            modifier = Modifier
                                .weight(1f)
                                .height(MsaTheme.minTouchSize.sm),
                            shape = MsaTheme.shapes.medium,
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = Color.Transparent,
                                contentColor = MsaTheme.colors.primary
                            ),
                            border = BorderStroke(1.dp, MsaTheme.colors.primary)
                        ) {
                            Text(
                                text = it,
                                style = MsaTheme.typography.bodyLarge
                            )
                        }
                    }

                    // OK Button
                    val okButtonModifier = if (isTwoButtons) {
                        Modifier
                            .weight(1f)
                            .height(MsaTheme.minTouchSize.sm)
                    } else {
                        Modifier
                            .wrapContentWidth()
                            .widthIn(min = 100.dp, max = 150.dp)
                            .height(MsaTheme.minTouchSize.sm)
                    }

                    Button(
                        onClick = { onOK() },
                        modifier = okButtonModifier,
                        shape = MsaTheme.shapes.medium,
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = MsaTheme.colors.primaryForeground
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            MsaTheme.colors.primaryGradient,
                                            MsaTheme.colors.primary
                                        )
                                    ),
                                    shape = MsaTheme.shapes.medium
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = okButtonText,
                                style = MsaTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun getDynamicFontSize(amountString: String): TextUnit {
    val length = amountString.length

    return when {
        // Less than 5 characters (including decimal point if present) -> 40.sp
        length <= 5 -> 40.sp
        // Less than 8 characters -> 35.sp
        length < 8 -> 35.sp
        // 8 or more characters -> 30.sp
        else -> 30.sp
    }
}


@Composable
fun CustomFadingCircularProgressIndicator(
    mainColor: Color,
    size: Dp,
    strokeWidth: Dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "CircleRotation")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "RotationAngle"
    )

    Canvas(modifier = Modifier.size(size)) {
        val strokePx = strokeWidth.toPx()
        val diameter = this.size.width - strokePx
        val radius = diameter / 2
        val style = Stroke(width = strokePx, cap = StrokeCap.Round)

        // Use the center of the canvas
        val center = Offset(this.size.width / 2, this.size.height / 2)
        val topLeft = Offset(center.x - radius, center.y - radius)

        rotate(
            degrees = rotationAngle, pivot = center
        ) {
            drawArc(
                color = mainColor,
                startAngle = 225f,
                sweepAngle = 120f,
                useCenter = false,
                topLeft = topLeft,
                size = Size(diameter, diameter),
                style = style
            )
        }
    }
}

@Composable
fun AnimatedCircularProgress(
    strokeWidth: Dp = 8.dp,
    size: Dp = 200.dp,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(size * 1.15f),
            contentAlignment = Alignment.Center
        ) {
            CustomFadingCircularProgressIndicator(
                mainColor = MsaTheme.colors.primary, size = size, strokeWidth = strokeWidth
            )
        }
    }
}


// Usage example
@Preview
@Composable
fun ProgressPreview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .background(Color.White) // Ensure visibility against your foreground colors
    ) {
        HistoryTransactionItem(
            paymentMethod = PaymentMethod.VISA_BRAND,
            accountNumberSuffix = "1234567890123456",
            referenceNumber = "REF-",
            amount = "EGP 44,345,123,456,789,01.00",
            status = TransactionStatus.APPROVED,
            transactionType = TransactionType.SALE,
            dateTime = "2025-10-29T11:41:34+0000",
            onItemClick = {}
        )
    }
}

