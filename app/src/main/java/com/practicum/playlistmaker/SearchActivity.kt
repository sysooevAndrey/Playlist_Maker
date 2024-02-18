package com.practicum.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private val trackList = ArrayList<TrackResponse.Track>()

    private var searchText: String = Search.SEARCH_TEXT_DEF

    private val baseUrl: String = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesSearchService: ITunesApi =
        retrofit.create(ITunesApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val placeholderEmpty = findViewById<LinearLayout>(R.id.placeholder_empty)

        val placeholderNetwork = findViewById<LinearLayout>(R.id.placeholder_network)

        val updateButton = findViewById<MaterialButton>(R.id.update_button)

        val trackAdapter = TrackAdapter(trackList)

        NavigationButton.back<ImageView>(this, R.id.back)
        val searchEditText =
            Search.search(
                this,
                R.id.search_edit_text,
                searchText,
                iTunesSearchService,
                trackList,
                trackAdapter,
                placeholderEmpty,
                placeholderNetwork,
                updateButton
            )
        searchText = searchEditText.text.toString()

        val searchRecyclerView =
            findViewById<RecyclerView>(R.id.search_recycler_view)

        searchRecyclerView.adapter = trackAdapter
    }
}