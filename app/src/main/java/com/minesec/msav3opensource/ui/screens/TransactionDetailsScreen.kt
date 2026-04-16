package com.minesec.msav3opensource.ui.screens

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minesec.msav3opensource.R
import com.minesec.msav3opensource.ui.helper.items.MSAButton
import com.minesec.msav3opensource.ui.helper.items.MSAButtonStyle
import com.minesec.msav3opensource.ui.helper.items.MSAIconButton
import com.minesec.msav3opensource.ui.helper.items.MSATextFieldDialog
import com.minesec.msav3opensource.ui.helper.items.PartialAmountWithPassCodeDialog
import com.minesec.msav3opensource.ui.helper.items.ShareReceiptQrDialog
import com.minesec.msav3opensource.ui.template.FeatureUnavailableDialog
import com.minesec.msav3opensource.ui.template.MsaUiStateHandler
import com.minesec.msav3opensource.ui.template.PasscodeDialog
import com.minesec.msav3opensource.ui.helper.template.PaymentCardInfo
import com.minesec.msav3opensource.ui.template.SettlementSuccessDialog
import com.minesec.msav3opensource.ui.helper.template.TransactionDetailItem
import com.minesec.msav3opensource.ui.helper.utils.copyTextToClipboard
import com.minesec.msav3opensource.ui.models.capitalizeFirstChar
import com.minesec.msav3opensource.ui.models.getPaymentSchemaIconRes
import com.minesec.msav3opensource.ui.theme.MsaTheme
import com.theminesec.multiplatform.msa_core.app.navigation.LocalWindowSize
import com.theminesec.multiplatform.msa_core.app.navigation.WindowWidthSize
import com.theminesec.multiplatform.msa_core.common.domain.models.MSAException
import com.theminesec.multiplatform.msa_core.feature.common.data.models.TransactionType
import com.theminesec.multiplatform.msa_core.feature.common.data.models.TransactionType.REFUND
import com.theminesec.multiplatform.msa_core.feature.common.domain.models.enums.PaymentMethod
import com.theminesec.multiplatform.msa_core.feature.common.domain.models.enums.SettleStatus
import com.theminesec.multiplatform.msa_core.feature.common.domain.models.enums.TransactionActionType
import com.theminesec.multiplatform.msa_core.feature.common.domain.models.enums.TransactionStatus
import com.theminesec.multiplatform.msa_core.feature.common.domain.models.enums.TransactionStatus.ADJUSTED
import com.theminesec.multiplatform.msa_core.feature.common.domain.models.enums.TransactionStatus.APPROVED
import com.theminesec.multiplatform.msa_core.feature.common.domain.models.enums.TransactionStatus.COMPLETED
import com.theminesec.multiplatform.msa_core.feature.common.domain.models.enums.TransactionStatus.PROCESSING
import com.theminesec.multiplatform.msa_core.feature.common.domain.models.enums.TransactionStatus.REFUNDED
import com.theminesec.multiplatform.msa_core.feature.common.domain.models.enums.TransactionStatus.REVERSED
import com.theminesec.multiplatform.msa_core.feature.common.domain.models.enums.TransactionStatus.VOIDED
import com.theminesec.multiplatform.msa_core.feature.history.domain.models.TransactionDetails
import com.theminesec.multiplatform.msa_core.feature.transactionStatus.data.models.enums.TrxEntryMode
import com.theminesec.multiplatform.msa_core.feature.transactionStatus.presentation.details.TransactionDetailsAction
import com.theminesec.multiplatform.msa_core.feature.transactionStatus.presentation.details.TransactionDetailsState
import com.theminesec.multiplatform.msa_core.util.extentions.DataTimeHelper
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch
import rememberAdaptiveTranDetailFontSize

