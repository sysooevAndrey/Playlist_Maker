package com.practicum.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<MaterialButton>(R.id.search)
        val mediaButton = findViewById<MaterialButton>(R.id.media)
        val settingButton = findViewById<MaterialButton>(R.id.settings)

        val buttonOnClickListener : View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity,"NOT YET IMPLEMENTED",Toast.LENGTH_SHORT).show()
            }

        }

        searchButton.setOnClickListener(buttonOnClickListener)
        mediaButton.setOnClickListener{
            Toast.makeText(this@MainActivity,"NOT YET IMPLEMENTED",Toast.LENGTH_SHORT).show()
        }
        settingButton.setOnClickListener{
            Toast.makeText(this@MainActivity,"NOT YET IMPLEMENTED",Toast.LENGTH_SHORT).show()
        }

    }

}