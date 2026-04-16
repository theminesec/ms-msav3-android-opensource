package com.minesec.msav3opensource.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.minesec.msav3opensource.R
import com.minesec.msav3opensource.ui.template.MsaUiStateHandler
import com.minesec.msav3opensource.ui.template.ReceiptNfcDialog
import com.minesec.msav3opensource.ui.helper.utils.shareImage
import com.minesec.msav3opensource.ui.theme.MsaTheme
import com.minesec.msav3opensource.ui.template.TicketShapeVertically
import com.theminesec.multiplatform.msa_core.feature.receiptScreen.domain.models.MerchantReceiptData
import com.theminesec.multiplatform.msa_core.feature.receiptScreen.presentation.ReceiptAction
import com.theminesec.multiplatform.msa_core.feature.receiptScreen.presentation.ReceiptState
import com.theminesec.multiplatform.msa_core.feature.settings.domain.models.DeviceInfo
import com.theminesec.multiplatform.msa_core.util.extentions.DataTimeHelper
import io.github.suwasto.capturablecompose.Capturable
import io.github.suwasto.capturablecompose.CompressionFormat
import io.github.suwasto.capturablecompose.rememberCaptureController
import io.github.suwasto.capturablecompose.toByteArray

// Color constants for better readability
private val ScreenBackgroundColor = Color(0xFFF5F5F5)
private val CardBackgroundColor = Color.White
private val TextColorBlack = Color.Black
private val TextColorGray = Color.Gray

