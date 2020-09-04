package com.javiermendonca.atomicassetsnotifier.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.javiermendonca.atomicassetsnotifier.data.repository.AtomicNftRepository

class NftCheckWorkerFactory(private val atomicNftRepository: AtomicNftRepository) :
    WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): CoroutineWorker? {
        return when (workerClassName) {
            NftCheckWorker::class.java.name -> NftCheckWorker(
                appContext,
                workerParameters,
                atomicNftRepository
            )
            else -> null
        }
    }
}