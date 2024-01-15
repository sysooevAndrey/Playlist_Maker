package com.practicum.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
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