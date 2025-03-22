package com.dakuon.salahcheckin.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.dakuon.salahcheckin.R
import com.dakuon.salahcheckin.data.model.PrayerTime
import com.dakuon.salahcheckin.data.repository.PrayerTimeRepository
import com.dakuon.salahcheckin.ui.MainActivity
import com.dakuon.salahcheckin.ui.dialog.PrayerReminderDialogActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@AndroidEntryPoint
class PrayerReminderService : Service() {

    @Inject
    lateinit var prayerTimeRepository: PrayerTimeRepository

    private val serviceScope = CoroutineScope(Dispatchers.Default + Job())
    private val NOTIFICATION_ID = 1001
    private val CHANNEL_ID = "prayer_reminder_channel"

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        serviceScope.launch {
            monitorPrayerTimes()
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private suspend fun monitorPrayerTimes() {
        while (true) {
            try {
                val prayerTimes = prayerTimeRepository.getTodayPrayerTimes()
                checkPrayerTimesAndNotify(prayerTimes)
            } catch (e: Exception) {
                // エラー処理
            }
            // 5分ごとにチェック
            delay(5 * 60 * 1000)
        }
    }

    private fun checkPrayerTimesAndNotify(prayerTimes: List<PrayerTime>) {
        val now = LocalDateTime.now()
        
        prayerTimes.forEach { prayerTime ->
            // 礼拝時間が過ぎていて、まだ完了していない場合
            if (prayerTime.time.isBefore(now) && prayerTime.status == com.dakuon.salahcheckin.data.model.PrayerStatus.PENDING) {
                showPrayerReminderDialog(prayerTime)
            }
        }
    }

    private fun showPrayerReminderDialog(prayerTime: PrayerTime) {
        val intent = Intent(this, PrayerReminderDialogActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra("PRAYER_ID", prayerTime.id)
            putExtra("PRAYER_NAME", prayerTime.name.name)
        }
        startActivity(intent)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.app_name))
            .setContentText("礼拝リマインダーサービス実行中")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(pendingIntent)
            .build()
    }
}
