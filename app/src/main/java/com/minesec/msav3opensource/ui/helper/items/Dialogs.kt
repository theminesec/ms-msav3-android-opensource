package com.minesec.msav3opensource.ui.helper.items

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.minesec.msav3opensource.R
import com.minesec.msav3opensource.ui.helper.template.MSAConfirmCancelButtons
import com.minesec.msav3opensource.ui.theme.MsaTheme
import com.theminesec.multiplatform.msa_core.feature.history.domain.models.TransactionDetails
import io.github.aakira.napier.Napier
import io.github.alexzhirkevich.qrose.options.QrBallShape
import io.github.alexzhirkevich.qrose.options.QrBrush
import io.github.alexzhirkevich.qrose.options.QrFrameShape
import io.github.alexzhirkevich.qrose.options.QrPixelShape
import io.github.alexzhirkevich.qrose.options.circle
import io.github.alexzhirkevich.qrose.options.roundCorners
import io.github.alexzhirkevich.qrose.options.solid
import io.github.alexzhirkevich.qrose.rememberQrCodePainter


@Composable
fun ShareReceiptQrDialog(
    qrURL: String,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = MsaTheme.shapes.medium,
            colors = CardDefaults.cardColors(
                containerColor = MsaTheme.colors.background
            ),
            elevation = CardDefaults.cardElevation(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Title
                Text(
                    text = "Share receipt via QR",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )


                // QR Code
                val qrcodePainter: Painter = rememberQrCodePainter(qrURL) {

                    shapes {
                        ball = QrBallShape.circle()
                        darkPixel = QrPixelShape.roundCorners()
                        frame = QrFrameShape.roundCorners(.25f)
                    }
                    colors {
                        frame = QrBrush.solid(Color.Black)
                    }
                }
                Image(
                    painter = qrcodePainter,
                    contentDescription = "Receipt QR Code",
                    modifier = Modifier
                        .size(MsaTheme.animationSize.md)
                        .padding(top = MsaTheme.spacing.xs)
                )

                // Text dismiss (clickable)
                Text(
                    text = "Dismiss",
                    style = MsaTheme.typography.bodyMedium.copy(
                        color = MsaTheme.colors.approval,
                    ),
                    modifier = Modifier.clickable { onDismiss() }
                )
            }
        }
    }
}

fun filterCurrencyInput(
    newValue: String, oldValue: String,
    maxDigitsBeforeDecimal: Int,
    maxDigitsAfterDecimal: Int,
): String {
    Napier.d(tag = "CurrencyFilter", message = "---- FILTER START ----")
    Napier.d(tag = "CurrencyFilter", message = "newValue='$newValue', oldValue='$oldValue'")

    if (newValue.isEmpty()) {
        Napier.d(tag = "CurrencyFilter", message = "Returned: empty string")
        Napier.d(tag = "CurrencyFilter", message = "---- FILTER END ----")
        return ""
    }

    if (newValue == "." || newValue == "0.") {
        Napier.d(tag = "CurrencyFilter", message = "Partial value (dot or 0.) allowed → $newValue")
        Napier.d(tag = "CurrencyFilter", message = "---- FILTER END ----")
        return newValue
    }

    var value = newValue.filter { it.isDigit() || it == '.' }
    Napier.d(tag = "CurrencyFilter", message = "After filter only digits and dots → '$value'")

    val lastDotIndex = value.lastIndexOf('.')
    if (lastDotIndex != -1) {
        val before = value.substring(0, lastDotIndex).replace(".", "")
        val after = value.substring(lastDotIndex + 1)
        value = "$before.$after"
        Napier.d(tag = "CurrencyFilter", message = "After keeping only last dot → '$value'")
    }

    val parts = value.split('.')
    var integerPart = parts[0]
    var decimalPart = parts.getOrNull(1) ?: ""
    Napier.d(
        tag = "CurrencyFilter",
        message = "Split → integer='$integerPart' decimal='$decimalPart'"
    )

    if (integerPart.length > maxDigitsBeforeDecimal) {
        integerPart = integerPart.take(maxDigitsBeforeDecimal)
        Napier.d(tag = "CurrencyFilter", message = "Trimmed integer part → '$integerPart'")
    }

    if (decimalPart.length > maxDigitsAfterDecimal) {
        decimalPart = decimalPart.take(maxDigitsAfterDecimal)
        Napier.d(tag = "CurrencyFilter", message = "Trimmed decimal part → '$decimalPart'")
    }

    val result = if (parts.size > 1) {
        if (maxDigitsAfterDecimal == 0) integerPart else "$integerPart.$decimalPart"
    } else integerPart

    Napier.d(tag = "CurrencyFilter", message = "Final result → '$result'")
    Napier.d(tag = "CurrencyFilter", message = "---- FILTER END ----")

    return result
}


