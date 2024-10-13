package com.dicoding.dicodingevent.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.dicodingevent.data.response.ListEventsItem
import com.dicoding.dicodingevent.databinding.ItemEventBinding

class EventAdapter(private val eventList: List<ListEventsItem>) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = eventList[position]
        holder.binding.tvtitle.text = event.name
        holder.binding.tvdescription.text = event.summary
        Glide.with(holder.itemView.context)
            .load(event.imageLogo)
            .into(holder.binding.imgItemPhoto)
    }

    override fun getItemCount(): Int = eventList.size

    class EventViewHolder(var binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root)
}

