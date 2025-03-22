package com.dakuon.salahcheckin.data.source.local

import androidx.room.TypeConverter
import com.dakuon.salahcheckin.data.model.PrayerName
import com.dakuon.salahcheckin.data.model.PrayerStatus
import java.time.LocalDateTime
import java.time.ZoneOffset

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? {
        return value?.let { LocalDateTime.ofEpochSecond(it, 0, ZoneOffset.UTC) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): Long? {
        return date?.toEpochSecond(ZoneOffset.UTC)
    }
    
    @TypeConverter
    fun fromPrayerName(value: PrayerName): String {
        return value.name
    }
    
    @TypeConverter
    fun toPrayerName(value: String): PrayerName {
        return PrayerName.valueOf(value)
    }
    
    @TypeConverter
    fun fromPrayerStatus(value: PrayerStatus): String {
        return value.name
    }
    
    @TypeConverter
    fun toPrayerStatus(value: String): PrayerStatus {
        return PrayerStatus.valueOf(value)
    }
}
