package com.minesec.msav3opensource.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minesec.msav3opensource.R
import com.minesec.msav3opensource.ui.template.MsaUiStateHandler
import com.minesec.msav3opensource.ui.models.capitalizeFirstChar
import com.minesec.msav3opensource.ui.theme.MsaTheme
import com.theminesec.multiplatform.msa_core.common.domain.models.MSAException
import com.theminesec.multiplatform.msa_core.feature.common.data.models.request.CardTransactionRequest
import com.theminesec.multiplatform.msa_core.feature.common.data.models.TransactionType
import com.theminesec.multiplatform.msa_core.feature.common.data.models.TransactionType.REFUND
import com.theminesec.multiplatform.msa_core.feature.common.domain.models.enums.PaymentMethod
import com.theminesec.multiplatform.msa_core.feature.common.domain.models.enums.SettleStatus
import com.theminesec.multiplatform.msa_core.feature.common.domain.models.enums.TransactionActionType
import com.theminesec.multiplatform.msa_core.feature.common.domain.models.enums.TransactionStatus
import com.theminesec.multiplatform.msa_core.feature.history.domain.models.TransactionDetails
import com.theminesec.multiplatform.msa_core.feature.transactionStatus.data.models.enums.TrxEntryMode
import com.theminesec.multiplatform.msa_core.feature.transactionStatus.presentation.result.TransactionResultAction
import com.theminesec.multiplatform.msa_core.feature.transactionStatus.presentation.result.TransactionResultState
import rememberAdaptiveTranDetailFontSize

@SuppressLint("UnrememberedMutableState")
@Composable
fun TransactionResultScreen(
    state: TransactionResultState,
    onTriggerAction: (TransactionResultAction) -> Unit
) = MsaUiStateHandler(state.isLoading, state.errorState) {

    val transactionDetails by mutableStateOf(state.transaction)
    val amountFontSize = rememberAdaptiveTranDetailFontSize(
        displayAmount = transactionDetails?.amount.toString()
    )

    var dialogStates by remember {
        mutableStateOf(DialogStates())
    }

    Scaffold(
        modifier = Modifier
            .background(MsaTheme.colors.background)
            .statusBarsPadding(),
        topBar = {
            ResultTransactionTopBar(
                transactionDetails = transactionDetails,
                state = state,
                title = "Result",
                addingActionButtons = false,
                isResult = true,
                amountFontSize = amountFontSize,
                onBackClicked = { },
                onEmailClicked = { dialogStates = dialogStates.copy(showEmailDialog = true) },
                onQRClicked = { dialogStates = dialogStates.copy(showQRDialog = true) },
                onReceiptClicked = { onTriggerAction(TransactionResultAction.OnReceiptClicked) }
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MsaTheme.colors.background)
            ) {
                if (state.errorState == null) state.transaction?.let {
                    TransactionActionsRow(
                        isReceiptEnabled = state.isReceiptEnabled,
                        onEmailClicked = {
                            dialogStates = dialogStates.copy(showEmailDialog = true)
                        },
                        onQRClicked = {
                            dialogStates = dialogStates.copy(showQRDialog = true)
                        },
                        onReceiptClicked = { onTriggerAction(TransactionResultAction.OnReceiptClicked) }
                    )
                }
                Spacer(Modifier.height(MsaTheme.spacing.md))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MsaTheme.colors.background)
                        .padding(vertical = MsaTheme.spacing.sm)
                        .padding(horizontal = MsaTheme.spacing.md)
                ) {
                    if (state.errorState != null || state.transaction != null) TransactionDetailsActionButton(
                        "Start new transaction",
                        TransactionActionType.START_NEW_TRX,
                        true,
                        backgroundColor = MsaTheme.colors.primary
                    ) { action, enabled ->
                        onTriggerAction(
                            TransactionResultAction.NavigateToNewTrxRequest(
                                state.cardTransactionRequest?.currency ?: ""
                            )
                        )
                    }
                }
                Spacer(Modifier.height(MsaTheme.spacing.md))
            }
        }
    ) { paddingValues ->

        RenderDialogs(
            dialogStates = dialogStates,
            transactionDetails = transactionDetails,
            onDismissFeatureUnavailable = {
                dialogStates = dialogStates.copy(showFeatureUnavailable = false)
            },
            onConfirmPassCode = {},
            onConfirmPartialRefund = { _, _ -> },
            onConfirmPartialAuthComp = { _, _ -> },
            onDismissPartialDialog = {},
            onConfirmEmail = { email ->
                dialogStates = dialogStates.copy(showEmailDialog = false)
                onTriggerAction(TransactionResultAction.OnMailClicked(email))
            },
            onDismissEmail = { dialogStates = dialogStates.copy(showEmailDialog = false) },
            onDismissPassCodeDialog = {},
            onDismissQR = { dialogStates = dialogStates.copy(showQRDialog = false) }
        )
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(MsaTheme.colors.background)
                .padding(horizontal = MsaTheme.spacing.md)
        ) {
            TransactionDetailsContent(
                transactionDetails = transactionDetails,
                exception = state.errorState?.exception,
                isFullDetails = false
            )
        }
    }
}


