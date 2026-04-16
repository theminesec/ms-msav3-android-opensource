package com.minesec.msav3opensource.ui.screens.app2app

import androidx.compose.runtime.Composable
import com.minesec.msav3opensource.ui.template.MsaUiStateHandler
import com.theminesec.multiplatform.msa_core.feature.app2app.presenation.sale.SaleApp2AppAction
import com.theminesec.multiplatform.msa_core.feature.app2app.presenation.sale.SaleApp2AppState

@Composable
fun App2AppSaleScreen(
    state: SaleApp2AppState,
    triggerAction: (SaleApp2AppAction) -> Unit
) = MsaUiStateHandler(state.isLoading) {}