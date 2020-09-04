package com.javiermendonca.atomicassetsnotifier.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.javiermendonca.atomicassetsnotifier.R
import com.javiermendonca.atomicassetsnotifier.data.api.AtomicDropTableResult
import com.javiermendonca.atomicassetsnotifier.data.dtos.TableRow
import com.javiermendonca.atomicassetsnotifier.data.repository.AtomicNftRepository
import kotlinx.coroutines.coroutineScope
import java.text.SimpleDateFormat
import java.util.*

class NftCheckWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val atomicNftRepository: AtomicNftRepository
) :
    CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = coroutineScope {
        with(applicationContext) context@{
            try {
                atomicNftRepository.getAtomicDrops(
                    TableRow(
                        code = ATOMIC_DROPS_TABLE_CODE,
                        scope = ATOMIC_DROPS_TABLE_SCOPE,
                        table = ATOMIC_DROPS_TABLE_NAME
                    )
                ).run {
                    if (isSuccessful && body() != null) {
                        (body() as AtomicDropTableResult).rows.run {
                            if (true) {
                                makeStatusNotification(
                                    getString(R.string.notifications_new_drop_title),
                                    getString(
                                        R.string.notifications_new_drop_message,
                                        last().collectionName,
                                        last().dropId,
                                        SimpleDateFormat(
                                            "yyyy-MM-dd'T'HH:mm:ssZ",
                                            Locale("sv", "SE")
                                        ).format(last().startTime * 1000)
                                    ),
                                    this@context
                                )
                            }
                        }
                        Result.success(Data.Builder().build())
                    } else {
                        retry(getString(R.string.error_fetching_drops))
                    }
                }
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

        const val ATOMIC_DROPS_TABLE_CODE = "atomicdropsx"
        const val ATOMIC_DROPS_TABLE_SCOPE = "atomicdropsx"
        const val ATOMIC_DROPS_TABLE_NAME = "drops"
    }
}