package com.javiermendonca.atomicdropsnotifier.data.repository

import android.content.SharedPreferences
import com.javiermendonca.atomicdropsnotifier.data.api.ChainApi
import com.javiermendonca.atomicdropsnotifier.data.dtos.TableRow

class AtomicDropRepository(
    private val chainApi: ChainApi,
    private val sharedPreferences: SharedPreferences
) {
    suspend fun getAtomicDrops(tableRow: TableRow) = chainApi.getAtomicDrops(tableRow)

    fun persistAtomicDrop(dropId: Int) = sharedPreferences
        .edit()
        .putInt(DROP_ID, dropId)
        .apply()

    fun lastPersistedDrop() = sharedPreferences.getInt(DROP_ID, -1)

    companion object {
        const val DROP_ID = "dropId"
    }
}