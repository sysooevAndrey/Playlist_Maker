package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("UNUSED_EXPRESSION")
class Search {

    companion object {

        const val SEARCH_TEXT_DEF: String = ""

        private var textRequest: String = ""

        fun search(
            appCompatActivity: AppCompatActivity,
            id: Int,
            inText: String,
            apiService: ITunesApi,
            trackList: ArrayList<TrackResponse.Track>,
            trackAdapter: TrackAdapter,
            placeholderEmpty: LinearLayout,
            placeholderNetwork: LinearLayout,
            updateButton: MaterialButton
        ): EditText {
            var searchText = inText
            val searchEditText = appCompatActivity.findViewById<EditText>(id)
            searchEditText.setText(searchText)

            updateButton.setOnClickListener() {
                createRequest(
                    textRequest,
                    apiService,
                    trackList,
                    trackAdapter,
                    placeholderEmpty,
                    placeholderNetwork,
                )
            }


            val clearButton =
                clearButton<ImageView>(
                    appCompatActivity,
                    searchEditText,
                    R.id.clear_text,
                    trackList,
                    trackAdapter
                )

            val searchEditTextWatcher = object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    clearButton.visibility = clearButtonVisibility(s)
                    searchText = s.toString()
                }

                override fun afterTextChanged(s: Editable?) {
                }
            }
            searchEditText.addTextChangedListener(searchEditTextWatcher)
            searchEditText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    textRequest = searchEditText.text.toString()
                    createRequest(
                        textRequest,
                        apiService,
                        trackList,
                        trackAdapter,
                        placeholderEmpty,
                        placeholderNetwork,
                    )
                    true
                }
                false
            }
            return searchEditText
        }
        @SuppressLint("NotifyDataSetChanged")
        private fun <V : View> clearButton(
            appCompatActivity: AppCompatActivity,
            editText: EditText,
            id: Int,
            trackList: ArrayList<TrackResponse.Track>,
            trackAdapter: TrackAdapter,
            ): V {
            val button = appCompatActivity.findViewById<V>(id)

            button.setOnClickListener {
                editText.setText(SEARCH_TEXT_DEF)
                trackList.clear()
                trackAdapter.notifyDataSetChanged()
                val inputMethodManager =
                    appCompatActivity
                        .getSystemService(Context.INPUT_METHOD_SERVICE)
                            as? InputMethodManager
                inputMethodManager
                    ?.hideSoftInputFromWindow(editText.windowToken, 0)
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

        private fun createRequest(
            textRequest: String,
            apiService: ITunesApi,
            trackList: ArrayList<TrackResponse.Track>,
            trackAdapter: TrackAdapter,
            placeholderEmpty: LinearLayout,
            placeholderNetwork: LinearLayout,
        ) {
            trackList.clear()
            placeholderEmpty.visibility = View.GONE
            placeholderNetwork.visibility = View.GONE
            apiService
                .search(textRequest)
                .enqueue(object : Callback<TrackResponse> {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onResponse(
                        call: Call<TrackResponse>,
                        response: Response<TrackResponse>
                    ) {
                        if (response.code() == 200) {
                            trackList.addAll(response.body()?.results!!)
                            trackAdapter.notifyDataSetChanged()
                            if (trackList.isEmpty()) {
                                placeholderEmpty.visibility = View.VISIBLE
                            }
                        } else {
                            placeholderNetwork.visibility = View.VISIBLE

                        }
                    }

                    override fun onFailure(
                        call: Call<TrackResponse>,
                        t: Throwable
                    ) {
                        placeholderNetwork.visibility = View.VISIBLE
                    }

                })
        }
    }
}





