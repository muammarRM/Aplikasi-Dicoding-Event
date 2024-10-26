package com.dicoding.dicodingevent.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.dicodingevent.data.local.entity.EventEntity
import com.dicoding.dicodingevent.data.remote.response.ListEventsItem
import com.dicoding.dicodingevent.databinding.ItemEventBinding

// Buat sealed class untuk membedakan item
sealed class EventItem {
    data class Regular(val event: ListEventsItem) : EventItem()
    data class Favorite(val event: EventEntity) : EventItem()
}

class EventAdapter(
    private val onItemClick: (EventItem) -> Unit
) : ListAdapter<EventItem, RecyclerView.ViewHolder>(EventDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> {
                val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                EventViewHolder(binding)
            }
            1 -> {
                val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false) // Ganti dengan layout yang sesuai untuk FavoriteEvent
                FavoriteEventViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val eventItem = getItem(position)
        when (holder) {
            is EventViewHolder -> {
                if (eventItem is EventItem.Regular) {
                    holder.bind(eventItem.event)
                }
            }
            is FavoriteEventViewHolder -> {
                if (eventItem is EventItem.Favorite) {
                    holder.bind(eventItem.event)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is EventItem.Regular -> 0
            is EventItem.Favorite -> 1
        }
    }

    inner class EventViewHolder(private val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem) {
            with(binding) {
                tvtitle.text = event.name
                tvdescription.text = event.summary
                Glide.with(itemView.context)
                    .load(event.imageLogo)
                    .into(imgItemPhoto)

                itemView.setOnClickListener { onItemClick(EventItem.Regular(event)) }
            }
        }
    }

    inner class FavoriteEventViewHolder(private val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: EventEntity) {
            with(binding) {
                tvtitle.text = event.name // Ganti dengan property yang sesuai dari EventEntity
                tvdescription.text = event.description // Ganti dengan property yang sesuai dari EventEntity
                Glide.with(itemView.context)
                    .load(event.mediaCover) // Ganti dengan property yang sesuai dari EventEntity
                    .into(imgItemPhoto)

                itemView.setOnClickListener { onItemClick(EventItem.Favorite(event)) }
            }
        }
    }

    class EventDiffCallback : DiffUtil.ItemCallback<EventItem>() {
        override fun areItemsTheSame(oldItem: EventItem, newItem: EventItem): Boolean {
            return when {
                oldItem is EventItem.Regular && newItem is EventItem.Regular -> oldItem.event.id == newItem.event.id
                oldItem is EventItem.Favorite && newItem is EventItem.Favorite -> oldItem.event.id == newItem.event.id
                else -> false
            }
        }

        override fun areContentsTheSame(oldItem: EventItem, newItem: EventItem): Boolean {
            return oldItem == newItem
        }
    }
}