/**
 * Main Composable for the Receipt Screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiptUIScreen(
    state: ReceiptState,
    onTriggerAction: (ReceiptAction) -> Unit
) = MsaUiStateHandler(state.isLoading, state.errorState) {

    //  Remember long-lived states outside recompositions
    val captureController2 = rememberCaptureController()

    val scrollState = rememberScrollState()
    var showDialog by remember { mutableStateOf(false) }

    //  Dialog not dependent on scroll or recomposition-sensitive states
    if (showDialog) {
        ReceiptNfcDialog(
            onDismiss = {
                showDialog = false
                onTriggerAction(ReceiptAction.OnDismissNFC)
            }
        )
    }

    Scaffold(
        topBar = {
            ReceiptTopBar(onBackClicked = { onTriggerAction(ReceiptAction.OnBackClicked) })
        },
        containerColor = ScreenBackgroundColor
    ) { innerPadding ->
        //  Wrap content in Box to center and constrain width
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 500.dp) // Constrain maximum width
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
            ) {
                Capturable(
                    captureController = captureController2,
                    onCaptured = { imageBitmap ->
                        // Convert the captured ImageBitmap to a ByteArray for saving
                        val byteArray = imageBitmap.toByteArray(CompressionFormat.PNG, 100)
                        shareImage(byteArray)
                    }
                ) {
                    ReceiptCard(
                        receiptData = state.receiptData, deviceInfo = state.deviceInfo
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                BottomActionBar(
                    onShare = { captureController2.capture() },
                    onNfcClick = {
                        showDialog = true
                        onTriggerAction(ReceiptAction.OnNFCClicked)
                    }
                )
            }
        }
    }
}


// --- Top Bar ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReceiptTopBar(onBackClicked: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = "Merchant Receipt",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = TextColorBlack
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClicked) {
                BackIcon()
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = CardBackgroundColor,
            titleContentColor = TextColorBlack,
            navigationIconContentColor = TextColorBlack
        )
    )
}

// --- Bottom Action Bar ---

@Composable
private fun BottomActionBar(
    onShare: () -> Unit,
    onNfcClick: () -> Unit
) {
    // Center the buttons with max width constraint
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardBackgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .widthIn(max = 500.dp) // Match the receipt card width
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            ActionButton(
                modifier = Modifier.weight(1f),
                iconRes = R.drawable.share,
                onClick = onShare
            )
            Spacer(modifier = Modifier.width(16.dp))
            ActionButton(
                modifier = Modifier.weight(1f),
                iconRes = R.drawable.nfc,
                onClick = onNfcClick
            )
        }
    }
}

@Composable
private fun ActionButton(
    modifier: Modifier = Modifier,
    iconRes: Int,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .clip(RoundedCornerShape(MsaTheme.radius.lg)) // Use standard shape for better M3 integration
            .background(MsaTheme.colors.background)
            .border(
                width = 1.dp,
                color = MsaTheme.colors.input,
                shape = RoundedCornerShape(MsaTheme.radius.lg)
            )
            .height(56.dp) // Standard height for action buttons
    ) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = null,
            modifier = Modifier.size(MsaTheme.iconSize.md),
            colorFilter = ColorFilter.tint(MsaTheme.colors.accentForeground)
        )
    }
}

// --- Receipt Card ---
@Composable
private fun ReceiptCard(
    receiptData: MerchantReceiptData?, deviceInfo: DeviceInfo?
) {
    val cardShape = TicketShapeVertically(0.dp, CornerSize(15.dp))

    Column(
        modifier = Modifier
            .background(ScreenBackgroundColor)
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp), // Adjusting padding to be less nested
            shape = cardShape,
            colors = CardDefaults.cardColors(containerColor = CardBackgroundColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp), // Slightly more internal padding
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Merchant Logo Section
                MerchantLogoSection(deviceInfo)

                Spacer(modifier = Modifier.height(16.dp))

                // Transaction Header Section
                TransactionHeaderSection(receiptData, deviceInfo)

                Spacer(modifier = Modifier.height(24.dp))

                // Transaction Summary
                ReceiptSummary(receiptData = receiptData)

                Spacer(modifier = Modifier.height(16.dp))
                DottedLine()

                // Total Amount
                Spacer(modifier = Modifier.height(16.dp))
                TotalAmountSection(receiptData)

                Spacer(modifier = Modifier.height(16.dp))
                DottedLine()

                // Base Info
                Spacer(modifier = Modifier.height(16.dp))
                BaseInfoSection(receiptData)

                Spacer(modifier = Modifier.height(16.dp))
                DottedLine()

                // Additional Info
                Spacer(modifier = Modifier.height(16.dp))
                AdditionalInfoSection(receiptData)

                Spacer(modifier = Modifier.height(16.dp))
                DottedLine()

                // Footer
                Spacer(modifier = Modifier.height(8.dp))
                FooterText(deviceInfo)
            }
        }
    }
}

// --- Receipt Card Sub-Sections ---
@Composable
private fun MerchantLogoSection(deviceInfo: DeviceInfo?) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape),
        contentAlignment = Alignment.Center
    ) {
        ReceiptIcon(
            mchLogo = deviceInfo?.mchLogo.orEmpty(),
            description = deviceInfo?.mchName.orEmpty()
        )
    }
}

@Composable
private fun TransactionHeaderSection(receiptData: MerchantReceiptData?, deviceInfo: DeviceInfo?) {
    Text(
        text = "TXN ${receiptData?.transactionId.orEmpty()}",
        fontSize = 12.sp,
        color = TextColorBlack,
        fontFamily = FontFamily.Monospace
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = receiptData?.merchantName.orEmpty(),
        fontSize = 18.sp,
        fontWeight = FontWeight.Medium,
        color = TextColorBlack
    )
    Text(
        text = deviceInfo?.mchContactAddress.orEmpty(),
        fontSize = 14.sp,
        color = TextColorGray,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(vertical = 4.dp)
    )
    Text(
        text = "Here's your receipt",
        fontSize = 14.sp,
        color = TextColorGray,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun ReceiptSummary(receiptData: MerchantReceiptData?) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = receiptData?.transactionType.orEmpty(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = TextColorBlack
            )
            Text(
                text = receiptData?.transactionState.orEmpty(),
                fontSize = 14.sp,
                color = TextColorGray
            )
        }
        val schemeName =
            receiptData?.paymentMethod?.lowercase()?.replaceFirstChar { it.uppercaseChar() }
                .orEmpty().ifEmpty { "Unknown" }

        Text(
            text = schemeName,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = TextColorBlack
        )
    }

    Spacer(modifier = Modifier.height(8.dp))

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = receiptData?.transactionDateTime.orEmpty(),
            fontSize = 12.sp,
            color = TextColorGray,
            fontFamily = FontFamily.Monospace
        )
        Text(
            text = receiptData?.cardNumber.orEmpty(),
            fontSize = 12.sp,
            color = TextColorBlack,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = FontFamily.Monospace
        )
    }
}

@Composable
private fun TotalAmountSection(receiptData: MerchantReceiptData?) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Total",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextColorBlack
        )
        val amount = "${receiptData?.currency.orEmpty()} ${receiptData?.totalAmount.orEmpty()}"
        Text(
            text = amount,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextColorBlack
        )
    }
}

@Composable
private fun BaseInfoSection(receiptData: MerchantReceiptData?) {
    val baseInfo = mapOf(
        "MID" to receiptData?.acquirerMid,
        "TID" to receiptData?.acquirerTid,
        "Batch" to receiptData?.acquirerBatchNo,
        "Trace" to receiptData?.acquirerTraceNo,
        "RRN" to receiptData?.acquirerRefNo,
        "Approval Code" to receiptData?.acquirerAuthCode
    )

    baseInfo.forEach { (key, value) ->
        ReceiptItem(key, value.orEmpty())
    }
}

@Composable
private fun AdditionalInfoSection(receiptData: MerchantReceiptData?) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Additional Information",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = TextColorBlack,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        val additionalInfo = mapOf(
            "TRANSACTION ID" to receiptData?.transactionId,
            "STATE" to receiptData?.transactionState,
            "DATETIME" to receiptData?.transactionDateTime,
            "ATC" to receiptData?.atc,
            "TVR" to receiptData?.tvr,
            "APP NAME" to receiptData?.appName?.trim(),
            "TSI" to receiptData?.tsi,
            "AID" to receiptData?.aid,
            "TC" to receiptData?.tc
        )

        additionalInfo.forEach { (key, value) ->
            ReceiptItem(key, value.orEmpty())
        }
    }
}

@Composable
private fun FooterText(deviceInfo: DeviceInfo?) {
    val currentYear = remember { DataTimeHelper.getCurrentYear() }
    val merchantName = deviceInfo?.mchName.orEmpty()
    Text(
        text = "Copyright © $currentYear $merchantName.\n All rights reserved.",
        fontSize = 8.sp,
        textAlign = TextAlign.Center,
        fontFamily = FontFamily.Monospace,
        color = TextColorGray,
    )
}

// --- UI Helpers ---
@Composable
private fun ReceiptItem(key: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = key,
            fontSize = 12.sp,
            color = TextColorGray,
            fontFamily = FontFamily.Monospace
        )
        Text(
            text = value,
            fontSize = 12.sp,
            color = TextColorBlack,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.End,
            fontFamily = FontFamily.Monospace
        )
    }
    Spacer(modifier = Modifier.height(4.dp))
}

@Composable
private fun DottedLine(
    modifier: Modifier = Modifier,
    color: Color = TextColorGray.copy(alpha = 0.5f)
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        repeat(30) {
            Box(
                modifier = Modifier
                    .size(2.dp)
                    .background(color, CircleShape)
            )
        }
    }
}

@Composable
private fun BackIcon() {
    Icon(
        painter = painterResource(R.drawable.back_arrow),
        contentDescription = "Back",
        tint = TextColorBlack
    )
}

@Composable
private fun ReceiptIcon(mchLogo: String, description: String) {
    AsyncImage(
        model = mchLogo,
        contentDescription = description,
        placeholder = painterResource(R.drawable.minesec_logo_dark),
        error = painterResource(R.drawable.minesec_logo_dark),
        modifier = Modifier
            .height(60.dp)
            .clip(RoundedCornerShape(16.dp))
    )
}