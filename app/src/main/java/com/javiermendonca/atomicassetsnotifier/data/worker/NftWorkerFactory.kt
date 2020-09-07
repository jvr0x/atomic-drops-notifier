package com.javiermendonca.atomicassetsnotifier.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.javiermendonca.atomicassetsnotifier.data.repository.AtomicNftRepository

class NftWorkerFactory(private val atomicNftRepository: AtomicNftRepository) :
    WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): CoroutineWorker? {
        return when (workerClassName) {
            NftDropCheckWorker::class.java.name -> NftDropCheckWorker(
                appContext,
                workerParameters,
                atomicNftRepository
            )
            else -> null
        }
    }
}