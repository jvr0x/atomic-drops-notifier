package com.javiermendonca.atomicdropsnotifier.data.repository.models

import android.content.Context
import com.javiermendonca.atomicdropsnotifier.R
import com.javiermendonca.atomicdropsnotifier.data.dtos.Template
import com.javiermendonca.atomicdropsnotifier.data.worker.SIMPLE_FORMAT_DATE

data class AtomicDropItem(
    val dropId: Int,
    val template: Template?,
    val templatesToMint: List<Int>,
    val listingPrice: String,
    val priceRecipient: String,
    val authRequired: Int,
    val accountLimit: Int,
    val accountLimitCooldown: Int,
    val maxClaimable: Int,
    val currentClaimable: Int,
    val startTime: Long,
    val endTime: Long,
    val description: String
)

fun AtomicDropItem.startTimeString(): String = SIMPLE_FORMAT_DATE.format(startTime * 1000L)
fun AtomicDropItem.endTimeString(): String = SIMPLE_FORMAT_DATE.format(endTime * 1000L)

fun AtomicDropItem.formattedPrice(context: Context): String = with(context) {
    val value = listingPrice.split("\\s".toRegex())[0].toDouble()
    val currency = listingPrice.split("\\s".toRegex())[1]

    return if (value == 0.0) {
        getString(R.string.drop_free)
    } else {
        getString(R.string.drop_price_format, value, currency)
    }
}
