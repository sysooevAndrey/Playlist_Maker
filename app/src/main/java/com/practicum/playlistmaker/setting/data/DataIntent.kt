package com.practicum.playlistmaker.setting.data

import android.content.Intent
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.setting.data.SharingRepositoryImpl.Companion.DATA_KEY
import com.practicum.playlistmaker.setting.data.SharingRepositoryImpl.Companion.TITLE_KEY
import com.practicum.playlistmaker.setting.data.SharingRepositoryImpl.Companion.TYPE_KEY

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