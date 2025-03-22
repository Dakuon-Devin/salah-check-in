package com.dakuon.salahcheckin.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dakuon.salahcheckin.data.model.PrayerTime
import java.time.LocalDateTime

@Dao
interface PrayerTimeDao {
    
    @Query("SELECT * FROM prayer_times WHERE date(time) = date('now') ORDER BY time ASC")
    suspend fun getTodayPrayerTimes(): List<PrayerTime>
    
    @Query("SELECT * FROM prayer_times WHERE id = :prayerId")
    suspend fun getPrayerTimeById(prayerId: Long): PrayerTime?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(prayerTimes: List<PrayerTime>)
    
    @Query("UPDATE prayer_times SET status = CASE WHEN :completed = 1 THEN 'COMPLETED' ELSE 'MISSED' END WHERE id = :prayerId")
    suspend fun updatePrayerStatus(prayerId: Long, completed: Boolean)
}
