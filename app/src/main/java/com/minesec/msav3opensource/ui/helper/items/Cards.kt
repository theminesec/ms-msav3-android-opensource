package com.minesec.msav3opensource.ui.helper.items

import adaptiveSpacingHeight
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minesec.msav3opensource.R
import com.minesec.msav3opensource.ui.theme.MsaTheme
import com.minesec.msav3opensource.ui.models.CardTab
import com.minesec.msav3opensource.ui.models.getSettlementStatusText
import com.theminesec.multiplatform.msa_core.feature.common.domain.models.enums.SettleStatus
import com.theminesec.multiplatform.msa_core.feature.common.domain.models.enums.SettleStatus.CREATED
import com.theminesec.multiplatform.msa_core.feature.common.domain.models.enums.SettleStatus.FAILED
import com.theminesec.multiplatform.msa_core.feature.common.domain.models.enums.SettleStatus.SETTLED

@Composable
fun MSACard(
    button: CardTab,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Button(
        onClick = onClick,
        modifier = modifier
            .height(adaptiveSpacingHeight(MsaTheme.minTouchSize.xl3))
            .shadow(
                elevation = 3.dp,
                shape = MsaTheme.shapes.large,
            )
            .border(
                width = if (isSelected) 1.dp else 0.dp,
                color = if (isSelected) MsaTheme.colors.primary else Color.Transparent,
                shape = MsaTheme.shapes.large
            ),
        shape = MsaTheme.shapes.large,
        colors = ButtonDefaults.buttonColors(
            containerColor = MsaTheme.colors.background,
            contentColor = MsaTheme.colors.foreground
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 6.dp
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                painter = painterResource(button.icon),
                contentDescription = button.name,
                modifier = Modifier.size(MsaTheme.iconSize.lg),
                tint = MsaTheme.colors.primary
            )
            Spacer(modifier = Modifier.height(MsaTheme.spacing.xs))
            Text(
                text = stringResource(button.stringRes),
                style = MsaTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                ),
                color = if (isSelected) MsaTheme.colors.primary else MsaTheme.colors.foreground
            )
        }
    }
}

@Preview
@Composable
fun PreviewMSACard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MsaTheme.spacing.md),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MsaTheme.spacing.md)
    ) {
        // Example 1: Unselected Card
        var isCashSelected by remember { mutableStateOf(false) }
        MSACard(
            button = CardTab.SALE,
            isSelected = isCashSelected,
            onClick = { isCashSelected = !isCashSelected },
            modifier = Modifier.fillMaxWidth(0.4f) // Example width for demonstration
        )

        // Example 2: Selected Card
        var isTransferSelected by remember { mutableStateOf(true) }
        MSACard(
            button = CardTab.REFUND,
            isSelected = isTransferSelected,
            onClick = { isTransferSelected = !isTransferSelected },
            modifier = Modifier.fillMaxWidth(0.4f)
        )

        // Example 3: Another unselected card
        var isCreditCardSelected by remember { mutableStateOf(false) }
        MSACard(
            button = CardTab.VOID,
            isSelected = isCreditCardSelected,
            onClick = { isCreditCardSelected = !isCreditCardSelected },
            modifier = Modifier.fillMaxWidth(0.4f)
        )

    }
}

@Composable
fun BatchSummaryCard(
    hostName: String,
    batchId: String,
    mid: String,
    totalSale: String,
    tid: String,
    totalRefund: String,
    settleStatus: SettleStatus,
    settleStatusBackgroundColor: Color,
    onCardClick: () -> Unit = {},
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() },
        colors = CardDefaults.cardColors(containerColor = MsaTheme.colors.background),
        shape = RectangleShape
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MsaTheme.spacing.md),
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Host name (left)
                Text(
                    text = hostName,
                    style = MsaTheme.typography.titleMedium,
                    color = MsaTheme.colors.foreground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Status (right)
                Surface(
                    shape = RoundedCornerShape(50),
                    color = MsaTheme.colors.background,
                    border = BorderStroke(1.dp, settleStatusBackgroundColor),
                    contentColor = Color.Transparent,
                    modifier = Modifier
                ) {
                    Text(
                        text = settleStatus.getSettlementStatusText(),
                        style = MsaTheme.typography.labelLarge,
                        color = settleStatusBackgroundColor,
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 10.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(MsaTheme.spacing.sm))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.mid),
                        style = MsaTheme.typography.labelSmall,
                        color = MsaTheme.colors.mutedForeground
                    )
                    Text(
                        text = mid,
                        style = MsaTheme.typography.bodyLarge,
                        color = MsaTheme.colors.foreground
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = stringResource(R.string.total_sale),
                        style = MsaTheme.typography.labelSmall,
                        color = MsaTheme.colors.mutedForeground
                    )
                    Text(
                        text = totalSale,
                        style = MsaTheme.typography.bodyLarge,
                        color = MsaTheme.colors.foreground
                    )
                }
            }

            Spacer(modifier = Modifier.height(MsaTheme.spacing.sm))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.tid),
                        style = MsaTheme.typography.labelSmall,
                        color = MsaTheme.colors.mutedForeground
                    )
                    Text(
                        text = tid,
                        style = MsaTheme.typography.bodyLarge,
                        color = MsaTheme.colors.foreground
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = stringResource(R.string.total_refund),
                        style = MsaTheme.typography.labelSmall,
                        color = MsaTheme.colors.mutedForeground
                    )
                    Text(
                        text = totalRefund,
                        style = MsaTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = MsaTheme.colors.foreground
                    )
                }
            }
            Spacer(Modifier.height(6.dp))

            if (settleStatus == SettleStatus.FAILED) Text(
                text = stringResource(R.string.settlement_failed_contact_bank_message),
                style = MsaTheme.typography.bodyLarge.copy(fontSize = 14.sp),
                color = MsaTheme.colors.error,
                maxLines = 1,
                modifier = Modifier.clickable { }
            )
            else if (settleStatus == SettleStatus.PROCESSING)
                Text(
                    text = stringResource(R.string.settlement_waiting_contact_bank_message),
                    style = MsaTheme.typography.bodyLarge.copy(fontSize = 14.sp),
                    color = MsaTheme.colors.primary,
                    maxLines = 1
                )
        }
        HorizontalDivider(
            thickness = 1.dp,
            color = MsaTheme.colors.ring
        )
    }
}

