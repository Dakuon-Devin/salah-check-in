package com.dakuon.salahcheckin.data.source.remote

import com.dakuon.salahcheckin.data.source.remote.model.PrayerTimesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface AladhanApiService {
    
    @GET("timingsByCity")
    suspend fun getPrayerTimes(
        @Query("city") city: String = "Tokyo",
        @Query("country") country: String = "Japan",
        @Query("method") method: Int = 2 // Islamic Society of North America
    ): PrayerTimesResponse
}
