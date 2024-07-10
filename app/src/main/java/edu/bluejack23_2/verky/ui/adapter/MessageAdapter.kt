package edu.bluejack23_2.verky.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack23_2.verky.R
import edu.bluejack23_2.verky.data.model.LoggedUser
import edu.bluejack23_2.verky.data.model.Message
import java.util.Calendar

class MessageAdapter : ListAdapter<Message, MessageAdapter.MessageViewHolder>(MessageDiffCallback()) {

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val receiverTextContainer: LinearLayout = itemView.findViewById(R.id.receiver_text)
        private val senderTextContainer: LinearLayout = itemView.findViewById(R.id.sender_text)
        private val receiverTextView: TextView = itemView.findViewById(R.id.receiver_chat_item)
        private val senderTextView: TextView = itemView.findViewById(R.id.sender_chat_item)
        private val receiverTimeTextView: TextView = itemView.findViewById(R.id.receiver_time_stamp)
        private val senderTimeTextView: TextView = itemView.findViewById(R.id.sender_time_stamp)

        private fun formatTimestamp(timestamp: Long?): String {
            timestamp?.let {
                val calendar = Calendar.getInstance().apply { timeInMillis = it }
                val hours = calendar.get(Calendar.HOUR)
                val minutes = calendar.get(Calendar.MINUTE)
                val period = if (calendar.get(Calendar.AM_PM) == Calendar.AM) "AM" else "PM"
                return String.format("%02d:%02d %s", if (hours == 0) 12 else hours, minutes, period)
            }
            return ""
        }

        fun bind(message: Message) {
            if (message.userID == LoggedUser.getInstance().getUser()?.id) {
                senderTextView.text = message.content
                senderTimeTextView.text = formatTimestamp(message.timestamp)
                senderTextContainer.visibility = View.VISIBLE
                receiverTextContainer.visibility = View.GONE
            } else {
                receiverTextView.text = message.content
                receiverTimeTextView.text = formatTimestamp(message.timestamp)
                receiverTextContainer.visibility = View.VISIBLE
                senderTextContainer.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_bubble, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = getItem(position)
        holder.bind(message)
    }

    private class MessageDiffCallback : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem.messageId == newItem.messageId
        }

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem == newItem
        }
    }
}
