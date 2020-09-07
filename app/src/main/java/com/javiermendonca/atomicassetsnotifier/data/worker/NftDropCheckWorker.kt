package com.javiermendonca.atomicassetsnotifier.data.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.javiermendonca.atomicassetsnotifier.R
import com.javiermendonca.atomicassetsnotifier.data.dtos.TableRow
import com.javiermendonca.atomicassetsnotifier.data.repository.AtomicNftRepository
import kotlinx.coroutines.coroutineScope
import java.text.SimpleDateFormat
import java.util.*

class NftDropCheckWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val atomicNftRepository: AtomicNftRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = coroutineScope {
        with(applicationContext) context@{
            try {
                val lastSeenDropId = atomicNftRepository.lastPersistedDrop()
                val lastUnSeenDrop = atomicNftRepository.getAtomicDrops(
                    TableRow(
                        limit = 1
                    )
                ).rows.first()

                lastUnSeenDrop.let {
                    if (lastSeenDropId != it.dropId) {
                        val upcomingDrops = it.dropId - lastSeenDropId
                        atomicNftRepository.persistLastDrop(it.dropId)
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
        Log.e(TAG, error)
        return Result.retry()
    }

    companion object {
        const val TAG = "NftCheckWorker"
        private val SIMPLE_FORMAT_DATE =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale("sv", "SE"))
    }
}