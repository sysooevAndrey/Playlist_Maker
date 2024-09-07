package com.practicum.playlistmaker.util.button

import android.app.Activity
import android.content.Intent
import android.view.View

object NavigationButton {
    fun <A : Activity> click(
        activity: Activity, view: View, activityType: Class<A>
    ) {
        view.setOnClickListener {
            val displayIntent = Intent(activity, activityType)
            activity.startActivity(displayIntent)
        }
    }
}
