package com.minesec.msav3opensource.ui.helper.template

import adaptiveSpacingHeight
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.minesec.msav3opensource.R
import com.minesec.msav3opensource.ui.models.BottomNavTab
import com.minesec.msav3opensource.ui.template.getDynamicFontSize
import com.minesec.msav3opensource.ui.theme.MsaTheme
import com.theminesec.multiplatform.msa_core.common.domain.models.MSAException
import com.theminesec.multiplatform.msa_core.util.amount.DisplayAmountFormat

@Composable
fun MSAConfirmCancelButtons(
    confirmText: String,
    cancelText: String,
    onConfirmClick: () -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            onClick = onCancelClick,
            modifier = Modifier.weight(1f)
        ) { Text(cancelText) }
        Button(
            onClick = onConfirmClick,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = MsaTheme.colors.primary)
        ) { Text(confirmText) }
    }
}



@Composable
fun PaymentCardInfo(
    cardBrand: String?, cardLastDigits: String?,
    cardTypeIcon: Int,
    modifier: Modifier = Modifier,
    exception: MSAException? = null,
    backgroundColor: Color = MsaTheme.colors.highlightCard,
    textColorRow2: Color = MsaTheme.colors.mutedForeground
) {
    Box(
        modifier = modifier
            .padding(vertical = MsaTheme.spacing.md)
            .background(
                color = backgroundColor,
                shape = MsaTheme.shapes.large
            )
    ) {
        if (exception != null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MsaTheme.spacing.md),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Transaction Failed",
                    color = Color.Black,
                    style = MsaTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                )


                Spacer(modifier = Modifier.width(MsaTheme.spacing.sm))
                Text(
                    text = exception.errorMessage ?: exception.message
                    ?: stringResource(R.string.unknow_error_occurred),
                    color = MsaTheme.colors.foreground,
                    style = MsaTheme.typography.bodySmall,
                )
            }
        } else {
            cardBrand?.let {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MsaTheme.spacing.md),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(cardTypeIcon),
                        contentDescription = "$cardBrand card",
                        modifier = Modifier.size(MsaTheme.iconSize.xl2)
                    )

                    Spacer(modifier = Modifier.width(MsaTheme.spacing.sm))
                    Text(
                        text = cardLastDigits ?: "",
                        color = textColorRow2,
                        style = MsaTheme.typography.titleSmall,
                    )
                }
            }
        }
    }
}

@Preview()
@Composable
fun PaymentCardInfoPreview() {
    PaymentCardInfo(
        cardBrand = "Mastercard",
        cardLastDigits = "5678",
        cardTypeIcon = R.drawable.mastercard
    )
}


@Composable
fun MSABottomBar(
    selectedItem: BottomNavTab,
    onBottomBarNavigate: (BottomNavTab) -> Unit,
    modifier: Modifier = Modifier,
    style: TextStyle = MsaTheme.typography.labelLarge,
    iconSize: Dp = MsaTheme.iconSize.md,
    textColorSelected: Color = MsaTheme.colors.primary,
    textColorUnselected: Color = MsaTheme.colors.accentForeground,
    iconColorSelected: Color = MsaTheme.colors.primary,
    iconColorUnselected: Color = MsaTheme.colors.accentForeground,
    backgroundColor: Color = MsaTheme.colors.background,
    indicatorColor: Color = Color.Transparent,
    tabs: List<BottomNavTab> = BottomNavTab.entries, // default list
) {
    NavigationBar(
        modifier = modifier,
        containerColor = backgroundColor,
    ) {
        tabs.forEach { tab ->
            val title = stringResource(tab.titleResId)
            val selected = selectedItem == tab
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = if (selected) painterResource(tab.iconResIdSurface) else painterResource(
                            tab.iconResId
                        ),
                        contentDescription = title,
                        modifier = Modifier.size(iconSize)
                    )
                },
                label = {
                    Text(
                        text = title,
                        style = style,
                    )
                },
                selected = selectedItem == tab,
                onClick = {
                    if (selectedItem != tab) onBottomBarNavigate(tab)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = iconColorSelected,
                    unselectedIconColor = iconColorUnselected,
                    selectedTextColor = textColorSelected,
                    unselectedTextColor = textColorUnselected,
                    indicatorColor = indicatorColor,
                )
            )
        }
    }
}


