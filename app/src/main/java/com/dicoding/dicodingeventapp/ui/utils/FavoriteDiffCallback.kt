package com.dicoding.dicodingeventapp.ui.utils

import androidx.recyclerview.widget.DiffUtil
import com.dicoding.dicodingeventapp.data.entity.FavoriteEvent

class FavoriteDiffCallback(
    private val oldList: List<FavoriteEvent>,
    private val newList: List<FavoriteEvent>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}