@Composable
private fun ResultTransactionTopBar(
    transactionDetails: TransactionDetails?,
    state: TransactionResultState,
    amountFontSize: TextUnit,
    addingActionButtons: Boolean = true,
    title: String = "Result",
    isResult: Boolean = false,
    onBackClicked: () -> Unit,
    onEmailClicked: () -> Unit,
    onQRClicked: () -> Unit,
    onReceiptClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MsaTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (state.errorState != null || state.transaction != null)
            TopBarHeader(title, isResult, onBackClicked)
        if (isResult) Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        ) {
            val iconRes = if (state.errorState == null) transactionDetails?.state?.let {
                getStatusIcon(transactionDetails.state)
            } else R.drawable.declined

            iconRes?.let {
                Image(
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        TransactionResultAmountSection(
            transactionDetails = transactionDetails,
            state = state,
            amountFontSize = amountFontSize
        )

        if (addingActionButtons) TransactionActionsRow(
            isReceiptEnabled = state.isReceiptEnabled,
            onEmailClicked = onEmailClicked,
            onQRClicked = onQRClicked,
            onReceiptClicked = onReceiptClicked
        )
    }
}

@Composable
private fun TransactionResultAmountSection(
    transactionDetails: TransactionDetails?,
    state: TransactionResultState,
    amountFontSize: TextUnit
) {
    Column(
        modifier = Modifier
            .padding(
                top = MsaTheme.spacing.md,
                start = MsaTheme.spacing.md,
                end = MsaTheme.spacing.md
            )
            .wrapContentSize(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val amount = transactionDetails?.getAmountFormattedWithCurrency()
            ?: if (state.errorState == null) "" else state.cardTransactionRequest?.getAmountFormatted()
                ?: ""
        Text(
            text = amount,
            color = MsaTheme.colors.foreground,
            style = MsaTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = amountFontSize,
                lineHeight = 40.sp,
            ),
            textAlign = TextAlign.Center,
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .height(MsaTheme.spacing.xl3)
        )

        //Spacer(Modifier.height(MsaTheme.spacing.xs2))

        TransactionResultStatusRow(
            transactionDetails,
            state.errorState?.exception,
            state.cardTransactionRequest
        )
        Spacer(Modifier.height(MsaTheme.spacing.lg))
    }
}

@Composable
private fun TransactionResultStatusRow(
    transactionDetails: TransactionDetails?,
    exception: MSAException?,
    cardTransactionRequest: CardTransactionRequest?
) {
    Row {
        val iconRes = if (exception == null) transactionDetails?.state?.let {
            getStatusIcon(transactionDetails.state)
        } else R.drawable.declined

        iconRes?.let {
            Image(
                painter = painterResource(iconRes),
                contentDescription = "Status",
                modifier = Modifier.size(MsaTheme.iconSize.md)
            )
        }

        Spacer(Modifier.width(MsaTheme.spacing.sm))

        val trxType: String = transactionDetails?.transactionType?.signature?.capitalizeFirstChar()
            ?: if (exception == null) "" else cardTransactionRequest?.trxType?.signature?.capitalizeFirstChar()
                ?: ""
        Text(
            text = trxType,
            color = if (transactionDetails?.transactionType == REFUND) {
                MsaTheme.colors.error
            } else {
                MsaTheme.colors.foreground
            },
            style = MsaTheme.typography.titleMedium,
        )

        val trxState = "-".plus(
            transactionDetails?.state?.signature ?: if (exception == null) "" else "Aborted"
        )
        val stateColor = transactionDetails?.state?.let { getStatusColor(it) }
            ?: if (exception == null) Color.Transparent else MsaTheme.colors.error

        Text(
            text = trxState,
            color = stateColor,
            style = MsaTheme.typography.titleMedium,
        )
    }
}


@Preview
@Composable
fun PreviewTrxResultScreen() {
    TransactionResultScreen(
        TransactionResultState(
            transaction = TransactionDetails(
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
                acquirerErrMsg = ""
            )
        )
    ) {}
}