package com.practicum.playlistmaker.util.button

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.View
import com.practicum.playlistmaker.R

object DataTransferButton {
    const val DATA_KEY: String = "DATA"
    const val TYPE_KEY: String = "TYPE"
    const val TITLE_KEY: String = "TITLE"
    fun click(
        activity: Activity, view: View, dataIntent: DataIntent
    ) {
        view.setOnClickListener {
            val displayIntent = Intent(dataIntent.action)
            with(displayIntent) {
                setIntentParams(activity, this, dataIntent)
                putIntentExtra(activity, this, dataIntent)
                startIntentActivity(activity, this, dataIntent)
            }
        }
    }

    private fun setIntentParams(
        activity:Activity,
        intent: Intent,
        dataIntent: DataIntent
    ) {
        with(intent) {
            for ((key, value) in dataIntent.params) {
                when (key) {
                    DATA_KEY -> data =
                        Uri.parse(activity.resources.getString(value))

                    TYPE_KEY -> type =
                        activity.resources.getString(value)
                }
            }
        }
    }

    private fun putIntentExtra(
        activity: Activity,
        intent: Intent,
        dataIntent: DataIntent
    ) {
        with(intent) {
            for ((key, value) in dataIntent.extra) {
                val extraValue =
                    when (key) {
                        Intent.EXTRA_EMAIL -> arrayOf(
                            activity.resources.getString(value)
                        )

                        else -> activity.resources.getString(value)
                    }
                putExtra(key, extraValue)
            }
        }
    }

    private fun startIntentActivity(
        activity: Activity,
        intent: Intent,
        dataIntent: DataIntent
    ) {
        when (dataIntent) {
            DataIntent.MESSAGE -> activity.startActivity(
                Intent.createChooser(
                    intent, activity.getString(dataIntent.params[TITLE_KEY]!!)
                )
            )

            else -> activity.startActivity(intent)
        }
    }
    enum class DataIntent(
        val action: String,
        val params: Map<String, Int>,
        val extra: Map<String, Int>
    ) {
        MAIL(
            Intent.ACTION_SENDTO,
            mapOf(
                Pair(DATA_KEY, R.string.data_mail),
                Pair(TITLE_KEY, R.string.title_mail)
            ),
            mapOf(
                Pair(Intent.EXTRA_EMAIL, R.string.developer_email),
                Pair(Intent.EXTRA_SUBJECT, R.string.subject_help),
                Pair(Intent.EXTRA_TEXT, R.string.message_help)
            )
        ),
        MESSAGE(
            Intent.ACTION_SEND,
            mapOf(
                Pair(TYPE_KEY, R.string.type_message),
                Pair(TITLE_KEY, R.string.title_message)
            ),
            mapOf(
                Pair(Intent.EXTRA_TEXT, R.string.url_link),
            )
        ),
        USER_AGREEMENT(
            Intent.ACTION_VIEW,
            mapOf(
                Pair(DATA_KEY, R.string.url_user_agreement)
            ),
            mapOf(
                Pair(Intent.EXTRA_TEXT, R.string.url_user_agreement)
            )
        );
    }
}