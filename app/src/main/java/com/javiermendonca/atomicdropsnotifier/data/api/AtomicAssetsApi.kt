package com.javiermendonca.atomicdropsnotifier.data.api

import com.javiermendonca.atomicdropsnotifier.data.dtos.Collection
import com.javiermendonca.atomicdropsnotifier.data.dtos.Template
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.GET
import retrofit2.http.Path

interface AtomicAssetsApi {

    @GET("atomicassets/v1/templates/{collection_name}/{template_id}")
    suspend fun getTemplate(
        @Path("collection_name") collectionName: String,
        @Path("template_id") templateId: Int
    ): TemplateResult
}

@JsonClass(generateAdapter = true)
data class CollectionResult(val success: Boolean, @Json(name = "data") val collection: Collection)

@JsonClass(generateAdapter = true)
data class TemplateResult(val success: Boolean, @Json(name = "data") val template: Template)