package com.practicum.playlistmaker

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class Search {

    companion object {

        const val SEARCH_TEXT: String = "SEARCH_TEXT"
        const val SEARCH_TEXT_DEF: String = ""

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
    }
}





