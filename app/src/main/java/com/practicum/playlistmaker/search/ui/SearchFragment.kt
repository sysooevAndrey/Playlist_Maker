package com.practicum.playlistmaker.search.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.player.ui.PlayerActivity
import com.practicum.playlistmaker.search.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {
    companion object {
        private const val SEARCH_TEXT_DEF: String = ""
    }

    private val viewModel: SearchViewModel by viewModel()
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private var searchText: String = SEARCH_TEXT_DEF
    private val searchAdapter: TracksAdapter = TracksAdapter()
    private val historyAdapter: TracksAdapter = TracksAdapter()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getSearchScreenStateLiveData().observe(viewLifecycleOwner) { screenState ->
            when (screenState) {

                is SearchScreenState.Wait -> {
                    searchAdapter.submitList(emptyList())
                    itemVisibility(SearchScreenStatus.WAIT)
                }

                is SearchScreenState.Loading -> {
                    itemVisibility(SearchScreenStatus.LOADING)
                }

                is SearchScreenState.History -> {
                    historyAdapter.submitList(screenState.tracks)
                    itemVisibility(SearchScreenStatus.HISTORY)
                }

                is SearchScreenState.Content -> {
                    searchAdapter.submitList(screenState.tracks)
                    itemVisibility(SearchScreenStatus.SUCCESS)
                }

                is SearchScreenState.Error -> {
                    itemVisibility(screenState.searchScreenStatus)
                }
            }
        }
        with(binding) {
            updateButton.setOnClickListener {
                viewModel.search(searchText)
            }
            clearText.setOnClickListener {
                searchEditText.setText(SEARCH_TEXT_DEF)
                viewModel.showHistory()
                val inputMethodManager =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager
                    ?.hideSoftInputFromWindow(searchEditText.windowToken, 0)
            }
            searchEditText.setText(searchText)
            searchEditText.setOnFocusChangeListener { _, _ -> viewModel.showHistory() }
            val searchEditTextWatcher = object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {}
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    clearText.isVisible = clearButtonVisibility(s)
                    searchText = s.toString()
                    viewModel.search(searchText, isDebounce = true)
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
                viewModel.showHistory()
            }
        }

        searchAdapter.onItemClickListener = {
            viewModel.addToHistory(it)
            openPlayerActivity(it)
        }
        historyAdapter.onItemClickListener = {
            viewModel.addToHistory(it)
            openPlayerActivity(it)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.showHistory(isHistoryState = binding.tracksRecyclerView.adapter == historyAdapter)
    }

    private fun clearButtonVisibility(s: CharSequence?): Boolean {
        return !s.isNullOrEmpty()
    }

    private fun openPlayerActivity(track: Track) {
        viewModel.selectTrack(track)
        this.startActivity(
            Intent(requireContext(), PlayerActivity::class.java)
        )
    }

    private fun itemVisibility(searchScreenStatus: SearchScreenStatus) {
        when (searchScreenStatus) {
            SearchScreenStatus.WAIT -> {
                with(binding) {
                    progressCircular.isVisible = false
                    tracksRecyclerView.isVisible = false
                    placeholderView.isVisible = false
                    updateButton.isVisible = false
                    historyItemVisibility(isVisible = false)
                }
            }

            SearchScreenStatus.LOADING -> {
                with(binding) {
                    progressCircular.isVisible = true
                    tracksRecyclerView.isVisible = false
                    placeholderView.isVisible = false
                    updateButton.isVisible = false
                    historyItemVisibility(isVisible = false)
                }
            }

            SearchScreenStatus.SUCCESS -> {
                with(binding) {
                    tracksRecyclerView.adapter = searchAdapter
                    progressCircular.isVisible = false
                    tracksRecyclerView.isVisible = true
                    placeholderView.isVisible = false
                    updateButton.isVisible = false
                    historyItemVisibility(isVisible = false)
                }
            }

            SearchScreenStatus.HISTORY -> {
                with(binding) {
                    tracksRecyclerView.adapter = historyAdapter
                    progressCircular.isVisible = false
                    tracksRecyclerView.isVisible = true
                    placeholderView.isVisible = false
                    updateButton.isVisible = false
                    historyItemVisibility(isVisible = true)
                }
            }

            SearchScreenStatus.NOT_FOUND -> {
                with(binding) {
                    progressCircular.isVisible = false
                    tracksRecyclerView.isVisible = false
                    placeholderView.isVisible = true
                    updateButton.isVisible = false
                    historyItemVisibility(isVisible = false)
                    placeholderImage.setImageResource(R.drawable.placeholder_empty)
                    placeholderText.setText(R.string.empty_text)
                }
            }

            SearchScreenStatus.NO_NETWORK -> {
                with(binding) {
                    progressCircular.isVisible = false
                    tracksRecyclerView.isVisible = false
                    placeholderView.isVisible = true
                    updateButton.isVisible = true
                    historyItemVisibility(isVisible = false)
                    placeholderImage.setImageResource(R.drawable.placeholder_network)
                    placeholderText.setText(R.string.network_error_text)
                }
            }

            SearchScreenStatus.CLIENT_ERROR -> {
                with(binding) {
                    progressCircular.isVisible = false
                    tracksRecyclerView.isVisible = false
                    placeholderView.isVisible = true
                    updateButton.isVisible = true
                    historyItemVisibility(isVisible = false)
                    placeholderImage.setImageResource(R.drawable.placeholder_empty)
                    placeholderText.setText(R.string.client_error_text)
                }
            }
        }
    }

    private fun historyItemVisibility(isVisible: Boolean) {
        with(binding) {
            clearHistoryButton.isVisible = isVisible
            searchHistoryHeader.isVisible = isVisible
        }
    }
}