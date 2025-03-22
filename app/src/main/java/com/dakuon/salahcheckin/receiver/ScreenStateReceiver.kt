package com.dakuon.salahcheckin.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.dakuon.salahcheckin.data.model.PrayerStatus
import com.dakuon.salahcheckin.data.repository.PrayerTimeRepository
import com.dakuon.salahcheckin.ui.dialog.PrayerReminderDialogActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@AndroidEntryPoint
class ScreenStateReceiver : BroadcastReceiver() {

    @Inject
    lateinit var prayerTimeRepository: PrayerTimeRepository
    
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_SCREEN_ON) {
            CoroutineScope(Dispatchers.IO).launch {
                checkPendingPrayers(context)
            }
        }
    }
    
    private suspend fun checkPendingPrayers(context: Context) {
        val now = LocalDateTime.now()
        val prayerTimes = prayerTimeRepository.getTodayPrayerTimes()
        
        // 時間が過ぎていて、まだ完了していない礼拝を探す
        val pendingPrayer = prayerTimes.find { 
            it.time.isBefore(now) && it.status == PrayerStatus.PENDING 
        }
        
        // 未完了の礼拝があればダイアログを表示
        if (pendingPrayer != null) {
            val intent = Intent(context, PrayerReminderDialogActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                putExtra("PRAYER_ID", pendingPrayer.id)
                putExtra("PRAYER_NAME", pendingPrayer.name.name)
            }
            context.startActivity(intent)
        }
    }
}