@Preview
@Composable
fun PreviewBatchSummaryCard() {
    val status = SettleStatus.FAILED
    BatchSummaryCard(
        hostName = "HostSim (VM)",
        batchId = "1000037",
        mid = "0138000",
        totalSale = "+$400.00",
        tid = "0000",
        totalRefund = "-$3.00",
        settleStatus = status,
        settleStatusBackgroundColor = when (status) {
            CREATED -> MsaTheme.colors.statusYellow
            SETTLED -> MsaTheme.colors.approval
            SettleStatus.PROCESSING -> MsaTheme.colors.statusYellow
            FAILED -> MsaTheme.colors.error
        },
        onCardClick = {}
    )
}

@Composable
fun BatchSummaryDetails(
    hostName: String,
    batchId: String,
    mid: String,
    totalSale: String,
    tid: String,
    totalRefund: String,
    settleStatusText: String,
    settleStatusBackgroundColor: Color,
    time: String,
    grossTotalCount: Int,
    grossTotalAmount: String,
    captureTotalCount: Int,
    captureTotalAmount: String,
    refundTotalCount: Int,
    refundTotalAmount: String,
    voidSaleCount: Int,
    voidSaleAmount: String,
    voidCaptureCount: Int,
    voidCaptureAmount: String,
    voidRefundCount: Int,
    voidRefundAmount: String,
    currency: String = "HKD"
) {
    Column(
        modifier = Modifier.padding(MsaTheme.spacing.sm)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .background(MsaTheme.colors.background)
                .padding(MsaTheme.spacing.xs),
            colors = CardDefaults.cardColors(containerColor = MsaTheme.colors.background),
            shape = RectangleShape
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = hostName,
                    style = MsaTheme.typography.titleMedium,
                    color = MsaTheme.colors.foreground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Surface(
                    shape = RoundedCornerShape(50),
                    color = MsaTheme.colors.background,
                    contentColor = Color.Transparent,
                    border = BorderStroke(1.dp, settleStatusBackgroundColor),
                ) {
                    Text(
                        text = settleStatusText,
                        style = MsaTheme.typography.labelLarge,
                        color = settleStatusBackgroundColor,
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 10.dp),
                        textAlign = TextAlign.Center

                    )
                }
            }


            Spacer(modifier = Modifier.height(MsaTheme.spacing.sm))

            Spacer(modifier = Modifier.height(MsaTheme.spacing.sm))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.mid),
                        style = MsaTheme.typography.labelSmall,
                        color = MsaTheme.colors.mutedForeground
                    )
                    Text(
                        text = mid,
                        style = MsaTheme.typography.bodyLarge,
                        color = MsaTheme.colors.foreground
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = stringResource(R.string.total_sale),
                        style = MsaTheme.typography.labelSmall,
                        color = MsaTheme.colors.mutedForeground
                    )
                    Text(
                        text = totalSale,
                        style = MsaTheme.typography.bodyLarge,
                        color = MsaTheme.colors.foreground
                    )
                }
            }

            Spacer(modifier = Modifier.height(MsaTheme.spacing.sm))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.tid),
                        style = MsaTheme.typography.labelSmall,
                        color = MsaTheme.colors.mutedForeground
                    )
                    Text(
                        text = tid,
                        style = MsaTheme.typography.bodyLarge,
                        color = MsaTheme.colors.foreground
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = stringResource(R.string.total_refund),
                        style = MsaTheme.typography.labelSmall,
                        color = MsaTheme.colors.mutedForeground
                    )
                    Text(
                        text = totalRefund,
                        style = MsaTheme.typography.bodyLarge,
                        color = MsaTheme.colors.foreground
                    )
                }
            }
            Spacer(modifier = Modifier.height(MsaTheme.spacing.sm))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.updated),
                    style = MsaTheme.typography.labelSmall,
                    color = MsaTheme.colors.mutedForeground,
                    textAlign = TextAlign.Start
                )
                Text(
                    text = time,
                    style = MsaTheme.typography.bodyMedium,
                    color = MsaTheme.colors.foreground,
                    textAlign = TextAlign.End
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MsaTheme.colors.background)
                .padding(MsaTheme.spacing.xs)
        ) {
            HorizontalDivider(
                thickness = 1.dp,
                color = MsaTheme.colors.ring
            )
            Spacer(Modifier.height(MsaTheme.spacing.xl))
            Text(
                text = stringResource(R.string.charge_summary),
                style = MsaTheme.typography.titleMedium,
                color = MsaTheme.colors.foreground,
            )
            Spacer(Modifier.height(MsaTheme.spacing.xs))

            SummaryItem(
                label = stringResource(R.string.gross_total),
                count = grossTotalCount,
                currency = currency,
                amount = grossTotalAmount
            )
            SummaryItem(
                label = stringResource(R.string.capture_total),
                count = captureTotalCount,
                currency = currency,
                amount = captureTotalAmount
            )
            SummaryItem(
                label = stringResource(R.string.refund_total),
                count = refundTotalCount,
                currency = currency,
                amount = refundTotalAmount,
            )

            Spacer(modifier = Modifier.height(MsaTheme.spacing.xs3))

            Text(
                text = stringResource(R.string.voided_transaction),
                style = MsaTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MsaTheme.colors.foreground,
            )
            Spacer(Modifier.height(MsaTheme.spacing.xs))

            SummaryItem(
                label = stringResource(R.string.v_sale),
                count = voidSaleCount,
                currency = currency,
                amount = voidSaleAmount
            )
            SummaryItem(
                label = stringResource(R.string.v_capture),
                count = voidCaptureCount,
                currency = currency,
                amount = voidCaptureAmount
            )
            SummaryItem(
                label = stringResource(R.string.v_refund),
                count = voidRefundCount,
                currency = currency,
                amount = voidRefundAmount
            )
        }
    }
}

