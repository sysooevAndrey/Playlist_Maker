package com.practicum.playlistmaker

import android.content.Intent

enum class DataIntent(
    val action: String,
    val params: Map<String, Int>,
    val extra: Map<String, Int>
) {
    MAIL(
        Intent.ACTION_SENDTO,
        mapOf(
            Pair(Action.DATA_KEY, R.string.data_mail),
            Pair(Action.TITLE_KEY, R.string.title_mail)
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
            Pair(Action.TYPE_KEY, R.string.type_message),
            Pair(Action.TITLE_KEY, R.string.title_message)
        ),
        mapOf(
            Pair(Intent.EXTRA_TEXT, R.string.url_link),
        )
    ),
    USER_AGREEMENT(
        Intent.ACTION_VIEW,
        mapOf(
            Pair(Action.DATA_KEY, R.string.url_user_agreement)
        ),
        mapOf(
            Pair(Intent.EXTRA_TEXT, R.string.url_user_agreement)
        )
    );
}
