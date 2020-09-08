package com.javiermendonca.atomicdropsnotifier.data.dtos

import android.content.Context
import com.javiermendonca.atomicdropsnotifier.R
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.text.SimpleDateFormat
import java.util.*

@JsonClass(generateAdapter = true)
data class AtomicDrop(
    @Json(name = "drop_id") val dropId: Int,
    @Json(name = "collection_name") val collectionName: String,
    @Json(name = "templates_to_mint") val templatesToMint: List<Int>,
    @Json(name = "listing_price") val listingPrice: String,
    @Json(name = "price_recipient") val priceRecipient: String,
    @Json(name = "auth_required") val authRequired: Int,
    @Json(name = "account_limit") val accountLimit: Int,
    @Json(name = "account_limit_cooldown") val accountLimitCooldown: Int,
    @Json(name = "max_claimable") val maxClaimable: Int,
    @Json(name = "current_claimed") val currentClaimable: Int,
    @Json(name = "start_time") val startTime: Long,
    @Json(name = "end_time") val endTime: Long,
    @Json(name = "description") val description: String
) : Dto

private val SIMPLE_FORMAT_DATE =
    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale("sv", "SE"))

fun AtomicDrop.startTimeString(): String = SIMPLE_FORMAT_DATE.format(startTime * 1000L)
fun AtomicDrop.endTimeString(): String = SIMPLE_FORMAT_DATE.format(endTime * 1000L)

fun AtomicDrop.formattedPrice(context: Context): String = with(context) {
    val value = listingPrice.split("\\s".toRegex())[0].toDouble()
    val currency = listingPrice.split("\\s".toRegex())[1]

    return if (value == 0.0) {
        getString(R.string.drop_free)
    } else {
        getString(R.string.drop_price_format, value, currency)
    }
}
