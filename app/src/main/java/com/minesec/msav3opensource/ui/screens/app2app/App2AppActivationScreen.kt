package com.minesec.msav3opensource.ui.screens.app2app

import androidx.compose.runtime.Composable
import com.minesec.msav3opensource.ui.helper.template.MsaUiStateHandler
import com.theminesec.multiplatform.msa_core.feature.app2app.presenation.activation.ActivationApp2AppAction
import com.theminesec.multiplatform.msa_core.feature.app2app.presenation.activation.ActivationApp2AppState

@Composable
fun App2AppActivationScreen(
    state: ActivationApp2AppState,
    triggerAction: (ActivationApp2AppAction) -> Unit
) = MsaUiStateHandler(state.isLoading) {}