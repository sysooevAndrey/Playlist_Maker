package com.practicum.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        Action.backButton<ImageView>(
            this, R.id.back
        )
        Action.dataTransferButton<ImageView>(
            this, R.id.help_button, DataIntent.MAIL
        )
        Action.dataTransferButton<ImageView>(
            this, R.id.link_button, DataIntent.MESSAGE
        )
        Action.dataTransferButton<ImageView>(
            this, R.id.user_agreement_button, DataIntent.USER_AGREEMENT
        )
    }
}