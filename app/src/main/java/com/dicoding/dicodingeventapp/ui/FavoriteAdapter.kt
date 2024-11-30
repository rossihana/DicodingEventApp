package com.dicoding.dicodingeventapp.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.dicodingeventapp.R
import com.dicoding.dicodingeventapp.data.entity.FavoriteEvent
import com.dicoding.dicodingeventapp.databinding.ItemEventBinding
import com.dicoding.dicodingeventapp.ui.utils.FavoriteDiffCallback

class FavoriteAdapter(private var listEventsFavorite: List<FavoriteEvent>): RecyclerView.Adapter<FavoriteAdapter.ListViewHolder>() {
    class ListViewHolder(var binding: ItemEventBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val event = listEventsFavorite[position]
        if (!event.imageEvent.isNullOrEmpty()) {
            Glide.with(holder.itemView.context)
                .load(event.imageEvent)
                .placeholder(R.drawable.baseline_broken_image_24)
                .into(holder.binding.imgItemPhoto)
        } else {
            holder.binding.imgItemPhoto.setImageResource(R.drawable.baseline_broken_image_24)
        }

        holder.binding.tvTitleItemEvent.text = event.title

        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, DetailActivity::class.java)
            intentDetail.putExtra(DetailActivity.EVENT_DETAIL, event.id)
            holder.itemView.context.startActivity(intentDetail)
        }
    }

    override fun getItemCount(): Int = listEventsFavorite.size

    fun updateData(newList: List<FavoriteEvent>) {
        val diffCallback = FavoriteDiffCallback(listEventsFavorite, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        listEventsFavorite = newList
        diffResult.dispatchUpdatesTo(this)
    }
}