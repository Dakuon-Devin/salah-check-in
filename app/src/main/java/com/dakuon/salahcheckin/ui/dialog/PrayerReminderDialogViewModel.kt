package com.dakuon.salahcheckin.ui.dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.dakuon.salahcheckin.data.repository.PrayerTimeRepository
import com.dakuon.salahcheckin.worker.PrayerReminderWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class PrayerReminderDialogViewModel @Inject constructor(
    private val prayerTimeRepository: PrayerTimeRepository,
    private val workManager: WorkManager
) : ViewModel() {

    fun markPrayerAsCompleted(prayerId: Long) {
        if (prayerId == -1L) return
        
        viewModelScope.launch {
            prayerTimeRepository.updatePrayerStatus(prayerId, true)
        }
    }
    
    fun schedulePrayerReminder(prayerId: Long) {
        if (prayerId == -1L) return
        
        // 30分後に再通知するワーカーをスケジュール
        val inputData = Data.Builder()
            .putLong("PRAYER_ID", prayerId)
            .build()
            
        val reminderWork = OneTimeWorkRequestBuilder<PrayerReminderWorker>()
            .setInputData(inputData)
            .setInitialDelay(30, TimeUnit.MINUTES)
            .build()
            
        workManager.enqueue(reminderWork)
    }
}
