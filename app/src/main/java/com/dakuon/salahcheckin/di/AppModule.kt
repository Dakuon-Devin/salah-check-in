package com.dakuon.salahcheckin.di

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.dakuon.salahcheckin.data.repository.PrayerTimeRepository
import com.dakuon.salahcheckin.data.source.local.AppDatabase
import com.dakuon.salahcheckin.data.source.local.PrayerTimeDao
import com.dakuon.salahcheckin.data.source.remote.AladhanApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "salah_checkin_db"
        ).build()
    }
    
    @Provides
    fun providePrayerTimeDao(database: AppDatabase): PrayerTimeDao {
        return database.prayerTimeDao()
    }
    
    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }
    
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }
    
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.aladhan.com/v1/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }
    
    @Provides
    @Singleton
    fun provideAladhanApiService(retrofit: Retrofit): AladhanApiService {
        return retrofit.create(AladhanApiService::class.java)
    }
    
    @Provides
    @Singleton
    fun providePrayerTimeRepository(
        aladhanApiService: AladhanApiService,
        prayerTimeDao: PrayerTimeDao
    ): PrayerTimeRepository {
        return PrayerTimeRepository(aladhanApiService, prayerTimeDao)
    }
}
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }
    
    @Provides
    @Singleton
    fun provideAladhanApiService(retrofit: Retrofit): AladhanApiService {
        return retrofit.create(AladhanApiService::class.java)
    }
    
    @Provides
    @Singleton
    fun providePrayerTimeRepository(
        aladhanApiService: AladhanApiService,
        prayerTimeDao: PrayerTimeDao
    ): PrayerTimeRepository {
        return PrayerTimeRepository(aladhanApiService, prayerTimeDao)
    }
}
