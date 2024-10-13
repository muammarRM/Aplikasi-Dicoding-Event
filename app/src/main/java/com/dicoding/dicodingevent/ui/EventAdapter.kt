package com.dicoding.dicodingevent.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.dicodingevent.data.response.ListEventsItem
import com.dicoding.dicodingevent.databinding.ItemEventBinding

class EventAdapter(
    private val eventList: List<ListEventsItem>,
    private val onItemClick: (ListEventsItem) -> Unit
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = eventList[position]
        holder.bind(event)
    }

    override fun getItemCount(): Int = eventList.size

    inner class EventViewHolder(private val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem) {
            with(binding) {
                tvtitle.text = event.name
                tvdescription.text = event.summary
                Glide.with(itemView.context)
                    .load(event.imageLogo)
                    .into(imgItemPhoto)

                itemView.setOnClickListener { onItemClick(event) }
            }
        }
    }
}
