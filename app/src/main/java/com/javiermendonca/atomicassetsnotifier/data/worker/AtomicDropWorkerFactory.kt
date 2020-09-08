package com.javiermendonca.atomicassetsnotifier.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.javiermendonca.atomicassetsnotifier.data.repository.AtomicDropRepository

class AtomicDropWorkerFactory(private val atomicDropRepository: AtomicDropRepository) :
    WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): CoroutineWorker? {
        return when (workerClassName) {
            AtomicDropWorker::class.java.name -> AtomicDropWorker(
                appContext,
                workerParameters,
                atomicDropRepository
            )
            else -> null
        }
    }
}