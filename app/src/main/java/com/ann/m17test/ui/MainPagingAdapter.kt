package com.ann.m17test.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.ann.m17test.data.model.User


class MainPagingAdapter : ListAdapter<User, UserViewHolder>(POST_COMPARATOR){
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder.create(parent)
    }

    companion object {
        val POST_COMPARATOR = object : DiffUtil.ItemCallback<User>() {
            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
                oldItem.id == newItem.id
        }
    }
}