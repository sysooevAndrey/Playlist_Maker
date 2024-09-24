package com.practicum.playlistmaker.setting.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.util.MyApplication
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent

class SettingActivity : AppCompatActivity(), KoinComponent {

    private val viewModel : SettingViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getThemeLiveData().observe(this) {
            binding.themeSwitcher.isChecked = it
            (applicationContext as MyApplication).switchTheme(it)
        }
        with(binding) {
            backButton.setOnClickListener { this@SettingActivity.finish() }
            themeSwitcher.setOnCheckedChangeListener { _, checked -> viewModel.saveTheme(checked) }
            helpButton.setOnClickListener { viewModel.supportContact() }
            linkButton.setOnClickListener { viewModel.shareApp() }
            userAgreementButton.setOnClickListener { viewModel.openTerms() }
        }
    }
}