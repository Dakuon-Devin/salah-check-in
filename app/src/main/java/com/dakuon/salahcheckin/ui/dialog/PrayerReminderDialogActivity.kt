package com.dakuon.salahcheckin.ui.dialog

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dakuon.salahcheckin.R
import com.dakuon.salahcheckin.data.model.PrayerName
import com.dakuon.salahcheckin.databinding.ActivityPrayerReminderDialogBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PrayerReminderDialogActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrayerReminderDialogBinding
    private val viewModel: PrayerReminderDialogViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrayerReminderDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ダイアログスタイルの設定
        setupDialogStyle()
        
        // 引数の取得
        val prayerId = intent.getLongExtra("PRAYER_ID", -1)
        val prayerNameStr = intent.getStringExtra("PRAYER_NAME") ?: ""
        val prayerName = try {
            PrayerName.valueOf(prayerNameStr)
        } catch (e: Exception) {
            PrayerName.FAJR // デフォルト
        }
        
        // UIの設定
        setupUI(prayerName)
        
        // ボタンのクリックリスナー
        binding.yesButton.setOnClickListener {
            viewModel.markPrayerAsCompleted(prayerId)
            finish()
        }
        
        binding.noButton.setOnClickListener {
            viewModel.schedulePrayerReminder(prayerId)
            finish()
        }
    }
    
    private fun setupDialogStyle() {
        // ダイアログスタイルの設定
        window.setBackgroundDrawableResource(android.R.color.transparent)
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
    }
    
    private fun setupUI(prayerName: PrayerName) {
        // 礼拝名の表示
        val prayerNameText = when (prayerName) {
            PrayerName.FAJR -> getString(R.string.fajr)
            PrayerName.DHUHR -> getString(R.string.dhuhr)
            PrayerName.ASR -> getString(R.string.asr)
            PrayerName.MAGHRIB -> getString(R.string.maghrib)
            PrayerName.ISHA -> getString(R.string.isha)
        }
        
        binding.prayerNameTextView.text = prayerNameText
    }
}
