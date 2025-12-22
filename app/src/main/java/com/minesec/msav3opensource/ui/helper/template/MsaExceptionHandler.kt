package com.theminesec.multiplatform.msa.template

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.minesec.msav3opensource.ui.helper.template.FeatureUnavailableDialog
import com.minesec.msav3opensource.ui.helper.template.ShowAlertDialog
import com.minesec.msav3opensource.ui.helper.template.ShowUnAuthorizedDialog
import com.theminesec.multiplatform.msa_core.common.domain.models.MSAException
import com.theminesec.multiplatform.msa_core.feature.home.presentation.ErrorState

@Composable
internal fun MsaExceptionHandler(
    errorState: ErrorState?,
    onFinish: (MSAException) -> Unit,
    onRetryAction: () -> Unit = {}
) {
    var currentException by remember { mutableStateOf<MSAException?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(errorState?.id) {
        if (errorState != null) {
            currentException = errorState.exception
            showDialog = true
        } else {
            showDialog = false
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            showDialog = false
        }
    }

    val handleDismiss = {
        showDialog = false
        currentException?.let { onFinish(it) }
        currentException = null
    }

    when (currentException) {
        null -> {}

        is MSAException.NetworkError.Unauthorized -> {
            ShowUnAuthorizedDialog(showDialog, onDismiss = handleDismiss)
        }

        is MSAException.Local.FeatureUnavailable -> {
            FeatureUnavailableDialog(
                showDialog,
                "Unavailable",
                currentException!!.errorMessage,
                onDismiss = handleDismiss
            )
        }

        else -> {
            ShowAlertDialog(
                currentException!!,
                showDialog,
                onRetry = {
                    onRetryAction()
                    handleDismiss()
                },
                onDismiss = handleDismiss
            )
        }
    }
}