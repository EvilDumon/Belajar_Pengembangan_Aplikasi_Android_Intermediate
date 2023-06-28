package com.example.mystoryapp.ui.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mystoryapp.data.response.ListStoryItem
import com.example.mystoryapp.databinding.ItemCardBinding

class RecyclerViewAdapter:
    PagingDataAdapter<ListStoryItem, RecyclerViewAdapter.ViewHolder>(DIFF_CALLBACK){
    private lateinit var onItemClickCallback: OnItemClickCallback

    class ViewHolder(private val binding: ItemCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(story: ListStoryItem, onItemClickCallback: OnItemClickCallback){
            binding.tvItemName.text = story.name
            Glide.with(itemView.context)
                .load(story.photoUrl)
                .into(binding.ivItemPhoto)

            itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(RecyclerViewData(story.photoUrl, story.name, story.id))
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: RecyclerViewData)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            holder.bind(story, onItemClickCallback)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}
