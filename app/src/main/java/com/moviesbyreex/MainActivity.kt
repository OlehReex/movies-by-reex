package com.moviesbyreex

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moviesbyreex.utils.JsonUtil
import com.moviesbyreex.utils.NetworkUtil

class MainActivity : AppCompatActivity() {
    private lateinit var textViewPopularity : TextView
    private lateinit var textViewTopRated : TextView
    private val movieAdapter = MovieAdapter()
    private var currentSwitchState = false
    private var pageCounter = 1

    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerViewPosters = findViewById<RecyclerView>(R.id.recyclerViewPosters)
        recyclerViewPosters.layoutManager = GridLayoutManager(this, getColumnCount())
        recyclerViewPosters.adapter = movieAdapter

        textViewPopularity = findViewById(R.id.textViewPopularity)
        textViewTopRated = findViewById((R.id.textViewTopRated))

        val switchSort = findViewById<Switch>(R.id.switchSort)
        switchSort.isChecked = true
        switchSort.setOnCheckedChangeListener { buttonView, isChecked ->
            setMethodOfSort(isChecked)
        }
        switchSort.isChecked = false

        movieAdapter.setOnPosterClickListener (object : MovieAdapter.OnPosterClickListener {
            override fun onPosterClick(position: Int) {
                val movie = movieAdapter.movies[position]
                intent = Intent(this@MainActivity, MovieDetailsActivity::class.java)
                intent.putExtra("id", movie.id)
                intent.putExtra("movie", movie)
                startActivity(intent)
            }
        })

        recyclerViewPosters.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val lastVisiblePosition = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    if (lastVisiblePosition == recyclerView.adapter!!.itemCount - 1) {
                        pageCounter++
                        setMethodOfSort(switchSort.isChecked)
                        Toast.makeText(this@MainActivity, "Download more", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setMethodOfSort(isTopRated: Boolean) {
        val methodOfSort: Int = if (isTopRated) {
            textViewTopRated.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
            textViewPopularity.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
            NetworkUtil.TOP_RATED
        } else {
            textViewTopRated.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
            textViewPopularity.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
            NetworkUtil.POPULARITY
        }
        if (isTopRated != currentSwitchState) {
            movieAdapter.movies.clear()
            movieAdapter.notifyDataSetChanged()
            currentSwitchState = isTopRated
            pageCounter = 1
        }

        val jsonObject = NetworkUtil.getJSONFromNetwork(methodOfSort, pageCounter)
        val movies = JsonUtil.getMoviesFromJSON(jsonObject)
        val startPosition = movieAdapter.movies.size
        movieAdapter.movies.addAll(movies)
        movieAdapter.notifyItemRangeInserted(startPosition, movies.size)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun getColumnCount(): Int {
        val displayMetrics = windowManager.currentWindowMetrics
        val bounds = displayMetrics.bounds
        val widthInDp = (bounds.width() / resources.displayMetrics.density).toInt()
        return (widthInDp / 185).coerceAtLeast(2)
    }
}