@SuppressLint("UnrememberedMutableState", "CoroutineCreationDuringComposition")
@Composable
fun TransactionDetailsScreen(
    state: TransactionDetailsState,
    onTriggerAction: (TransactionDetailsAction) -> Unit
) = MsaUiStateHandler(state.isLoading, state.errorState) {

    val transactionDetails by mutableStateOf(state.transaction)
    val amountFontSize = rememberAdaptiveTranDetailFontSize(
        displayAmount = transactionDetails?.amount.toString()
    )

    var dialogStates by remember {
        mutableStateOf(DialogStates())
    }

    Napier.e { "TransactionDetailsScreen: transactionDetails=$transactionDetails" }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    if (!state.emailReceiptMessage.isNullOrBlank()) {
        scope.launch {
            snackbarHostState.showSnackbar(state.emailReceiptMessage!!)
        }
    }
    SettlementSuccessDialog(
        !state.emailReceiptMessage.isNullOrBlank(),
        "The receipt has been successfully sent to your mail"
    ) {}

    Scaffold(
        modifier = Modifier.background(MsaTheme.colors.background)
            .statusBarsPadding(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TransactionTopBar(
                transactionDetails = transactionDetails,
                state = state,
                title = "Details",
                isResult = false,
                amountFontSize = amountFontSize,
                onBackClicked = { onTriggerAction(TransactionDetailsAction.OnBackClicked) },
                onEmailClicked = { dialogStates = dialogStates.copy(showEmailDialog = true) },
                onQRClicked = { dialogStates = dialogStates.copy(showQRDialog = true) },
                onReceiptClicked = { onTriggerAction(TransactionDetailsAction.OnReceiptClicked) }
            )
        },
    ) { paddingValues ->

        val scrollState = rememberScrollState()

        // Dialogs
        RenderDialogs(
            dialogStates = dialogStates,
            transactionDetails = transactionDetails,
            onDismissFeatureUnavailable = {
                dialogStates = dialogStates.copy(showFeatureUnavailable = false)
            },
            onConfirmPassCode = {
                dialogStates = dialogStates.copy(showPasscodeDialog = false)

                when (dialogStates.currentActionSelected) {
                    TransactionActionType.VOID_ACTION -> onTriggerAction(
                        TransactionDetailsAction.VoidTransaction(it)
                    )

                    TransactionActionType.REFUND_ACTION -> {
                        onTriggerAction(
                            TransactionDetailsAction.RefundTransaction(
                                it,
                                transactionDetails!!.amount.toString()
                            )
                        )
                    }

                    TransactionActionType.COMPLETION_ACTION -> {
                        onTriggerAction(
                            TransactionDetailsAction.CompletionTransaction(
                                it,
                                transactionDetails!!.getAmountFormattedWithCurrency()
                            )
                        )
                    }

                    else -> {
                        Napier.e {
                            "No action trigger :: action is --> ${
                                dialogStates.currentActionSelected
                            }"
                        }
                    }
                }
            },
            onConfirmPartialRefund = { amount,passCode ->
                onTriggerAction(TransactionDetailsAction.RefundTransaction(passCode, amount))
            },
            onConfirmPartialAuthComp = { amount ,passCode->
                onTriggerAction(
                    TransactionDetailsAction.CompletionTransaction(
                        passCode, amount
                    )
                )
            },
            onDismissPartialDialog = {
                dialogStates = dialogStates.copy(
                    showPartialAuthAmountDialog = false,
                    showPartialRefundAmountDialog = false
                )
            },
            onConfirmEmail = { email ->
                dialogStates = dialogStates.copy(showEmailDialog = false)
                onTriggerAction(TransactionDetailsAction.OnMailClicked(email))
            },
            onDismissEmail = { dialogStates = dialogStates.copy(showEmailDialog = false) },
            onDismissPassCodeDialog = { dialogStates = dialogStates.copy(showPasscodeDialog = false) },
            onDismissQR = { dialogStates = dialogStates.copy(showQRDialog = false) }
        )
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(MsaTheme.colors.background)
                .padding(horizontal = MsaTheme.spacing.md)
        ) {
            TransactionDetailsContent(
                transactionDetails = transactionDetails
            )
            Spacer(Modifier.height(MsaTheme.spacing.sm))

            TransactionBottomBar(
                transactionDetails = transactionDetails,
                state = state,
                onActionSelected = { actionType, isEnabled ->
                    dialogStates = dialogStates.copy(
                        showFeatureUnavailable = !isEnabled,
                        currentActionSelected = actionType
                    )
                    if (isEnabled) {
                        handleTransactionAction(
                            state.isPartialRefundEnabled,
                            state.isPartialAuthCompEnabled,
                            actionType,
                            onShowPassCode = {
                                dialogStates =
                                    dialogStates.copy(showPasscodeDialog = true)
                            },
                            onShowPartialRefund = {
                                dialogStates =
                                    dialogStates.copy(showPartialRefundAmountDialog = true)
                            },
                            onShowPartialAuthComp = {
                                dialogStates = dialogStates.copy(showPartialAuthAmountDialog = true)
                            }
                        )
                    }
                },
                onNewTransaction = {}
            )

            Spacer(Modifier.height(MsaTheme.spacing.xl))
        }

    }
}

