package com.minesec.msav3opensource.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.minesec.msav3opensource.ui.theme.MsaTheme
import com.theminesec.multiplatform.msa_core.feature.transactionStatus.presentation.processingInquiry.ProcessingInquiryAction
import com.theminesec.multiplatform.msa_core.feature.transactionStatus.presentation.processingInquiry.ProcessingInquiryState
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun ProcessingInquiryScreenUI(
    state: ProcessingInquiryState,
    onAction: (ProcessingInquiryAction) -> Unit
) {
    Scaffold(
        modifier = Modifier
            .background(MsaTheme.colors.background)
            .statusBarsPadding()
            .navigationBarsPadding(),
        containerColor = MsaTheme.colors.background,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MsaTheme.colors.background)
                .padding(horizontal = MsaTheme.spacing.md),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(MsaTheme.spacing.xl2))

            // ── Transaction Type Label ──
            Text(
                text = state.transactionType,
                style = MsaTheme.typography.titleMedium,
                color = MsaTheme.colors.mutedForeground,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(MsaTheme.spacing.xs2))

            // ── Amount ──
            Text(
                text = "${state.currency} ${state.formattedAmount}",
                style = MsaTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                color = MsaTheme.colors.foreground,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.weight(0.3f))

            // ── Circular Progress Animation ──
            ProcessingCircularAnimation(
                modifier = Modifier.size(200.dp)
            )

            Spacer(Modifier.weight(0.3f))

            // ── Status Text ──
            Text(
                text = state.statusText,
                style = MsaTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                color = MsaTheme.colors.foreground,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(MsaTheme.spacing.xs))

            // ── Payment Method Label ──
            Text(
                text = state.paymentMethodLabel,
                style = MsaTheme.typography.bodyMedium,
                color = MsaTheme.colors.accentForeground,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            // ── Hint Text ──
            Text(
                text = state.hintText,
                style = MsaTheme.typography.bodyMedium,
                color = MsaTheme.colors.accentForeground,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(MsaTheme.spacing.sm))

            // ── Countdown Timer ──
            Text(
                text = formatCountdown(state.countdownSeconds),
                style = MsaTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                color = MsaTheme.colors.mutedForeground,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.weight(0.4f))
        }
    }
}

/**
 * Format countdown seconds into mm:ss or h:mm:ss.
 * e.g. 65 → "01:05", 3661 → "1:01:01"
 */
private fun formatCountdown(totalSeconds: Int): String {
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return if (hours > 0) {
        "$hours:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
    } else {
        "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
    }
}

/**
 * Custom circular progress animation matching the Figma design:
 * A green gradient ring that rotates continuously.
 */
@Composable
private fun ProcessingCircularAnimation(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "processingRotation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    val primaryColor = MsaTheme.colors.primary

    Canvas(modifier = modifier) {
        val strokeWidth = 10.dp.toPx()
        val arcSize = size.minDimension - strokeWidth
        val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)

        // Background track (light gray)
        drawArc(
            color = Color(0xFFE8ECF0),
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            topLeft = topLeft,
            size = Size(arcSize, arcSize),
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )

        // Gradient arc (green)
        drawArc(
            brush = Brush.sweepGradient(
                0f to Color.Transparent,
                0.7f to primaryColor.copy(alpha = 0.6f),
                1f to primaryColor
            ),
            startAngle = rotation - 90f,
            sweepAngle = 270f,
            useCenter = false,
            topLeft = topLeft,
            size = Size(arcSize, arcSize),
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
    }
}


@Preview
@Composable
fun PreviewProcessingInquiryScreen() {
    ProcessingInquiryScreenUI(
        state = ProcessingInquiryState(
            transactionType = "Sale",
            formattedAmount = "25.00",
            currency = "USD",
            paymentMethodLabel = "Visa 1234",
            statusText = "Authorizing...",
            hintText = "Processing transaction with payment host.",
            countdownSeconds = 60
        ),
        onAction = {}
    )
}
