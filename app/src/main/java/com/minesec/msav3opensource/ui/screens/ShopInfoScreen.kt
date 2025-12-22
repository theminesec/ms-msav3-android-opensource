package com.minesec.msav3opensource.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minesec.msav3opensource.R
import com.minesec.msav3opensource.ui.helper.template.KeyValueRow
import com.minesec.msav3opensource.ui.helper.template.MsaUiStateHandler
import com.minesec.msav3opensource.ui.theme.MsaTheme
import com.theminesec.multiplatform.msa_core.feature.settings.presenation.SettingsAction
import com.theminesec.multiplatform.msa_core.feature.settings.presenation.SettingsState


@Composable
fun ShopInfoUIScreen(
    state: SettingsState,
    triggerAction: (SettingsAction) -> Unit
) = MsaUiStateHandler(state.isLoading, state.errorState) {
    Scaffold(
        modifier = Modifier.background(MsaTheme.colors.background)
            .statusBarsPadding()
            .navigationBarsPadding(),
        topBar = { ShopInfoTopBar(onBack = { triggerAction(SettingsAction.BackClicked) }) },
        bottomBar = {}
    ) { paddingValues ->
        Spacer(modifier = Modifier.background(MsaTheme.colors.accent).fillMaxWidth().height(15.dp))
        ShopInfoContent(
            modifier = Modifier.padding(paddingValues),
            state = state
        )
    }
}

@Composable
private fun ShopInfoTopBar(onBack: () -> Unit) {
    Box(
        modifier = Modifier.background(MsaTheme.colors.background)
            .fillMaxWidth()
            .height(MsaTheme.spacing.xl2)
            .padding(start = MsaTheme.spacing.xs2),
        contentAlignment = Alignment.CenterStart
    ) {
        Image(
            painter = painterResource(R.drawable.back_arrow),
            contentDescription = "Back",
            modifier = Modifier.padding(MsaTheme.spacing.sm)
                .size(MsaTheme.iconSize.xs)
                .clickable(onClick = onBack),
            colorFilter = ColorFilter.tint(MsaTheme.colors.foreground)
        )

        Text(
            text = stringResource(R.string.shop_information_setting_title),
            color = MsaTheme.colors.foreground,
            style = MsaTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun ShopInfoContent(
    modifier: Modifier = Modifier,
    state: SettingsState
) {
    val deviceInfo = state.deviceInfo

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MsaTheme.colors.background)
            .padding(horizontal = MsaTheme.spacing.md, vertical = MsaTheme.spacing.sm),
        verticalArrangement = Arrangement.spacedBy(MsaTheme.spacing.sm)
    ) {
        item {
            KeyValueRow(
                key = R.string.merchant_name,
                value = deviceInfo?.mchName.orEmpty().ifEmpty { "No merchant name available" }
            )
        }

        item {
            KeyValueRow(
                key = R.string.mid,
                value = deviceInfo?.mcc.orEmpty().ifEmpty { "No merchant Id available" }
            )
        }

        item {
            KeyValueRow(
                key = R.string.device_id,
                value = deviceInfo?.mchDeviceID.orEmpty().ifEmpty { "No merchant DeviceID available" }
            )
        }

        item {
            KeyValueRow(
                key = R.string.business_address,
                // FIX: Simplified null/empty check
                value = deviceInfo?.mchContactAddress.orEmpty().ifEmpty { "No merchant Contact Address available" }
            )
        }
    }
}

@Preview
@Composable
fun PreviewShopInfoScreen() {
    ShopInfoUIScreen(SettingsState()) {}
}
