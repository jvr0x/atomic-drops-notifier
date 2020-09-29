package com.javiermendonca.atomicdropsnotifier.data.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.javiermendonca.atomicdropsnotifier.R
import com.javiermendonca.atomicdropsnotifier.core.extensions.ended
import com.javiermendonca.atomicdropsnotifier.data.dtos.TableRow
import com.javiermendonca.atomicdropsnotifier.data.repository.AtomicDropRepository
import kotlinx.coroutines.coroutineScope
import java.util.*

class AtomicDropWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val atomicDropRepository: AtomicDropRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = coroutineScope {
        with(applicationContext) context@{
            try {
                makeStatusNotification(
                    getString(R.string.notifications_health_title),
                    SIMPLE_FORMAT_DATE.format(Calendar.getInstance().timeInMillis),
                    this@context,
                    notificationChannelId = R.string.notifications_health_channel_id
                )

                val lastSeenDropId = atomicDropRepository.lastPersistedDrop()
                val lastDrop =
                    atomicDropRepository.getAtomicDrops(TableRow(limit = 1)).rows.first()

                lastDrop.dropId?.let {
                    atomicDropRepository.persistAtomicDrop(it)
                    if (lastSeenDropId < it) {
                        if (lastDrop.endTime == 0L || !lastDrop.endTime.ended()) {
                            val upcomingDrops = it - lastSeenDropId
                            if (upcomingDrops > 0) {
                                makeStatusNotification(
                                    getString(R.string.notifications_new_drop_title),
                                    getString(
                                        R.string.notifications_new_drop_message,
                                        upcomingDrops
                                    ),
                                    this@context
                                )
                            }
                        }
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