@file:OptIn(ExperimentalMaterial3Api::class)

package com.minesec.msav3opensource.ui.screens

import adaptive
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minesec.msav3opensource.ui.models.capitalizeFirstChar
import com.minesec.msav3opensource.ui.models.getPaymentSchemaIconRes
import com.minesec.msav3opensource.ui.template.MsaUiStateHandler
import com.theminesec.multiplatform.msa_core.feature.common.data.models.TransactionType
import com.theminesec.multiplatform.msa_core.feature.common.domain.models.enums.PaymentMethod
import com.theminesec.multiplatform.msa_core.feature.common.domain.models.enums.SettleStatus
import com.theminesec.multiplatform.msa_core.feature.common.domain.models.enums.TransactionStatus
import com.theminesec.multiplatform.msa_core.feature.history.domain.models.TransactionDetails
import com.theminesec.multiplatform.msa_core.feature.history.presentation.HistoryAction
import com.theminesec.multiplatform.msa_core.feature.history.presentation.HistoryState
import com.theminesec.multiplatform.msa_core.feature.transactionStatus.data.models.enums.TrxEntryMode
import com.theminesec.multiplatform.msa_core.util.extentions.DataTimeHelper
import com.minesec.msav3opensource.ui.helper.template.FilterTransactionBottomSheet
import com.minesec.msav3opensource.ui.template.OnGridPagination
import com.minesec.msav3opensource.ui.theme.MsaTheme
import com.minesec.msav3opensource.R


@Composable
fun HistoryScreen(
    state: HistoryState, triggerAction: (HistoryAction) -> Unit
) = MsaUiStateHandler(
    isLoading = state.isLoading,
    errorState = state.errorState,
    noDataFound = state.transactions.isEmpty() && !state.isLoading
) {

    var showFilterSheet by remember { mutableStateOf(false) }
    var isRefreshing by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .statusBarsPadding(),
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(MsaTheme.spacing.xl2)
                    .background(MsaTheme.colors.background),
            ) {
                Image(
                    painter = painterResource(R.drawable.back_arrow),
                    contentDescription = "Back",
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(MsaTheme.spacing.sm)
                        .padding(MsaTheme.spacing.xs2)
                        .size(MsaTheme.iconSize.xs)
                        .clickable { triggerAction(HistoryAction.BackClicked) },
                    colorFilter = ColorFilter.tint(MsaTheme.colors.foreground)
                )

                Text(
                    text = stringResource(R.string.history_label),
                    color = MsaTheme.colors.foreground,
                    style = MsaTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    ) { paddingValues ->
        val lazyState = rememberLazyListState()

        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                isRefreshing=false
                triggerAction(HistoryAction.LoadTransactionMore(true))
            },
            modifier = Modifier
                .fillMaxSize()
                .background(MsaTheme.colors.background)
                .padding(paddingValues)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MsaTheme.colors.background),
                contentAlignment = Alignment.TopCenter
            ) {
                LazyColumn(
                    state = lazyState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MsaTheme.spacing.md)
                        .widthIn(max = 800.dp) // centers nicely on tablets, but fills landscape phones
                ) {
                    items(state.transactions) { item ->
                        HistoryTransactionItem(
                            paymentMethod = item.paymentMethod,
                            accountNumberSuffix = item.cardMaskedPan,
                            referenceNumber = item.transactionId,
                            amount = item.getAmountFormattedWithCurrency(),
                            status = item.state,
                            transactionType = item.transactionType,
                            dateTime = item.successTime,
                            onItemClick = { triggerAction(HistoryAction.TransactionItemClicked(item)) }
                        )
                        HorizontalDivider(
                            color = MsaTheme.colors.ring,
                            thickness = 0.5.dp,
                            modifier = Modifier.padding(horizontal = MsaTheme.spacing.md)
                        )
                    }
                }
            }
            OnGridPagination(
                gridState = lazyState,
                totalItems = state.transactions.size,
                maxItems = 20
            ) {
                triggerAction(HistoryAction.LoadTransactionMore(false))
            }
        }

        if (showFilterSheet) {
            FilterTransactionBottomSheet(
                showBottomSheet = showFilterSheet,
                onDismissRequest = {
                    showFilterSheet = false
                },
                onApplyFilters = { transactionId, min, max, type, status, methods, dateRange ->
                    showFilterSheet = false
                },
                onResetFilters = {

                }
            )
        }
    }
}

