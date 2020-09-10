package com.javiermendonca.atomicdropsnotifier.data.dtos

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Template(
    val contract: String?,
    @Json(name = "template_id") val id: String,
    @Json(name = "is_transferable") val isTransferable: Boolean,
    @Json(name = "is_burnable") val isBurnable: Boolean,
    @Json(name = "issued_supply") val issueSupply: String,
    @Json(name = "max_supply") val maxSupply: String,
    val collection: Collection,
    @Json(name = "immutable_data") val attributes: TemplateAttributes,
    @Json(name = "created_at_time") val createdAt: String,
) : Dto