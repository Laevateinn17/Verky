package edu.bluejack23_2.verky.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.bluejack23_2.verky.R
import edu.bluejack23_2.verky.data.model.Chat

class ChatAdapter : ListAdapter<Chat, ChatAdapter.ChatViewHolder>(ChatDiffCallback()) {

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView : TextView = itemView.findViewById(R.id.chat_item_name)
        val imageChat : ImageView = itemView.findViewById(R.id.chat_item_image)
//        val newMessageTextView : TextView = itemView.findViewById(R.id.chat_item_newmessage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = getItem(position)
        holder.nameTextView.text = chat.partnerUser?.name ?: "Unknown"
        Glide.with(holder.itemView.context)
            .load(chat.partnerUser?.profile_picture)
            .placeholder(R.color.gray)
            .into(holder.imageChat)
        val statusTrueCount = chat.countMessagesWithStatusTrue()
//        holder.newMessageTextView.text = "$statusTrueCount new message!"

//        holder.newMessageTextView.visibility = if (statusTrueCount == 0) View.VISIBLE else View.GONE
    }

    private class ChatDiffCallback : DiffUtil.ItemCallback<Chat>() {
        override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem.chatId == newItem.chatId
        }

        override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem == newItem
        }
    }

}