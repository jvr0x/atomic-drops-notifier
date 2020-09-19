package com.javiermendonca.atomicdropsnotifier.data.repository

import android.content.SharedPreferences
import com.javiermendonca.atomicdropsnotifier.data.api.AtomicAssetsApi
import com.javiermendonca.atomicdropsnotifier.data.api.ChainApi
import com.javiermendonca.atomicdropsnotifier.data.api.TemplateResult
import com.javiermendonca.atomicdropsnotifier.data.dtos.TableRow
import com.javiermendonca.atomicdropsnotifier.data.dtos.Template

class AtomicDropRepository(
    private val chainApi: ChainApi,
    private val atomicAssetsApi: AtomicAssetsApi,
    private val sharedPreferences: SharedPreferences
) {
    suspend fun getAtomicDrops(tableRow: TableRow) = chainApi.getAtomicDrops(tableRow)

    suspend fun fetchTemplate(collectionName: String?, templateId: Int?): TemplateResult {
        return if (!collectionName.isNullOrBlank() && templateId != null) {
            atomicAssetsApi.getTemplate(collectionName, templateId)
        } else {
            TemplateResult(success = false, template = Template())
        }
    }


    fun persistAtomicDrop(dropId: Int) = sharedPreferences
        .edit()
        .putInt(DROP_ID, dropId)
        .apply()

    fun lastPersistedDrop() = sharedPreferences.getInt(DROP_ID, -1)

    companion object {
        const val DROP_ID = "dropId"
    }
}