@Composable
fun PartialAmountWithPassCodeDialog(
    transactionDetails: TransactionDetails,
    isPartialRefund: Boolean = true,
    onConfirm: (amount: String, passcode: String) -> Unit,
    onDismiss: () -> Unit,
) {
    var rawInput by remember {
        mutableStateOf(
            transactionDetails.getAmountFormatted().replace(",", ".")
        )
    }
    var passcode by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var passwordVisible by remember { mutableStateOf(false) }

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

    AlertDialog(
        modifier = Modifier
            .background(MsaTheme.colors.background, MsaTheme.shapes.medium)
            .fillMaxWidth()
            .wrapContentHeight(),
        onDismissRequest = onDismiss,
        containerColor = MsaTheme.colors.background,
        title = {
            Text(if (isPartialRefund) "Partial Refund" else "Capture Amount")
        },
        text = {
            Column {
                // Amount Input
                OutlinedTextField(
                    value = rawInput,
                    onValueChange = { newValue ->
                        rawInput = newValue
                        errorMessage = null
                    },
                    colors = TextFieldDefaults.colors(
                        unfocusedTextColor = MsaTheme.colors.foreground,
                        focusedTextColor = MsaTheme.colors.foreground,
                        unfocusedIndicatorColor = MsaTheme.colors.input,
                        unfocusedContainerColor = MsaTheme.colors.background,
                        focusedIndicatorColor = MsaTheme.colors.primary,
                        focusedContainerColor = MsaTheme.colors.approvalForeground,
                    ),
                    label = { Text(if (isPartialRefund) "Refund amount" else "Capture amount") },
                    placeholder = { Text("Enter amount") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Display max allowed amount
                Text(
                    text = "Max allowed: ${transactionDetails.getAmountFormatted()}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Passcode Input
                OutlinedTextField(
                    value = passcode,
                    onValueChange = {
                        val digits = it.filter { c -> c.isDigit() }
                        if (digits.length <= 6) {
                            passcode = digits
                            errorMessage = null
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    label = { Text("Passcode") },
                    placeholder = { Text("Enter 4–6 digit passcode") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.NumberPassword
                    ),
                    singleLine = true,
                    textStyle = MsaTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth(),
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

                // Error Message
                errorMessage?.let {
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
                    val enteredAmount =
                        parseAmountUserInput(rawInput).ifEmpty { "0" }.toDoubleOrNull() ?: 0.0
                    val maxAmount =
                        transactionDetails.amount / 100.0 // Convert cents to decimal (111 → 1.11)
                    when {
                        enteredAmount <= 0.0 ->
                            errorMessage = "Amount must be greater than 0"

                        enteredAmount > maxAmount ->
                            errorMessage =
                                "Amount cannot exceed ${transactionDetails.getAmountFormatted()}"

                        passcode.isBlank() ->
                            errorMessage = "Passcode cannot be empty"

                        passcode.length !in 4..6 ->
                            errorMessage = "Passcode must be 4–6 digits"

                        else -> {
                            onConfirm(rawInput, passcode)
                            onDismiss()
                        }
                    }
                }
            )
        }
    )
}

fun parseAmountUserInput(rawInput: String): String {
    val result =
        rawInput.replaceFirst("(.*)\\.".toRegex(), "$1").replace('.', ' ').replace(' ', '.')
    return rawInput.replace("\\.(?=.*\\.)".toRegex(), "")
}


@Composable
fun MSATextFieldDialog(
    initialNote: String,
    onAddNote: (String) -> Unit,
    onDismiss: () -> Unit,
    title: String,
    dismissButtonText: String = stringResource(R.string.cancel_button),
    confirmButtonText: String = stringResource(R.string.confirm_button),
    textFieldLeadingIcon: Int? = null,
    ontextFieldLeadingIconClick: (() -> Unit)? = null,
    textFieldTrailingIconResource: Int? = null,
    onTextFieldTrailingIconClick: (() -> Unit)? = null,
    isPassword: Boolean = false,
    maxLength: Int = 100,
    errorText: String? = null, // 🔹 external error
    onClearError: (() -> Unit)? = null, // 🔹 callback to clear it
    modifier: Modifier = Modifier
) {
    var noteText by remember { mutableStateOf(initialNote) }
    var passwordVisible by remember { mutableStateOf(false) }

    val actualTrailingIconResource: Int? =
        remember(isPassword, passwordVisible, textFieldTrailingIconResource) {
            if (isPassword) {
                if (passwordVisible) R.drawable.show_password
                else R.drawable.hide_password
            } else {
                textFieldTrailingIconResource
            }
        }

    val actualOnTextFieldTrailingIconClick: (() -> Unit)? =
        remember(isPassword, onTextFieldTrailingIconClick) {
            if (isPassword) {
                { passwordVisible = !passwordVisible }
            } else {
                onTextFieldTrailingIconClick
            }
        }

    Dialog(onDismissRequest = {
        onClearError?.invoke()
        onDismiss()
    }) {
        Column(
            modifier = modifier
                .background(MsaTheme.colors.background, MsaTheme.shapes.medium)
                .padding(vertical = MsaTheme.spacing.lg, horizontal = MsaTheme.spacing.md)
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MsaTheme.spacing.md)
        ) {
            Text(
                text = title,
                style = MsaTheme.typography.titleMedium,
                color = MsaTheme.colors.foreground,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.Start)
            )

            OutlinedTextField(
                value = noteText,
                onValueChange = { newText ->
                    if (newText.length <= maxLength) {
                        noteText = newText
                        // 🔹 clear error when user edits text
                        onClearError?.invoke()
                    }
                },
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
                singleLine = true,
                visualTransformation = if (isPassword && !passwordVisible)
                    PasswordVisualTransformation() else VisualTransformation.None,
                leadingIcon = textFieldLeadingIcon?.let {
                    {
                        Image(
                            painter = painterResource(it),
                            contentDescription = "Textfield icon",
                            modifier = Modifier
                                .size(MsaTheme.iconSize.sm)
                                .clickable(onClick = { ontextFieldLeadingIconClick?.invoke() }),
                            colorFilter = ColorFilter.tint(MsaTheme.colors.mutedForeground)
                        )
                    }
                },
                trailingIcon = actualTrailingIconResource?.let {
                    {
                        Image(
                            painter = painterResource(it),
                            contentDescription = "Textfield trailing icon",
                            modifier = Modifier
                                .size(MsaTheme.iconSize.sm)
                                .clickable(onClick = { actualOnTextFieldTrailingIconClick?.invoke() }),
                            colorFilter = ColorFilter.tint(MsaTheme.colors.mutedForeground)
                        )
                    }
                }
            )

            // 🔹 Show external error text if not null
            if (!errorText.isNullOrEmpty()) {
                Text(
                    text = errorText,
                    color = Color.Red,
                    style = MsaTheme.typography.labelSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                )
            }

            Text(
                text = "${noteText.length} / $maxLength",
                style = MsaTheme.typography.labelSmall,
                color = MsaTheme.colors.mutedForeground,
                modifier = Modifier.align(Alignment.End)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MsaTheme.spacing.md)
            ) {
                Button(
                    onClick = {
                        onClearError?.invoke()
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
                    Text(dismissButtonText, style = MsaTheme.typography.bodyLarge)
                }

                Button(
                    onClick = {
                        onClearError?.invoke()
                        onAddNote(noteText)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(MsaTheme.minTouchSize.sm),
                    shape = MsaTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Transparent
                    ),
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


@Preview()
@Composable
fun PreviewMSATextFieldDialog() {

    var showDialog by remember { mutableStateOf(true) }
    var currentNote by remember { mutableStateOf("#556889123") }

    if (showDialog) {
        MSATextFieldDialog(
            initialNote = currentNote,
            onAddNote = { newNote ->
                currentNote = newNote
                showDialog = false

            },
            onDismiss = {
                showDialog = false
            },
            title = stringResource(R.string.add_merchant_note),
            dismissButtonText = stringResource(R.string.cancel_button),
            confirmButtonText = stringResource(R.string.add_button)
        )
    }

}

@Preview()
@Composable
fun PreviewMSATextFieldDialogWithIcon() {
    var showDialog by remember { mutableStateOf(true) }
    var currentNote by remember { mutableStateOf("#556889123") }

    if (showDialog) {
        MSATextFieldDialog(
            initialNote = currentNote,
            onAddNote = { newNote ->
                currentNote = newNote
                showDialog = false
            },
            onDismiss = {
                showDialog = false
            },
            title = stringResource(R.string.add_merchant_note),
            dismissButtonText = stringResource(R.string.cancel_button),
            confirmButtonText = stringResource(R.string.add_button),

            // add parameters to enable the icon
            textFieldTrailingIconResource = R.drawable.ms_activation_qr,
            onTextFieldTrailingIconClick = {}
        )
    }
}


@Composable
fun MSATextDialog(
    message: String,
    buttonText: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        onDismissRequest = onDismiss, // Handles clicking outside or back button
        modifier = modifier,
        title = null, // No separate title, message serves as main text
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(MsaTheme.spacing.md)
            ) {
                Text(
                    text = message,
                    style = MsaTheme.typography.bodySmall,
                    color = MsaTheme.colors.accentForeground,
                    //modifier = Modifier.padding(bottom = MsaTheme.spacing.md) // Add some padding below message
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .wrapContentWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MsaTheme.colors.primaryGradient,
                                MsaTheme.colors.primary
                            )
                        ),
                        shape = MsaTheme.shapes.medium
                    )
                    .height(MsaTheme.minTouchSize.sm),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = MsaTheme.colors.primaryForeground
                ),
                shape = MsaTheme.shapes.medium
            ) {
                Text(
                    text = buttonText,
                    style = MsaTheme.typography.bodyLarge
                )
            }
        },

        containerColor = MsaTheme.colors.background,
        shape = MsaTheme.shapes.medium
    )
}

@Preview
@Composable
fun PreviewMSATextDialog() {
    var showDialog by remember { mutableStateOf(true) }

    if (showDialog) {
        MSATextDialog(
            message = stringResource(R.string.action_not_allowed),
            buttonText = stringResource(R.string.dismiss_button),
            onDismiss = { showDialog = false },
        )
    }
}


//-----------------------------AddTheseDialogInImplementationLater--------------------------------
@Preview
@Composable
fun PreviewEmailReceiptDialog() {
    var showDialog by remember { mutableStateOf(true) }
    var currentNote by remember { mutableStateOf("") }

    if (showDialog) {
        MSATextFieldDialog(
            initialNote = currentNote,
            onAddNote = { newNote ->
                currentNote = newNote
                showDialog = false
            },
            onDismiss = {
                showDialog = false
            },
            title = stringResource(R.string.send_receipt_via_email),
            dismissButtonText = stringResource(R.string.cancel_button),
            confirmButtonText = stringResource(R.string.send_button),
            textFieldLeadingIcon = R.drawable.email,
            ontextFieldLeadingIconClick = {}
        )
    }
}

@Preview
@Composable
fun PreviewTranIDDialog() {
    var showDialog by remember { mutableStateOf(true) }
    var currentNote by remember { mutableStateOf("") }

    if (showDialog) {
        MSATextFieldDialog(
            initialNote = currentNote,
            onAddNote = { newNote ->
                showDialog = false
            },
            onDismiss = {
                showDialog = false
            },
            title = stringResource(R.string.original_transaction_id),
            dismissButtonText = stringResource(R.string.cancel_button),
            confirmButtonText = stringResource(R.string.confirm_button),
        )
    }
}

@Preview()
@Composable
fun PreviewEnterMerchantPassword() {
    var showDialog by remember { mutableStateOf(true) }
    var currentNote by remember { mutableStateOf("") }

    if (showDialog) {
        MSATextFieldDialog(
            initialNote = currentNote,
            onAddNote = { newNote ->
                currentNote = newNote
                showDialog = false
            },
            onDismiss = {
                showDialog = false
            },
            title = stringResource(R.string.enter_merchant_password),
            dismissButtonText = stringResource(R.string.cancel_button),
            confirmButtonText = stringResource(R.string.confirm_button),
            isPassword = true,
        )
    }
}

@Preview()
@Composable
fun PreviewRefundAmount() {
    var showDialog by remember { mutableStateOf(true) }
    var currentNote by remember { mutableStateOf("$") }

    if (showDialog) {
        MSATextFieldDialog(
            initialNote = currentNote,
            onAddNote = { newNote ->
                currentNote = newNote
                showDialog = false
            },
            onDismiss = {
                showDialog = false
            },
            title = stringResource(R.string.enter_refund_amount),
            dismissButtonText = stringResource(R.string.cancel_button),
            confirmButtonText = stringResource(R.string.confirm_button),

            )
    }
}

@Preview()
@Composable
fun PreviewCompletionAmount() {
    var showDialog by remember { mutableStateOf(true) }
    var currentNote by remember { mutableStateOf("$") }

    if (showDialog) {
        MSATextFieldDialog(
            initialNote = currentNote,
            onAddNote = { newNote ->
                currentNote = newNote
                showDialog = false
            },
            onDismiss = {
                showDialog = false
            },
            title = stringResource(R.string.enter_completion_amount),
            dismissButtonText = stringResource(R.string.cancel_button),
            confirmButtonText = stringResource(R.string.confirm_button),

            )
    }
}

@Preview
@Composable
fun PreviewReceiptSentSuccessDialog() {
    var showDialog by remember { mutableStateOf(true) }
    if (showDialog) {
        MSATextDialog(
            message = stringResource(R.string.receipt_sent_success_message),
            buttonText = stringResource(R.string.dismiss_button),
            onDismiss = { showDialog = false },
        )
    }
}

@Preview
@Composable
fun PreviewAmountExceedsDialog() {
    var showDialog by remember { mutableStateOf(true) }
    if (showDialog) {
        MSATextDialog(
            message = stringResource(R.string.amount_exceeds_original_transaction_amount_message),
            buttonText = stringResource(R.string.dismiss_button),
            onDismiss = { showDialog = false },
        )
    }
}

@Preview
@Composable
fun PreviewActionNotAllowedDialog() {
    var showDialog by remember { mutableStateOf(true) }
    if (showDialog) {
        MSATextDialog(
            message = stringResource(R.string.action_not_allowed),
            buttonText = stringResource(R.string.dismiss_button),
            onDismiss = { showDialog = false },
        )
    }
}

@Preview
@Composable
fun PreviewAuthCompletionApprovedDialog() {
    var showDialog by remember { mutableStateOf(true) }
    if (showDialog) {
        MSATextDialog(
            message = stringResource(R.string.auth_completion_approved),
            buttonText = stringResource(R.string.confirm_button),
            onDismiss = { showDialog = false },
        )
    }
}


//--------------------------------------------------------------------------
@Composable
fun CurrencySelectionDialog(
    hkdAmount: String,
    usdAmount: String,
    onDismiss: () -> Unit,
    onCurrencySelected: (String) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MsaTheme.spacing.md),
            shape = MsaTheme.shapes.medium,
            colors = CardDefaults.cardColors(containerColor = MsaTheme.colors.background),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(MsaTheme.spacing.md),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(R.string.select_currency),
                    style = MsaTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MsaTheme.colors.foreground,
                    modifier = Modifier.padding(bottom = MsaTheme.spacing.md)
                )

                // HKD to USD conversion display
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = MsaTheme.spacing.sm),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "HKD",
                        style = MsaTheme.typography.bodyLarge,
                        color = MsaTheme.colors.accentForeground
                    )
                    Spacer(Modifier.width(MsaTheme.spacing.sm))
                    Image(
                        painter = painterResource(R.drawable.convert),
                        contentDescription = "Convert",
                        modifier = Modifier.size(MsaTheme.iconSize.xs),
                        colorFilter = ColorFilter.tint(MsaTheme.colors.mutedForeground)
                    )
                    Spacer(Modifier.width(MsaTheme.spacing.sm))
                    Text(
                        text = "USD",
                        style = MsaTheme.typography.bodyLarge,
                        color = MsaTheme.colors.accentForeground
                    )
                }

                Spacer(Modifier.height(MsaTheme.spacing.md))

                // HKD Amount
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Image(
                        painter = painterResource(R.drawable.help),
                        contentDescription = "HongKong Flag",
                        modifier = Modifier.size(MsaTheme.iconSize.sm)
                    )
                    Spacer(Modifier.width(MsaTheme.spacing.xs))
                    Text(
                        text = "HKD $hkdAmount",
                        style = MsaTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        color = MsaTheme.colors.foreground
                    )
                }

                Spacer(Modifier.height(MsaTheme.spacing.md))

                // FX Rate information
                Text(
                    text = stringResource(R.string.fx_rate_message, "1HKD", "0.1275USD"),
                    style = MsaTheme.typography.bodyMedium,
                    color = MsaTheme.colors.accentForeground,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = stringResource(R.string.markup_included_message, "3.20%"),
                    style = MsaTheme.typography.bodyMedium,
                    color = MsaTheme.colors.accentForeground,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(MsaTheme.spacing.md))

                // USD Amount
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Image(
                        painter = painterResource(R.drawable.help),
                        contentDescription = "United States Flag",
                        modifier = Modifier.size(MsaTheme.iconSize.sm)
                    )
                    Spacer(Modifier.width(MsaTheme.spacing.xs))
                    Text(
                        text = "USD $usdAmount",
                        style = MsaTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        color = MsaTheme.colors.foreground
                    )
                }

                Spacer(Modifier.height(MsaTheme.spacing.md))

                // Currency selection buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    MSAButton(
                        text = "HKD",
                        onClick = { onCurrencySelected("HKD") },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = MsaTheme.spacing.xs2)
                            .height(MsaTheme.minTouchSize.sm)
                            .fillMaxWidth(),
                    )
                    MSAButton(
                        text = "USD",
                        onClick = { onCurrencySelected("USD") },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = MsaTheme.spacing.xs2)
                            .height(MsaTheme.minTouchSize.sm)
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewCurrencySelectionDialog() {
    CurrencySelectionDialog(
        hkdAmount = "195.30",
        usdAmount = "25.00",
        onDismiss = {},
        onCurrencySelected = {}
    )
}


