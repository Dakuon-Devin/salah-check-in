package com.dakuon.salahcheckin.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dakuon.salahcheckin.data.model.PrayerTime

@Database(entities = [PrayerTime::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun prayerTimeDao(): PrayerTimeDao
}
