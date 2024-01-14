package com.practicum.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        Action.backButton<ImageView>(this, R.id.back)
        val searchEditText =
            Action.search(this, R.id.search_edit_text, searchText)
        searchText = searchEditText.text.toString()

        val searchRecyclerView = findViewById<RecyclerView>(R.id.search_recycler_view)

        val trackAdapter = TrackAdapter(mockTrackList)

        searchRecyclerView.adapter = trackAdapter
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(Action.SEARCH_TEXT, searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString(Action.SEARCH_TEXT, Action.SEARCH_TEXT_DEF)
    }

    private var searchText: String = Action.SEARCH_TEXT_DEF
}