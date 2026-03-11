package com.minesec.msav3opensource.ui.screens


import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.minesec.msav3opensource.R
import com.minesec.msav3opensource.ui.helper.items.MSACard
import com.minesec.msav3opensource.ui.helper.template.MSABottomBar
import com.minesec.msav3opensource.ui.models.BottomNavTab
import com.minesec.msav3opensource.ui.models.CardTab
import com.minesec.msav3opensource.ui.helper.template.MsaUiStateHandler
import com.minesec.msav3opensource.ui.helper.template.PasscodeDialog
import com.minesec.msav3opensource.ui.helper.template.TransactionAuthCardVoidDialog
import com.theminesec.multiplatform.msa_core.feature.home.presentation.HomeAction
import com.theminesec.multiplatform.msa_core.feature.home.presentation.HomeState
import com.minesec.msav3opensource.ui.theme.MsaTheme


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HomeScreenUI(
    state: HomeState, triggerAction: (HomeAction) -> Unit
) = MsaUiStateHandler(isLoading = state.isLoading, errorState = state.errorState) {
    var selectedTab by remember { mutableStateOf(BottomNavTab.Payment) }
    var showPasscodeDialog by remember { mutableStateOf(false) }
    var showCardVoidDialog by remember { mutableStateOf(false) }
    val uriHandler = LocalUriHandler.current

    BackHandler(enabled = true) {
        // do nothing → disables back press
    }
    Scaffold(
        modifier = Modifier
            .statusBarsPadding(),
        bottomBar = {
            MSABottomBar(
                selectedItem = selectedTab,
                onBottomBarNavigate = { tab ->
                    selectedTab = tab
                    when (tab) {
                        BottomNavTab.Payment -> {}
                        BottomNavTab.Help -> {
                            uriHandler.openUri(state.deviceInfo?.helpSupportUrl ?: "")
                            selectedTab = BottomNavTab.Payment
                        }
                         BottomNavTab.Settings -> triggerAction(HomeAction.NavigateToSettings)
                    }
                },
                modifier = Modifier.navigationBarsPadding()
            )
        }
    ) { paddingValues ->



        if (showPasscodeDialog) {
            PasscodeDialog(
                onConfirm = { enteredPasscode ->
                    showPasscodeDialog = false
                    triggerAction(HomeAction.NavigateToCardRefund(enteredPasscode))
                },
                onDismiss = {
                    showPasscodeDialog = false
                },
            )
        }


        if (showCardVoidDialog) {
            TransactionAuthCardVoidDialog(
                onConfirm = { enteredPasscode,trxId -> },
                onDismiss = {
                    showCardVoidDialog = false
                }
            )
        }

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MsaTheme.colors.accent)
                //.verticalScroll(rememberScrollState())
                .padding(MsaTheme.spacing.md)

        ) {
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth(0.4f)
//                    .height(60.dp),
//                contentAlignment = Alignment.Center
//            ) {
//                AsyncImage(
//                    model = state.deviceInfo?.mchLogo,
//                    contentDescription = state.deviceInfo?.mchName,
//                    placeholder = painterResource(Res.drawable.minesec_logo_dark),
//                    error = painterResource(Res.drawable.minesec_logo_dark),
//                    contentScale = ContentScale.FillBounds,
//                    modifier = Modifier
//                        .fillMaxHeight()
//                        .clip(RoundedCornerShape(16.dp))
//                )
//            }

            Spacer(Modifier.height(MsaTheme.spacing.lg))
            Text(
                text = stringResource(R.string.hello),
                style = MsaTheme.typography.headlineSmall,
                color = MsaTheme.colors.foreground,
//                modifier = Modifier.padding(start = MsaTheme.spacing.xs)
            )
            Text(
                text = state.deviceInfo?.mchName ?: "",
                style = MsaTheme.typography.headlineSmall,
                color = MsaTheme.colors.foreground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
//                    modifier = Modifier.weight(1f)
            )

            Spacer(Modifier.height(MsaTheme.spacing.lg))
            HomeScreenGrid(
                state,
                modifier = Modifier
                    .fillMaxSize(),
                onOperationSelected = { operation ->
                    when (operation) {
                        CardTab.SALE -> triggerAction(HomeAction.NavigateToSale)
                        CardTab.AUTH -> triggerAction(HomeAction.NavigateToPreAuth)
                        CardTab.REFUND -> showPasscodeDialog = true
                        CardTab.VOID -> showCardVoidDialog=true
                        CardTab.HISTORY -> triggerAction(HomeAction.NavigateToHistory)
                        CardTab.SETTLEMENT -> triggerAction(HomeAction.NavigateToSettlement)
                        else -> {}
                    }
                }
            )
        }
    }
}

@Composable
fun HomeScreenGrid(
    state: HomeState,
    modifier: Modifier = Modifier,
    onOperationSelected: (CardTab) -> Unit
) {
    val operations = CardTab.homeTabs.filterNot { op ->
        (op == CardTab.REFUND && !state.isCardRefundEnable) ||
                (op == CardTab.VOID && !state.isCardVoidEnable) ||
                (op == CardTab.AUTH && !state.isCardAuthEnable)
    }
    var selectedButton by remember { mutableStateOf<CardTab?>(null) }
    state.errorState?.let {
        selectedButton = null
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier
            .background(MsaTheme.colors.accent),
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
                modifier = Modifier.fillMaxWidth()
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
fun PreviewHomeScreen() {
    HomeScreenUI(
        state = HomeState(), {}
    )
}