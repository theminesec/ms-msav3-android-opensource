package com.minesec.msav3opensource.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minesec.msav3opensource.ui.helper.template.MsaUiStateHandler
import com.minesec.msav3opensource.ui.theme.MsaTheme
import com.theminesec.multiplatform.msa_core.feature.cardTapping.paymentSelectionScreen.presentation.PaymentSelectionAction
import com.theminesec.multiplatform.msa_core.feature.cardTapping.paymentSelectionScreen.presentation.PaymentSelectionState
import io.github.alexzhirkevich.qrose.QrCodePainter


@Composable
fun PaymentQRSelectionScreen(
    state: PaymentSelectionState, triggerAction: (PaymentSelectionAction) -> Unit
) = MsaUiStateHandler(state.isLoading, state.errorState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MsaTheme.colors.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top Bar (Merchant name + Timeout)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = state.paymentSelectionArg?.deviceInfo?.mchName ?: "",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            Text(
                text = "${state.timeoutSeconds}s",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Amount
        Text(
            text = state.paymentSelectionArg?.amount ?: "",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Payment method (GrabPay logo or text)
        Text(
            text = state.paymentSelectionArg?.deviceInfo?.mchName ?: "",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF00AA00)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // QR Code
        Box(
            modifier = Modifier
                .size(220.dp)
                .border(
                    BorderStroke(4.dp, Color(0xFF00C853)),
                    shape = RoundedCornerShape(8.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            // You’ll need a QR generator – placeholder here
            state.payDataQRCode?.let {
                Image(
                    painter = QrCodePainter(it),
                    contentDescription = "QR Code",
                    modifier = Modifier.fillMaxSize().padding(8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Instruction
        Text(
            text = "Present QR Code",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )
        Text(
            text = "Present the above QR code to consumer",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Footer
        Text(
            text = "© 2025 MineSec Limited.",
            fontSize = 12.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}