package com.practicum.playlistmaker.button

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class NavigationButton {
    companion object {
        fun <V : View, A : Activity> navigate(
            appCompatActivity: AppCompatActivity, id: Int, activityType: Class<A>
        ): V {
            val button = appCompatActivity.findViewById<V>(id)
            button.setOnClickListener {
                val displayIntent = Intent(appCompatActivity, activityType)
                appCompatActivity.startActivity(displayIntent)
            }
            return button
        }

        fun <V : View> back(appCompatActivity: AppCompatActivity, id: Int): V {
            val button = appCompatActivity.findViewById<V>(id)
            button.setOnClickListener {
                appCompatActivity.finish()
            }
            return button
        }
    }
}