data class BottomNavItem(
    val title: String,
    val iconResId: Int,
)

@Preview
@Composable
fun PreviewBottomBar() {
    MSABottomBar(
        selectedItem = BottomNavTab.Payment,
        onBottomBarNavigate = {}
    )
}

@Composable
fun MSASwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = modifier,
            enabled = enabled,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MsaTheme.colors.background,
                checkedTrackColor = MsaTheme.colors.primary,
                uncheckedThumbColor = MsaTheme.colors.background,
                uncheckedTrackColor = MsaTheme.colors.input,
                uncheckedBorderColor = MsaTheme.colors.input
//            disabledCheckedThumbColor = MsaTheme.colors.primary.copy(alpha = 0.3f),
//            disabledCheckedTrackColor = MsaTheme.colors.primary.copy(alpha = 0.12f),
//            disabledUncheckedThumbColor = MsaTheme.colors.background.copy(alpha = 0.3f),
            )
        )
    }
}

@Preview
@Composable
fun PreviewMSASwitch() {
    Column(modifier = Modifier.wrapContentSize()) {
        var isSwitchOn by remember { mutableStateOf(true) }
        MSASwitch(
            checked = isSwitchOn,
            onCheckedChange = { newValue ->
                isSwitchOn = newValue
                println("Switch state changed to: $newValue")
            }
        )
        var isSwitchOff by remember { mutableStateOf(false) }
        MSASwitch(
            checked = isSwitchOff,
            onCheckedChange = { newValue ->
                isSwitchOn = newValue
                println("Switch state changed to: $newValue")
            }
        )
    }
}


@Composable
fun TransactionDetailItem(
    label: Int,
    value: String,
    clipIconVisible: Boolean = false,
    onCopyClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier.clickable {
            onCopyClick(value)
        },
    ) {
        Text(
            text = stringResource(label),
            color = MsaTheme.colors.mutedForeground,
            style = MsaTheme.typography.labelSmall,
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = value,
                color = MsaTheme.colors.foreground,
                style = MsaTheme.typography.bodyMedium,
            )
            if (clipIconVisible) Icon(
                painter = painterResource(R.drawable.copy),
                "",
                tint = MsaTheme.colors.primary,
                modifier = modifier
                    .padding(5.dp)
                    .clickable {
                        onCopyClick(value)
                    })
        }
    }
}


@Preview
@Composable
fun PreviewTransactionDetailItem() {
    TransactionDetailItem(
        label = R.string.tsi,
        value = "100027"
    )

}


@Composable
fun AmountDisplay(
    currency: String, amount: String,
) {
    val dynamicFontSize = getDynamicFontSize(DisplayAmountFormat.formatAmount(amount, currency))

    Row(
        modifier = Modifier.height(MsaTheme.spacing.xl2),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = currency,
            color = Color.Black,
            style = MsaTheme.typography.labelMedium.copy(
                fontSize = dynamicFontSize,
                fontWeight = FontWeight.Bold
            ),
        )
        Text(
            text = DisplayAmountFormat.formatAmount(amount, currency),
            color = Color.Black,
            style = MsaTheme.typography.labelMedium.copy(
                fontSize = dynamicFontSize,
                fontWeight = FontWeight.Bold
            )
        )
    }
    Spacer(Modifier.height(adaptiveSpacingHeight(MsaTheme.spacing.xs)))
}

@Preview
@Composable
fun PreviewAmountDisplay() {
    AmountDisplay(
        currency = "HKD",
        amount = "0.00"
    )
}


