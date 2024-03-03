package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Suppress("UNUSED_EXPRESSION")
class SearchActivity : AppCompatActivity() {
    companion object {
        private const val SEARCH_TEXT_DEF: String = ""
        private const val BASE_URL: String = "https://itunes.apple.com"
        val trackList = ArrayList<TrackResponse.Track>()
        val searchHistoryList = ArrayList<TrackResponse.Track>()
    }

    private var searchText: String = SEARCH_TEXT_DEF

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesSearchService: ITunesApi =
        retrofit.create(ITunesApi::class.java)

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val sharedPrefs = getSharedPreferences(App.PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)

        val type = object : TypeToken<ArrayList<TrackResponse.Track>>() {}.type

        val json = sharedPrefs.getString(App.SEARCH_HISTORY_KEY, null)

        if (!json.isNullOrEmpty()) {
            searchHistoryList.addAll(
                Gson().fromJson(
                    json,
                    type
                )
            )
        }

        val placeholderSearch = findViewById<FrameLayout>(R.id.placeholder_view)

        val placeholderSearchImageView = findViewById<ImageView>(R.id.placeholder_image)

        val placeholderSearchTextView = findViewById<TextView>(R.id.placeholder_text)

        val updateButton = findViewById<MaterialButton>(R.id.update_button)

        val clearButton = findViewById<ImageView>(R.id.clear_text)

        val searchEditText = findViewById<EditText>(R.id.search_edit_text)

        val clearHistoryButton = findViewById<MaterialButton>(R.id.clear_history_button)

        val searchHistoryTextView = findViewById<TextView>(R.id.search_history_header)

        val searchRecyclerView = findViewById<RecyclerView>(R.id.search_recycler_view)

        val trackListAdapter = TrackListAdapter(trackList)

        val searchHistoryAdapter = HistoryTrackListAdapter(searchHistoryList)

        NavigationButton.back<ImageView>(this, R.id.back)

        searchEditText.setText(searchText)

        fun placeholderVisibility(searchStatus: SearchStatus) {
            when (searchStatus) {
                SearchStatus.SUCCESS -> {
                    placeholderSearch.isVisible = false
                }

                SearchStatus.NOT_FOUND -> {
                    placeholderSearchImageView.setImageResource(R.drawable.placeholder_empty)
                    placeholderSearchTextView.setText(R.string.empty_text)
                    placeholderSearch.isVisible = true
                    updateButton.isVisible = false
                }

                SearchStatus.NO_NETWORK -> {
                    placeholderSearchImageView.setImageResource(R.drawable.placeholder_network)
                    placeholderSearchTextView.setText(R.string.network_error_text)
                    placeholderSearch.isVisible = true
                    updateButton.isVisible = true
                }
            }
        }

        fun createRequest() {
            trackList.clear()
            placeholderSearch.isVisible = false
            iTunesSearchService
                .search(searchText)
                .enqueue(object : Callback<TrackResponse> {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onResponse(
                        call: Call<TrackResponse>,
                        response: Response<TrackResponse>
                    ) {
                        if (response.code() == 200) {
                            trackList.addAll(response.body()?.results!!)
                            trackListAdapter.notifyDataSetChanged()
                            if (trackList.isEmpty()) {
                                placeholderVisibility(SearchStatus.NOT_FOUND)
                            }
                        } else {
                            placeholderVisibility(SearchStatus.NO_NETWORK)
                        }
                    }

                    override fun onFailure(
                        call: Call<TrackResponse>,
                        t: Throwable
                    ) {
                        placeholderVisibility(SearchStatus.NO_NETWORK)
                    }
                })
            true
        }

        fun historyListItemVisibility(hasFocus: Boolean) {
            if (hasFocus && searchEditText.text.isEmpty() && searchHistoryList.isNotEmpty()) {
                searchRecyclerView.adapter = searchHistoryAdapter
                clearHistoryButton.isVisible = true
                searchHistoryTextView.isVisible = true
            } else {
                searchRecyclerView.adapter = trackListAdapter
                clearHistoryButton.isVisible = false
                searchHistoryTextView.isVisible = false
            }
        }

        updateButton.setOnClickListener {
            createRequest()
        }

        clearButton.setOnClickListener {
            searchEditText.setText(SEARCH_TEXT_DEF)
            trackList.clear()
            trackListAdapter.notifyDataSetChanged()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager
                ?.hideSoftInputFromWindow(searchEditText.windowToken, 0)
        }

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
                clearButton.isVisible = clearButtonVisibility(s)
                searchText = s.toString()
                historyListItemVisibility(searchEditText.hasFocus())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

        searchEditText.addTextChangedListener(searchEditTextWatcher)
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                createRequest()
            }
            false
        }

        searchEditText.setOnFocusChangeListener { _, hasFocus ->
            historyListItemVisibility(hasFocus)
        }
        clearHistoryButton.setOnClickListener {
            searchHistoryList.clear()
            historyListItemVisibility(searchEditText.hasFocus())
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Boolean {
        return !s.isNullOrEmpty()
    }

    override fun onDestroy() {
        super.onDestroy()
        val sharedPrefs = getSharedPreferences(App.PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        sharedPrefs.edit()
            .putString(App.SEARCH_HISTORY_KEY, Gson().toJson(searchHistoryList))
            .apply()
    }
}

