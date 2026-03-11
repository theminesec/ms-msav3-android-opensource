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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minesec.msav3opensource.R
import com.minesec.msav3opensource.ui.helper.items.BatchSummaryCard
import com.minesec.msav3opensource.ui.helper.items.MSAButton
import com.minesec.msav3opensource.ui.helper.items.MSAButtonStyle
import com.minesec.msav3opensource.ui.helper.items.MSATextFieldDialog
import com.minesec.msav3opensource.ui.helper.template.MsaUiStateHandler
import com.minesec.msav3opensource.ui.theme.MsaTheme
import com.theminesec.multiplatform.msa_core.feature.common.domain.models.enums.SettleStatus
import com.theminesec.multiplatform.msa_core.feature.common.domain.models.enums.SettleStatus.CREATED
import com.theminesec.multiplatform.msa_core.feature.common.domain.models.enums.SettleStatus.FAILED
import com.theminesec.multiplatform.msa_core.feature.common.domain.models.enums.SettleStatus.SETTLED
import com.theminesec.multiplatform.msa_core.feature.settlement.presentation.SettlementAction
import com.theminesec.multiplatform.msa_core.feature.settlement.presentation.SettlementState
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SettlementBatchResultScreen(
    state: SettlementState, onTriggerAction: (SettlementAction) -> Unit
) = MsaUiStateHandler(state.isLoading, state.errorState) {
    var showEmailDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    if (state.reportEmailSuccess) {
        scope.launch {
            snackbarHostState.showSnackbar("Settlement Share to email successfully ✅")
        }
    }
    Scaffold(
        modifier = Modifier
            .background(MsaTheme.colors.background)
            .statusBarsPadding(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
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
                        modifier = Modifier
                            .padding(MsaTheme.spacing.sm)
                            .size(MsaTheme.iconSize.xs)
                            .clickable { onTriggerAction(SettlementAction.BackClicked) }
                        //.align(Alignment.CenterStart)
                        ,
                        colorFilter = ColorFilter.tint(MsaTheme.colors.foreground)
                    )
                    Text(
                        text = stringResource(R.string.batches_result),
                        color = MsaTheme.colors.foreground,
                        style = MsaTheme.typography.titleMedium,
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
            }
        },
        bottomBar = {
//            if (state.settlementsCompleted.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MsaTheme.spacing.sm)
                    .background(MsaTheme.colors.background),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = MsaTheme.spacing.xs2),
                    contentAlignment = Alignment.CenterStart
                ) {
                    MSAButton(
                        text = stringResource(R.string.email_report),
                        defaultButtonStyle = MSAButtonStyle.OUTLINED,
                        onClick = { showEmailDialog = true }
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = MsaTheme.spacing.xs2),
                    contentAlignment = Alignment.CenterStart
                ) {
                    MSAButton(
                        text = stringResource(R.string.done),
                        onClick = { onTriggerAction(SettlementAction.BackClicked) }
                    )
                }
            }

        }
//        }
    ) { paddingValues ->


        if (showEmailDialog) {
            MSATextFieldDialog(
                initialNote = "",
                onAddNote = {
                    showEmailDialog = false
                    onTriggerAction(SettlementAction.EmailReport(it))
                },
                onDismiss = {
                    showEmailDialog = false
                },
                title = stringResource(R.string.enter_merchant_email)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(MsaTheme.colors.background)
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier.background(MsaTheme.colors.background)
                    .fillMaxSize()
            ) {
                item {
                    Spacer(Modifier.height(MsaTheme.spacing.md))
                    Text(
                        text = "${state.settlementsCompleted.size} Batches Completed",
                        color = MsaTheme.colors.foreground,
                        style = MsaTheme.typography.titleMedium,
                        modifier = Modifier.padding(MsaTheme.spacing.md)
                    )
                    Spacer(Modifier.height(MsaTheme.spacing.md))
                }
                items(state.settlementsCompleted) { settleItem ->

                    BatchSummaryCard(
                        hostName = settleItem.getHostBathName(),
                        batchId = settleItem.getBatchNumber(),
                        mid = settleItem.acquirerMid,
                        totalSale = settleItem.totalSale(),
                        tid = settleItem.acquirerTid,
                        totalRefund = settleItem.totalRefund(),
                        settleStatus = settleItem.state,
                        settleStatusBackgroundColor = when (settleItem.state) {
                            CREATED -> MsaTheme.colors.statusYellow
                            SETTLED -> MsaTheme.colors.approval
                            SettleStatus.PROCESSING -> MsaTheme.colors.statusYellow
                            FAILED -> MsaTheme.colors.error
                        },
                        onCardClick = {
                            onTriggerAction(
                                SettlementAction.SettleItemClicked(
                                    settleItem.batchId
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun PreviewSettlementBatchResultScreen() {
    SettlementBatchResultScreen(SettlementState()) {}
}