@Composable
fun LogoutConfirmationDialog(
    onCancelClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Dialog(onDismissRequest = onCancelClick) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MsaTheme.shapes.medium)
                .background(MsaTheme.colors.background)
                .padding(MsaTheme.spacing.lg),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = stringResource(R.string.logout_confirmation_message),
                style = MsaTheme.typography.bodySmall,
                color = MsaTheme.colors.accentForeground,
                modifier = Modifier.padding(bottom = MsaTheme.spacing.lg)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
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
                        text = stringResource(R.string.cancel_button),
                        style = MsaTheme.typography.bodyLarge
                    )
                }

                Button(
                    onClick = onLogoutClick,
                    modifier = Modifier
                        .weight(1f)
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
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = MsaTheme.colors.primaryForeground
                    )
                ) {
                    Text(
                        text = stringResource(R.string.logout_button),
                        style = MsaTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
fun ConfirmEmailDialog(
    email: String,
    onDismissRequest: () -> Unit,
    onEditClick: () -> Unit,
    onConfirmClick: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MsaTheme.shapes.medium)
                .background(MsaTheme.colors.background)
                .padding(MsaTheme.spacing.lg),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = stringResource(R.string.confirm_email),
                style = MsaTheme.typography.titleMedium,
                color = MsaTheme.colors.foreground,
                modifier = Modifier.padding(bottom = MsaTheme.spacing.sm)
            )
            Text(
                text = stringResource(R.string.email_info_message, "\n$email"),
                style = MsaTheme.typography.bodySmall,
                color = MsaTheme.colors.accentForeground,
                modifier = Modifier.padding(bottom = MsaTheme.spacing.lg)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MsaTheme.spacing.md)
            ) {

                Button(
                    onClick = onEditClick,
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
                        text = stringResource(R.string.edit_button),
                        style = MsaTheme.typography.bodyLarge
                    )
                }

                Button(
                    onClick = onConfirmClick,
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    MsaTheme.colors.primaryGradient,
                                    MsaTheme.colors.primary
                                )
                            ),
                            shape = MsaTheme.shapes.medium
                        )
                        .height(MsaTheme.minTouchSize.sm),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = MsaTheme.colors.primaryForeground
                    ),
                ) {
                    Text(
                        text = stringResource(R.string.confirm_button),
                        style = MsaTheme.typography.bodyLarge
                    )
                }
            }

        }
    }
}

