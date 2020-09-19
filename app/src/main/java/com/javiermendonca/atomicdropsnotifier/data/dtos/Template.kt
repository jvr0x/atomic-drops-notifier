package com.javiermendonca.atomicdropsnotifier.data.dtos

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Template(
    val contract: String? = null,
    @Json(name = "template_id") val id: String? = null,
    @Json(name = "is_transferable") val isTransferable: Boolean? = null,
    @Json(name = "is_burnable") val isBurnable: Boolean? = null,
    @Json(name = "issued_supply") val issueSupply: String? = null,
    @Json(name = "max_supply") val maxSupply: String? = null,
    val collection: Collection? = null,
    @Json(name = "immutable_data") val attributes: TemplateAttributes? = null,
    @Json(name = "created_at_time") val createdAt: String? = null,
) : Dto