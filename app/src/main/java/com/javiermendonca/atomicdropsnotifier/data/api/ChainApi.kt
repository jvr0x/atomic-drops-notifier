package com.javiermendonca.atomicdropsnotifier.data.api

import com.javiermendonca.atomicdropsnotifier.data.dtos.AtomicDrop
import com.javiermendonca.atomicdropsnotifier.data.dtos.TableRow
import com.squareup.moshi.JsonClass
import retrofit2.http.Body
import retrofit2.http.POST

interface ChainApi {

    @POST("v1/chain/get_table_rows")
    suspend fun getAtomicDrops(@Body tableRow: TableRow): AtomicDropTableResult
}

@JsonClass(generateAdapter = true)
data class AtomicDropTableResult(val rows: List<AtomicDrop>)