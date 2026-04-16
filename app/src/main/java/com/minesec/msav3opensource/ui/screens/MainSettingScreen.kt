package com.minesec.msav3opensource.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import com.minesec.msav3opensource.R
import com.minesec.msav3opensource.ui.helper.items.LogoutConfirmationDialog
import com.minesec.msav3opensource.ui.template.FeatureUnavailableDialog
import com.minesec.msav3opensource.ui.template.MsaUiStateHandler
import com.minesec.msav3opensource.ui.helper.template.SettingsItem
import com.minesec.msav3opensource.ui.theme.MsaTheme
import com.theminesec.multiplatform.msa_core.feature.settings.presenation.SettingsAction
import com.theminesec.multiplatform.msa_core.feature.settings.presenation.SettingsState


@Composable
fun MainSettingScreen(
    state: SettingsState, triggerAction: (SettingsAction) -> Unit
) = MsaUiStateHandler(state.isLoading, state.errorState) {

    var showBiometricDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    // === Dialogs ===
    FeatureUnavailableDialog(
        showBiometricDialog,
        "Biometric Unavailable",
        state.errorState?.exception?.errorMessage,
        onDismiss = { showBiometricDialog = false })

    if (showLogoutDialog) {
        LogoutConfirmationDialog(onCancelClick = { showLogoutDialog = false }, onLogoutClick = {
            showLogoutDialog = false
            triggerAction(SettingsAction.Logout)
        })
    }

    Scaffold(
        modifier = Modifier
            .background(MsaTheme.colors.background)
            .statusBarsPadding()
            .navigationBarsPadding(),
        topBar = {
            SettingsTopBar(
                title = stringResource(R.string.settings_title),
                onBackClick = { triggerAction(SettingsAction.BackClicked) })
        }) { paddingValues ->
        SettingsContent(
            state = state,
            onShowLogoutDialog = { showLogoutDialog = it },
            triggerAction = triggerAction,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun SettingsTopBar(title: String, onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .background(MsaTheme.colors.background)
            .fillMaxWidth()
            .height(MsaTheme.spacing.xl2)
            .padding(start = MsaTheme.spacing.xs2),
        contentAlignment = Alignment.CenterStart
    ) {
        Image(
            painter = painterResource(R.drawable.back_arrow),
            contentDescription = "Back",
            modifier = Modifier
                .padding(MsaTheme.spacing.sm)
                .size(MsaTheme.iconSize.xs)
                .clickable { onBackClick() },
            colorFilter = ColorFilter.tint(MsaTheme.colors.foreground)
        )

        Text(
            text = title,
            color = MsaTheme.colors.foreground,
            style = MsaTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

// --------------------
// Main Content Section
// --------------------
@Composable
private fun SettingsContent(
    state: SettingsState,
    onShowLogoutDialog: (Boolean) -> Unit,
    triggerAction: (SettingsAction) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MsaTheme.colors.accent)
    ) {
        // ==== App Settings ====
        item { SectionHeader(R.string.app_settings_section_title) }

        item {
            SettingsItem(
                icon = painterResource(R.drawable.terminal_app),
                title = stringResource(R.string.terminal_app_setting_title),
                onClick = {},
                endContent = {
                    Icon(
                        painter = painterResource(R.drawable.arrow_right),
                        contentDescription = null,
                        modifier = Modifier.size(MsaTheme.iconSize.xs),
                        tint = MsaTheme.colors.accentForeground
                    )
                }
            )
        }

        if (state.isBiometricSupported) {
            item {
                BiometricSettingItem(
                    isEnabled = state.isBiometricEnabled, onToggle = { enabled ->
                        triggerAction(
                            if (enabled) SettingsAction.ActiveBiometric
                            else SettingsAction.DissActiveBiometric
                        )
                    })
            }
        }

        item {
            PayServerSettingItem(
                isPayServerEnabled = state.isPayServerEnabled,
                isPayServerRunning = state.isPayServerRunning,
                payServerAddress = state.payServerAddress,
                onAction = triggerAction
            )
        }


        // ==== Language ====
        item {
            LanguageSettingItem(
                currentLanguage = state.currentLanguage.signature,
                availableLanguages = state.languages.map { it.name },
                onLanguageSelected = { lang ->
                    triggerAction(
                        SettingsAction.OnSelectLanguageClicked(
                            state.languages.first { it.name == lang })
                    )
                })
        }

        // ==== Shop ====
        item { SectionHeader(R.string.shop_section_title) }

        item {
            SettingsItem(
                icon = painterResource(R.drawable.shop_info),
                title = stringResource(R.string.shop_information_setting_title),
                onClick = { triggerAction(SettingsAction.NavigateToShopInfo) },
                endContent = {
                    Icon(
                        painter = painterResource(R.drawable.arrow_right),
                        contentDescription = null,
                        modifier = Modifier.size(MsaTheme.iconSize.xs),
                        tint = MsaTheme.colors.accentForeground
                    )
                }
            )
        }

        // ==== Account ====
        item { SectionHeader(R.string.account_section_title) }

        item {
            SettingsItem(
                icon = painterResource(R.drawable.profile_icon),
                title = state.deviceInfo?.mchName ?: "".ifEmpty { "No merchant name available" },
                onClick = {},
                endContent = { })
        }

        item {
            SettingsItem(
                icon = painterResource(R.drawable.email),
                title = state.deviceInfo?.mchEmail.orEmpty()
                    .ifEmpty { "No merchant Email available" },
                onClick = {},
                endContent = { })
        }

        // ==== Logout ====
        item {
            SettingsItem(
                icon = painterResource(R.drawable.logout),
                title = stringResource(R.string.logout_button),
                onClick = { onShowLogoutDialog(true) },
                endContent = { ArrowIcon() })
        }
    }
}

@Composable
fun PayServerSettingItem(
    isPayServerEnabled: Boolean,
    isPayServerRunning: Boolean,
    payServerAddress: String,
    onAction: (SettingsAction) -> Unit
) {
    if (!isPayServerEnabled) return

    var expanded by remember { mutableStateOf(false) }

    SettingsItem(
        icon = painterResource(R.drawable.wechat_pay_icon),
        title = stringResource(R.string.payserver_status),
        onClick = { expanded = true },
        endContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (isPayServerRunning) "On" else "Off",
                    style = MsaTheme.typography.bodyLarge,
                    color = MsaTheme.colors.foreground
                )

                Spacer(Modifier.width(MsaTheme.spacing.xs))

                Icon(
                    painter = painterResource(R.drawable.arrow_right),
                    contentDescription = null,
                    modifier = Modifier.size(MsaTheme.iconSize.xs),
                    tint = MsaTheme.colors.accentForeground
                )

                DropdownMenu(
                    expanded = expanded, onDismissRequest = { expanded = false },
                    containerColor = MsaTheme.colors.background
                ) {
                    listOf(1, 2).forEach {
                        DropdownMenuItem(text = { Text(if (it == 1) "ON" else "OFF") }, onClick = {
                            expanded = false
                            onAction(
                                if (it == 1) SettingsAction.StartPayServer
                                else SettingsAction.StopPayServer
                            )
                        })
                    }
                }
            }
        })

    if (isPayServerRunning) CenteredText(payServerAddress)
}

