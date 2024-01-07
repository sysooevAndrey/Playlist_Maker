package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView


class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<ImageView>(R.id.back)
        val helpLine = findViewById<ImageView>(R.id.help_button)
        val linkLine = findViewById<ImageView>(R.id.link_button)
        val userAgreementLine = findViewById<ImageView>(R.id.user_agreement_button)

        backButton.setOnClickListener {
            this.finish()
        }
        helpLine.setOnClickListener {
            val helpIntent = Intent(Intent.ACTION_SENDTO)
            with(helpIntent) {
                data = Uri.parse("mailto:")
                putExtra(
                    Intent.EXTRA_EMAIL,
                    arrayOf(resources.getString(R.string.developer_email))
                )
                putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.subject_help))
                putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.message_help))
                startActivity(this)
            }
        }
        linkLine.setOnClickListener {
            val linkIntent = Intent(Intent.ACTION_SEND)
            with(linkIntent) {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.url_link))
                startActivity(this)
            }
        }
        userAgreementLine.setOnClickListener {
            val userAgreementIntent = Intent(Intent.ACTION_VIEW)
            userAgreementIntent.data = Uri.parse(resources.getString(R.string.url_user_agreement))
            startActivity(userAgreementIntent)
        }
    }
}