package com.minesec.msav3opensource.ui.screens

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minesec.msav3opensource.R
import com.minesec.msav3opensource.ui.helper.items.MSACard
import com.minesec.msav3opensource.ui.helper.template.AmountDisplay
import com.minesec.msav3opensource.ui.helper.template.MsaUiStateHandler
import com.minesec.msav3opensource.ui.models.CardTab
import com.minesec.msav3opensource.ui.models.getPaymentSchemaIconRes
import com.minesec.msav3opensource.ui.theme.MsaTheme
import com.theminesec.multiplatform.msa_core.app.navigation.LocalWindowSize
import com.theminesec.multiplatform.msa_core.app.navigation.WindowWidthSize
import com.theminesec.multiplatform.msa_core.feature.cardTapping.paymentSelectionScreen.presentation.PaymentSelectionAction
import com.theminesec.multiplatform.msa_core.feature.cardTapping.paymentSelectionScreen.presentation.PaymentSelectionState
import com.theminesec.multiplatform.msa_core.feature.common.domain.models.enums.PaymentMethod

@Composable
fun PaymentSelectionScree(
    state: PaymentSelectionState, triggerAction: (PaymentSelectionAction) -> Unit
) = MsaUiStateHandler(state.isLoading, state.errorState) {

    Scaffold(
        modifier = Modifier
            .background(MsaTheme.colors.primary.copy(0.1f))
            .statusBarsPadding()
            .background(MsaTheme.colors.background)
            .navigationBarsPadding(),
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .background(MsaTheme.colors.primary.copy(0.1f))
                    .height(MsaTheme.spacing.xl2)
                    .padding(start = MsaTheme.spacing.md),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.back_arrow),
                    contentDescription = "Back",
                    modifier = Modifier
                        .clickable { triggerAction(PaymentSelectionAction.OnBackClicked) }
                        .align(Alignment.CenterStart),
                    colorFilter = ColorFilter.tint(MsaTheme.colors.foreground)
                )
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MsaTheme.colors.background)
                    .padding(MsaTheme.spacing.sm),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.copyright_footer),
                    color = MsaTheme.colors.mutedForeground,
                    style = MsaTheme.typography.labelMedium,
                )
            }
        }

    ) { paddingValues ->
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .padding(paddingValues)
                .fillMaxSize()
                .background(MsaTheme.colors.background)
            //.verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White) // color will be the same no matter what the background color is( in light or dark theme)
                    .background(MsaTheme.colors.primary.copy(alpha = 0.1f))
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
//                Text(
//                    text = stringResource(R.string.sale_label),
//                    style = MsaTheme.typography.titleMedium,
//                    color = MsaTheme.colors.primary
//                )
                AmountDisplay(
                    currency = state.paymentSelectionArg?.deviceInfo?.currency ?: "",
                    amount = state.paymentSelectionArg?.amount ?: ""
                )
                if (state.paymentSelectionArg?.descriptionNote?.isNotEmpty() == true) {
                    Row {
                        Image(
                            painter = painterResource(R.drawable.circle_add),
                            contentDescription = "Add",
                            colorFilter = ColorFilter.tint(MsaTheme.colors.primary),
                            modifier = Modifier.size(MsaTheme.iconSize.sm)
                        )
                        Spacer(Modifier.width(MsaTheme.spacing.xs))
                        Text(
                            text = state.paymentSelectionArg?.descriptionNote ?: "",
                            style = MsaTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = MsaTheme.colors.secondary
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .background(MsaTheme.colors.primary.copy(0.1f))
                    .clip(
                        RoundedCornerShape(
                            topStart = MsaTheme.spacing.md,
                            topEnd = MsaTheme.spacing.md
                        )
                    )
                    .background(MsaTheme.colors.background)
            ) {
                Spacer(Modifier.height(adaptiveSpacingHeight(10.dp)))
                var selectedTab by remember { mutableStateOf(CardTab.CARD) }
                var selectedPaymentMethod by remember { mutableStateOf<PaymentMethod?>(null) }
                SelectInstrumentGrid(
                    state,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    onOperationSelected = { operation ->
                        selectedTab = operation
                        when (operation) {
                            CardTab.CARD -> triggerAction(PaymentSelectionAction.OnCardSelected)
                            CardTab.MERCHANT_QR -> triggerAction(PaymentSelectionAction.OnMerchantQRSelected)
                            CardTab.CONSUMER_QR -> triggerAction(PaymentSelectionAction.ConsumerScanClicked)
                            CardTab.ALTERNATIVE -> triggerAction(PaymentSelectionAction.AlternativeClicked)
                            else -> {}
                        }
                    },
                )


                if (selectedTab == CardTab.MERCHANT_QR) {
                    Spacer(Modifier.height(MsaTheme.spacing.md))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        if (state.paymentQRSupported.isEmpty()) {
                            Text(
                                text = "No payment methods supported yet",
                                style = MsaTheme.typography.labelMedium,
                                color = MsaTheme.colors.mutedForeground,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = MsaTheme.spacing.lg),
                                textAlign = TextAlign.Center
                            )
                        } else {
                            PaymentMethodsList(
                                state.paymentQRSupported,
                                onPaymentMethodClick = { method ->
                                    triggerAction(
                                        PaymentSelectionAction.PaymentMethodSelected(
                                            method
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }


        }
    }
}

@Composable
fun PaymentMethodsList(
    paymentMethods: List<PaymentMethod>,
    onPaymentMethodClick: (PaymentMethod) -> Unit
) {
    val columns = when (LocalWindowSize.current.width) {
        WindowWidthSize.Compact -> 1
        WindowWidthSize.Medium -> 2
        WindowWidthSize.Expanded -> 4
        else -> 1
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            modifier = Modifier
                .fillMaxWidth(),

            horizontalArrangement = Arrangement.spacedBy(MsaTheme.spacing.md)
        ) {
            items(paymentMethods) { method ->
                Column(modifier = Modifier.fillMaxWidth()) {
                    PaymentMethodItem(method = method, onClick = onPaymentMethodClick)
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        color = MsaTheme.colors.ring.copy(alpha = 0.4f),
                        thickness = 1.dp
                    )
                }
            }
        }
    }
}

@Composable
fun PaymentMethodItem(
    method: PaymentMethod,
    onClick: (PaymentMethod) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(method) }
            .padding(vertical = MsaTheme.spacing.md),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MsaTheme.spacing.md)
    ) {
        Image(
            painter = painterResource(method.getPaymentSchemaIconRes()),
            contentDescription = method.name,
            modifier = Modifier.size(MsaTheme.iconSize.md)
        )
        Text(
            text = method.name,
            style = MsaTheme.typography.bodySmall,
            color = MsaTheme.colors.foreground
        )
    }
}

