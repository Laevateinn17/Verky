package edu.bluejack23_2.verky.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.bluejack23_2.verky.R
import edu.bluejack23_2.verky.data.model.Chat

class ChatAdapter(private val onItemClicked: (Chat) -> Unit) : ListAdapter<Chat, ChatAdapter.ChatViewHolder>(ChatDiffCallback()) {

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView : TextView = itemView.findViewById(R.id.chat_item_name)
        val imageChat : ImageView = itemView.findViewById(R.id.chat_item_image)
        val newMessageTextView : TextView = itemView.findViewById(R.id.chat_item_newmessage)
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClicked(getItem(position))
                }
            }
        }
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
            .placeholder(R.color.gray_200)
            .into(holder.imageChat)
        val statusTrueCount = chat.countMessagesWithStatusTrue()

//        holder.newMessageTextView.text = if (statusTrueCount == 0) "No new message" else "$statusTrueCount new message!"
//        val gray = ContextCompat.getColor(holder.itemView.context, R.color.gray_200)
//        val primary = ContextCompat.getColor(holder.itemView.context, R.color.primary)
//
//        holder.newMessageTextView.setTextColor(if (statusTrueCount == 0) gray else primary)
//        Log.e("chat", chat.toString())
        if(chat.message.size == 0){
            holder.newMessageTextView.text = ""
        }else{
            holder.newMessageTextView.text = chat.message.get(chat.message.size - 1).content
        }
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