data class TransactionDisplayProps(
    val displayAmount: String,
    val amountColor: Color,
    val textDecoration: TextDecoration?,
    val statusFormatted: String,
    val statusColor: Color
)

/**
 * Calculates all necessary display properties for a transaction item.
 * This separates the data/business logic from the UI logic in the Composable.
 */
@Composable
fun calculateTransactionDisplayProps(
    amount: String,
    status: TransactionStatus,
    transactionType: TransactionType
): TransactionDisplayProps {


    // --- STATUS LOGIC ---
    val transactionTypeName =
        transactionType.name.capitalizeFirstChar()
    val statusName = status.name.capitalizeFirstChar()
    val statusFormatted = "$transactionTypeName-$statusName"

    val statusColor = when (status) {
        TransactionStatus.APPROVED -> MsaTheme.colors.approval // Green
        TransactionStatus.DECLINED -> MsaTheme.colors.error
        TransactionStatus.VOIDED -> MsaTheme.colors.foreground
        else -> MsaTheme.colors.accentForeground
    }

    // --- AMOUNT LOGIC ---
    var displayAmount = amount
    var amountColor = MsaTheme.colors.foreground
    var textDecoration: TextDecoration? = null

    when (status) {
        TransactionStatus.APPROVED -> when (transactionType) {
            TransactionType.SALE -> {
                amountColor = MsaTheme.colors.foreground
            }

            TransactionType.REFUND -> {
                amountColor = MsaTheme.colors.error
                displayAmount = "-$amount"
            }

            TransactionType.AUTH -> {
                amountColor = MsaTheme.colors.foreground
                displayAmount = amount
            }

            TransactionType.AUTH_COMP -> {
                amountColor = MsaTheme.colors.foreground
                displayAmount = amount
            }
            else -> {

            }
        }

        TransactionStatus.DECLINED -> {
            amountColor = MsaTheme.colors.foreground
            if (transactionType == TransactionType.REFUND) displayAmount = "-$amount"
        }

        TransactionStatus.VOIDED -> {
            amountColor = MsaTheme.colors.foreground
            textDecoration = TextDecoration.LineThrough
            if (transactionType == TransactionType.REFUND) displayAmount = "-$amount"
        }

        // Handle other statuses if necessary, keeping existing defaults
        else -> Unit
    }

    return TransactionDisplayProps(
        displayAmount = displayAmount,
        amountColor = amountColor,
        textDecoration = textDecoration,
        statusFormatted = statusFormatted,
        statusColor = statusColor
    )
}

