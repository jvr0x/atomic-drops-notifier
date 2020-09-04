package com.javiermendonca.atomicassetsnotifier.worker

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.javiermendonca.atomicassetsnotifier.R

class NftCheckWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    override fun doWork(): Result = with(applicationContext) {
        return try {
            val refreshTime = Data.Builder()
                .putString("refreshTime", "" + System.currentTimeMillis())
                .build()

            if (true) { //if there's a new drop
                makeStatusNotification(
                    getString(R.string.notifications_new_drop_title),
                    getString(R.string.notifications_new_drop_message),
                    this
                )
            }
            Result.success()
        } catch (throwable: Throwable) {
            Log.e(TAG, throwable.message ?: "Error checking the drops")
            Result.retry()
        }
    }

    companion object {
        const val TAG = "NftCheckWorker"
    }
}
