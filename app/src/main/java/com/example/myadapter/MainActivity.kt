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
import java.util.*
import kotlin.concurrent.thread
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var items: MutableList<Any>
    private val adapter = DataAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        init()

        adapter.submitList(items)
        binding.rvItems.adapter = adapter

        binding.swipeRefreshLayout.setOnRefreshListener {
            swipeRefresh()
        }

        onClick()
        setupSwipeListener(binding.rvItems)
    }

    private fun init() {
        items = listOfItems()
    }

    private fun onClick() {

        adapter.onNewsClickListener = {
            Toast.makeText(this, "You clicked on news", Toast.LENGTH_SHORT).show()
        }
        adapter.onWeatherClickListener = {
            Toast.makeText(this, "You clicked on weather", Toast.LENGTH_SHORT).show()
        }
        adapter.onNewsLongClickListener = {

        }
        adapter.onWeatherLongClickListener = {

        }
    }

    private fun swipeRefresh() {
        binding.swipeRefreshLayout.isRefreshing = true
        items.add(News(id = count, title = "Title $count", news = "News $count"))
        count++
        items.add(
            Weather(
                id = count,
                city = "City $count",
                overview = "Overview $count"
            )
        )
        count++
        adapter.submitList(items)
        binding.rvItems.adapter = adapter
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
                    items.remove(item)
                    count--
                    adapter.submitList(items)
                    binding.rvItems.adapter = adapter
                } else if (item is Weather) {
                    items.remove(item)
                    count--
                    adapter.submitList(items)
                    binding.rvItems.adapter = adapter
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvItem)
    }

    private fun listOfItems(): MutableList<Any> {

        return mutableListOf(
            News(count++, getString(R.string.Title1), getString(R.string.News1)),
            News(count++, getString(R.string.Title2), getString(R.string.News2)),
            Weather(count++, "Алматы", "Температура +19, Сильный ветер"),
            Weather(count++, "Шымкент", "Температура +25, Солнечно")
        )
    }

    companion object {

        var count = 0
    }
}