@Composable
fun EnableBiometricDialog(
    onDismissRequest: () -> Unit,
    onCancelClick: () -> Unit,
    onEnableClick: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MsaTheme.shapes.medium)
                .background(MsaTheme.colors.background)
                .padding(MsaTheme.spacing.lg),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = stringResource(R.string.enable_login_with_biometric),
                style = MsaTheme.typography.titleSmall,
                color = MsaTheme.colors.foreground,
                modifier = Modifier.padding(bottom = MsaTheme.spacing.sm)
            )
            Text(
                text = stringResource(R.string.biometric_login_message),
                style = MsaTheme.typography.bodySmall,
                color = MsaTheme.colors.accentForeground,
                modifier = Modifier.padding(bottom = MsaTheme.spacing.lg)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = MsaTheme.spacing.lg),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(MsaTheme.iconSize.xl2)
                        .background(MsaTheme.colors.input.copy(alpha = 0.4f), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.fingerprint_icon_button),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MsaTheme.colors.background)
                    )
                }
                Spacer(Modifier.width(MsaTheme.spacing.lg))
                Box(
                    modifier = Modifier
                        .size(MsaTheme.iconSize.xl2)
                        .background(MsaTheme.colors.input.copy(alpha = 0.4f), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.face_auth_icon),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MsaTheme.colors.background)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
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
                        contentColor = MsaTheme.colors.primary,

                        ),
                    border = BorderStroke(1.dp, MsaTheme.colors.primary)
                ) {
                    Text(
                        text = "Cancel",
                        style = MsaTheme.typography.bodyLarge
                    )
                }

                Button(
                    onClick = onEnableClick,
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    MsaTheme.colors.primaryGradient,
                                    MsaTheme.colors.primary
                                )
                            ),
                            shape = MsaTheme.shapes.medium
                        )
                        .height(MsaTheme.minTouchSize.sm),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = MsaTheme.colors.primaryForeground
                    ),
                ) {
                    Text(
                        text = "Enable",
                        style = MsaTheme.typography.bodyLarge
                    )
                }

            }
        }
    }
}

