package com.javiermendonca.atomicdropsnotifier.data.dtos

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AssetToMint(
    @Json(name = "template_id") val templateId: Int
    //@Json(name = "tokens_to_back") val tokensToBack: List<>,
) : Dto