@Composable
fun SettingsItem(
    icon: Painter?,
    iconOriginalColor: Boolean = false,
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    clickable: Boolean = true,
    endContent: @Composable (() -> Unit)? = null // optional
) {
    Column(modifier = modifier
        .background(MsaTheme.colors.background)
        .fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .then(if (clickable) Modifier.clickable(onClick = onClick) else Modifier)//.clickable(onClick = onClick)
                .padding(vertical = MsaTheme.spacing.md, horizontal = MsaTheme.spacing.md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (icon != null) {
                    Icon(
                        painter = icon,
                        contentDescription = null,
                        modifier = Modifier.size(MsaTheme.iconSize.sm),
                        tint = if (iconOriginalColor) Color.Unspecified else MsaTheme.colors.primary
                    )
                    Spacer(Modifier.width(MsaTheme.spacing.md))
                }
                Text(
                    text = title,
                    style = MsaTheme.typography.bodySmall,
                    color = MsaTheme.colors.foreground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            endContent?.invoke() // Display optional end content
        }
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MsaTheme.spacing.md),
            color = MsaTheme.colors.ring.copy(alpha = 0.4f),
            thickness = 1.dp
        )
    }
}

@Composable
fun KeyValueRow(
    key: Int,
    value: String,
    modifier: Modifier = Modifier
) {
    println("KeyValueRow → ${stringResource(key)} = $value")

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = MsaTheme.spacing.sm)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Start (key) label
            Text(
                text = stringResource(key),
                style = MsaTheme.typography.labelMedium,
                color = MsaTheme.colors.foreground,
                modifier = Modifier
                    .padding(end = MsaTheme.spacing.sm)
                    .alignByBaseline()
            )

            // Value label — this one should always show
            Text(
                text = value,
                style = MsaTheme.typography.labelSmall,
                color = MsaTheme.colors.foreground,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .weight(1f)
                    .alignByBaseline(),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = MsaTheme.spacing.sm),
            color = MsaTheme.colors.accent,
            thickness = 1.dp
        )
    }
}


@Preview
@Composable
fun PreviewSettingsItem() {
    LazyColumn(
        modifier = Modifier
            .background(MsaTheme.colors.accent)
            .wrapContentSize()
    ) {
        item {
            SettingsItem(
                icon = painterResource(R.drawable.payment_method_icon),
                title = "Payment method",
                onClick = {}, // clickable
                endContent = {
                    Icon(
                        painter = painterResource(R.drawable.arrow_right),
                        contentDescription = null,
                        modifier = Modifier.size(MsaTheme.iconSize.xs),
                        tint = MsaTheme.colors.accentForeground
                    )
                }
            )
        }
        item {
            SettingsItem(
                icon = null,
                title = "MID",
                onClick = { },
                clickable = false, //not clickable, will not call the lambda above
                endContent = {
                    Text(
                        text = "M1744179436",
                        style = MsaTheme.typography.bodyMedium,
                        color = MsaTheme.colors.foreground
                    )
                }
            )
        }
    }
}

@Composable
fun MsaOutlinedTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    label: String, // Text above the textfield
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    trailingIcon: @Composable (() -> Unit)? = null, // Optional trailing icon
    isError: Boolean = false, // error styling
    errorMessage: String? = null, // Text displayed below textfield
    isProcessing: Boolean = false, // enabled state (disabled when processing)
    singleLine: Boolean = true,
    textStyle: TextStyle = MsaTheme.typography.bodySmall,
    visualTransformation: VisualTransformation = VisualTransformation.None // password masking ,etc
) {
    val enabled = !isProcessing

    Column(modifier = modifier.background(MsaTheme.colors.background)) {
        Text(
            text = label,
            style = MsaTheme.typography.bodySmall,
            color = MsaTheme.colors.foreground
        )
        Spacer(Modifier.height(MsaTheme.spacing.xs2))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            modifier = Modifier.fillMaxWidth(),
            textStyle = textStyle,
            trailingIcon = trailingIcon,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            isError = isError,
            visualTransformation = visualTransformation,
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = MsaTheme.colors.input,
                focusedIndicatorColor = MsaTheme.colors.approval,
                unfocusedContainerColor = MsaTheme.colors.background,
                focusedContainerColor = MsaTheme.colors.approvalForeground,
                errorContainerColor = MsaTheme.colors.errorForeground,
                errorIndicatorColor = MsaTheme.colors.error,
                focusedPlaceholderColor = MsaTheme.colors.foreground,
                disabledContainerColor = MsaTheme.colors.muted,
                disabledIndicatorColor = MsaTheme.colors.input,
                disabledTextColor = MsaTheme.colors.mutedForeground,
                cursorColor = Color.Black,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )
        if (isError && !errorMessage.isNullOrBlank()) {
            Spacer(Modifier.height(MsaTheme.spacing.xs2))
            Text(
                text = errorMessage,
                style = MsaTheme.typography.labelMedium,
                color = MsaTheme.colors.error
            )
        }
    }
}

