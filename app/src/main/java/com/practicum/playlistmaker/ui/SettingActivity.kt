package com.practicum.playlistmaker.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.ThemeManager
import com.practicum.playlistmaker.util.button.DataIntent
import com.practicum.playlistmaker.util.button.DataTransferButton
import com.practicum.playlistmaker.util.button.NavigationButton
import com.practicum.playlistmaker.util.App
import com.practicum.playlistmaker.util.Creator

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val themeManager: ThemeManager = Creator.getThemeManager(this)
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.theme_switcher)
        themeSwitcher.isChecked =
            themeManager.getData()
        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)
            themeManager.saveData(checked)
        }
        NavigationButton.back<ImageView>(
            this, R.id.back
        )
        DataTransferButton.button<ImageView>(
            this, R.id.help_button, DataIntent.MAIL
        )
        DataTransferButton.button<ImageView>(
            this, R.id.link_button, DataIntent.MESSAGE
        )
        DataTransferButton.button<ImageView>(
            this, R.id.user_agreement_button, DataIntent.USER_AGREEMENT
        )
    }
}