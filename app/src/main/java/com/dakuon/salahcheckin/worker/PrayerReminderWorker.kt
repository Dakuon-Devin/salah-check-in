package com.dakuon.salahcheckin.worker

import android.content.Context
import android.content.Intent
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.dakuon.salahcheckin.data.model.PrayerStatus
import com.dakuon.salahcheckin.data.repository.PrayerTimeRepository
import com.dakuon.salahcheckin.ui.dialog.PrayerReminderDialogActivity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class PrayerReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val prayerTimeRepository: PrayerTimeRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val prayerId = inputData.getLong("PRAYER_ID", -1)
            if (prayerId == -1L) {
                return@withContext Result.failure()
            }
            
            // 礼拝のステータスを確認
            val prayerTime = prayerTimeRepository.getPrayerTimeById(prayerId)
            
            // まだ完了していない場合は再通知
            if (prayerTime != null && prayerTime.status == PrayerStatus.PENDING) {
                showPrayerReminderDialog(prayerId, prayerTime.name.name)
            }
            
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
    
    private fun showPrayerReminderDialog(prayerId: Long, prayerName: String) {
        val intent = Intent(applicationContext, PrayerReminderDialogActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra("PRAYER_ID", prayerId)
            putExtra("PRAYER_NAME", prayerName)
        }
        applicationContext.startActivity(intent)
    }
}
