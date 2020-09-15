package com.javiermendonca.atomicdropsnotifier.data.dtos

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DisplayData(val description: String) : Dto