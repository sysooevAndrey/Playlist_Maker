package com.practicum.playlistmaker.search.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.player.domain.api.SelectTrackInteractor
import com.practicum.playlistmaker.search.domain.api.HistoryInteractor
import com.practicum.playlistmaker.search.domain.api.TrackInteractor
import com.practicum.playlistmaker.search.domain.models.Resource
import com.practicum.playlistmaker.search.domain.models.Track

class SearchViewModel(
    private val selectTrackInteractor: SelectTrackInteractor,
    private val trackInteractor: TrackInteractor,
    private val historyInteractor: HistoryInteractor
) : ViewModel() {

    private val mainThreadHandler = Handler(Looper.getMainLooper())

    private val requestRunnable = Runnable { createRequest() }

    private var searchText = SEARCH_TEXT_DEF

    private val searchScreenStateLiveData =
        MutableLiveData<SearchScreenState>(SearchScreenState.Wait)

    fun getSearchScreenStateLiveData(): LiveData<SearchScreenState> = searchScreenStateLiveData

    fun addToHistory(track: Track) {
        var updHistory = emptyList<Track>()
        historyInteractor.getHistory(object : HistoryInteractor.TrackHistoryConsumer {
            override fun consume(tracksHistory: List<Track>) {
                val prevHistory = ArrayList(tracksHistory)
                updHistory =
                    if (prevHistory.contains(track)) prevHistory.apply { sortBy { it != track } }
                    else prevHistory.apply { add(0, track) }
            }
        })
        historyInteractor.saveHistory(updHistory)
    }

    fun search(request: String, isDebounce: Boolean = false) {
        if (request.isNotEmpty()) {
            setState(SearchScreenState.Loading)
            this.searchText = request
            if (isDebounce) {
                searchDebounce()
            } else createRequest()
        } else {
            removeSearchCallback()
            showHistory()
        }
    }

    fun clearHistory() {
        historyInteractor.clearHistory()
    }

    fun showHistory(isHistoryState: Boolean = true) {
        if (isHistoryState) {
            historyInteractor.getHistory(object : HistoryInteractor.TrackHistoryConsumer {
                override fun consume(tracksHistory: List<Track>) {
                    setState(
                        if (tracksHistory.isNotEmpty()) SearchScreenState.History(tracksHistory)
                        else SearchScreenState.Wait
                    )
                }
            })
        }
    }

    fun selectTrack(track: Track) = selectTrackInteractor.setSelected(track)

    private fun createRequest() {
        trackInteractor.searchTrack(searchText, object : TrackInteractor.TrackConsumer {
            override fun consume(resource: Resource<List<Track>>) {
                when (resource) {
                    is Resource.Error.ClientError ->
                        setState(SearchScreenState.Error(SearchScreenStatus.CLIENT_ERROR))

                    is Resource.Error.NoNetwork ->
                        setState(SearchScreenState.Error(SearchScreenStatus.NO_NETWORK))

                    is Resource.Error.NotFound ->
                        setState(SearchScreenState.Error(SearchScreenStatus.NOT_FOUND))

                    is Resource.Success ->
                        setState(SearchScreenState.Content(resource.data!!))
                }
            }
        })
    }

    private fun setState(state: SearchScreenState) = searchScreenStateLiveData.postValue(state)

    private fun searchDebounce() {
        mainThreadHandler.removeCallbacks(requestRunnable)
        mainThreadHandler.postDelayed(requestRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun removeSearchCallback() = mainThreadHandler.removeCallbacks(requestRunnable)

    private companion object {
        const val SEARCH_TEXT_DEF: String = ""
        const val SEARCH_DEBOUNCE_DELAY: Long = 2000L
    }
}