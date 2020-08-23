package com.ann.m17test.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ann.m17test.data.model.User


class MainPagingAdapter : RecyclerView.Adapter<UserViewHolder>() {
    val data = mutableListOf<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = UserViewHolder(parent)

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size
}