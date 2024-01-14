package com.practicum.playlistmaker

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class Action {

    companion object {
        const val DATA_KEY: String = "DATA"
        const val TYPE_KEY: String = "TYPE"
        const val TITLE_KEY: String = "TITLE"
        const val SEARCH_TEXT: String = "SEARCH_TEXT"
        const val SEARCH_TEXT_DEF: String = ""

        fun <V : View, A : Activity> navigationButton(
            appCompatActivity: AppCompatActivity, id: Int, activityType: Class<A>
        ): V {
            val button = appCompatActivity.findViewById<V>(id)
            button.setOnClickListener {
                val displayIntent = Intent(appCompatActivity, activityType)
                appCompatActivity.startActivity(displayIntent)
            }
            return button
        }

        fun <V : View> backButton(appCompatActivity: AppCompatActivity, id: Int): V {
            val button = appCompatActivity.findViewById<V>(id)
            button.setOnClickListener {
                appCompatActivity.finish()
            }
            return button
        }

        fun search(
            appCompatActivity: AppCompatActivity,
            id: Int,
            inText: String
        ): EditText {
            var searchText = inText
            val searchEditText = appCompatActivity.findViewById<EditText>(id)
            searchEditText.setText(searchText)

            val button =
                clearButton<ImageView>(appCompatActivity, searchEditText, R.id.clear_text)

            val searchEditTextWatcher = object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    button.visibility = clearButtonVisibility(s)
                    searchText = s.toString()
                }

                override fun afterTextChanged(s: Editable?) {
                }
            }
            searchEditText.addTextChangedListener(searchEditTextWatcher)
            return searchEditText
        }

        private fun <V : View> clearButton(
            appCompatActivity: AppCompatActivity,
            editText: EditText,
            id: Int
        ): V {
            val button = appCompatActivity.findViewById<V>(id)
            button.setOnClickListener {
                editText.setText(SEARCH_TEXT_DEF)
                val inputMethodManager =
                    appCompatActivity
                        .getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager?.hideSoftInputFromWindow(editText.windowToken, 0)
            }
            return button
        }

        private fun clearButtonVisibility(s: CharSequence?): Int {
            return if (s.isNullOrEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }

        fun <V : View> dataTransferButton(
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





