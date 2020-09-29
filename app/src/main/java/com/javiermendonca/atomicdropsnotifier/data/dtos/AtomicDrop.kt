package com.javiermendonca.atomicdropsnotifier.data.dtos

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AtomicDrop(
    @Json(name = "drop_id") val dropId: Int?,
    @Json(name = "collection_name") val collectionName: String?,
    @Json(name = "assets_to_mint") val assetsToMint: List<AssetToMint>?,
    @Json(name = "listing_price") val listingPrice: String?,
    @Json(name = "auth_required") val authRequired: Int?,
    @Json(name = "account_limit") val accountLimit: Int?,
    @Json(name = "account_limit_cooldown") val accountLimitCooldown: Int?,
    @Json(name = "max_claimable") val maxClaimable: Int?,
    @Json(name = "current_claimed") val currentClaimable: Int?,
    @Json(name = "start_time") val startTime: Long = 0,
    @Json(name = "end_time") val endTime: Long = 0,
    @Json(name = "display_data") val displayData: String?
) : Dto