package com.minesec.msav3opensource.ui.screens.app2app

import androidx.compose.runtime.Composable
import com.minesec.msav3opensource.ui.template.MsaUiStateHandler
import com.theminesec.multiplatform.msa_core.feature.app2app.presenation.entryPoint.SplashApp2AppAction
import com.theminesec.multiplatform.msa_core.feature.app2app.presenation.entryPoint.SplashApp2AppState

@Composable
fun App2AppSplashScreen(
    state: SplashApp2AppState,
    triggerAction: (SplashApp2AppAction) -> Unit
) = MsaUiStateHandler(state.isLoading) {}