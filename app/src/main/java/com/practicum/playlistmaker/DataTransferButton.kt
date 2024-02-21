package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class DataTransferButton {
    companion object {
        const val DATA_KEY: String = "DATA"
        const val TYPE_KEY: String = "TYPE"
        const val TITLE_KEY: String = "TITLE"
        fun <V : View> button(
            appCompatActivity: AppCompatActivity, id: Int, dataIntent: DataIntent
        ): V {
            val button = appCompatActivity.findViewById<V>(id)
            button.setOnClickListener {
                val displayIntent = Intent(dataIntent.action)
                with(displayIntent) {
                    setIntentParams(appCompatActivity, this, dataIntent)
                    putIntentExtra(appCompatActivity, this, dataIntent)
                    startIntentActivity(appCompatActivity, this, dataIntent)
                }
            }
            return button
        }

        private fun setIntentParams(
            appCompatActivity: AppCompatActivity,
            intent: Intent,
            dataIntent: DataIntent
        ) {
            with(intent) {
                for ((key, value) in dataIntent.params) {
                    when (key) {
                        DATA_KEY -> data =
                            Uri.parse(appCompatActivity.resources.getString(value))

                        TYPE_KEY -> type =
                            appCompatActivity.resources.getString(value)
                    }
                }
            }
        }

        private fun putIntentExtra(
            appCompatActivity: AppCompatActivity,
            intent: Intent,
            dataIntent: DataIntent
        ) {
            with(intent) {
                for ((key, value) in dataIntent.extra) {
                    val extraValue =
                        when (key) {
                            Intent.EXTRA_EMAIL -> arrayOf(
                                appCompatActivity.resources.getString(value)
                            )

                            else -> appCompatActivity.resources.getString(value)
                        }
                    putExtra(key, extraValue)
                }
            }
        }

        private fun startIntentActivity(
            appCompatActivity: AppCompatActivity,
            intent: Intent,
            dataIntent: DataIntent
        ) {
            when (dataIntent) {
                DataIntent.MESSAGE -> appCompatActivity.startActivity(
                    Intent.createChooser(
                        intent, appCompatActivity.getString(dataIntent.params[TITLE_KEY]!!)
                    )
                )

                else -> appCompatActivity.startActivity(intent)
            }
        }
    }
}