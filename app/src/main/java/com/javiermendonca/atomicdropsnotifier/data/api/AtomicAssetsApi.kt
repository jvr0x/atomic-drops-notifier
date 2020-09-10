package com.javiermendonca.atomicdropsnotifier.data.api

import com.javiermendonca.atomicdropsnotifier.data.dtos.Collection
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.GET
import retrofit2.http.Path

interface AtomicAssetsApi {

    @GET("atomicassets/v1/collections/{collection_name}")
    suspend fun getCollection(@Path("collection_name") collectionName: String): CollectionResult
}

@JsonClass(generateAdapter = true)
data class CollectionResult(val success: Boolean, @Json(name = "data") val collection: Collection)