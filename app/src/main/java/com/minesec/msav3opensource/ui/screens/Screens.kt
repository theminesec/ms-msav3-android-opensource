package com.minesec.msav3opensource.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.theminesec.multiplatform.msa_core.feature.app2app.presenation.voidRefund.VoidRefundApp2AppAction
import com.theminesec.multiplatform.msa_core.feature.app2app.presenation.voidRefund.VoidRefundApp2AppState
import com.theminesec.multiplatform.msa_core.feature.app2app.presenation.warmup.WarmupApp2AppAction
import com.theminesec.multiplatform.msa_core.feature.app2app.presenation.warmup.WarmupApp2AppState
import com.theminesec.multiplatform.msa_core.feature.cardTapping.cardRefund.presentation.CardRefundScreen
import com.theminesec.multiplatform.msa_core.feature.cardTapping.paymentSelectionScreen.presentation.ConsumerQRScanAction
import com.theminesec.multiplatform.msa_core.feature.cardTapping.paymentSelectionScreen.presentation.ConsumerQRScanState
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
    override val consumerQRScanScreen =
        ComposableScreen<ConsumerQRScanState, ConsumerQRScanAction> { _, _ ->
            // Consumer QR scan uses camera - placeholder for opensource
            androidx.compose.foundation.layout.Box(
                modifier = androidx.compose.ui.Modifier.fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) { Text("Consumer QR Scan") }
        }
    override val processingInquiryScreen =
        ComposableScreen { s, a -> ProcessingInquiryScreenUI(s, a) }
    override val settingsScreen = ComposableScreen { s, a -> MainSettingScreen(s, a) }
    override val shopInfoScreen = ComposableScreen { s, a -> ShopInfoUIScreen(s, a) }
    override val activationApp2AppScreen =
        ComposableScreen { s, a -> App2AppActivationScreen(s, a) }

    override val saleApp2AppScreen =
        ComposableScreen { s, a -> App2AppSaleScreen(s, a) }
    override val queryApp2AppScreen = ComposableScreen { s, a -> App2AppQueryScreen(s, a) }
    override val settlementApp2AppScreen =
        ComposableScreen { s, a -> App2AppSettlementScreen(s, a) }
    override val voidRefundApp2AppScreen =
        ComposableScreen<VoidRefundApp2AppState, VoidRefundApp2AppAction> { _, _ ->
            // Void/Refund App2App - placeholder
            androidx.compose.foundation.layout.Box(
                modifier = androidx.compose.ui.Modifier.fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) { Text("Void/Refund") }
        }
    override val splashApp2AppScreen = ComposableScreen { s, a -> App2AppSplashScreen(s, a) }
    override val warmupApp2AppScreen =
        ComposableScreen<WarmupApp2AppState, WarmupApp2AppAction> { _, _ ->
            // Warmup App2App - placeholder
            androidx.compose.foundation.layout.Box(
                modifier = androidx.compose.ui.Modifier.fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) { Text("Warmup") }
        }
}