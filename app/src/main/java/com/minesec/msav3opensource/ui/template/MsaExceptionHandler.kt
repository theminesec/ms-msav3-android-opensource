package com.minesec.msav3opensource.ui.template

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.theminesec.multiplatform.msa_core.common.domain.models.MSAException

@Composable
internal fun MsaExceptionHandler(
    exception: MSAException,
    onFinish: (MSAException) -> Unit,
    dialogVisible: Boolean = true,
    onRetryAction: () -> Unit = {}
) {

    var dialogVisibleState by remember { mutableStateOf(dialogVisible) }
    when (exception) {

        is MSAException.NetworkError.Unauthorized -> {
            ShowUnAuthorizedDialog(dialogVisibleState) {
                dialogVisibleState = false
                onFinish(exception)
            }
        }
        is MSAException.Local.FeatureUnavailable -> {
            FeatureUnavailableDialog(
                dialogVisibleState, "Unavailable", exception.errorMessage,
                onDismiss = {
                    onFinish(exception)
                }
            )
        }

        else -> {
            ShowAlertDialog(exception, dialogVisibleState, onRetry = {
                onRetryAction()
                dialogVisibleState = false
            }) {
                dialogVisibleState = false
                onFinish(exception)
            }
        }
    }
}