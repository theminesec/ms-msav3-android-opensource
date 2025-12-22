package com.minesec.msav3opensource.ui.screens.app2app

import androidx.compose.runtime.Composable
import com.minesec.msav3opensource.ui.helper.template.MsaUiStateHandler
import com.theminesec.multiplatform.msa_core.feature.app2app.presenation.settlement.SettlementApp2AppAction
import com.theminesec.multiplatform.msa_core.feature.app2app.presenation.settlement.SettlementApp2AppState

@Composable
fun App2AppSettlementScreen(
    state: SettlementApp2AppState,
    triggerAction: (SettlementApp2AppAction) -> Unit
) = MsaUiStateHandler(state.isLoading) {}