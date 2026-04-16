package com.minesec.msav3opensource.ui.screens.app2app

import androidx.compose.runtime.Composable
import com.minesec.msav3opensource.ui.template.MsaUiStateHandler
import com.theminesec.multiplatform.msa_core.feature.app2app.presenation.queryTransaction.QueryApp2AppAction
import com.theminesec.multiplatform.msa_core.feature.app2app.presenation.queryTransaction.QueryApp2AppState

@Composable
fun App2AppQueryScreen(
    state: QueryApp2AppState,
    triggerAction: (QueryApp2AppAction) -> Unit
) = MsaUiStateHandler(state.isLoading) {}