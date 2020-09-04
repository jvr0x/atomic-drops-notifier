package com.javiermendonca.atomicassetsnotifier.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.work.*
import com.javiermendonca.atomicassetsnotifier.data.repository.AtomicNftRepository
import com.javiermendonca.atomicassetsnotifier.worker.NftCheckWorker
import java.util.*
import java.util.concurrent.TimeUnit


class AtomicNftsViewModel(atomicNftRepository: AtomicNftRepository, application: Application) :
    AndroidViewModel(application) {
    private val workManager = WorkManager.getInstance(application)

    internal fun fetchAtomicDrops() {
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
        val dailyWorkRequest = OneTimeWorkRequestBuilder<NftCheckWorker>()
            .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
            .addTag(WORKER_TAG)
            .setConstraints(constraints)
            .build()

        val testJob = PeriodicWorkRequestBuilder<NftCheckWorker>(3, TimeUnit.SECONDS).build()

        workManager.enqueue(testJob)
    }

    companion object {
        const val WORKER_TAG = "AtomicDrops"
    }
}

