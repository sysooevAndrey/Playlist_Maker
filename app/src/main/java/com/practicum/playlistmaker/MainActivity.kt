package com.practicum.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<MaterialButton>(R.id.search)
        val mediaButton = findViewById<MaterialButton>(R.id.media)
        val settingButton = findViewById<MaterialButton>(R.id.settings)

        searchButton.setOnClickListener {
            val displayIntent = Intent(this,SearchActivity::class.java)
            startActivity(displayIntent)
        }
        mediaButton.setOnClickListener{
            val displayIntent = Intent(this,MediaActivity::class.java)
            startActivity(displayIntent)
        }
        settingButton.setOnClickListener{
            val displayIntent = Intent(this,SettingActivity::class.java)
            startActivity(displayIntent)
        }
    }
}