@Preview
@Composable
fun PreviewLogoutConfirmationDialog() {
    LogoutConfirmationDialog(
        onCancelClick = { },
        onLogoutClick = { }
    )
}

@Preview
@Composable
fun PreviewConfirmEmailDialog() {

    ConfirmEmailDialog(
        email = "information223500201@qq.com",
        onDismissRequest = { },
        onEditClick = { },
        onConfirmClick = { }
    )

}

@Preview
@Composable
fun PreviewEnableBiometricDialog() {

    EnableBiometricDialog(
        onDismissRequest = { },
        onCancelClick = { },
        onEnableClick = { }
    )

}

@Composable
fun FingerprintScanDialog(
    onDismissRequest: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = true,
            dismissOnBackPress = true
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.fingerprint),
                contentDescription = "Fingerprint scan icon",
                modifier = Modifier.size(MsaTheme.animationSize.sm),
//                colorFilter = ColorFilter.tint(MsaTheme.colors.background)
            )
            Spacer(Modifier.height(MsaTheme.spacing.lg))
            Text(
                stringResource(R.string.click_to_log_in),
                style = MsaTheme.typography.titleSmall,
                color = MsaTheme.colors.background
            )
        }
    }
}

@Preview
@Composable
fun PreviewFingerprintScanDialog() {
    FingerprintScanDialog(
        onDismissRequest = { },
    )
}

