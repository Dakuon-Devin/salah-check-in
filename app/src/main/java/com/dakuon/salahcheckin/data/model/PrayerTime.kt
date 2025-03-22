package com.dakuon.salahcheckin.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "prayer_times")
data class PrayerTime(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: PrayerName,
    val time: LocalDateTime,
    val status: PrayerStatus = PrayerStatus.PENDING
)

enum class PrayerName {
    FAJR, DHUHR, ASR, MAGHRIB, ISHA
}

enum class PrayerStatus {
    PENDING, COMPLETED, MISSED
}
