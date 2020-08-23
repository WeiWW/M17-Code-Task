package com.ann.m17test.ui.viewHolder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ann.m17test.R
import com.ann.m17test.data.model.User
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_layout.view.*

class UserViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
) {

    fun bind(user: User) {
        itemView.textViewUserName.text = user.login
        Glide.with(itemView.imageViewAvatar.context)
            .load(user.avatar_url)
            .into(itemView.imageViewAvatar)
    }
}