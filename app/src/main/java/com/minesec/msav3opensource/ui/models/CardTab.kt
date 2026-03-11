package com.minesec.msav3opensource.ui.models

import com.minesec.msav3opensource.R


enum class CardTab(val stringRes: Int, val icon: Int) {
    SALE(R.string.sale_label, R.drawable.sale_label),
    AUTH(R.string.auth_pre_auth_label, R.drawable.authpre_auth),
    REFUND(R.string.refund_label, R.drawable.refund),
    VOID(R.string.void_label, R.drawable.home_void_icon),
    HISTORY(R.string.history_label, R.drawable.history),
    SETTLEMENT(R.string.settlement_label, R.drawable.settlement),

    CARD(R.string.card, R.drawable.card),
    MERCHANT_QR(R.string.merchant_qr, R.drawable.ms_activation_qr),
    CONSUMER_QR(R.string.consumer_qr, R.drawable.consumer_qr),
    ALTERNATIVE(R.string.alternative, R.drawable.alternative);


    companion object {
        val homeTabs = listOf(
            SALE, AUTH, REFUND, VOID, HISTORY, SETTLEMENT
        )

        val paymentSelectionTabs = listOf(
            CARD, MERCHANT_QR, CONSUMER_QR, ALTERNATIVE
        )
    }
}
