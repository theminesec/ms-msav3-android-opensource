package com.minesec.msav3opensource.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minesec.msav3opensource.R
import com.minesec.msav3opensource.ui.helper.items.BatchSummaryDetails
import com.minesec.msav3opensource.ui.helper.items.MSAButton
import com.minesec.msav3opensource.ui.template.MsaUiStateHandler
import com.minesec.msav3opensource.ui.template.SettlementSuccessDialog
import com.minesec.msav3opensource.ui.models.getSettlementStatusText
import com.minesec.msav3opensource.ui.theme.MsaTheme
import com.theminesec.multiplatform.msa_core.feature.common.domain.models.enums.SettleStatus
import com.theminesec.multiplatform.msa_core.feature.common.domain.models.enums.SettleStatus.CREATED
import com.theminesec.multiplatform.msa_core.feature.common.domain.models.enums.SettleStatus.FAILED
import com.theminesec.multiplatform.msa_core.feature.common.domain.models.enums.SettleStatus.SETTLED
import com.theminesec.multiplatform.msa_core.feature.settlement.presentation.SettlementAction
import com.theminesec.multiplatform.msa_core.feature.settlement.presentation.SettlementState
import com.theminesec.multiplatform.msa_core.util.extentions.DataTimeHelper

 @SuppressLint("UnrememberedMutableState")
 @Composable
fun SettlementBatchDetailsScreen(
    state: SettlementState, onTriggerAction: (SettlementAction) -> Unit
 ) = MsaUiStateHandler(state.isLoading, state.errorState) {
    val currentBatch by mutableStateOf(state.settlementItem)


     SettlementSuccessDialog(
         state.settlementItemSuccess,
         "Settlement successfully completed"
     ) {}

    Scaffold(
        modifier = Modifier.background(MsaTheme.colors.background)
            .statusBarsPadding()
            .safeDrawingPadding(),
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MsaTheme.colors.background)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(MsaTheme.spacing.xl2)
                        .padding(start = MsaTheme.spacing.xs2),
                    contentAlignment = Alignment.CenterStart
                ) {

                    Image(
                        painter = painterResource(R.drawable.back_arrow),
                        contentDescription = "Back",
                        modifier = Modifier.padding(MsaTheme.spacing.sm)
                            .size(MsaTheme.iconSize.xs)
                            .clickable { onTriggerAction(SettlementAction.BackClicked) }
                        //.align(Alignment.CenterStart)
                        ,
                        colorFilter = ColorFilter.tint(MsaTheme.colors.foreground)
                    )
                    Text(
                        text = stringResource(R.string.batch_details),
                        color = MsaTheme.colors.foreground,
                        style = MsaTheme.typography.titleMedium,
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
            }
        },
        bottomBar = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                if (state.isSettleAllEnabled) {
                    Column(
                        modifier = Modifier
                            .widthIn(max = 600.dp)
                            .fillMaxWidth()
                            .background(MsaTheme.colors.background)
                            .padding(vertical = MsaTheme.spacing.sm)
                            .padding(horizontal = MsaTheme.spacing.md),
                    ) {
                        if (currentBatch?.state != SETTLED) {
                            MSAButton(
                                text = stringResource(R.string.settle_batch),
                                onClick = { onTriggerAction(SettlementAction.SettleCurrentBatch) },
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.background(MsaTheme.colors.background)
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 600.dp) // Sets the maximum width to 600dp(adaptive)
                    .fillMaxWidth()
            ) {
                currentBatch?.let {
                    BatchSummaryDetails(
                        hostName = it.getHostBathName(),
                        batchId = it.getBatchNumber(),
                        mid = it.acquirerMid,
                        totalSale = it.totalSale(),
                        tid = it.acquirerTid,
                        totalRefund = it.totalRefund(),
                        settleStatusText = it.state.getSettlementStatusText(),
                        settleStatusBackgroundColor = when (it.state) {
                            CREATED -> MsaTheme.colors.statusYellow
                            SETTLED -> MsaTheme.colors.approval
                            SettleStatus.PROCESSING -> MsaTheme.colors.statusYellow
                            FAILED -> MsaTheme.colors.error
                        },
                        time = DataTimeHelper.formatTransactionTime(it.updatedAt),
                        grossTotalCount = it.grossTotalCount,
                        grossTotalAmount = it.grossTotalAmount,

                        captureTotalCount = it.captureTotalCount,
                        captureTotalAmount = it.captureTotalAmount,

                        refundTotalCount = it.refundTotalCount,
                        refundTotalAmount = it.refundTotalAmount,

                        voidSaleCount = it.voidSaleTotalCount,
                        voidSaleAmount = it.voidSaleTotalAmount,

                        voidCaptureCount = it.voidCaptureTotalCount,
                        voidCaptureAmount = it.voidCaptureTotalAmount,

                        voidRefundCount = it.voidRefundTotalCount,
                        voidRefundAmount = it.voidRefundTotalAmount,

                        currency = it.currency
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun PreviewSettlementBatchDetailsScreen() {
    SettlementBatchDetailsScreen(SettlementState()) {}
}