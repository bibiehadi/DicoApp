package com.example.dicoapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.dicoapp.R
import com.example.dicoapp.data.remote.response.ListEventsItem
import com.example.dicoapp.databinding.EventItemBinding
import com.example.dicoapp.utils.EventType
import com.example.dicoapp.utils.formatEventDateRange


class EventAdapter(
    private val eventType: EventType,
    private val onItemClick: (ListEventsItem) -> Unit
) :
    ListAdapter<ListEventsItem, EventAdapter.EventViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventViewHolder {
        val binding = EventItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
    }

    inner class EventViewHolder(val binding: EventItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem) {
            binding.apply {
                tvEventName.text = event.name
                tvEventOwner.text = event.ownerName
                Glide.with(itemView.context)
                    .load(event.imageLogo)
                    .transform(CenterCrop(), RoundedCorners(16))
                    .into(ivMediaCover)

                when (eventType) {
                    EventType.UPCOMING -> {
                        tvEventDatetime.text = formatEventDateRange(event.beginTime, event.endTime)
                    }

                    EventType.FINISHED -> {
                        icEventStatus.setImageResource(R.drawable.ic_done)
                        tvEventDatetime.text = formatEventDateRange(event.beginTime, event.endTime)
                    }
                }


            }
            itemView.setOnClickListener {
                onItemClick(event)
            }
        }

    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}