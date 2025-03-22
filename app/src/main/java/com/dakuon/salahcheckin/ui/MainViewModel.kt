package com.dakuon.salahcheckin.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dakuon.salahcheckin.data.model.PrayerTime
import com.dakuon.salahcheckin.data.repository.PrayerTimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val prayerTimeRepository: PrayerTimeRepository
) : ViewModel() {
    
    private val _prayerTimes = MutableLiveData<List<PrayerTime>>()
    val prayerTimes: LiveData<List<PrayerTime>> = _prayerTimes
    
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading
    
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error
    
    fun loadTodayPrayerTimes() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val times = prayerTimeRepository.getTodayPrayerTimes()
                _prayerTimes.value = times
                _loading.value = false
            } catch (e: Exception) {
                _error.value = e.message
                _loading.value = false
            }
        }
    }
}
