package com.zetta.gochickfarm.utils

import java.text.NumberFormat
import java.util.Locale
import kotlin.text.replace

fun formatRupiah(amount: Int): String {
    val format = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID"))
    val result = format.format(amount)

    return result.replace(",00", ",-")
}