// Data class to manage dialog states
data class DialogStates(
    val showQRDialog: Boolean = false,
    val showPasscodeDialog: Boolean = false,
    val showPartialRefundAmountDialog: Boolean = false,
    val showPartialAuthAmountDialog: Boolean = false,
    val showEmailDialog: Boolean = false,
    val showFeatureUnavailable: Boolean = false,
    val currentActionSelected: TransactionActionType = TransactionActionType.UNKNOW_ACTION
)

@Composable
fun RenderDialogs(
    dialogStates: DialogStates,
    transactionDetails: TransactionDetails?,
    onDismissFeatureUnavailable: () -> Unit,
    onConfirmPartialRefund: (String, String) -> Unit,
    onConfirmPassCode: (String) -> Unit,
    onConfirmPartialAuthComp: (String, String) -> Unit,
    onDismissPartialDialog: () -> Unit,
    onDismissPassCodeDialog: () -> Unit,
    onConfirmEmail: (String) -> Unit,
    onDismissEmail: () -> Unit,
    onDismissQR: () -> Unit
) {
    var errorEmailMessage by remember { mutableStateOf<String?>(null) }

    if (dialogStates.showFeatureUnavailable) {
        FeatureUnavailableDialog(
            true,
            "Can't Proceed",
            "Can't invoke this action for transaction, is not available at your profile.",
            onDismissFeatureUnavailable
        )
    }

    if (dialogStates.showPasscodeDialog) {
        PasscodeDialog(
            onConfirm = { enteredPasscode ->
                onConfirmPassCode(enteredPasscode)
            },
            onDismiss = onDismissPassCodeDialog
        )
    }

    if (dialogStates.showPartialRefundAmountDialog && transactionDetails != null) {
        PartialAmountWithPassCodeDialog(
            transactionDetails,
            isPartialRefund = true,
            onConfirm = onConfirmPartialRefund,
            onDismiss = onDismissPartialDialog
        )
    }

    if (dialogStates.showPartialAuthAmountDialog && transactionDetails != null) {
        PartialAmountWithPassCodeDialog(
            transactionDetails,
            isPartialRefund = false,
            onConfirm = onConfirmPartialAuthComp,
            onDismiss = onDismissPartialDialog
        )
    }

    if (dialogStates.showEmailDialog) {
        MSATextFieldDialog(
            initialNote = "",
            errorText = errorEmailMessage,
            onClearError = { errorEmailMessage = null },
            onAddNote = {
                if (it.isBlank()) {
                    errorEmailMessage = "Email cannot be empty"
                } else if (!it.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))) {
                    errorEmailMessage = "Please enter a valid email address"
                } else {
                    onConfirmEmail(it)
                }
            },
            onDismiss = onDismissEmail,
            textFieldLeadingIcon = R.drawable.email,
            ontextFieldLeadingIconClick = {},
            title = stringResource(R.string.enter_merchant_email)
        )
    }

    if (dialogStates.showQRDialog) {
        ShareReceiptQrDialog(
            transactionDetails?.receiptAddressOrigin ?: "",
            onDismiss = onDismissQR
        )
    }
}