@Preview
@Composable
fun PreviewMsaOutlinedTextField() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MsaTheme.colors.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = MsaTheme.spacing.md)
    ) {

        var emailValue by remember { mutableStateOf(TextFieldValue("")) }
        var isEmailError by remember { mutableStateOf(false) }
        var isEmailProcessing by remember { mutableStateOf(false) }

        // State for Password field
        var passwordValue by remember { mutableStateOf(TextFieldValue("")) }
        var isPasswordError by remember { mutableStateOf(false) }
        var passwordVisible by remember { mutableStateOf(false) }
        var isPasswordProcessing by remember { mutableStateOf(false) }

        // State for Confirm Password field
        var confirmPasswordValue by remember { mutableStateOf(TextFieldValue("")) }
        var isConfirmPasswordError by remember { mutableStateOf(false) }
        var confirmPasswordVisible by remember { mutableStateOf(false) }
        var isConfirmPasswordProcessing by remember { mutableStateOf(false) }


        MsaOutlinedTextField(
            value = emailValue,
            onValueChange = {
                emailValue = it
                // Logic to clear error if user starts typing after an error
                if (isEmailError) isEmailError = false
            },
            label = "Email",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            trailingIcon = {
                IconButton(onClick = { }) {
                    Icon(
                        painter = painterResource(R.drawable.dropdown),
                        contentDescription = "Dropdown"
                    )
                }
            },
            isError = isEmailError,
            errorMessage = if (isEmailError) "Invalid email" else null,
            isProcessing = isEmailProcessing
        )

        Spacer(Modifier.height(MsaTheme.spacing.lg))

        MsaOutlinedTextField(
            value = passwordValue,
            onValueChange = {
                passwordValue = it
                if (isPasswordError) isPasswordError = false
            },
            label = "Password",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image =
                    if (passwordVisible) R.drawable.show_password else R.drawable.hide_password
                val description = if (passwordVisible) "Show password" else "Hide password"
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Image(painter = painterResource(image), contentDescription = description)
                }
            },
            isError = isPasswordError,
            errorMessage = if (isPasswordError) "Your password should have 8+ characters." else "Your password should have 8+ characters.",
            isProcessing = isPasswordProcessing
        )

        Spacer(Modifier.height(MsaTheme.spacing.lg))

        MsaOutlinedTextField(
            value = confirmPasswordValue,
            onValueChange = {
                confirmPasswordValue = it
                if (isConfirmPasswordError) isConfirmPasswordError = false
            },
            label = "Confirm password",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image =
                    if (confirmPasswordVisible) R.drawable.show_password else R.drawable.hide_password
                val description =
                    if (confirmPasswordVisible) "Show password" else "Hide password"
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Image(painter = painterResource(image), contentDescription = description)
                }
            },
            isError = isConfirmPasswordError,
            errorMessage = if (isConfirmPasswordError) "Passwords do not match." else null,
            isProcessing = isConfirmPasswordProcessing
        )
    }
}
