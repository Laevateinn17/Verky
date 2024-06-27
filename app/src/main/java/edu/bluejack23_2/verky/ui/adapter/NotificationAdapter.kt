package edu.bluejack23_2.verky.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.bluejack23_2.verky.R
import edu.bluejack23_2.verky.data.model.Notification

class NotificationAdapter(private var notificationList: List<Notification>) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notificationList[position]
        holder.bind(notification)
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }

    inner class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val notificationImage : ImageView = itemView.findViewById(R.id.item_notification_image)
        private val titleTextView: TextView = itemView.findViewById(R.id.item_notification_name)
        private val messageTextView: TextView = itemView.findViewById(R.id.item_notification_time)

        fun bind(notification: Notification) {
            notification.fromUser?.profile_picture?.let { url ->
                Glide.with(itemView)
                    .load(url)
                    .placeholder(R.color.gray_200)
                    .into(notificationImage)
            }
            titleTextView.text = notification.fromUser!!.name + " want's to connect with you"
            messageTextView.text = "test"
        }
    }

    fun updateData(newNotificationList: List<Notification>) {
        notificationList = newNotificationList
        notifyDataSetChanged()
    }
}
