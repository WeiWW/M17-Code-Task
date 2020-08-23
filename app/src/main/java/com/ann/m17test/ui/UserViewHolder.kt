package com.ann.m17test.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ann.m17test.R
import com.ann.m17test.data.model.User
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_layout.view.*

class UserViewHolder (view: View): RecyclerView.ViewHolder(view){

    private var user:User? = null

    companion object{
        fun create(parent: ViewGroup):UserViewHolder{
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_layout, parent, false)
            return UserViewHolder(view)
        }
    }

    fun bind(user:User?){
        this.user = user
        itemView.textViewUserName.text = user?.login
        Glide.with(itemView.imageViewAvatar.context)
            .load(user?.avatar_url)
            .into(itemView.imageViewAvatar)
    }

    fun updateUser(user: User?) {
        this.user = user
        itemView.textViewUserName.text = user?.login
        Glide.with(itemView.imageViewAvatar.context)
            .load(user?.avatar_url)
            .into(itemView.imageViewAvatar)
    }
}