package com.javiermendonca.atomicassetsnotifier.data.repository

import com.javiermendonca.atomicassetsnotifier.data.api.ChainApi
import com.javiermendonca.atomicassetsnotifier.data.dtos.TableRow

class AtomicNftRepository(private val chainApi: ChainApi) {

    suspend fun getAtomicDrops(tableRow: TableRow) = chainApi.getAtomicDrops(tableRow)
}