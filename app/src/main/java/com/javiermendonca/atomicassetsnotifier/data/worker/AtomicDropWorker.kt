package com.javiermendonca.atomicassetsnotifier.data.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.javiermendonca.atomicassetsnotifier.R
import com.javiermendonca.atomicassetsnotifier.data.dtos.TableRow
import com.javiermendonca.atomicassetsnotifier.data.repository.AtomicDropRepository
import kotlinx.coroutines.coroutineScope

class AtomicDropWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val atomicDropRepository: AtomicDropRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = coroutineScope {
        with(applicationContext) context@{
            try {
                val lastSeenDropId = atomicDropRepository.lastPersistedDrop()
                val lastUnSeenDrop =
                    atomicDropRepository.getAtomicDrops(TableRow(limit = 1)).rows.first()

                lastUnSeenDrop.let {
                    if (lastSeenDropId != it.dropId) {
                        val upcomingDrops = it.dropId - lastSeenDropId
                        atomicDropRepository.persistAtomicDrop(it.dropId)
                        makeStatusNotification(
                            getString(R.string.notifications_new_drop_title),
                            getString(R.string.notifications_new_drop_message, upcomingDrops),
                            this@context
                        )
                    }
                }

                Result.success()
            } catch (throwable: Throwable) {
                makeStatusNotification(
                    getString(R.string.notifications_new_drop_title_error),
                    null,
                    this@context
                )
                retry(throwable.message ?: getString(R.string.error_fetching_drops))
            }
        }
    }

    private fun retry(error: String): Result {
        Log.e(this@AtomicDropWorker.javaClass.simpleName, error)
        return Result.retry()
    }
}