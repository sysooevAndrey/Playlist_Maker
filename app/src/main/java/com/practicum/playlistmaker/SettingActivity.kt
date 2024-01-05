package com.practicum.playlistmaker

import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import kotlin.math.absoluteValue

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
            helpIntent.data = Uri.parse("mailto:")
            helpIntent.putExtra(
                Intent.EXTRA_EMAIL,
                arrayOf(resources.getString(R.string.developer_email))
            )
            helpIntent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.subject_help))
            helpIntent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.message_help))
            startActivity(helpIntent)
        }

        linkLine.setOnClickListener {
            val linkIntent = Intent(Intent.ACTION_SEND)
            linkIntent.type = "text/plain"
            linkIntent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.url_link))
            startActivity(linkIntent)
        }



        userAgreementLine.setOnClickListener {
            val userAgreementIntent = Intent(Intent.ACTION_VIEW)
            userAgreementIntent.data = Uri.parse(resources.getString(R.string.url_user_agreement))
            startActivity(userAgreementIntent)
        }


    }
}