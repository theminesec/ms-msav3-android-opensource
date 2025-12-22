package com.minesec.msav3opensource.ui.screens

import adaptiveSpacingHeight
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.minesec.msav3opensource.R
import com.minesec.msav3opensource.ui.helper.items.MSAButton
import com.minesec.msav3opensource.ui.helper.items.MSATextFieldDialog
import com.minesec.msav3opensource.ui.helper.template.MsaUiStateHandler
import com.minesec.msav3opensource.ui.helper.template.OriginalTransactionIdDialog
import com.minesec.msav3opensource.ui.helper.template.getDynamicFontSize
import com.minesec.msav3opensource.ui.theme.MsaTheme
import com.theminesec.multiplatform.msa_core.app.navigation.LocalWindowSize
import com.theminesec.multiplatform.msa_core.app.navigation.WindowHeightSize
import com.theminesec.multiplatform.msa_core.feature.cardTapping.cardRefund.presentation.CardRefundAction
import com.theminesec.multiplatform.msa_core.feature.cardTapping.cardRefund.presentation.CardRefundState

@SuppressLint("UnrememberedMutableState")
@Composable
fun CardRefundScreen(
    state: CardRefundState,
    triggerAction: (CardRefundAction) -> Unit
) = MsaUiStateHandler(state.isLoading, state.errorState) {
    var amount by rememberSaveable { mutableStateOf("") }
    val displayAmount = remember { DisplayAmount(amount) }
    //var selectedTab by remember { mutableStateOf("Payment") }
    val amountAlpha = remember(displayAmount.amount) {
        val isZeroValue = displayAmount.amount.isEmpty()
        if (isZeroValue) 0.6f else 1f
    }
    val currencySelected by mutableStateOf(state.currency)
    var merchantNote by remember { mutableStateOf("") }
    var showOriginatorIdDialog by remember { mutableStateOf(false) }

    val formattedAmount = displayAmount.formatAmountForDisplay(state.currency)
    val dynamicFontSize = getDynamicFontSize(formattedAmount)
    LaunchedEffect(formattedAmount) {
        amount = displayAmount.amount
    }

    if (showOriginatorIdDialog) {
        OriginalTransactionIdDialog(onConfirm = { trxId ->
            triggerAction(
                CardRefundAction.StartCardRefundRequest(
                    amount = displayAmount.formatAmountForDisplay(state.currency),
                    description = merchantNote,
                    originalTransactionID = trxId
                )
            )
        }, onDismiss = {
            showOriginatorIdDialog = false
        })
    }

    Scaffold(
        modifier = Modifier.background(MsaTheme.colors.primary)
            .statusBarsPadding()
            .background(MsaTheme.colors.background)
            .navigationBarsPadding(),
        containerColor = MsaTheme.colors.background,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.23f)
                    .background(MsaTheme.colors.primary)
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(adaptiveSpacingHeight(MsaTheme.spacing.xl2))
                        .padding(start = adaptiveSpacingHeight(MsaTheme.spacing.xs2)),
                    contentAlignment = Alignment.CenterStart
                ) {

                    Image(
                        painter = painterResource(R.drawable.back_arrow),
                        contentDescription = "Back",
                        modifier =  Modifier.padding(MsaTheme.spacing.sm)
                            .size(MsaTheme.iconSize.xs)
                            .clickable { triggerAction(CardRefundAction.OnBackClicked) }
                        //.align(Alignment.CenterStart)
                        ,
                        colorFilter = ColorFilter.tint(MsaTheme.colors.background)
                    )
                    Text(
                        text = stringResource(R.string.refund_label),
                        color = MsaTheme.colors.background,
                        style = MsaTheme.typography.titleMedium,
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(horizontal = MsaTheme.spacing.xs)
                        .wrapContentSize(Alignment.TopStart)
                        .align(Alignment.CenterHorizontally)
                        .alpha(amountAlpha),
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier
//                            .padding(3.dp)
                            .wrapContentHeight(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = currencySelected,
                            color = MsaTheme.colors.background,
                            style = MsaTheme.typography.labelMedium.copy(
                                fontSize = dynamicFontSize,
                                fontWeight = FontWeight.Bold
                            ),
                            //modifier = Modifier.align(Alignment.Top)
                        )
                        Text(
                            text = formattedAmount,//"0.00",
                            color = MsaTheme.colors.background,
                            style = MsaTheme.typography.labelMedium.copy(
                                fontSize = dynamicFontSize,
                                fontWeight = FontWeight.Bold
                            ),
                            maxLines = 2,//modifier = Modifier.align(Alignment.Top)
                        )
                    }
                    //Spacer(Modifier.height(adaptiveSpacingHeight(MsaTheme.spacing.xs)))

                }
                Box(
                    modifier = Modifier
                        .padding(horizontal = MsaTheme.spacing.xs)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    var showDialog by remember { mutableStateOf(false) }
                    if (merchantNote.isNotEmpty()) {
                        Row(
                            modifier = Modifier
                                .clickable(onClick = {
                                    showDialog = true
                                })
                                .wrapContentWidth()
                                .padding(MsaTheme.spacing.xs2),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = merchantNote,
                                color = MsaTheme.colors.background,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = MsaTheme.typography.bodyLarge,
                            )
                        }
                    } else {
                        Row(
                            modifier = Modifier
                                .clickable(
                                    enabled = displayAmount.amount.isNotEmpty(),
                                    onClick = {
                                        showDialog = true
                                    })
                                .padding(adaptiveSpacingHeight(MsaTheme.spacing.xs2))
                                .alpha(amountAlpha),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
//                                Image(
//                                    painter = painterResource(Res.drawable.circle_add),
//                                    contentDescription = "Add",
//                                    colorFilter = ColorFilter.tint(MsaTheme.colors.background)
//                                )
//                                Spacer(Modifier.width(adaptiveSpacingHeight(MsaTheme.spacing.xs)))
                            Text(
                                text = stringResource(R.string.add_description_button),
                                color = MsaTheme.colors.background,
                                style = MsaTheme.typography.bodyLarge,
                            )
                        }
                    }

                    if (showDialog) {
                        MSATextFieldDialog(
                            initialNote = merchantNote,
                            onAddNote = { newNote ->
                                if (newNote.isNotEmpty()) {
                                    merchantNote = newNote
                                } else {
                                    merchantNote =
                                        ""
                                }
                                showDialog = false //after adding/editing
                            },
                            onDismiss = {
                                showDialog = false // cancelled
                            },
                            title = stringResource(R.string.add_merchant_note),
                            dismissButtonText = stringResource(R.string.cancel_button),
                            confirmButtonText = stringResource(R.string.add_button)
                        )
                    }


                }
                if (LocalWindowSize.current.height == WindowHeightSize.Compact) {
                    Spacer( Modifier.height(MsaTheme.spacing.lg))
                } else {
                    Spacer( Modifier.height(MsaTheme.minTouchSize.md)) // medium, expanded
                }
            }

        },
        bottomBar = {
            Column {
                Column(
                    modifier =  Modifier.background(MsaTheme.colors.background)
                        .padding(MsaTheme.spacing.md)
                ) {
                    MSAButton(
                        text = stringResource(
                            R.string.charge_amount_button,
                            displayAmount.formatAmountForDisplay(state.currency)
                        ),
                        enabled = displayAmount.amount.isNotEmpty() && displayAmount.formatAmountForDisplay(
                            state.currency
                        ) != "0.00",
                        onClick = {
                            showOriginatorIdDialog = true
                        },

                        )
                }
//                MSABottomBar(
//                    selectedItem = selectedTab,
//                    onItemSelected = { selectedTab = it },
//                )
            }
        }
    ) { paddingValues ->

        Column(
            modifier =  Modifier.padding(paddingValues)
                .background(MsaTheme.colors.primary)
                .clip(
                    RoundedCornerShape(
                        topStart = MsaTheme.spacing.md,//16
                        topEnd = MsaTheme.spacing.md
                    )
                )
                .background(MsaTheme.colors.background)
                .fillMaxSize()

                .padding(
                    top = MsaTheme.spacing.md,
                    start = MsaTheme.spacing.md,
                    end = MsaTheme.spacing.md
                )
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                //.padding(16.dp)
            ) {
                MSAPinPad(
                    modifier = Modifier.fillMaxSize(),
                    displayAmount = displayAmount,
                    onValueChanged = { println("Current amount: ${displayAmount.formatAmountForDisplay(
                        state.currency
                    )
                    }") }
                )
            }

        }
    }
}

@Preview()
@Composable
fun PreviewCardRefund() {
    CardRefundScreen(CardRefundState()) {

    }
}
