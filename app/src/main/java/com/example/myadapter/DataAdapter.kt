package com.example.myadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myadapter.databinding.ItemNewsBinding
import com.example.myadapter.databinding.ItemWeatherBinding
import com.example.myadapter.viewHolders.NewsViewHolder
import com.example.myadapter.viewHolders.WeatherViewHolder
import java.lang.RuntimeException

class DataAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onNewsClickListener: ((News) -> Unit)? = null

    var onWeatherClickListener: ((Weather) -> Unit)? = null

    private val diffCallback = object : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when {
                oldItem is News && newItem is News -> {
                    oldItem.id == newItem.id
                }
                oldItem is Weather && newItem is Weather -> {
                    oldItem.id == newItem.id
                }
                else -> false
            }
        }

        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when {
                oldItem is News && newItem is News -> {
                    oldItem as News == newItem as News
                }
                oldItem is Weather && newItem is Weather -> {
                    oldItem as Weather == newItem as Weather
                }
                else -> false
            }
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<Any>?) {
        differ.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        when (viewType) {
            NEWS_ITEM -> {
                return NewsViewHolder(
                    ItemNewsBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
            WEATHER_ITEM -> {
                return WeatherViewHolder(
                    ItemWeatherBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
            else -> {
                throw RuntimeException("Wrong Item")
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            NEWS_ITEM -> {
                val viewHolder = holder as NewsViewHolder
                val view = viewHolder.binding
                val item = differ.currentList[position] as News
                with(view) {
                    tvTitle.text = item.title
                    tvNews.text = item.news
                }
                view.cardView.setOnClickListener {
                    onNewsClickListener?.invoke(item)
                }
            }
            WEATHER_ITEM -> {
                val viewHolder = holder as WeatherViewHolder
                val view = viewHolder.binding
                val item = differ.currentList[position] as Weather
                with(view) {
                    tvCity.text = item.city
                    tvOverview.text = item.overview
                }
                view.cardView.setOnClickListener {
                    onWeatherClickListener?.invoke(item)
                }
            }

        }
    }

    override fun getItemViewType(position: Int): Int =
        when (differ.currentList[position]) {
            is News -> NEWS_ITEM
            is Weather -> WEATHER_ITEM
            else -> throw IllegalStateException("Incorrect ViewType found")
        }


    override fun getItemCount(): Int = differ.currentList.size

    companion object {

        private const val NEWS_ITEM = 100
        private const val WEATHER_ITEM = 101
    }
}