@Composable
fun FaceIDDialog(
    onDismissRequest: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = true,
            dismissOnBackPress = true
        )
    ) {
        Column(
            modifier = Modifier
                .clip(MsaTheme.shapes.large)
                .background(Color.Black, shape = RectangleShape)
                .size(MsaTheme.animationSize.sm),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.face_auth_icon),
                contentDescription = "Fingerprint scan icon",
                modifier = Modifier.size(MsaTheme.iconSize.xl3),
            )
            Spacer(Modifier.height(MsaTheme.spacing.md))
            Text(
                stringResource(R.string.face_id),
                style = MsaTheme.typography.titleSmall,
                color = Color.White
            )
        }
    }
}

@Preview
@Composable
fun PreviewFaceIDDialog() {
    FaceIDDialog(
        onDismissRequest = { },
    )
}


@Composable
fun FaceNotRecognizedDialog(
    onDismissRequest: () -> Unit,
    onTryAgainClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MsaTheme.shapes.medium)
                .background(MsaTheme.colors.background)
                .padding(horizontal = MsaTheme.spacing.md)
                .padding(top = MsaTheme.spacing.lg, bottom = MsaTheme.spacing.sm),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.face_auth_icon),
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .padding(bottom = MsaTheme.spacing.md),
                colorFilter = ColorFilter.tint(MsaTheme.colors.input)
            )
            Text(
                text = stringResource(R.string.face_not_recognized),
                style = MsaTheme.typography.titleSmall,
                color = MsaTheme.colors.foreground,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(R.string.try_again),
                style = MsaTheme.typography.titleSmall,
                color = MsaTheme.colors.foreground,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = MsaTheme.spacing.lg)
            )
            HorizontalDivider(
                thickness = 1.dp,
                color = MsaTheme.colors.input.copy(alpha = 0.4f)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = MsaTheme.spacing.xs)
                    .clip(MsaTheme.shapes.medium)
                    .background(MsaTheme.colors.background)
                    .clickable { onTryAgainClick() },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.try_face_id_again),
                    style = MsaTheme.typography.titleSmall,
                    color = MsaTheme.colors.primary
                )
            }
            HorizontalDivider(
                thickness = 1.dp,
                color = MsaTheme.colors.input.copy(alpha = 0.4f)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = MsaTheme.spacing.xs)
                    .clip(MsaTheme.shapes.medium)
                    .background(MsaTheme.colors.background)
                    .clickable { onCancelClick() },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.cancel_button),
                    style = MsaTheme.typography.titleSmall,
                    color = MsaTheme.colors.error
                )
            }
        }
    }
}


@Preview
@Composable
fun PreviewFaceNotRecognizedDialog() {

    FaceNotRecognizedDialog(
        onDismissRequest = { },
        onTryAgainClick = { },
        onCancelClick = { }
    )

}
