package com.practicum.playlistmaker.setting.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker.setting.domain.api.SharingRepository

class SharingRepositoryImpl(private val context: Context) : SharingRepository {
    companion object {
        const val DATA_KEY: String = "DATA"
        const val TYPE_KEY: String = "TYPE"
        const val TITLE_KEY: String = "TITLE"
    }

    override fun shareApp() = createIntent(DataIntent.MESSAGE)
    override fun supportContact() = createIntent(DataIntent.MAIL)
    override fun openTerms() = createIntent(DataIntent.USER_AGREEMENT)

    private fun createIntent(dataIntent: DataIntent) {
        val displayIntent = Intent(dataIntent.action)
        setIntentParams(context, displayIntent, dataIntent)
        putIntentExtra(context, displayIntent, dataIntent)
        startIntentActivity(context, displayIntent, dataIntent)
    }

    private fun setIntentParams(
        context: Context,
        intent: Intent,
        dataIntent: DataIntent
    ) {
        with(intent) {
            for ((key, value) in dataIntent.params) {
                when (key) {
                    DATA_KEY -> data =
                        Uri.parse(context.resources.getString(value))

                    TYPE_KEY -> type =
                        context.resources.getString(value)
                }
            }
        }
    }

    private fun putIntentExtra(
        context: Context,
        intent: Intent,
        dataIntent: DataIntent
    ) {
        with(intent) {
            for ((key, value) in dataIntent.extra) {
                val extraValue =
                    when (key) {
                        Intent.EXTRA_EMAIL -> arrayOf(
                            context.resources.getString(value)
                        )

                        else -> context.resources.getString(value)
                    }
                putExtra(key, extraValue)
            }
        }
    }

    private fun startIntentActivity(
        context: Context,
        intent: Intent,
        dataIntent: DataIntent
    ) {
        intent.flags += Intent.FLAG_ACTIVITY_NEW_TASK
        when (dataIntent) {
            DataIntent.MESSAGE -> {
                val chooser =
                    Intent.createChooser(intent, context.getString(dataIntent.params[TITLE_KEY]!!))
                chooser.flags += Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(chooser)
            }

            else -> context.startActivity(intent)
        }
    }
}