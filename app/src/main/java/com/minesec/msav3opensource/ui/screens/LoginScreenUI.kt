package com.minesec.msav3opensource.ui.screens

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minesec.msav3opensource.ui.helper.template.BiometricDialog
import com.minesec.msav3opensource.ui.helper.template.MsaUiStateHandler
import com.theminesec.multiplatform.msa_core.feature.login.presentation.LoginAction
import com.theminesec.multiplatform.msa_core.feature.login.presentation.LoginState
import org.publicvalue.multiplatform.qrcode.CodeType
import org.publicvalue.multiplatform.qrcode.ScannerWithPermissions
import com.minesec.msav3opensource.ui.theme.MsaTheme
import com.minesec.msav3opensource.R
import com.minesec.msav3opensource.ui.helper.items.MSAButton
import com.minesec.msav3opensource.ui.helper.items.MSAButtonStyle


@SuppressLint("UnrememberedMutableState", "UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreenUI(
    state: LoginState, triggerAction: (LoginAction) -> Unit
) = MsaUiStateHandler(isLoading = state.isLoading, state.deviceErrorState) {
    var textFieldValue by remember { mutableStateOf(TextFieldValue("")) }
    val isButtonEnabled = textFieldValue.text.filter { it.isDigit() }.length == 12
    val uriHandler = LocalUriHandler.current
    val isBiometricEnabled  by mutableStateOf(state.isBiometricEnabled)
    val isBiometricSupported  by mutableStateOf(state.isBiometricSupported)
    var showScannerWithQRCode by remember { mutableStateOf(false) }

    // Format with hyphens every 4 digits
    fun formatWithHyphens(text: String): String {
        val digitsOnly = text.filter { it.isDigit() }.take(12)
        return buildString {
            digitsOnly.forEachIndexed { index, char ->
                append(char)
                if ((index + 1) % 4 == 0 && index != digitsOnly.lastIndex) {
                    append('-')
                }
            }
        }
    }

    BackHandler(enabled = showScannerWithQRCode) {
        showScannerWithQRCode = false
    }

    if (state.showBioDialog) {
        BiometricDialog(onConfirm = {
            triggerAction(LoginAction.EnableBiometric(true))
        }, onDismiss = {
            triggerAction(LoginAction.EnableBiometric(false))
        })
    }

    if (showScannerWithQRCode) {
        Box(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
            Column(
                modifier = Modifier
                    .background(Color(0xFF1D1C22))
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .clip(shape = RoundedCornerShape(size = 14.dp))
                        .clipToBounds()
                        .border(2.dp, Color.Gray, RoundedCornerShape(size = 14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    ScannerWithPermissions(
                        onScanned = {
                            triggerAction(
                                LoginAction.LoginClicked(
                                    it.replace(
                                        "-",
                                        ""
                                    ).trim()
                                )
                            )
                            showScannerWithQRCode = false
                            true
                        }, modifier = Modifier
                            .clipToBounds()
                            .clip(shape = RoundedCornerShape(size = 14.dp)),
                        types = listOf(CodeType.QR, CodeType.Codabar)
                    )
                }
            }
        }
        return@MsaUiStateHandler
    }

    if (state.showBioScreen) {
        Box(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
            Column(
                modifier = Modifier
                    .background(Color(0xFF1D1C22))
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val titleText = stringResource(R.string.enable_biometric_title)
                val messageText = stringResource(R.string.enable_biometric_message)

                Scaffold(
                    modifier = Modifier.background(MsaTheme.colors.background)
                        .statusBarsPadding()
                        .navigationBarsPadding(),
                    bottomBar = {
                        Column(
                            modifier = Modifier.background(MsaTheme.colors.background)
                                .fillMaxWidth()
                                .padding(horizontal = MsaTheme.spacing.md)
                                .padding(bottom = MsaTheme.spacing.md)
                                .navigationBarsPadding()
                        ) {
                            MSAButton(
                                text = stringResource(R.string.yes),
                                enabled = isButtonEnabled,
                                onClick = {
                                    triggerAction(
                                        LoginAction.EnableBiometric(true)
                                    )
                                },
                                modifier = Modifier.fillMaxWidth()
                            )

                            MSAButton(
                                text = stringResource(R.string.no),
                                enabled = isButtonEnabled,
                                defaultButtonStyle = MSAButtonStyle.OUTLINED,
                                onClick = {
                                    triggerAction(
                                        LoginAction.EnableBiometric(false)
                                    )
                                },
                                modifier = Modifier.padding(top = 10.dp).fillMaxWidth()
                            )
                        }
                    },
                    content = {
                        Column(
                            modifier = Modifier.background(MsaTheme.colors.background)
                                .fillMaxSize()
                                .padding(horizontal = MsaTheme.spacing.md)
                                .padding(bottom = MsaTheme.spacing.md)
                                .navigationBarsPadding(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.ic_bio_face_icon),
                                    contentDescription = null,
                                    modifier = Modifier.size(60.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Image(
                                    painter = painterResource(R.drawable.ic_bio_login),
                                    contentDescription = null,
                                    modifier = Modifier.size(60.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(30.dp))

                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = titleText,
                                    style = MsaTheme.typography.titleMedium,
                                    color = MsaTheme.colors.foreground,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = messageText,
                                    style = MsaTheme.typography.bodySmall,
                                    color = MsaTheme.colors.foreground,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                )
            }

        }
        return@MsaUiStateHandler
    }
        Scaffold(
            modifier = Modifier.background(MsaTheme.colors.background)
                .statusBarsPadding()
                .navigationBarsPadding(),
            bottomBar = {
                Column(
                    modifier = Modifier.background(MsaTheme.colors.background)
                        .fillMaxWidth()
                        .padding(horizontal = MsaTheme.spacing.md)
                        .padding(bottom = MsaTheme.spacing.md)
                        .navigationBarsPadding()
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(MsaTheme.spacing.md), // Spacing between the two buttons
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        MSAButton(
                            text = stringResource(R.string.login_button),
                            enabled = isButtonEnabled,
                            onClick = {
                                triggerAction(
                                    LoginAction.LoginClicked(
                                        textFieldValue.text.replace(
                                            "-",
                                            ""
                                        )
                                    )
                                )
                            },
                            modifier = Modifier.weight(1f).fillMaxWidth()
                        )
                        if (isBiometricSupported && isBiometricEnabled) {
                            Box(
                                modifier = Modifier.size(MsaTheme.minTouchSize.lg)
                                    .clip(MsaTheme.shapes.large)
                                    .background(MsaTheme.colors.primary)
                                    .clickable(enabled = isBiometricEnabled, onClick = {
                                        triggerAction(LoginAction.BioMetricClicked)
                                    }),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.fingerprint_icon_button), // or Res.face_auth_icon
                                    contentDescription = "Biometric Login",
                                    modifier = Modifier.size(MsaTheme.iconSize.xl)
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(MsaTheme.spacing.md))
                    Text(
                        text = stringResource(R.string.copyright_footer),
                        color = MsaTheme.colors.mutedForeground,
                        style = MsaTheme.typography.labelMedium,
                        modifier = Modifier.padding(MsaTheme.spacing.sm)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(MsaTheme.colors.background)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = MsaTheme.spacing.md)
            ) {
                Text(
                    text = stringResource(R.string.passcode_login_title),
                    color = MsaTheme.colors.foreground,
                    style = MsaTheme.typography.headlineSmall,
                )
                Spacer(Modifier.height(MsaTheme.spacing.xl))
                Text(
                    text = stringResource(R.string.passcode_label),
                    style = MsaTheme.typography.bodySmall,
                    color = MsaTheme.colors.accentForeground
                )
                Spacer(Modifier.height(MsaTheme.spacing.xs2))

                val isError by remember { mutableStateOf(false) }

                val isProcessing by remember { mutableStateOf(false) }
                OutlinedTextField(
                    enabled = if (isProcessing) false else true,
                    value = textFieldValue,
                    onValueChange = { newValue ->
                        val oldText = textFieldValue.text
                        val newText = newValue.text

                        val formatted = formatWithHyphens(newText)

                        val cursorOffset = when {
                            newText.length > oldText.length -> 1
                            newText.length < oldText.length ->
                                if (oldText.getOrNull(newValue.selection.start) == '-') -1 else 0

                            else -> 0
                        }

                        val addedFourthDigit = formatted.length > newText.length &&
                                newText.length % 4 == 0

                        val newCursor = (newValue.selection.start + cursorOffset +
                                if (addedFourthDigit) 1 else 0).coerceIn(0, formatted.length)

                        textFieldValue = TextFieldValue(
                            text = formatted,
                            selection = TextRange(newCursor)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = MsaTheme.typography.bodySmall,
                    placeholder = {
                        Text(
                            text = stringResource(R.string.passcode_placeholder),
                            color = MsaTheme.colors.input,
                            style = MsaTheme.typography.bodySmall
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                showScannerWithQRCode = true
                            },
                            modifier = Modifier.size(MsaTheme.iconSize.md)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.ms_activation_qr),
                                contentDescription = "Scan QR code",
                                modifier = Modifier.size(MsaTheme.iconSize.sm)
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        unfocusedIndicatorColor = if (isError) MsaTheme.colors.error else MsaTheme.colors.input,
                        unfocusedContainerColor = if (isError) MsaTheme.colors.errorForeground else MsaTheme.colors.background,
                        unfocusedPlaceholderColor = MsaTheme.colors.input,
                        focusedIndicatorColor = if (isError) MsaTheme.colors.error else MsaTheme.colors.approval,
                        focusedContainerColor = if (isError) MsaTheme.colors.errorForeground else MsaTheme.colors.approvalForeground,
                        focusedPlaceholderColor = MsaTheme.colors.foreground,
                        disabledContainerColor = MsaTheme.colors.muted,
                        disabledIndicatorColor = MsaTheme.colors.input,
                        disabledTextColor = MsaTheme.colors.mutedForeground,
                        cursorColor = Color.Black,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )
                Spacer(Modifier.height(MsaTheme.spacing.xs2))
                state.errorState?.exception?.errorMessage?.let {
                    Text(
                        text = it,
                        style = MsaTheme.typography.labelMedium,
                        color = MsaTheme.colors.error
                    )
                }

                Spacer(modifier = Modifier.height(MsaTheme.spacing.md))

                Text(
                    text = stringResource(R.string.passcode_info_message),
                    style = MsaTheme.typography.bodySmall,
                    color = MsaTheme.colors.mutedForeground,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(MsaTheme.spacing.xl))

                Text(
                    text = stringResource(R.string.cannot_find_passcode_question),
                    style = MsaTheme.typography.bodySmall,
                    color = MsaTheme.colors.mutedForeground
                )

                Text(
                    text = stringResource(R.string.contact_support_here_link),
                    color = MsaTheme.colors.primary,
                    style = MsaTheme.typography.labelSmall,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable {
                        uriHandler.openUri(state.helpUrLSupport ?: "")
                    }
                )

                Spacer(modifier = Modifier.weight(1f))
            }
        }
}

@Preview
@Composable
fun PreviewPasscodeLogin() {
    LoginScreenUI(
        state = LoginState(), triggerAction = {}
    )
}