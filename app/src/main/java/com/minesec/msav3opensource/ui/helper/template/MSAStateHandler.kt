package com.minesec.msav3opensource.ui.helper.template

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.minesec.msav3opensource.ui.theme.MsaTheme
import com.theminesec.multiplatform.msa.template.MsaExceptionHandler
import com.theminesec.multiplatform.msa_core.common.domain.models.MSAException
import com.theminesec.multiplatform.msa_core.feature.home.presentation.ErrorState
import com.minesec.msav3opensource.R

@Composable
internal fun MsaUiStateHandler(
    isLoading: Boolean = false,
    errorState: ErrorState?=null,
    noDataFound: Boolean = false,
    onExceptionHandled: (MSAException) -> Unit = {},
    screenContent: @Composable (() -> Unit),
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        screenContent()
        if (noDataFound) {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.no_data_found),
                    color = MsaTheme.colors.primary,
                    style = MsaTheme.typography.bodyMedium
                )
            }
        }
        if (isLoading) AnimatedCircularProgress()

        // Optional: Handle exceptions
        MsaExceptionHandler(errorState, onExceptionHandled) {
            // retry action clicked....
            // TODO: look here ... how we can do that ??  \
        }
    }
}