@Composable
fun BiometricSettingItem(
    isEnabled: Boolean, onToggle: (Boolean) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    SettingsItem(
        icon = painterResource(R.drawable.fingerprint_icon_button),
        title = stringResource(R.string.activate_biometrics),
        onClick = { onToggle(!isEnabled) },
        endContent = {
            Switch(
                checked = isEnabled,
                onCheckedChange = {
                    onToggle(!isEnabled)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MsaTheme.colors.accent,
                    checkedTrackColor = MsaTheme.colors.primary,
                    uncheckedThumbColor = MsaTheme.colors.accent,
                    uncheckedTrackColor = MsaTheme.colors.muted
                )
            )
        },
        modifier = Modifier.clickable(
            enabled = false,
            interactionSource = interactionSource,
            indication = null,
            onClickLabel = "Toggle biometric setting",
            role = Role.Switch,
            onClick = { }
        )
    )
}

@Composable
fun LanguageSettingItem(
    currentLanguage: String, availableLanguages: List<String>, onLanguageSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    SettingsItem(
        icon = painterResource(R.drawable.language_icon),
        title = stringResource(R.string.language_label),
        onClick = { expanded = true },
        endContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = currentLanguage,
                    style = MsaTheme.typography.bodyMedium,
                    color = MsaTheme.colors.foreground.copy(alpha = 0.8f),
                    modifier = Modifier.padding(end = MsaTheme.spacing.xs)
                )

                Icon(
                    painter = painterResource(R.drawable.arrow_right),
                    contentDescription = null,
                    modifier = Modifier.size(MsaTheme.iconSize.xs),
                    tint = MsaTheme.colors.accentForeground
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .background(MsaTheme.colors.background)
                        .padding(vertical = MsaTheme.spacing.xs)
                ) {
                    availableLanguages.forEach { language ->
                        DropdownMenuItem(text = {
                            Text(
                                text = language,
                                style = MsaTheme.typography.bodyMedium,
                                color = if (language == currentLanguage) MsaTheme.colors.accentForeground
                                else MsaTheme.colors.foreground
                            )
                        }, onClick = {
                            onLanguageSelected(language)
                            expanded = false
                        })
                    }
                }
            }
        })
}


@Composable
private fun SectionHeader(titleRes: Int) {
    Text(
        text = stringResource(titleRes),
        style = MsaTheme.typography.bodyMedium,
        color = MsaTheme.colors.foreground,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = MsaTheme.spacing.md, top = MsaTheme.spacing.sm, bottom = MsaTheme.spacing.xs
            )
    )
}

@Composable
private fun CenteredText(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MsaTheme.colors.background)
            .padding(
                top = MsaTheme.spacing.md,
                bottom = MsaTheme.spacing.md,
                start = MsaTheme.minTouchSize.md - MsaTheme.spacing.xs3
            ), contentAlignment = Alignment.TopStart
    ) {
        Text(
            text = text, style = MsaTheme.typography.bodyLarge, color = MsaTheme.colors.foreground
        )
    }
}

@Composable
private fun ArrowIcon() {
    Icon(
        painter = painterResource(R.drawable.arrow_right),
        contentDescription = null,
        modifier = Modifier.size(MsaTheme.iconSize.xs),
        tint = MsaTheme.colors.accentForeground
    )
}


@Preview
@Composable
fun PreviewMainSettingScreen() {
    MainSettingScreen(SettingsState()) {}
}
