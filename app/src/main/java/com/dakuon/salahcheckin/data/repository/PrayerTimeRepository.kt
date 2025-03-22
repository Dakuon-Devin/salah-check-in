package com.dakuon.salahcheckin.data.repository

import com.dakuon.salahcheckin.data.model.PrayerTime
import com.dakuon.salahcheckin.data.source.local.PrayerTimeDao
import com.dakuon.salahcheckin.data.source.remote.AladhanApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrayerTimeRepository @Inject constructor(
    private val aladhanApiService: AladhanApiService,
    private val prayerTimeDao: PrayerTimeDao
) {
    
    suspend fun getTodayPrayerTimes(): List<PrayerTime> = withContext(Dispatchers.IO) {
        // ローカルDBから取得を試みる
        val cachedTimes = prayerTimeDao.getTodayPrayerTimes()
        
        if (cachedTimes.isNotEmpty()) {
            return@withContext cachedTimes
        }
        
        // APIから取得
        try {
            val apiResponse = aladhanApiService.getPrayerTimes()
            val prayerTimes = apiResponse.toPrayerTimeEntities()
            
            // ローカルDBに保存
            prayerTimeDao.insertAll(prayerTimes)
            
            return@withContext prayerTimes
        } catch (e: Exception) {
            // エラー処理
            throw e
        }
    }
    
    suspend fun updatePrayerStatus(prayerId: Long, completed: Boolean) = withContext(Dispatchers.IO) {
        prayerTimeDao.updatePrayerStatus(prayerId, completed)
    }
}