@Composable
fun TransactionTopBar(
    transactionDetails: TransactionDetails?,
    state: TransactionDetailsState,
    amountFontSize: TextUnit,
    addingActionButtons: Boolean = true,
    title: String = "Details",
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
        TopBarHeader(title, isResult, onBackClicked)
        TransactionAmountSection(
            transactionDetails = transactionDetails,
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
fun TopBarHeader(title: String = "Details", isResult: Boolean, onBackClicked: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(MsaTheme.spacing.xl2)
            .padding(start = MsaTheme.spacing.xs2),
        contentAlignment = Alignment.CenterStart
    ) {
        if (!isResult) Image(
            painter = painterResource(R.drawable.back_arrow),
            contentDescription = "Back",
            modifier =Modifier. padding(MsaTheme.spacing.sm)
                .size(MsaTheme.iconSize.xs)
                .clickable { onBackClicked() },
            colorFilter = ColorFilter.tint(MsaTheme.colors.foreground)
        )

        Text(
            text = title,
            color = MsaTheme.colors.foreground,
            style = MsaTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun TransactionAmountSection(
    transactionDetails: TransactionDetails?,
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
        Text(
            text = transactionDetails?.getAmountFormattedWithCurrency() ?: "",
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
                .heightIn(min = MsaTheme.spacing.xl3)
        )

        TransactionStatusRow(transactionDetails)
        Spacer(Modifier.height(MsaTheme.spacing.lg))
    }
}

@Composable
private fun TransactionStatusRow(
    transactionDetails: TransactionDetails?
) {
    Row {
        transactionDetails?.state?.let {
            Image(
                painter = painterResource(getStatusIcon(it)),
                contentDescription = "Status",
                modifier = Modifier.size(MsaTheme.iconSize.md)
            )
        }

        Spacer(Modifier.width(MsaTheme.spacing.sm))

        Text(
            text = transactionDetails?.transactionType?.signature?.capitalizeFirstChar() ?: "",
            color = if (transactionDetails?.transactionType == REFUND) {
                MsaTheme.colors.error
            } else {
                MsaTheme.colors.foreground
            },
            style = MsaTheme.typography.titleMedium,
        )

        Text(
            text = "-${transactionDetails?.state?.signature ?: "Aborted"}",
            color = getStatusColor(transactionDetails?.state),
            style = MsaTheme.typography.titleMedium,
        )
    }
}

@Composable
fun TransactionActionsRow(
    isReceiptEnabled: Boolean,
    onEmailClicked: () -> Unit,
    onQRClicked: () -> Unit,
    onReceiptClicked: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(
            MsaTheme.spacing.sm,
            Alignment.CenterHorizontally
        ),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = MsaTheme.minTouchSize.sm)
            .padding(
                start = MsaTheme.spacing.md,
                end = MsaTheme.spacing.md
            )
    ) {
        MSAIconButton(
            iconResId = R.drawable.email,
            onClick = onEmailClicked,
            modifier = Modifier.weight(1f)
        )

        MSAIconButton(
            iconResId = R.drawable.ms_activation_qr,
            onClick = onQRClicked,
            modifier = Modifier.weight(1f)
        )

        if (isReceiptEnabled) {
            MSAIconButton(
                iconResId = R.drawable.receipt,
                onClick = onReceiptClicked,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun TransactionBottomBar(
    transactionDetails: TransactionDetails?,
    state: TransactionDetailsState,
    onActionSelected: (TransactionActionType, Boolean) -> Unit,
    onNewTransaction: () -> Unit
) {
    val actionType = transactionDetails?.getActionType() ?: TransactionActionType.UNKNOW_ACTION
    if (actionType == TransactionActionType.UNKNOW_ACTION) return

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MsaTheme.colors.background)
    ) {
        // Completion button for COMPLETION_VOID_ACTION
        if (actionType == TransactionActionType.COMPLETION_VOID_ACTION) {
            TransactionDetailsActionButton(
                text = "Completion",
                actionType = TransactionActionType.COMPLETION_ACTION,
                isEnabled = state.isCompletionEnabled,
                onActionSelected = onActionSelected
            )
            Spacer(modifier = Modifier.height(MsaTheme.spacing.sm))
        }

        // Main action button
        val (buttonText, primaryActionType, isEnabled) = getActionButtonConfig(
            actionType = actionType,
            state = state
        )

        TransactionDetailsActionButton(
            text = buttonText,
            actionType = primaryActionType,
            isEnabled = isEnabled,
            onActionSelected = onActionSelected
        )

        Spacer(modifier = Modifier.height(MsaTheme.spacing.sm))

        // New transaction button
        if (state.isStartNewTransactionEnabled) {
            MSAButton(
                text = "Start new Transaction",
                onClick = onNewTransaction,
                defaultButtonStyle = MSAButtonStyle.FILLED,
                backgroundColor = MsaTheme.colors.primary
            )
        }
    }
}

@Composable
fun TransactionDetailsActionButton(
    text: String,
    actionType: TransactionActionType,
    isEnabled: Boolean,
    backgroundColor : Color= MsaTheme.colors.secondary,
    onActionSelected: (TransactionActionType, Boolean) -> Unit
) {
    MSAButton(
        text = text,
        onClick = { onActionSelected(actionType, isEnabled) },
        defaultButtonStyle = MSAButtonStyle.FILLED,
        backgroundColor =backgroundColor
    )
}

@Composable
fun TransactionDetailsContent(
    exception: MSAException? = null,
    transactionDetails: TransactionDetails?,
    isFullDetails: Boolean = true
) {
    val clipboard = LocalClipboard.current
    val scope = rememberCoroutineScope()
    val columns = getGridColumns()

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        PaymentCardInfo(
            cardBrand = transactionDetails?.paymentMethod?.signature?.lowercase(),
            cardLastDigits = transactionDetails?.cardMaskedPan,
            exception = exception,
            cardTypeIcon = transactionDetails?.paymentMethod?.getPaymentSchemaIconRes()
                ?: R.drawable.payment_method_icon
        )

        Column {
            if (isFullDetails) Text(
                text = transactionDetails?.getTransactionTime().toString(),
                color = MsaTheme.colors.mutedForeground,
                style = MsaTheme.typography.labelMedium
            )

            Spacer(modifier = Modifier.height(MsaTheme.spacing.md))


            val trxId = transactionDetails?.transactionId ?: if (exception == null) null else "n/a"
            trxId?.let {
                TransactionDetailItem(
                    label = R.string.tran_id,
                    trxId,
                    clipIconVisible = !transactionDetails?.transactionId.isNullOrEmpty(),
                    onCopyClick = {
                        if (it.isNotEmpty()) scope.launch {
                            copyTextToClipboard(clipboard, it)
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(MsaTheme.spacing.md))

            if (exception == null) {
                transactionDetails?.let {
                    DetailItemsGrid(
                        columns = columns,
                        detailItems = buildDetailItems(transactionDetails, isFullDetails)
                    ) {
                        if (it.isNotEmpty()) scope.launch {
                            copyTextToClipboard(clipboard, it)
                        }
                    }
                }
            } else {
                TransactionDetailItem(
                    label = R.string.code,
                    value = when (exception) {
                        is MSAException.Local.MPOCSdkException -> exception.code.toString()
                        else -> "n/a"
                    }
                )

                Spacer(modifier = Modifier.height(MsaTheme.spacing.md))

                TransactionDetailItem(
                    label = R.string.description,
                    value = exception.message ?: exception.cause?.message ?: "n/a"
                )

            }
        }
    }
}

@Composable
private fun DetailItemsGrid(
    columns: Int,
    detailItems: List<Pair<Int, String>>,
    onCopyClicked: (String) -> Unit
) {
    // Calculate dynamic height based on content
    val rows = (detailItems.size + columns - 1) / columns
    // Estimate item height - adjust this value based on your TransactionDetailItem typical height
    val estimatedItemHeight = when (LocalWindowSize.current.width) {
        WindowWidthSize.Compact -> 70.dp
        WindowWidthSize.Medium -> 75.dp
        WindowWidthSize.Expanded -> 80.dp
        else -> 75.dp
    }
    val spacingHeight = MsaTheme.spacing.md * (rows - 1).coerceAtLeast(0)
    val totalHeight = (estimatedItemHeight * rows) + spacingHeight

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        verticalArrangement = Arrangement.spacedBy(MsaTheme.spacing.md),
        horizontalArrangement = Arrangement.spacedBy(MsaTheme.spacing.md),
        userScrollEnabled = false,
        modifier = Modifier
            .fillMaxWidth()
            .height(totalHeight) // Use calculated height instead of heightIn
    ) {
        items(detailItems) { (label, value) ->
            TransactionDetailItem(label = label, value = value, onCopyClick = onCopyClicked)
        }
    }
}

// Helper Functions
fun getStatusIcon(state: TransactionStatus?): Int {
    return when (state) {
        APPROVED -> R.drawable.success_icon
        VOIDED -> R.drawable.void_icon
        REFUNDED -> R.drawable.success_icon
        else -> R.drawable.declined
    }
}

@Composable
fun getStatusColor(state: TransactionStatus?): Color {
    return when (state) {
        PROCESSING, REFUNDED, REVERSED, ADJUSTED, COMPLETED, APPROVED -> MsaTheme.colors.approval
        VOIDED -> MsaTheme.colors.foreground
        else -> MsaTheme.colors.error
    }
}

@Composable
private fun getGridColumns(): Int {
    return when (LocalWindowSize.current.width) {
        WindowWidthSize.Compact -> 2
        WindowWidthSize.Medium -> 2
        WindowWidthSize.Expanded -> 4
        else -> 1
    }
}

private fun getActionButtonConfig(
    actionType: TransactionActionType,
    state: TransactionDetailsState
): Triple<String, TransactionActionType, Boolean> {
    return when (actionType) {
        TransactionActionType.VOID_ACTION -> Triple(
            "Void",
            TransactionActionType.VOID_ACTION,
            state.isVoidEnabled
        )

        TransactionActionType.REFUND_ACTION -> Triple(
            "Refund",
            TransactionActionType.REFUND_ACTION,
            state.isRefundEnabled
        )

        TransactionActionType.COMPLETION_ACTION -> Triple(
            "Completion",
            TransactionActionType.COMPLETION_ACTION,
            state.isCompletionEnabled
        )

        TransactionActionType.COMPLETION_VOID_ACTION -> Triple(
            "Void",
            TransactionActionType.VOID_ACTION,
            state.isVoidEnabled
        )

        else -> Triple("", actionType, false)
    }
}

private fun handleTransactionAction(
    isPartialRefundEnabled: Boolean,
    isPartialAuthCompEnabled: Boolean,
    actionType: TransactionActionType,
    onShowPartialRefund: () -> Unit,
    onShowPartialAuthComp: () -> Unit,
    onShowPassCode: () -> Unit
) {
    when (actionType) {
        TransactionActionType.VOID_ACTION -> onShowPassCode()

        TransactionActionType.REFUND_ACTION -> {
            if (isPartialRefundEnabled) onShowPartialRefund()
            else onShowPassCode()
        }

        TransactionActionType.COMPLETION_ACTION -> {
            if (isPartialAuthCompEnabled) onShowPartialAuthComp()
            else onShowPassCode()
        }

        else -> {
            Napier.e { "No action trigger :: action is --> $actionType" }
        }
    }
}

private fun buildDetailItems(
    transactionDetails: TransactionDetails,
    isFullDetails: Boolean = true
): List<Pair<Int, String>> {
    val items = mutableListOf(
        Pair(R.string.trace, transactionDetails.acquirerTraceNo),
        Pair(R.string.approval_code, transactionDetails.acquirerAuthCode)
    )

    if (!isFullDetails) {
        transactionDetails.successTime.let {
            val dataTimePair = DataTimeHelper.extractDateAndTime(it)
            items += listOf(
                Pair(R.string.time, dataTimePair.first),
                Pair(R.string.date, dataTimePair.second)
            )
        }
    }

    if (isFullDetails) {
        items += listOf(
            Pair(R.string.batch_status, transactionDetails.settleState.signature),
            Pair(R.string.mid, transactionDetails.acquirerMid),
            Pair(R.string.tid, transactionDetails.acquirerTid),
            Pair(R.string.batch, transactionDetails.acquirerBatchNo),
            Pair(R.string.rrn_label, transactionDetails.acquirerRefNo),
            Pair(R.string.pos_ref, transactionDetails.posMessageId),
            Pair(R.string.aid, transactionDetails.AID),
            Pair(R.string.app_name, transactionDetails.getAppName()),
            Pair(R.string.tc, transactionDetails.TC),
            Pair(R.string.atc, transactionDetails.ATC),
            Pair(R.string.tvr, transactionDetails.TVR),
            Pair(R.string.tsi, transactionDetails.TSI)
        )
    }

    return items.map { (label, value) -> label to value }
}


@Preview
@Composable
fun PreviewVoidScreen() {
    TransactionDetailsScreen(
        TransactionDetailsState(
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