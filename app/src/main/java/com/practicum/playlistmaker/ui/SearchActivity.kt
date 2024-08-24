package com.practicum.playlistmaker.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import com.practicum.playlistmaker.util.Creator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.util.button.NavigationButton
import com.practicum.playlistmaker.domain.TrackDataManager
import com.practicum.playlistmaker.domain.api.TrackInteractor
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.util.Converter
import com.practicum.playlistmaker.util.Resource
import com.practicum.playlistmaker.util.SearchStatus

class SearchActivity : AppCompatActivity() {
    companion object {
        private const val SEARCH_TEXT_DEF: String = ""
        private const val SEARCH_DEBOUNCE_DELAY: Long = 2000L
        val trackList = ArrayList<Track>()
        val searchHistoryList = ArrayList<Track>()
    }

    private var searchText: String = SEARCH_TEXT_DEF
    private lateinit var placeholderSearch: FrameLayout
    private lateinit var placeholderSearchImageView: ImageView
    private lateinit var placeholderSearchTextView: TextView
    private lateinit var updateButton: MaterialButton
    private lateinit var clearButton: ImageView
    private lateinit var searchEditText: EditText
    private lateinit var clearHistoryButton: MaterialButton
    private lateinit var searchHistoryTextView: TextView
    private lateinit var searchRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private val trackListAdapter = TrackListAdapter(trackList)
    private val searchHistoryAdapter = TrackListAdapter(searchHistoryList)
    private lateinit var trackDataManager: TrackDataManager
    private lateinit var mainHandler: Handler

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        trackDataManager = Creator.getTrackDataManager(this)
        progressBar = findViewById(R.id.progress_circular)
        placeholderSearch = findViewById(R.id.placeholder_view)
        placeholderSearchImageView = findViewById(R.id.placeholder_image)
        placeholderSearchTextView = findViewById(R.id.placeholder_text)
        updateButton = findViewById(R.id.update_button)
        clearButton = findViewById(R.id.clear_text)
        searchEditText = findViewById(R.id.search_edit_text)
        clearHistoryButton = findViewById(R.id.clear_history_button)
        searchHistoryTextView = findViewById(R.id.search_history_header)
        searchRecyclerView = findViewById(R.id.search_recycler_view)
        mainHandler = Handler(Looper.getMainLooper())
        NavigationButton.back<ImageView>(this, R.id.back)
        val searchRunnable = Runnable { createRequest() }
        val searchThread = Thread {
            Looper.prepare()
            Looper.loop()
        }.start()

        fun searchDebounce() {
            searchThread.run {
                val handler = Handler(Looper.myLooper()!!)
                handler.removeCallbacks(searchRunnable)
                handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
            }
        }
        searchHistoryList.clear()
        searchHistoryList.addAll(trackDataManager.getData()
            .map { Converter.convertModel(it) as Track })
        searchEditText.setText(searchText)
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
                searchDebounce()
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
        trackListAdapter.onItemClickListener = {
            with(searchHistoryList) {
                if (contains(it)) {
                    remove(it)
                    add(0, it)
                } else if (size == 10) {
                    removeLast()
                    add(0, it)
                } else {
                    add(0, it)
                }
                trackListAdapter.notifyDataSetChanged()
                openTrackActivity(it)
            }
        }
        searchHistoryAdapter.onItemClickListener = {
            openTrackActivity(it)
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Boolean {
        return !s.isNullOrEmpty()
    }

    override fun onDestroy() {
        super.onDestroy()
        trackDataManager.saveData(searchHistoryList.map {
            Converter.convertModel(it)
        })
    }

    private fun placeholderVisibility(searchStatus: SearchStatus) {
        when (searchStatus) {
            SearchStatus.SUCCESS -> {
                placeholderSearch.isVisible = false
                progressBar.isVisible = false
            }

            SearchStatus.NOT_FOUND -> {
                placeholderSearchImageView.setImageResource(R.drawable.placeholder_empty)
                placeholderSearchTextView.setText(R.string.empty_text)
                placeholderSearch.isVisible = true
                updateButton.isVisible = false
                progressBar.isVisible = false
            }

            SearchStatus.NO_NETWORK -> {
                placeholderSearchImageView.setImageResource(R.drawable.placeholder_network)
                placeholderSearchTextView.setText(R.string.network_error_text)
                placeholderSearch.isVisible = true
                updateButton.isVisible = true
                progressBar.isVisible = false
            }

            SearchStatus.WAIT -> {
                progressBar.isVisible = true
                placeholderSearch.isVisible = false
                updateButton.isVisible = false
            }
        }
    }

    private fun createRequest() {
        trackList.clear()
        placeholderVisibility(SearchStatus.WAIT)
        val trackInteractor = Creator.provideTrackInteractor()
        trackInteractor.searchTrack(searchText, object : TrackInteractor.TrackConsumer {
            @SuppressLint("NotifyDataSetChanged")
            override fun consume(resource: Resource<List<Track>>) {
                mainHandler.post {
                    if (resource is Resource.Success) {
                        if (resource.data?.isNotEmpty() == true) {
                            trackList.addAll(resource.data)
                            trackListAdapter.notifyDataSetChanged()
                            placeholderVisibility(SearchStatus.SUCCESS)
                        } else placeholderVisibility(SearchStatus.NOT_FOUND)
                    } else
                        placeholderVisibility(SearchStatus.NO_NETWORK)
                }
            }
        })
    }

    private fun historyListItemVisibility(hasFocus: Boolean) {
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

    private fun openTrackActivity(track: Track) {
        val displayIntent = Intent(this, PlayerActivity::class.java)
        displayIntent.putExtra(
            PlayerActivity.TRACK_KEY,
            Gson().toJson(track).toString()
        )
        this.startActivity(displayIntent)
    }
}