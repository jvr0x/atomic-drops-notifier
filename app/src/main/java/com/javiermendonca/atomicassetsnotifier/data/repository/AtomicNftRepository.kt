package com.javiermendonca.atomicassetsnotifier.data.repository

import android.content.SharedPreferences
import com.javiermendonca.atomicassetsnotifier.data.api.ChainApi
import com.javiermendonca.atomicassetsnotifier.data.dtos.TableRow

class AtomicNftRepository(
    private val chainApi: ChainApi,
    private val sharedPreferences: SharedPreferences
) {

    suspend fun getAtomicDrops(tableRow: TableRow) = chainApi.getAtomicDrops(tableRow)

    fun persistLastDrop(dropId: Int) = sharedPreferences
        .edit()
        .putInt(DROP_ID, dropId)
        .apply()


    fun lastPersistedDrop() = sharedPreferences.getInt(DROP_ID, -1)

    companion object {
        const val DROP_ID = "dropId"
    }
}