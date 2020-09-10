package com.javiermendonca.atomicdropsnotifier.data.dtos

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Collection(
    val contract: String,
    @Json(name = "collection_name") val collectionName: String,
    val name: String,
    val author: String,
    @Json(name = "authorized_accounts") val authorizedAccounts: List<String>,
    @Json(name = "market_fee") val marketFee: Double,
    @Json(name = "img") val image: String
) : Dto
