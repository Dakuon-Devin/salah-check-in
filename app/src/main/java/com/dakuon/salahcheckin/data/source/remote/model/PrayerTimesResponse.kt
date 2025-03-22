package com.dakuon.salahcheckin.data.source.remote.model

import com.dakuon.salahcheckin.data.model.PrayerName
import com.dakuon.salahcheckin.data.model.PrayerTime
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@JsonClass(generateAdapter = true)
data class PrayerTimesResponse(
    @Json(name = "data") val data: PrayerData
) {
    fun toPrayerTimeEntities(): List<PrayerTime> {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val now = LocalDateTime.now()
        val timings = data.timings
        
        return listOf(
            PrayerTime(
                name = PrayerName.FAJR,
                time = parseTimeString(timings.fajr, now, formatter)
            ),
            PrayerTime(
                name = PrayerName.DHUHR,
                time = parseTimeString(timings.dhuhr, now, formatter)
            ),
            PrayerTime(
                name = PrayerName.ASR,
                time = parseTimeString(timings.asr, now, formatter)
            ),
            PrayerTime(
                name = PrayerName.MAGHRIB,
                time = parseTimeString(timings.maghrib, now, formatter)
            ),
            PrayerTime(
                name = PrayerName.ISHA,
                time = parseTimeString(timings.isha, now, formatter)
            )
        )
    }
    
    private fun parseTimeString(timeStr: String, date: LocalDateTime, formatter: DateTimeFormatter): LocalDateTime {
        val time = LocalDateTime.parse(timeStr, formatter)
        return date.withHour(time.hour).withMinute(time.minute).withSecond(0).withNano(0)
    }
}

@JsonClass(generateAdapter = true)
data class PrayerData(
    @Json(name = "timings") val timings: Timings
)

@JsonClass(generateAdapter = true)
data class Timings(
    @Json(name = "Fajr") val fajr: String,
    @Json(name = "Dhuhr") val dhuhr: String,
    @Json(name = "Asr") val asr: String,
    @Json(name = "Maghrib") val maghrib: String,
    @Json(name = "Isha") val isha: String
)
