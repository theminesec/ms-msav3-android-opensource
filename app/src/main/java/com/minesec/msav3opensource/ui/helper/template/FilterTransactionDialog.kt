package com.minesec.msav3opensource.ui.helper.template

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minesec.msav3opensource.R
import com.minesec.msav3opensource.ui.helper.items.MSAButton
import com.minesec.msav3opensource.ui.helper.items.MSAButtonStyle
import com.minesec.msav3opensource.ui.theme.MsaTheme
import kotlinx.coroutines.launch

@Composable
fun FilterSection(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier = modifier) {
        content()
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownFilter(
    selectedValue: String,
    onValueChange: (String) -> Unit,
    options: List<String>,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    FilterSection(modifier = modifier) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedValue,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    Image(
                        painter = painterResource(R.drawable.dropdown),
                        contentDescription = "Dropdown arrow",
                        modifier = Modifier.size(MsaTheme.iconSize.md)
                    )
                },
                placeholder = {
                    Text(
                        text = placeholder,
                        color = MsaTheme.colors.input,
                        style = MsaTheme.typography.bodySmall
                    )
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = MsaTheme.colors.ring,
                    focusedIndicatorColor = MsaTheme.colors.primary,
                    unfocusedContainerColor = MsaTheme.colors.background,
                    focusedContainerColor = MsaTheme.colors.background,
                    focusedTextColor = MsaTheme.colors.foreground,
                    unfocusedTextColor = MsaTheme.colors.foreground
                )
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                containerColor = MsaTheme.colors.background,
                modifier = Modifier.background(MsaTheme.colors.background)
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onValueChange(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultiSelectDropdownFilter(
    label: String,
    selectedValues: Set<String>,
    onValuesChange: (Set<String>) -> Unit,
    options: List<String>,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    FilterSection(modifier = modifier) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            val displayValue = if (selectedValues.isEmpty()) {
                placeholder
            } else {
                selectedValues.joinToString(", ")
            }

            OutlinedTextField(
                value = displayValue,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    Image(
                        painter = painterResource(R.drawable.dropdown),
                        contentDescription = "Dropdown arrow",
                        modifier = Modifier.size(MsaTheme.iconSize.md)
                    )
                },
                placeholder = {
                    Text(
                        text = placeholder,
                        color = MsaTheme.colors.input,
                        style = MsaTheme.typography.bodySmall
                    )
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = MsaTheme.colors.ring,
                    focusedIndicatorColor = MsaTheme.colors.primary,
                    unfocusedContainerColor = MsaTheme.colors.background,
                    focusedContainerColor = MsaTheme.colors.background,
                    focusedTextColor = MsaTheme.colors.foreground,
                    unfocusedTextColor = MsaTheme.colors.foreground
                )
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                containerColor = MsaTheme.colors.background,
                modifier = Modifier.background(MsaTheme.colors.background)
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Checkbox(
                                    checked = option in selectedValues,
                                    onCheckedChange = { isChecked ->
                                        onValuesChange(
                                            if (isChecked) {
                                                selectedValues + option
                                            } else {
                                                selectedValues - option
                                            }
                                        )
                                    },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = MsaTheme.colors.primary,
                                        uncheckedColor = MsaTheme.colors.input,
                                        checkmarkColor = Color.White
                                    )
                                )
                                Spacer(Modifier.width(MsaTheme.spacing.xs))
                                Text(text = option)
                            }
                        },
                        onClick = {
                            onValuesChange(
                                if (option in selectedValues) {
                                    selectedValues - option
                                } else {
                                    selectedValues + option
                                }
                            )
                        },
                        enabled = true,
                        contentPadding = PaddingValues(0.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AmountRangeFilter(
    minAmount: TextFieldValue,
    onMinAmountChange: (TextFieldValue) -> Unit,
    maxAmount: TextFieldValue,
    onMaxAmountChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MsaTheme.spacing.md),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$",
            style = MsaTheme.typography.bodySmall,
            color = MsaTheme.colors.accentForeground,
        )
        OutlinedTextField(
            value = minAmount,
            onValueChange = onMinAmountChange,
            modifier = Modifier.weight(1f),
            textStyle = MsaTheme.typography.bodySmall,
            placeholder = {
                Text(
                    text = stringResource(R.string.min_value),
                    color = MsaTheme.colors.input,
                    style = MsaTheme.typography.bodySmall
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = MsaTheme.colors.input,
                focusedIndicatorColor = MsaTheme.colors.primary,
                unfocusedContainerColor = MsaTheme.colors.background,
                focusedContainerColor = MsaTheme.colors.background,
                focusedTextColor = MsaTheme.colors.foreground,
                unfocusedTextColor = MsaTheme.colors.foreground
            )
        )

        Text(
            text = "-",
            style = MsaTheme.typography.headlineSmall,
            color = MsaTheme.colors.mutedForeground,
        )

        OutlinedTextField(
            value = maxAmount,
            onValueChange = onMaxAmountChange,
            modifier = Modifier.weight(1f),
            textStyle = MsaTheme.typography.bodySmall,
            placeholder = {
                Text(
                    text = stringResource(R.string.max_value),
                    color = MsaTheme.colors.input,
                    style = MsaTheme.typography.bodySmall
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = MsaTheme.colors.input,
                focusedIndicatorColor = MsaTheme.colors.primary,
                unfocusedContainerColor = MsaTheme.colors.background,
                focusedContainerColor = MsaTheme.colors.background,
                focusedTextColor = MsaTheme.colors.foreground,
                unfocusedTextColor = MsaTheme.colors.foreground
            )
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterTransactionBottomSheet(
    showBottomSheet: Boolean,
    onDismissRequest: () -> Unit,
    onApplyFilters: (
        transactionId: String,
        amountMin: String,
        amountMax: String,
        saleType: String,
        status: String,
        paymentMethods: Set<String>,
        dateRange: String
    ) -> Unit,
    onResetFilters: () -> Unit
) {
    if (!showBottomSheet) return // Only show if showBottomSheet is true

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()


    var transactionId by remember { mutableStateOf(TextFieldValue("")) }
    var amountMin by remember { mutableStateOf(TextFieldValue("")) }
    var amountMax by remember { mutableStateOf(TextFieldValue("")) }
    var selectedSaleOption by remember { mutableStateOf("Sale") }
    var selectedStatusOption by remember { mutableStateOf("All") }
    val allPaymentMethods = remember { listOf("Visa", "Mastercard", "UnionPay", "Amex", "JCB") }
    var selectedPaymentMethods by remember { mutableStateOf(setOf("Visa", "Mastercard")) }
    var selectedDateRangeOption by remember { mutableStateOf("Date Range") }

    ModalBottomSheet(
        onDismissRequest = {
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                if (!sheetState.isVisible) {
                    onDismissRequest()
                }
            }
        },
        sheetState = sheetState,
        dragHandle = {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(
                    Modifier
                        .width(32.dp)
                        .height(4.dp)
                        .background(Color.Gray, RoundedCornerShape(100))
                )
            }
        },
        containerColor = MsaTheme.colors.background,
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier
                .padding(vertical = MsaTheme.spacing.lg, horizontal = MsaTheme.spacing.md)
                .verticalScroll(rememberScrollState()) // Keep scrollable if content is long
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.filter_transaction),
                    style = MsaTheme.typography.titleSmall,
                    color = MsaTheme.colors.foreground
                )
                IconButton(onClick = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            onDismissRequest() // close button , close sheet
                        }
                    }
                }) {
                    Icon(
                        painter = painterResource(R.drawable.close),
                        contentDescription = "Close",
                        tint = MsaTheme.colors.foreground
                    )
                }
            }

            Spacer(Modifier.height(MsaTheme.spacing.lg))

            FilterSection {
                OutlinedTextField(
                    value = transactionId,
                    onValueChange = { transactionId = it },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = MsaTheme.typography.bodySmall,
                    placeholder = {
                        Text(
                            text = stringResource(R.string.transaction_id),
                            color = MsaTheme.colors.input,
                            style = MsaTheme.typography.bodySmall
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { transactionId = TextFieldValue("") }) {
                            Icon(
                                painter = painterResource(R.drawable.close),
                                contentDescription = "Clear",
                                tint = MsaTheme.colors.foreground
                            )
                        }
                    },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        unfocusedIndicatorColor = MsaTheme.colors.input,
                        focusedIndicatorColor = MsaTheme.colors.primary,
                        unfocusedContainerColor = MsaTheme.colors.background,
                        focusedContainerColor = MsaTheme.colors.background,
                        focusedTextColor = MsaTheme.colors.foreground,
                        unfocusedTextColor = MsaTheme.colors.foreground
                    )
                )
            }

            Spacer(Modifier.height(MsaTheme.spacing.lg))

            AmountRangeFilter(
                minAmount = amountMin,
                onMinAmountChange = { amountMin = it },
                maxAmount = amountMax,
                onMaxAmountChange = { amountMax = it }
            )

            Spacer(Modifier.height(MsaTheme.spacing.lg))

            DropdownFilter(
                selectedValue = selectedSaleOption,
                onValueChange = { selectedSaleOption = it },
                options = listOf("Sale", "Refund", "Pre-Auth"),
                placeholder = stringResource(R.string.type_filter)
            )

            Spacer(Modifier.height(MsaTheme.spacing.lg))

            DropdownFilter(
                selectedValue = selectedStatusOption,
                onValueChange = { selectedStatusOption = it },
                options = listOf("All", "Approved", "Declined", "Pending"),
                placeholder = stringResource(R.string.status_filter)
            )

            Spacer(Modifier.height(MsaTheme.spacing.lg))

            MultiSelectDropdownFilter(
                label = "Payment",
                selectedValues = selectedPaymentMethods,
                onValuesChange = { selectedPaymentMethods = it },
                options = allPaymentMethods,
                placeholder = stringResource(R.string.select_payments)
            )

            Spacer(Modifier.height(MsaTheme.spacing.lg))

            FilterSection {
                OutlinedTextField(
                    value = selectedDateRangeOption,
                    onValueChange = { selectedDateRangeOption = it },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = MsaTheme.typography.bodySmall,
                    placeholder = {
                        Text(
                            text = stringResource(R.string.date_range),
                            color = MsaTheme.colors.input,
                            style = MsaTheme.typography.bodySmall
                        )
                    },
                    trailingIcon = {
                        Image(
                            painter = painterResource(R.drawable.dropdown),
                            contentDescription = "Date picker",
                            modifier =  Modifier.size(MsaTheme.iconSize.md)
                        )
                    },
                    singleLine = true,
                    readOnly = true,
                    colors = TextFieldDefaults.colors(
                        unfocusedIndicatorColor = MsaTheme.colors.ring,
                        focusedIndicatorColor = MsaTheme.colors.primary,
                        unfocusedContainerColor = MsaTheme.colors.background,
                        focusedContainerColor = MsaTheme.colors.background,
                        focusedTextColor = MsaTheme.colors.foreground,
                        unfocusedTextColor = MsaTheme.colors.foreground
                    )
                )
            }

            Spacer(Modifier.height(MsaTheme.spacing.xl))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MsaTheme.spacing.md)
            ) {
                MSAButton(
                    text = stringResource(R.string.reset_button),
                    onClick = {
                        transactionId = TextFieldValue("")
                        amountMin = TextFieldValue("")
                        amountMax = TextFieldValue("")
                        selectedSaleOption = "Sale"
                        selectedStatusOption = "All"
                        selectedPaymentMethods = setOf()
                        selectedDateRangeOption = "Date Range"
                        onResetFilters()
                    },
                    defaultButtonStyle = MSAButtonStyle.OUTLINED,
                    backgroundColor = MsaTheme.colors.primary,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
                MSAButton(
                    text = stringResource(R.string.search_button),
                    onClick = {
                        onApplyFilters(
                            transactionId.text,
                            amountMin.text,
                            amountMax.text,
                            selectedSaleOption,
                            selectedStatusOption,
                            selectedPaymentMethods,
                            selectedDateRangeOption
                        )

                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                onDismissRequest()
                            }
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewFilterTransactionBottomSheet() {
    var showSheet by remember { mutableStateOf(true) }

    if (showSheet) {
        FilterTransactionBottomSheet(
            showBottomSheet = showSheet,
            onDismissRequest = { showSheet = false },
            onApplyFilters = { _, _, _, _, _, _, _ -> },
            onResetFilters = {}
        )
    }

}