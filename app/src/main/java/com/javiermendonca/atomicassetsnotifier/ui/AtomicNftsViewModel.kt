package com.javiermendonca.atomicassetsnotifier.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.javiermendonca.atomicassetsnotifier.data.dtos.AtomicDrop
import com.javiermendonca.atomicassetsnotifier.data.dtos.TableRow
import com.javiermendonca.atomicassetsnotifier.data.repository.AtomicNftRepository
import com.javiermendonca.atomicassetsnotifier.data.worker.NftDropCheckWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit


class AtomicNftsViewModel(val atomicNftRepository: AtomicNftRepository, application: Application) :
    AndroidViewModel(application) {
    private val workManager = WorkManager.getInstance(application)
    val atomicDrops = MutableLiveData<List<AtomicDrop>>()

    init {
        queueBackgroundJob()
    }

    fun fetchAtomicDrops() {
        viewModelScope.launch(Dispatchers.IO) {
            atomicDrops.postValue(atomicNftRepository.getAtomicDrops(TableRow(limit = 10)).rows)
        }
    }

    private fun queueBackgroundJob() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val currentDate = Calendar.getInstance()
        val dueDate = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 9)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            if (before(currentDate)) {
                add(Calendar.HOUR_OF_DAY, 24)
            }
        }

        val timeDiff = dueDate.timeInMillis - currentDate.timeInMillis
        val dailyWorkRequest = OneTimeWorkRequestBuilder<NftDropCheckWorker>()
            .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
            .addTag(WORKER_TAG)
            .setConstraints(constraints)
            .build()

        val testJob = PeriodicWorkRequestBuilder<NftDropCheckWorker>(3, TimeUnit.SECONDS).build()

        workManager.enqueue(testJob)
    }

    companion object {
        const val WORKER_TAG = "AtomicDrops"
    }
}

