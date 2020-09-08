package com.javiermendonca.atomicdropsnotifier.data.dtos

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TableRow(
    val json: Boolean = true,
    val code: String = ATOMIC_DROPS_TABLE_CODE,
    val scope: String = ATOMIC_DROPS_TABLE_SCOPE,
    val table: String = ATOMIC_DROPS_TABLE_NAME,
    @Json(name = "table_key") val tableKey: String = "",
    @Json(name = "lower_bound") val lowerBound: String = "",
    @Json(name = "upper_bound") val upperBound: String = "",
    val limit: Int = 100,
    @Json(name = "key_type") val keyType: Int = 10,
    @Json(name = "index_position") val indexPosition: String = "",
    @Json(name = "encode_type") val encondeType: String = "dec",
    val reverse: Boolean = true,
    @Json(name = "show_payer") val showPayer: Boolean = false
) : Dto

const val ATOMIC_DROPS_TABLE_CODE = "atomicdropsx"
const val ATOMIC_DROPS_TABLE_SCOPE = "atomicdropsx"
const val ATOMIC_DROPS_TABLE_NAME = "drops"