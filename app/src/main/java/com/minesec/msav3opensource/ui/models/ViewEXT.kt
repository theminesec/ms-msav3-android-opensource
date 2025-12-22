package com.minesec.msav3opensource.ui.models

import com.theminesec.multiplatform.msa_core.feature.common.domain.models.enums.PaymentMethod
import com.theminesec.multiplatform.msa_core.feature.common.domain.models.enums.PaymentMethod.*
import com.theminesec.multiplatform.msa_core.feature.common.domain.models.enums.SettleStatus
import com.minesec.msav3opensource.R


fun PaymentMethod.getPaymentSchemaIconRes() = when (this) {
    VISA_BRAND -> R.drawable.visa
    MASTERCARD_QR -> R.drawable.mastercard
    UNION_PAY_QR -> R.drawable.unionpay
    AMEX_BRAND -> R.drawable.amex
    JCB_BRAND -> R.drawable.jcb
    DISCOVER_BRAND -> R.drawable.discover
    DINERS_CARD -> R.drawable.diners
    MC_BRAND ->  R.drawable.mastercard
    APPLE_PAY ->  R.drawable.apple_pay
    GOOGLE_PAY -> R.drawable.google_pay
    SAMSUNG_PAY ->  R.drawable.samsung_pay
    HUAWEI_PAY ->  R.drawable.huawei_pay
    WECHAT ->  R.drawable.wechat_pay_icon
    ALIPAY ->  R.drawable.alipay_icon
    VISA_QR ->  R.drawable.visa
    ALIPAY_PLUS -> R.drawable.alipay_hk_icon
    GRAB_PAY-> R.drawable.acceptance_grab_pay
    SHOPEE_PAY-> R.drawable.acceptance_shopee_pay
    PAY_NOW-> R.drawable.acceptance_paynow
    LINEPAY -> R.drawable.acceptance_fps
    DUIT_NOW -> R.drawable.acceptance_duitnow
    else -> R.drawable.payment_method_icon
}


fun String.capitalizeFirstChar()=this.lowercase().replaceFirstChar { it.uppercaseChar() }

fun SettleStatus.getSettlementStatusText() = when (this) {
    SettleStatus.CREATED -> "Wait for settle"
    SettleStatus.PROCESSING -> "Processing"
    SettleStatus.SETTLED -> "Settled"
    SettleStatus.FAILED -> "Failed"
}