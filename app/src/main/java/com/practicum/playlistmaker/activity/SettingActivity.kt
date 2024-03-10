package com.practicum.playlistmaker.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.button.DataIntent
import com.practicum.playlistmaker.button.DataTransferButton
import com.practicum.playlistmaker.button.NavigationButton
import com.practicum.playlistmaker.R

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val sharedPrefs = getSharedPreferences(App.PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.theme_switcher)

        themeSwitcher.isChecked =
            sharedPrefs.getBoolean(App.APP_THEME_KEY, App.DARK_APP_THEME_DEFAULT)

        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)
            sharedPrefs.edit()
                .putBoolean(App.APP_THEME_KEY, checked)
                .apply()
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