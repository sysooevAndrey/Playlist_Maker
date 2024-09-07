package com.practicum.playlistmaker.setting.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.util.button.DataTransferButton
import com.practicum.playlistmaker.util.App

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel = ViewModelProvider(
            this,
            SettingViewModel.getViewModelFactory()
        )[SettingViewModel::class.java]

        viewModel.getThemeLiveData().observe(this) {
            binding.themeSwitcher.isChecked = it
            (applicationContext as App).switchTheme(it)
        }

        binding.backButton.setOnClickListener { this.finish() }

        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.saveTheme(checked)
        }
        DataTransferButton
            .click(this, binding.helpButton, DataTransferButton.DataIntent.MAIL)
        DataTransferButton
            .click(this, binding.linkButton, DataTransferButton.DataIntent.MESSAGE)
        DataTransferButton.click(
            this,
            binding.userAgreementButton,
            DataTransferButton.DataIntent.USER_AGREEMENT
        )
    }
}