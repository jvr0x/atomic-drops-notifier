package com.javiermendonca.atomicdropsnotifier.data.dtos

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

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