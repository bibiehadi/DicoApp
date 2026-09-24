package com.example.dicoapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicoapp.data.response.ListEventsItem
import com.example.dicoapp.databinding.EventHorizontalItemBinding


class EventHorizontalCardAdapter(private val onItemClick: (ListEventsItem) -> Unit) :
    ListAdapter<ListEventsItem, EventHorizontalCardAdapter.EventHorizontalViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventHorizontalViewHolder {
        val binding =
            EventHorizontalItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventHorizontalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventHorizontalViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
    }

    inner class EventHorizontalViewHolder(val binding: EventHorizontalItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem) {
            binding.tvEventName.text = event.name
            binding.tvSummary.text = event.summary

            Glide.with(itemView.context)
                .load(event.mediaCover)
                .into(binding.ivMediaCover)

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