// ------------------------------------------------------------------------------------------------------------------
@Composable
fun HistoryTransactionItem(
    paymentMethod: PaymentMethod,
    accountNumberSuffix: String,
    referenceNumber: String,
    amount: String,
    status: TransactionStatus,
    transactionType: TransactionType,
    dateTime: String,
    onItemClick: () -> Unit
) {
    val props = calculateTransactionDisplayProps(amount, status, transactionType)

    Row(
        modifier = Modifier
            .clickable { onItemClick() }
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f, fill = false),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Box(
                modifier = Modifier.size(MsaTheme.iconSize.xl)
                    .wrapContentSize(Alignment.Center)
            ) {
                Image(
                    painter = painterResource(paymentMethod.getPaymentSchemaIconRes()),
                    contentDescription = "${paymentMethod.name} card",
                    modifier = Modifier.size(MsaTheme.iconSize.xl2)
                )
            }

            Spacer(modifier =Modifier. width(MsaTheme.spacing.md))

            Column {
                Text(
                    text = paymentMethod.signature.capitalizeFirstChar(),
                    style = MsaTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                    ),
                    color = MsaTheme.colors.foreground
                )
                // If accountNumberSuffix is too long, we apply maxLines/overflow
                // to prevent it from pushing the right content out.
                Text(
                    text = accountNumberSuffix,
                    style = MsaTheme.typography.labelSmall.copy(fontSize = 12.sp.adaptive()),
                    color = MsaTheme.colors.accentForeground,
                    maxLines = 1,
                )
                Text(
                    text = referenceNumber,
                    style = MsaTheme.typography.labelSmall.copy(fontSize = 12.sp.adaptive()),
                    color = MsaTheme.colors.accentForeground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis // Ensure long text truncates gracefully
                )
            }
        }

        // --- Right content (Amount/Status) - No weight needed, its size is fixed ---
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = props.displayAmount,
                style = MsaTheme.typography.labelSmall.copy(
                    textDecoration = props.textDecoration,
                    fontWeight = FontWeight.SemiBold
                ),
                maxLines = 1,
                color = props.amountColor
            )
            Text(
                text = props.statusFormatted,
                style = MsaTheme.typography.labelSmall.copy(fontSize = 12.sp.adaptive()),
                color = props.statusColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = DataTimeHelper.formatTransactionTime(dateTime),
                style = MsaTheme.typography.labelSmall.copy(fontSize = 12.sp.adaptive()),
                color = MsaTheme.colors.accentForeground
            )
        }
    }
}
@Preview
@Composable
fun PreviewHistoryScreen() {
    HistoryScreen(
        HistoryState(
            transactions = arrayListOf(
                TransactionDetails(
                    groupId = "",
                    cardMaskedPan = "476173******0135",
                    refundTimes = 0,
                    deviceId = "",
                    acquirerAuthCode = "174150",
                    createdAt = "2025-11-06T09:41:50+0000",
                    TVR = "0000000000",
                    successTime = "2025-11-06T09:41:50+0000",
                    receiptAddress = "https://uat-mtms.mspayhub.com/api/anon/scan/imgs/a5fe7b81bfc51cacd28ef84dfbadd30639b2749031195fb19feb5b0a7e1e08a7fe8ef4eda278909c0146659def5d9131276f5c19eecf1de272ed85191cc9ed13bda81c65bcb47661a71d722c3d541c128d1427794d027188928d36a7b317d9d1564b43921b5e7254ac0b5c1d0ca5c87c.png",
                    receiptAddressOrigin = "https://uat-mms.mspayhub.com/payment/#/receipt/9eea7408cffbe8fb8af4ab7b3b6119debc1a4f5beb9dce0784c95137ac5c0228",
                    currency = "HKD",
                    acquirerInvoiceNo = "",
                    state = TransactionStatus.APPROVED,
                    AID = "A0000000031010",
                    refundAmount = 0,
                    updatedAt = "2025-11-06T09:41:50+0000",
                    deviceType = "",
                    refundState = 0,
                    amount = 1,
                    merchantUuid = "",
                    acquirerMid = "155294279746",
                    acquirerTraceNo = "031553",
                    cardExpData = "",
                    acquirerTxnTime = "",
                    mchName = "Minesec-DemoShop",
                    TSI = "",
                    expiredTime = "",
                    transactionId = "M1986368418162458625",
                    TC = "D8FDFA2903E1AA7E",
                    notifyState = 0,
                    acquirerBatchNo = "004176",
                    clientIp = "",
                    paymentMethod = PaymentMethod.VISA_BRAND,
                    acquirerTid = "647972492552",
                    payId = "tran_01K9C8PE8MQTQA41E7QAW5F8K1",
                    acquirerRefNo = "251106174150",
                    appName = "",
                    settleState = SettleStatus.CREATED,
                    transactionType = TransactionType.SALE,
                    ATC = "1C40",
                    posMessageId = "ddfea3e91d7949eaab32f3f8a8f9ebd6",
                    description = "",
                    trxEntryMode = TrxEntryMode.MOBILE_NFC,
                    payData = "",
                    acquirerErrMsg =""
                )

            )
        )
    ) {}
}