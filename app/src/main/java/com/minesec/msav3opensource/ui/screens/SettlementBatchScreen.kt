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
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minesec.msav3opensource.R
import com.minesec.msav3opensource.ui.helper.items.BatchSummaryCard
import com.minesec.msav3opensource.ui.helper.template.MsaUiStateHandler
import com.minesec.msav3opensource.ui.theme.MsaTheme
import com.theminesec.multiplatform.msa_core.feature.common.domain.models.enums.SettleStatus
import com.theminesec.multiplatform.msa_core.feature.common.domain.models.enums.SettleStatus.CREATED
import com.theminesec.multiplatform.msa_core.feature.common.domain.models.enums.SettleStatus.FAILED
import com.theminesec.multiplatform.msa_core.feature.common.domain.models.enums.SettleStatus.SETTLED
import com.theminesec.multiplatform.msa_core.feature.settlement.data.models.SettlementTypeRequest
import com.theminesec.multiplatform.msa_core.feature.settlement.presentation.SettlementAction
import com.theminesec.multiplatform.msa_core.feature.settlement.presentation.SettlementState
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SettlementBatchScreen(
    state: SettlementState, onTriggerAction: (SettlementAction) -> Unit
) = MsaUiStateHandler(
    state.isLoading, state.errorState,
    noDataFound = state.settlements.isEmpty() && !state.isLoading
) {

    var selectedTab by remember { mutableStateOf(SettlementTypeRequest.CURRENT) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(selectedTab) {
        onTriggerAction(SettlementAction.GetSettlement(selectedTab))
    }


    if (state.reportEmailSuccess) {
        scope.launch {
            snackbarHostState.showSnackbar("Settlement Share to email successfully ✅")
        }
    }
    Scaffold(
        modifier = Modifier
            .background(MsaTheme.colors.background)
            .statusBarsPadding()
            .safeDrawingPadding(),
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
                        text = if (selectedTab == SettlementTypeRequest.CURRENT) "Current" else "History",
                        color = MsaTheme.colors.foreground,
                        style = MsaTheme.typography.titleMedium,
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .widthIn(max = 600.dp)
                            .fillMaxWidth()
                            .padding(MsaTheme.spacing.sm),
                    ) {
                        Row(
                            modifier = Modifier
                                .shadow(
                                    elevation = 5.dp,
                                    shape = MsaTheme.shapes.medium,
                                )
                                .background(
                                    color = MsaTheme.colors.background,
                                    shape = MsaTheme.shapes.medium
                                )
                                .padding(MsaTheme.spacing.xs2)
                                .height(MsaTheme.iconSize.xl),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            SettlementToggleButton(
                                text = stringResource(R.string.current),
                                iconRes = R.drawable.current_settlement,
                                isSelected = selectedTab == SettlementTypeRequest.CURRENT,
                                onClick = { selectedTab = SettlementTypeRequest.CURRENT },
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(MsaTheme.spacing.xs2))
                            SettlementToggleButton(
                                text = stringResource(R.string.settled),
                                iconRes = R.drawable.settled_icon,
                                isSelected = selectedTab == SettlementTypeRequest.SETTLED,
                                onClick = { selectedTab = SettlementTypeRequest.SETTLED },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        },
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(MsaTheme.colors.background)
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier
                    .background(MsaTheme.colors.background)
                    .fillMaxSize()
            ) {
                items(state.settlements) { settleItem ->

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

@Composable
fun SettlementToggleButton(
    text: String,
    iconRes: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedGradientColors = listOf(MsaTheme.colors.primaryGradient, MsaTheme.colors.primary)
    val unSelectedBackgroundColor = MsaTheme.colors.background
    val contentColor =
        if (isSelected) MsaTheme.colors.primaryForeground else MsaTheme.colors.mutedForeground

    Surface(
        modifier = modifier
            .fillMaxSize()
            .clip(MsaTheme.shapes.medium)
            .then(
                if (isSelected) {
                    Modifier.background(brush = Brush.verticalGradient(selectedGradientColors))
                } else {
                    Modifier.background(color = unSelectedBackgroundColor)
                }
            )
            .clickable(onClick = onClick),
        color = Color.Transparent,
        contentColor = contentColor
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(iconRes),
                contentDescription = text,
                modifier = Modifier.size(MsaTheme.iconSize.sm),
                colorFilter = ColorFilter.tint(contentColor)
            )
            Spacer(modifier = Modifier.width(MsaTheme.spacing.xs))
            Text(text = text, style = MsaTheme.typography.bodyMedium)
        }
    }
}


@Preview
@Composable
fun PreviewSettlementBatchScreen() {
    SettlementBatchScreen(SettlementState()) {

    }
}