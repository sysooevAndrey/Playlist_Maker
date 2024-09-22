package com.practicum.playlistmaker.search.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.player.ui.PlayerActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {
    companion object {
        private const val SEARCH_TEXT_DEF: String = ""
    }

    private val viewModel: SearchViewModel by viewModel()

    private lateinit var binding: ActivitySearchBinding

    private var searchText: String = SEARCH_TEXT_DEF
    private val searchAdapter: TracksAdapter = TracksAdapter()
    private val historyAdapter: TracksAdapter = TracksAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getHistoryLiveData()
            .observe(this) { tracks ->
                historyAdapter.submitList(tracks)
            }

        viewModel.getSearchScreenStateLiveData().observe(this) { screenState ->
            when (screenState) {

                is SearchScreenState.Wait -> {
                    searchItemVisibility(SearchStatus.WAIT)
                    historyItemVisibility()
                }

                is SearchScreenState.Loading -> {
                    searchItemVisibility(SearchStatus.LOADING)
                    historyItemVisibility()
                }

                is SearchScreenState.Content -> {
                    searchAdapter.submitList(screenState.tracks)
                    searchItemVisibility(SearchStatus.SUCCESS)
                    historyItemVisibility()

                }

                is SearchScreenState.Error -> {
                    searchItemVisibility(screenState.searchStatus)
                    historyItemVisibility()
                }

            }
        }
        with(binding) {
            backButton.setOnClickListener {
                this@SearchActivity.finish()
            }
            searchEditText.setText(searchText)
            searchEditText.setOnFocusChangeListener { _, _ -> historyItemVisibility() }
            updateButton.setOnClickListener {
                viewModel.search(searchText)
            }
            clearText.setOnClickListener {
                searchEditText.setText(SEARCH_TEXT_DEF)
                searchAdapter.submitList(emptyList())
                historyItemVisibility()
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
                    clearText.isVisible = clearButtonVisibility(s)
                    searchText = s.toString()
                    historyItemVisibility()
                    viewModel.search(searchText, isDebounce = true)
                }

                override fun afterTextChanged(s: Editable?) {
                }
            }
            searchEditText.addTextChangedListener(searchEditTextWatcher)
            searchEditText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    viewModel.search(searchText)
                }
                false
            }
            clearHistoryButton.setOnClickListener {
                viewModel.clearHistory()
            }
        }

        searchAdapter.onItemClickListener = {
            addToHistory(it)
            openPlayerActivity(it)
        }
        historyAdapter.onItemClickListener = {
            addToHistory(it)
            openPlayerActivity(it)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.saveHistory()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.saveHistory()
    }

    private fun clearButtonVisibility(s: CharSequence?): Boolean {
        return !s.isNullOrEmpty()
    }

    private fun addToHistory(track: Track) {
        viewModel.addToHistory(track)
    }

    private fun openPlayerActivity(track: Track) {
        viewModel.selectTrack(track)
        this.startActivity(
            Intent(this, PlayerActivity::class.java)
        )
    }

    private fun searchItemVisibility(searchStatus: SearchStatus) {
        when (searchStatus) {
            SearchStatus.WAIT -> {
                with(binding) {
                    progressCircular.isVisible = false
                    tracksRecyclerView.isVisible = true
                    placeholderView.isVisible = false
                    updateButton.isVisible = false
                }
            }

            SearchStatus.LOADING -> {
                with(binding) {
                    progressCircular.isVisible = true
                    tracksRecyclerView.isVisible = false
                    placeholderView.isVisible = false
                    updateButton.isVisible = false
                }
            }

            SearchStatus.SUCCESS -> {
                with(binding) {
                    progressCircular.isVisible = false
                    tracksRecyclerView.isVisible = true
                    placeholderView.isVisible = false
                    updateButton.isVisible = false
                }
            }

            SearchStatus.NOT_FOUND -> {
                with(binding) {
                    progressCircular.isVisible = false
                    tracksRecyclerView.isVisible = false
                    placeholderView.isVisible = true
                    updateButton.isVisible = false
                    placeholderImage.setImageResource(R.drawable.placeholder_empty)
                    placeholderText.setText(R.string.empty_text)
                }
            }

            SearchStatus.NO_NETWORK -> {
                with(binding) {
                    progressCircular.isVisible = false
                    tracksRecyclerView.isVisible = false
                    placeholderView.isVisible = true
                    updateButton.isVisible = true
                    placeholderImage.setImageResource(R.drawable.placeholder_network)
                    placeholderText.setText(R.string.network_error_text)
                }
            }
        }
    }

    private fun historyItemVisibility() {
        with(binding) {
            if (
                searchEditText.hasFocus()
                && searchEditText.text.isEmpty()
                && historyAdapter.currentList.isNotEmpty()
            ) {
                tracksRecyclerView.adapter = historyAdapter
                clearHistoryButton.isVisible = true
                searchHistoryHeader.isVisible = true
            } else {
                tracksRecyclerView.adapter = searchAdapter
                clearHistoryButton.isVisible = false
                searchHistoryHeader.isVisible = false
            }
        }
    }
}