@Composable
fun SelectInstrumentGrid(
    state: PaymentSelectionState,
    modifier: Modifier = Modifier,
    onOperationSelected: (CardTab) -> Unit,
) {
    val operations = CardTab.paymentSelectionTabs.filterNot { op ->
        (op == CardTab.MERCHANT_QR && !state.isMerchantQRCodeEnabled) ||
                (op == CardTab.CONSUMER_QR && !state.isConsumerQRCodeEnabled) ||
                (op == CardTab.ALTERNATIVE && !state.isAlternativeEnabled)
    }
    var selectedButton by remember { mutableStateOf<CardTab?>(null) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier
            .background(MsaTheme.colors.background),
        verticalArrangement = Arrangement.spacedBy(MsaTheme.spacing.md),
        horizontalArrangement = Arrangement.spacedBy(MsaTheme.spacing.md)
    ) {

        items(operations) { itemTab ->
            MSACard(
                button = itemTab,
                isSelected = selectedButton == itemTab,
                onClick = {
                    selectedButton = itemTab
                    onOperationSelected(itemTab)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            )
        }

        if (operations.size % 2 != 0) {
            item {
                Spacer(modifier = Modifier)
            }
        }
    }
}

@Preview
@Composable
fun PreviewPaymentSelection() {
    PaymentSelectionScree(state = PaymentSelectionState(), triggerAction = {})
}