@Composable
fun SummaryItem(
    label: String,
    count: Int,
    currency: String,
    amount: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = MsaTheme.spacing.xs),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MsaTheme.typography.labelSmall,
            color = MsaTheme.colors.mutedForeground,
            textAlign = TextAlign.Start,
            modifier = Modifier.weight(1f)
        )
        //Spacer(modifier = Modifier.width(MsaTheme.spacing.md))
        Text(
            text = count.toString(),
            style = MsaTheme.typography.labelSmall,
            color = MsaTheme.colors.mutedForeground,
            textAlign = TextAlign.Center,

            modifier = Modifier.weight(1f)
        )

        Text(
            text = amount,
            style = MsaTheme.typography.bodyMedium,
            color = MsaTheme.colors.foreground,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview
@Composable
fun PreviewBatchSummaryDetails() {
    BatchSummaryDetails(
        hostName = "HostSim (VM)",
        batchId = "1000037",
        mid = "0138000",
        totalSale = "+$400.00",
        tid = "0000",
        totalRefund = "-$3.00",
        settleStatusText = "Wait for settle",
        settleStatusBackgroundColor = MsaTheme.colors.statusYellow,
        time = "2025/03/21 11:23:59(GMT+8)",
        grossTotalCount = 6,
        grossTotalAmount = "400.00",
        captureTotalCount = 2,
        captureTotalAmount = "197.00",
        refundTotalCount = 1,
        refundTotalAmount = "-3.00",
        voidSaleCount = 0,
        voidSaleAmount = "0.00",
        voidCaptureCount = 0,
        voidCaptureAmount = "0.00",
        voidRefundCount = 0,
        voidRefundAmount = "0.00",
        currency = "HKD"
    )
}

@Composable
fun AddDocumentCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: Painter,
    contentDescription: String? = null,
    backgroundColor: Color = MsaTheme.colors.background,
    iconColor: Color = MsaTheme.colors.input
) {
    Card(
        modifier = modifier
            .size(MsaTheme.minTouchSize.xl3)
            .clickable(onClick = onClick),
        shape = MsaTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(MsaTheme.spacing.md),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = icon,
                contentDescription = contentDescription,
                tint = iconColor,
                modifier = Modifier.size(MsaTheme.spacing.xl2)
            )
        }
    }
}

@Preview
@Composable
fun PreviewAddDocumentCard() {
    Column(
        modifier = Modifier
            .padding(MsaTheme.spacing.md)
            .wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MsaTheme.spacing.md)
    ) {

        AddDocumentCard(
            onClick = { },
            icon = painterResource(R.drawable.add_image_icon),
            contentDescription = "Add Photo"
        )

        // Larger card example
        AddDocumentCard(
            modifier = Modifier.size(150.dp),
            onClick = { },
            icon = painterResource(R.drawable.add_image_icon),
            contentDescription = "Upload General Document",
            iconColor = MsaTheme.colors.primary
        )
    }
}