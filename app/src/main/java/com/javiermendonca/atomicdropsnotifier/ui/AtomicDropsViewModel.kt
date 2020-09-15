package com.javiermendonca.atomicdropsnotifier.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.javiermendonca.atomicdropsnotifier.data.dtos.TableRow
import com.javiermendonca.atomicdropsnotifier.data.repository.AtomicDropRepository
import com.javiermendonca.atomicdropsnotifier.data.repository.models.AtomicDropItem
import com.javiermendonca.atomicdropsnotifier.data.worker.AtomicDropWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AtomicDropsViewModel(
    private val atomicDropRepository: AtomicDropRepository,
    application: Application
) :
    AndroidViewModel(application) {
    private val workManager = WorkManager.getInstance(application)
    val atomicDrops = MutableLiveData<List<AtomicDropItem>>()

    init {
        queueBackgroundJob()
    }

    fun fetchAtomicDrops() {
        viewModelScope.launch(Dispatchers.IO) {
            val drops =
                atomicDropRepository.getAtomicDrops(TableRow(limit = DROPS_LIMIT)).rows

            val collectionNames = drops.distinctBy { it.assetsToMint.first().templateId }
                .map { it.collectionName to it.assetsToMint.first().templateId }

            val templates = collectionNames
                .map { async { atomicDropRepository.fetchTemplate(it.first, it.second) } }
                .map { it.await() }
                .map { it.template }
                .toList()

            val atomicDropItems = drops.asSequence().map {
                val templateId = it.assetsToMint.first().templateId
                AtomicDropItem(
                    dropId = it.dropId,
                    template = templates.find { it.id.toInt() == templateId },
                    templatesToMint = it.assetsToMint.map { it.templateId },
                    listingPrice = it.listingPrice,
                    authRequired = it.authRequired,
                    accountLimit = it.accountLimit,
                    accountLimitCooldown = it.accountLimitCooldown,
                    maxClaimable = it.maxClaimable,
                    currentClaimable = it.currentClaimable,
                    startTime = it.startTime,
                    endTime = it.endTime,
                    description = it.displayData
                )
            }.toList()

            atomicDrops.postValue(atomicDropItems)
        }
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
        const val WORKER_FREQUENCY = 5L

        const val DROPS_LIMIT = 20
    }
}

