package com.javiermendonca.atomicdropsnotifier.data.dtos

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TemplateAttributes(
    val name: String?,
    @Json(name = "img") val image: String?
) : Dto