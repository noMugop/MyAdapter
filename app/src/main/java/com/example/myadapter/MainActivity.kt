package com.example.myadapter

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.myadapter.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.concurrent.thread
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var news: MutableList<News>
    private lateinit var weather: MutableList<Weather>
    private val adapter = DataAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        init()

        adapter.submitList(news + weather)
        binding.rvItems.adapter = adapter

        binding.swipeRefreshLayout.setOnRefreshListener {
            swipeRefresh()
        }

        onClick()
        setupSwipeListener(binding.rvItems)
    }

    private fun init() {
        news = listOfNews()
        weather = listOfWeather()
    }

    private fun onClick() {

        adapter.onNewsClickListener = {
            Toast.makeText(this, "You clicked on news", Toast.LENGTH_SHORT).show()
        }
        adapter.onWeatherClickListener = {
            Toast.makeText(this, "You clicked on weather", Toast.LENGTH_SHORT).show()
        }
    }

    private fun swipeRefresh() {
        binding.swipeRefreshLayout.isRefreshing = true
        news.add(News(id = countNews, title = "Title $countNews", news = "News $countNews"))
        countNews++
        weather.add(Weather(id = countWeather, city = "City $countWeather", overview = "Overview $countWeather"))
        countWeather++
        (binding.rvItems.adapter as DataAdapter).submitList(news + weather)
        binding.swipeRefreshLayout.isRefreshing = false
    }

    private fun setupSwipeListener(rvItem: RecyclerView) {
        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = adapter.differ.currentList[viewHolder.adapterPosition]
                if (item is News) {
                    news.removeAt(item.id)
                    countNews--
                    (binding.rvItems.adapter as DataAdapter).submitList(news + weather)

                } else if (item is Weather){
                    weather.removeAt(item.id)
                    countWeather--
                    (binding.rvItems.adapter as DataAdapter).submitList(news + weather)
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvItem)
    }

    private fun listOfNews(): MutableList<News> {

        return mutableListOf(
            News(0, getString(R.string.Title1), getString(R.string.News1)),
            News(1, getString(R.string.Title2), getString(R.string.News2))
        )
    }

    private fun listOfWeather(): MutableList<Weather> {

        return mutableListOf(
            Weather(0, "Алматы", "Температура +19, Сильный ветер"),
            Weather(1, "Шымкент", "Температура +25, Солнечно")
        )
    }

    companion object {

        var countNews = 2
        var countWeather = 2
    }
}