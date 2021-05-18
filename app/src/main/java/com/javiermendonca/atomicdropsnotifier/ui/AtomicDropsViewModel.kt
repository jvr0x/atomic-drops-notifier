package com.javiermendonca.atomicdropsnotifier.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.javiermendonca.atomicdropsnotifier.data.dtos.AtomicDrop
import com.javiermendonca.atomicdropsnotifier.data.dtos.TableRow
import com.javiermendonca.atomicdropsnotifier.data.repository.AtomicDropRepository
import com.javiermendonca.atomicdropsnotifier.data.repository.models.AtomicDropItem
import com.javiermendonca.atomicdropsnotifier.data.worker.AtomicDropWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AtomicDropsViewModel(
    private val atomicDropRepository: AtomicDropRepository,
    application: Application
) :
    AndroidViewModel(application) {
    private val workManager = WorkManager.getInstance(application)
    val atomicDrops = MutableLiveData<MutableList<AtomicDropItem>>()

    val loadingMore = MutableLiveData(false)
    val loading = MutableLiveData(false)
    val error = MutableLiveData(false)

    private var page: Int = 1

    init {
        queueBackgroundJob()
    }

    fun fetchAtomicDrops() {
        viewModelScope.launch {
            loading.postValue(true)

            try {
                val drops = atomicDropRepository.getAtomicDrops(TableRow(limit = DROPS_LIMIT)).rows
                val collectionNames = drops.distinctBy { it.assetsToMint?.first()?.templateId }
                    .map { it.collectionName to it.assetsToMint?.first()?.templateId }

                val atomicDropItems = getTemplates(collectionNames, drops)

                error.postValue(false)
                atomicDrops.postValue(atomicDropItems.toMutableList())
            } catch (e: Exception) {
                error.postValue(true)
            }

            loading.postValue(false)
        }
    }

    fun loadMore() {
        page += 1

        viewModelScope.launch {
            loadingMore.postValue(true)

            try {
                val drops =
                    atomicDropRepository.getAtomicDrops(TableRow(limit = DROPS_LIMIT * page)).rows

                val collectionNames =
                    drops.takeLast(DROPS_LIMIT)
                        .distinctBy { it.assetsToMint?.first()?.templateId }
                        .map { it.collectionName to it.assetsToMint?.first()?.templateId }

                val atomicDropItems = getTemplates(collectionNames, drops)

                error.postValue(false)
                val newList = atomicDrops.value?.let { it + atomicDropItems }?.toMutableList()
                    ?: atomicDrops.value

                atomicDrops.postValue(newList)
            } catch (e: Exception) {
                error.postValue(true)
            }

            loadingMore.postValue(false)
        }
    }

    private suspend fun CoroutineScope.getTemplates(
        collectionNames: List<Pair<String?, Int?>>,
        drops: List<AtomicDrop>
    ): List<AtomicDropItem> {
        val templates = collectionNames
            .map { async { atomicDropRepository.fetchTemplate(it.first, it.second) } }
            .map { it.await() }
            .filter { it.success }
            .map { it.template }
            .toList()

        return drops.asSequence().map {
            val templateId = it.assetsToMint?.first()?.templateId
            AtomicDropItem(
                dropId = it.dropId ?: -1,
                template = templates.find { it.id?.toInt() == templateId },
                templatesToMint = it.assetsToMint?.map { it.templateId ?: -1 } ?: listOf(),
                listingPrice = it.listingPrice ?: "",
                authRequired = it.authRequired ?: 0,
                accountLimit = it.accountLimit ?: 0,
                accountLimitCooldown = it.accountLimitCooldown ?: 0,
                maxClaimable = it.maxClaimable ?: 0,
                currentClaimable = it.currentClaimable ?: 0,
                startTime = it.startTime,
                endTime = it.endTime,
                description = it.displayData ?: ""
            )
        }.toList()
    }

    private fun queueBackgroundJob() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodicWork =
            PeriodicWorkRequestBuilder<AtomicDropWorker>(WORKER_FREQUENCY, TimeUnit.HOURS)
                .setConstraints(constraints)
                .addTag(WORKER_TAG)
                .build()

        workManager.enqueue(periodicWork)
    }

    companion object {
        const val WORKER_TAG = "AtomicDrops"
        const val WORKER_FREQUENCY = 3L

        const val DROPS_LIMIT = 20
    }
}

