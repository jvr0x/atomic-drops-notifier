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

            val collectionNames = drops.distinctBy { it.collectionName }.map { it.collectionName }

            val collections = collectionNames
                .map { async { atomicDropRepository.fetchCollectionImage(it) } }
                .map { it.await() }
                .map { it.collection }
                .toList()

            val atomicDropItems = drops.asSequence().map {
                val collectionName = it.collectionName
                AtomicDropItem(
                    dropId = it.dropId,
                    collection = collections.find { it.collectionName == collectionName },
                    templatesToMint = it.templatesToMint,
                    listingPrice = it.listingPrice,
                    priceRecipient = it.priceRecipient,
                    authRequired = it.authRequired,
                    accountLimit = it.accountLimit,
                    accountLimitCooldown = it.accountLimitCooldown,
                    maxClaimable = it.maxClaimable,
                    currentClaimable = it.currentClaimable,
                    startTime = it.startTime,
                    endTime = it.endTime,
                    description = it.description
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

        const val DROPS_LIMIT = 10
    }
}

