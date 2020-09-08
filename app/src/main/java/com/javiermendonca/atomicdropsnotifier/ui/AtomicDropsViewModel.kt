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
import com.javiermendonca.atomicdropsnotifier.data.worker.AtomicDropWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AtomicDropsViewModel(
    private val atomicDropRepository: AtomicDropRepository,
    application: Application
) :
    AndroidViewModel(application) {
    private val workManager = WorkManager.getInstance(application)
    val atomicDrops = MutableLiveData<List<AtomicDrop>>()

    init {
        queueBackgroundJob()
    }

    fun fetchAtomicDrops() {
        viewModelScope.launch(Dispatchers.IO) {
            atomicDrops.postValue(atomicDropRepository.getAtomicDrops(TableRow(limit = DROPS_LIMIT)).rows)
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
        const val WORKER_FREQUENCY = 10L

        const val DROPS_LIMIT = 10
    }
}

