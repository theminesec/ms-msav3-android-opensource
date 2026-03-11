package com.minesec.msav3opensource.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minesec.msav3opensource.ui.theme.MsaTheme
import com.theminesec.multiplatform.msa_core.util.amount.DisplayAmountFormat

class DisplayAmount(
    var initialAmount: String
) {
    var amount by mutableStateOf(initialAmount)
    private val maxDigits = 11

    fun formatAmountForDisplay(currency: String): String =
        DisplayAmountFormat.formatAmount(amount, currency)


    fun addDigit(digit: String) {
        if (amount.length >= maxDigits) return
        if (digit == "00") {
            if (amount.isEmpty()) return
            if (amount.length + 1 < maxDigits) {
                amount += "00"
            }
            return
        }
        if (digit == "0") {
            if (amount.isEmpty()) return
            if (amount.length < maxDigits) {
                amount += "0"
            }
            return
        }
        amount += digit
    }

    fun deleteLastDigit() {
        if (amount.isNotEmpty()) {
            amount = amount.dropLast(1)
        }
    }
}

@Composable
fun KeypadButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            //.fillMaxHeight()
            //.aspectRatio(1f)
            .padding(MsaTheme.spacing.xs2)
            .wrapContentSize()
            .clip(CircleShape),
        shape = CircleShape,//RectangleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = MsaTheme.colors.background,
            contentColor = MsaTheme.colors.accentForeground //mutedForeground
        ),
        contentPadding = PaddingValues(0.dp),
    ) {
        Text(
            text = text,
            //---closest---
            style = MsaTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
            ),
            maxLines = 1,
            modifier = Modifier.padding(6.dp)

        )
    }
}

@Composable
fun MSAPinPad(
    modifier: Modifier = Modifier,
    displayAmount: DisplayAmount,
    onValueChanged: (String) -> Unit = {},

    ) {

    Column(
        modifier = modifier
            .background(MsaTheme.colors.background)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                KeypadButton(
                    text = "1",
                    onClick = { displayAmount.addDigit("1"); onValueChanged(displayAmount.amount) })
                KeypadButton(
                    text = "2",
                    onClick = { displayAmount.addDigit("2"); onValueChanged(displayAmount.amount) })
                KeypadButton(
                    text = "3",
                    onClick = { displayAmount.addDigit("3"); onValueChanged(displayAmount.amount) })
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                KeypadButton(
                    text = "4",
                    onClick = { displayAmount.addDigit("4"); onValueChanged(displayAmount.amount) })
                KeypadButton(
                    text = "5",
                    onClick = { displayAmount.addDigit("5"); onValueChanged(displayAmount.amount) })
                KeypadButton(
                    text = "6",
                    onClick = { displayAmount.addDigit("6"); onValueChanged(displayAmount.amount) })
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                KeypadButton(
                    text = "7",
                    onClick = { displayAmount.addDigit("7"); onValueChanged(displayAmount.amount) })
                KeypadButton(
                    text = "8",
                    onClick = { displayAmount.addDigit("8"); onValueChanged(displayAmount.amount) })
                KeypadButton(
                    text = "9",
                    onClick = { displayAmount.addDigit("9"); onValueChanged(displayAmount.amount) })
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                KeypadButton(
                    text = "00",
                    onClick = { displayAmount.addDigit("00"); onValueChanged(displayAmount.amount) })
                KeypadButton(
                    text = "0",
                    onClick = { displayAmount.addDigit("0"); onValueChanged(displayAmount.amount) })
                KeypadButton(
                    text = "⌫",
                    onClick = { displayAmount.deleteLastDigit(); onValueChanged(displayAmount.amount) })
            }
        }
    }

}


@Preview()
@Composable
fun PreviewPinpadCompactWidth() {
    val displayAmount = remember { DisplayAmount("") }
    MSAPinPad(
        modifier = Modifier.fillMaxSize(),
        displayAmount = displayAmount,
        onValueChanged = { println("Current amount: ${displayAmount.formatAmountForDisplay("USD")}") },

        )
}


