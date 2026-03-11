package com.minesec.msav3opensource.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.minesec.msav3opensource.ui.screens.app2app.App2AppActivationScreen
import com.minesec.msav3opensource.ui.screens.app2app.App2AppQueryScreen
import com.minesec.msav3opensource.ui.screens.app2app.App2AppSaleScreen
import com.minesec.msav3opensource.ui.screens.app2app.App2AppSettlementScreen
import com.minesec.msav3opensource.ui.screens.app2app.App2AppSplashScreen
import com.minesec.msav3opensource.ui.theme.MsaTheme
import com.theminesec.multiplatform.msa_core.app.navigation.AppScreen
import com.theminesec.multiplatform.msa_core.app.navigation.ComposableScreen
import com.theminesec.multiplatform.msa_core.app.navigation.ScreenProviders
import com.theminesec.multiplatform.msa_core.feature.cardTapping.cardRefund.presentation.CardRefundScreen
import com.theminesec.multiplatform.msa_core.feature.cardTapping.preAuth.presentation.PreAuthScreen
import com.theminesec.multiplatform.msa_core.feature.cardTapping.sale.presentation.SaleScreen
import com.theminesec.multiplatform.msa_core.feature.home.presentation.HomeScreen
import com.theminesec.multiplatform.msa_core.feature.landing.presentation.LandingScreen
import com.theminesec.multiplatform.msa_core.feature.login.presentation.LoginScreen


val providers = object : ScreenProviders {
    override val statusBarColor: @Composable (AppScreen) -> Color = { screenType ->
        when (screenType) {
            LandingScreen -> MsaTheme.colors.mutedForeground
            LoginScreen -> MsaTheme.colors.background
            HomeScreen -> MsaTheme.colors.accent
            SaleScreen, PreAuthScreen, CardRefundScreen -> MsaTheme.colors.primary
            else -> MsaTheme.colors.background
        }
    }

    override val loginScreen = ComposableScreen { s, a -> LoginScreenUI(s, a) }
    override val landingScreen = ComposableScreen { s, a -> LandingScreenUI(s, a) }
    override val homeScreen = ComposableScreen { s, a -> HomeScreenUI(s, a) }
    override val historyScreen = ComposableScreen { s, a -> HistoryScreen(s, a) }
    override val trxDetailsScreen = ComposableScreen { s, a -> TransactionDetailsScreen(s, a) }
    override val trxResultScreen = ComposableScreen { s, a -> TransactionResultScreen(s, a) }
    override val trxReceiptScreen = ComposableScreen { s, a -> ReceiptUIScreen(s, a) }
    override val settlementScreen = ComposableScreen { s, a -> SettlementBatchScreen(s, a) }
    override val settlementDetailsScreen =
        ComposableScreen { s, a -> SettlementBatchDetailsScreen(s, a) }
    override val settlementResultScreen =
        ComposableScreen { s, a -> SettlementBatchResultScreen(s, a) }
    override val saleScreen = ComposableScreen { s, a -> SaleScreen(s, a) }
    override val preAuthScreen = ComposableScreen { s, a -> PreAuthScreen(s, a) }
    override val cardRefundScreen = ComposableScreen { s, a -> CardRefundScreen(s, a) }
    override val paymentSelectionScreen = ComposableScreen { s, a -> PaymentSelectionScree(s, a) }
    override val paymentQRcodeSelectionScreen =
        ComposableScreen { s, a -> PaymentQRSelectionScreen(s, a) }
    override val settingsScreen = ComposableScreen { s, a -> MainSettingScreen(s, a) }
    override val shopInfoScreen = ComposableScreen { s, a -> ShopInfoUIScreen(s, a) }
    override val activationApp2AppScreen =
        ComposableScreen { s, a -> App2AppActivationScreen(s, a) }

    override val saleApp2AppScreen =
        ComposableScreen { s, a -> App2AppSaleScreen(s, a) }
    override val queryApp2AppScreen = ComposableScreen { s, a -> App2AppQueryScreen(s, a) }
    override val settlementApp2AppScreen =
        ComposableScreen { s, a -> App2AppSettlementScreen(s, a) }
    override val splashApp2AppScreen = ComposableScreen { s, a -> App2AppSplashScreen(s, a) }
}