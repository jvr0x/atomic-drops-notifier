package com.javiermendonca.atomicassetsnotifier.data.dtos

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TableRow(
    val json: Boolean = true,
    val code: String,
    val scope: String,
    val table: String,
    @Json(name = "table_key") val tableKey: String = "",
    @Json(name = "lower_bound") val lowerBound: String = "",
    @Json(name = "upper_bound") val upperBound: String = "",
    val limit: Int = 10,
    @Json(name = "key_type") val keyType: Int = 10,
    @Json(name = "index_position") val indexPosition: String = "",
    @Json(name = "encode_type") val encondeType: String = "dec",
    val reverse: Boolean = false,
    @Json(name = "show_payer") val showPayer: Boolean = false
) : Dto